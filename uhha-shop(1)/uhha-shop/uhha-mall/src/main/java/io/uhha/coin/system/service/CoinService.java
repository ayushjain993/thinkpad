package io.uhha.coin.system.service;

import io.uhha.coin.capital.domain.FVirtualCapitalOperation;
import io.uhha.coin.capital.domain.QVirtualCapitalSmall;
import io.uhha.coin.capital.mapper.FVirtualCapitalOperationMapper;
import io.uhha.coin.capital.mapper.QVirtualCapitalSmallMapper;
import io.uhha.coin.common.Enum.DataSourceEnum;
import io.uhha.coin.common.Enum.SystemCoinSortEnum;
import io.uhha.coin.common.Enum.VirtualCapitalOperationInStatusEnum;
import io.uhha.coin.common.Enum.VirtualCapitalOperationTypeEnum;
import io.uhha.coin.common.Enum.validate.PlatformEnum;
import io.uhha.coin.common.coin.CoinDriver;
import io.uhha.coin.common.coin.CoinDriverFactory;
import io.uhha.coin.common.coin.TxInfo;
import io.uhha.common.exception.BCException;
import io.uhha.coin.common.framework.redis.RedisCryptoHelper;
import io.uhha.coin.common.util.DateUtils;
import io.uhha.common.utils.Utils;
import io.uhha.coin.system.domain.FSystemArgs;
import io.uhha.coin.system.domain.SystemCoinType;
import io.uhha.coin.system.mapper.FSystemArgsMapper;
import io.uhha.coin.user.domain.FUserVirtualAddress;
import io.uhha.coin.user.mapper.FUserVirtualAddressMapper;
import io.uhha.coin.user.service.UserwalletRechargeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service("coinService")
// 每次调用新建一个实例
@Scope("prototype")
@Slf4j
public class CoinService {

    @Autowired
    private FVirtualCapitalOperationMapper fVirtualCapitalOperationMapper;
    @Autowired
    private FUserVirtualAddressMapper fUserVirtualAddressMapper;
    @Autowired
    private RedisCryptoHelper redisCryptoHelper;
    @Autowired
    private UserwalletRechargeService userwalletRechargeService;
    @Autowired
    private FSystemArgsMapper systemArgsMapper;
    @Autowired
    private QVirtualCapitalSmallMapper qVirtualCapitalSmallMapper;

    /**
     * 定时创建充值订单
     *
     * @param coinType 虚拟币
     * @throws Exception 执行异常
     */
    public void updateRecharge(final SystemCoinType coinType) {
        int coinid = coinType.getId();
        int cType = coinType.getCoinType();
        String sName = coinType.getShortName();
        BigDecimal minCount = coinType.getMinCount();
        minCount = minCount == null ? BigDecimal.valueOf(0.001) : minCount;
        // 分页起始数字
        int begin = 0;
        // 每页显示数
        int step = 100;
        // 7天之前
        Date cutDate = DateUtils.getPastdayDate(7);
        // 币种驱动
        CoinDriver coinDriver = CoinDriverFactory.getDriver(cType, coinType.getAccessKey(),
                coinType.getSecrtKey(), coinType.getIp(), coinType.getPort(), null, coinType.getAssetId(),
                coinType.getEthAccount());

        if (coinDriver == null) {
            log.warn("not found coin driver:" + cType);
            return;
        }
        List<TxInfo> txInfos;

        while (true) {
            log.debug("{} updateRecharge step {} begin {}", sName, step, begin);
            try {
                txInfos = coinDriver.listTransactions(step, begin);
            } catch (Exception e) {
                log.error(sName + " listTransactions error:" + e.getMessage(), e);
                break;
            }
            begin += step;

            if (txInfos == null || txInfos.isEmpty()) {
                log.info("{} updateRecharge finished on {}", sName, begin);
                break;
            }
            // 按照时间降序排列
            Collections.sort(txInfos, Comparator.comparing(TxInfo::getTime).reversed());

            for (TxInfo txInfo : txInfos) {
                // 判断交易时间在7天之前
                if (cType == SystemCoinSortEnum.BTC.getCode()
                        && txInfo.getTime().compareTo(cutDate) < 0) {
                    log.info("{} updateRecharge cut on {}", sName, begin);
                    break;
                }
                // } else { 7天之内
                // 必然有txid
                String txid = txInfo.getTxid().trim();


                // 必然有address
                String address = txInfo.getAddress().trim();
                // 判断值是否小于充值最小值
                BigDecimal amount = txInfo.getAmount();

                if (minCount.compareTo(amount) > 0) {
                    if (qVirtualCapitalSmallMapper.countByUniquenumber(txid) <= 0) {
                        QVirtualCapitalSmall record = new QVirtualCapitalSmall();
                        record.setQamount(amount);
                        record.setQcoinid(coinid);
                        record.setQcreatetime(Utils.getTimestamp());
//						record.setQnonce(qnonce);
                        record.setQplatform(PlatformEnum.UHHA.getCode());
                        record.setQrechargeaddress(address);
                        record.setQsource(DataSourceEnum.WEB.getCode());
                        record.setQtype(VirtualCapitalOperationTypeEnum.COIN_IN.getCode());
                        record.setQuniquenumber(txid);
                        record.setTxTime(txInfo.getTime());
                        qVirtualCapitalSmallMapper.insert(record);
                    }
                    continue;
                }
                // 判断操作记录
                List<FVirtualCapitalOperation> fvirtualcaptualoperations = fVirtualCapitalOperationMapper
                        .selectMoreByTxid(txid);

                if (!fvirtualcaptualoperations.isEmpty()) {
                    continue;
                }
                FVirtualCapitalOperation fvirtualcaptualoperation = new FVirtualCapitalOperation();

                boolean hasOwner = true;
                FUserVirtualAddress fvirtualaddresse = this.fUserVirtualAddressMapper.selectByCoinAndAddress(coinid,
                        address);

                if (fvirtualaddresse == null) {
                    hasOwner = false;// 没有这个地址，充错进来了？没收！
                } else {
                    // 有就设置用户ID
                    fvirtualcaptualoperation.setFuid(fvirtualaddresse.getFuid());
                }
                // 真实的充值数量
                fvirtualcaptualoperation.setFamount(amount);
                // 手续费
                fvirtualcaptualoperation.setFfees(BigDecimal.ZERO);
                // 币种ID
                fvirtualcaptualoperation.setFcoinid(coinid);
                // 状态=充值
                fvirtualcaptualoperation.setFtype(VirtualCapitalOperationTypeEnum.COIN_IN.getCode());
                // 状态确认=第一次确认
                fvirtualcaptualoperation.setFstatus(VirtualCapitalOperationInStatusEnum.WAIT_0);
                // 是否自己的充值
                fvirtualcaptualoperation.setFhasowner(hasOwner);
                // BTC网络手续费
                fvirtualcaptualoperation.setFbtcfees(BigDecimal.ZERO);
                // 区块数
                fvirtualcaptualoperation.setFblocknumber(0);
                // 币种确认数(同后台币种设置确认数)
                fvirtualcaptualoperation.setFconfirmations(0);
                // 充值地址 同于 用户获取的充值地址
                fvirtualcaptualoperation.setFrechargeaddress(address);
                Timestamp tmp = Utils.getTimestamp();
                fvirtualcaptualoperation.setFcreatetime(tmp);
                fvirtualcaptualoperation.setFupdatetime(tmp);
                fvirtualcaptualoperation.setVersion(0);
                fvirtualcaptualoperation.setFplatform(PlatformEnum.UHHA.getCode());
                // 来源=网络
                fvirtualcaptualoperation.setFsource(DataSourceEnum.WEB.getCode());
                // 交易时间
                fvirtualcaptualoperation.setTxTime(txInfo.getTime());
                // 交易识别码
                fvirtualcaptualoperation.setFuniquenumber(txid);
                // 区块高度
                if (txInfo.getBlockNumber() != null) {
                    fvirtualcaptualoperation.setFblocknumber(txInfo.getBlockNumber());
                }
                if (fVirtualCapitalOperationMapper.insert(fvirtualcaptualoperation) <= 0) {
                    log.error("Coin updateRecharge insert failed");
                }
            }
        }
    }

    /**
     * 定时刷新确认数
     *
     * @param coinType 虚拟币
     * @throws Exception 执行异常
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateCoinCome(final SystemCoinType coinType) throws Exception {
        int coinid = coinType.getId();
        int cType = coinType.getCoinType();
        CoinDriver coinDriver = CoinDriverFactory.getDriver(cType, coinType.getAccessKey(),
                coinType.getSecrtKey(), coinType.getIp(), coinType.getPort(), null, coinType.getAssetId(),
                coinType.getEthAccount());

        if (coinDriver == null) {
            log.warn("not found coin driver:" + cType);
            return;
        }
        // 充值到账确认数coinType.getConfirmations()
        List<FVirtualCapitalOperation> fVirtualCapitalOperations = fVirtualCapitalOperationMapper.seletcGoing(coinid,
                coinType.getConfirmations());
        // 遍历
        for (FVirtualCapitalOperation fvirtualcaptualoperation : fVirtualCapitalOperations) {
            // if (fvirtualcaptualoperation == null) {
            // continue;
            // }
            // 确认数
            int confirmations = 0;
            // 充值数量
            BigDecimal amount = BigDecimal.ZERO;

            if (cType == SystemCoinSortEnum.BTC.getCode()) {// BTC处理
                // 远程获取数据(获取交易详情)
                TxInfo btcInfo = coinDriver.getTransaction(fvirtualcaptualoperation.getFuniquenumber());

                if (btcInfo == null) {
                    continue;
                }
                confirmations = btcInfo.getConfirmations();
                amount = btcInfo.getAmount();
            }
            // 若远程确认数大于数据库中确认数(约定getFconfirmations不为0以下)
            if (confirmations > fvirtualcaptualoperation.getFconfirmations()) {
                fvirtualcaptualoperation.setFconfirmations(confirmations);
                fvirtualcaptualoperation.setFupdatetime(Utils.getTimestamp());
                // 确认状态(前置seletcGoing已经抛弃该状态,此时判断的意义)
                if (fvirtualcaptualoperation.getFstatus() != VirtualCapitalOperationInStatusEnum.SUCCESS) {
                    // 币种确认数
                    if (confirmations >= coinType.getConfirmations()) {
                        // FIXME 充提记录更新+钱包更新
                        userwalletRechargeService.txSuccess(fvirtualcaptualoperation, coinType, amount);
                        return;
                    }
                }
                // 若远程确认数小于则缓存确认数或状态为成功
                if (this.fVirtualCapitalOperationMapper.updateByPrimaryKey(fvirtualcaptualoperation) <= 0) {
                    throw new BCException("FVirtualCapitalOperation updateByPrimaryKey by " + fvirtualcaptualoperation.getFstatus());

                }
            }
        }
        log.info("updateCoinCome success {}", coinType.getShortName());
    }

    /**
     * 更新订单为成功
     *
     * @param fvirtualcaptualoperation
     * @throws Exception
     */
    @Deprecated
    void operationSuccess(FVirtualCapitalOperation fvirtualcaptualoperation) throws Exception {
        fvirtualcaptualoperation.setFstatus(VirtualCapitalOperationInStatusEnum.SUCCESS);
        if (this.fVirtualCapitalOperationMapper.updateByPrimaryKey(fvirtualcaptualoperation) <= 0) {
            throw new Exception();
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateBtsCoinCome(SystemCoinType coinType) throws Exception {
        CoinDriver coinDriver = CoinDriverFactory.getDriver(coinType);
        Integer from = -1;
        Boolean floge = false;
        String shorName = coinType.getShortName();
        List<TxInfo> txInfos = coinDriver.listTransactions(-300, from);
        for (TxInfo txInfo : txInfos) {
            Long uid = 0L;
            try {
                uid = Long.parseLong(txInfo.getComment());
            } catch (Exception e) {
                continue;
            }
            List<FVirtualCapitalOperation> fvirtualcaptualoperations = this.fVirtualCapitalOperationMapper.selectMoreByTxid(txInfo.getTxid());
            FVirtualCapitalOperation fvirtualcaptualoperation;
            if (fvirtualcaptualoperations != null && fvirtualcaptualoperations.size() > 0) {
                fvirtualcaptualoperation = fvirtualcaptualoperations.get(0);
                if (fvirtualcaptualoperation.getFstatus() == VirtualCapitalOperationInStatusEnum.SUCCESS) {
                    continue;
                }
                if (txInfo.getConfirmations() > fvirtualcaptualoperation.getFconfirmations()) {
                    fvirtualcaptualoperation.setFconfirmations(txInfo.getConfirmations());
                    fvirtualcaptualoperation.setFupdatetime(Utils.getTimestamp());
                    if (txInfo.getConfirmations() > coinType.getConfirmations()) {
                        userwalletRechargeService.txSuccess(fvirtualcaptualoperation, coinType, BigDecimal.ZERO);
                    } else {
                        if (this.fVirtualCapitalOperationMapper.updateByPrimaryKey(fvirtualcaptualoperation) <= 0) {
                            throw new Exception();
                        }
                    }
                }
            } else {
                fvirtualcaptualoperation = new FVirtualCapitalOperation();
                fvirtualcaptualoperation.setVersion(0);
                fvirtualcaptualoperation.setFsource(DataSourceEnum.WEB.getCode());
                fvirtualcaptualoperation.setTxTime(txInfo.getTime());
                fvirtualcaptualoperation.setFcoinid(coinType.getId());
                fvirtualcaptualoperation.setFcreatetime(Utils.getTimestamp());
                fvirtualcaptualoperation.setFupdatetime(Utils.getTimestamp());
                fvirtualcaptualoperation.setFconfirmations(txInfo.getConfirmations());
                fvirtualcaptualoperation.setFblocknumber(txInfo.getBlockNumber());
                fvirtualcaptualoperation.setFstatus(VirtualCapitalOperationInStatusEnum.WAIT_0);
                fvirtualcaptualoperation.setFuid(uid);
                fvirtualcaptualoperation.setFuniquenumber(txInfo.getTxid().trim());

                fvirtualcaptualoperation.setFrechargeaddress(txInfo.getTo());

                fvirtualcaptualoperation.setFamount(txInfo.getAmount());
                fvirtualcaptualoperation.setFtype(VirtualCapitalOperationTypeEnum.COIN_IN.getCode());
                int result = this.fVirtualCapitalOperationMapper.insert(fvirtualcaptualoperation);
                if (result <= 0) {
                    log.error("Coin updateRecharge insert failed");
                }
            }
        }
        if (floge) {
            FSystemArgs systemArgs = new FSystemArgs();
            systemArgs.setFkey(shorName + "CoinStartRemark");
            systemArgs.setFvalue(from + "");
            systemArgsMapper.updateByValue(systemArgs);
            redisCryptoHelper.setSystemArgs(shorName + "CoinStartRemark", from + "");
        }
        log.info("updateCoinCome success 6 {}", coinType.getShortName());
    }
}

package io.uhha.coin.system.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.uhha.coin.capital.domain.FVirtualCapitalOperation;
import io.uhha.coin.capital.domain.QVirtualCapitalSmall;
import io.uhha.coin.capital.mapper.FVirtualCapitalOperationMapper;
import io.uhha.coin.capital.mapper.QVirtualCapitalSmallMapper;
import io.uhha.coin.common.Enum.DataSourceEnum;
import io.uhha.coin.common.Enum.VirtualCapitalOperationInStatusEnum;
import io.uhha.coin.common.Enum.VirtualCapitalOperationOutStatusEnum;
import io.uhha.coin.common.Enum.VirtualCapitalOperationTypeEnum;
import io.uhha.coin.common.Enum.validate.PlatformEnum;
import io.uhha.coin.common.domain.EtherscanTx;
import io.uhha.common.exception.BCException;
import io.uhha.coin.common.framework.redis.RedisCryptoHelper;
import io.uhha.coin.common.http.HttpPoolingManager;
import io.uhha.coin.common.http.HttpSSLType;
import io.uhha.coin.common.util.ArgsConstant;
import io.uhha.common.utils.Utils;
import io.uhha.coin.system.domain.SystemCoinType;
import io.uhha.coin.user.domain.FUserVirtualAddress;
import io.uhha.coin.user.mapper.FUserVirtualAddressMapper;
import io.uhha.coin.user.service.UserwalletRechargeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service("ethService")
@Slf4j
public class EthService {
    private static String ASCHSECRTKEY = "****";

    @Autowired
    private FVirtualCapitalOperationMapper fVirtualCapitalOperationMapper;
    @Autowired
    private FUserVirtualAddressMapper fUserVirtualAddressMapper;
    @Autowired
    private UserwalletRechargeService userwalletRechargeService;
    @Autowired
    private QVirtualCapitalSmallMapper qVirtualCapitalSmallMapper;

    @Autowired
    private RedisCryptoHelper redisCryptoHelper;

    private static String FEE_FROM_ADDRESS = "*******";

    private List<FUserVirtualAddress> getUserCoinAddresses(int coinId) {
        // SQL: select * from f_user_virtual_address where fcoinid =
        List<FUserVirtualAddress> addresses = fUserVirtualAddressMapper.listUserEthAddresses(coinId);
        return addresses;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addRecharge(SystemCoinType coinType, FUserVirtualAddress address, EtherscanTx tx, BigDecimal eth)
            throws Exception {
        FVirtualCapitalOperation fvirtualcaptualoperation = new FVirtualCapitalOperation();
        fvirtualcaptualoperation.setFuid(address.getFuid());
        fvirtualcaptualoperation.setFrechargeaddress(address.getFadderess());
        fvirtualcaptualoperation.setFamount(eth);
        fvirtualcaptualoperation.setFfees(BigDecimal.ZERO);
        fvirtualcaptualoperation.setFnonce(tx.getNonce());
        int confirmations = NumberUtils.toInt(tx.getConfirmations(), 0);
        fvirtualcaptualoperation.setFconfirmations(confirmations);
        fvirtualcaptualoperation.setFblocknumber(tx.getBlockNumber());
        fvirtualcaptualoperation.setFuniquenumber(tx.getHash());
        fvirtualcaptualoperation.setFtype(VirtualCapitalOperationTypeEnum.COIN_IN.getCode());
        Timestamp time = Utils.getTimestamp();
        fvirtualcaptualoperation.setFcreatetime(time);
        fvirtualcaptualoperation.setFupdatetime(time);
        fvirtualcaptualoperation.setTxTime(new Date(tx.getTimeStamp() * 1000));
        fvirtualcaptualoperation.setFcoinid(coinType.getId());
        fvirtualcaptualoperation.setFhasowner(true);
        fvirtualcaptualoperation.setFfees(BigDecimal.ZERO);
        fvirtualcaptualoperation.setFsource(DataSourceEnum.WEB.getCode());
        fvirtualcaptualoperation.setFplatform(PlatformEnum.UHHA.getCode());
        fvirtualcaptualoperation.setVersion(0);
        // 创建订单前已经达到确认数
        if (confirmations > coinType.getConfirmations()) {
            userwalletRechargeService.txSuccess(fvirtualcaptualoperation, coinType, eth);
        } else {
            fvirtualcaptualoperation.setFstatus(VirtualCapitalOperationInStatusEnum.WAIT_0);

            if (fVirtualCapitalOperationMapper.insert(fvirtualcaptualoperation) <= 0) {
                throw new BCException("addRecharge error");
            }
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateRecharge(SystemCoinType coinType, FVirtualCapitalOperation fVirtualCapitalOperation,
                               EtherscanTx tx, BigDecimal eth) throws Exception {
        fVirtualCapitalOperation.setFupdatetime(Utils.getTimestamp());
        // 确认数
        int confirmations = NumberUtils.toInt(tx.getConfirmations(), 0);
        fVirtualCapitalOperation.setFconfirmations(confirmations);
        // 订单已经达到确认数
        if (confirmations > coinType.getConfirmations()) {
            userwalletRechargeService.txSuccess(fVirtualCapitalOperation, coinType, eth);
        } else if (fVirtualCapitalOperationMapper.updateByPrimaryKey(fVirtualCapitalOperation) <= 0) {
            throw new BCException("updateRecharge error");
        }
    }

    /**
     * 充值ETH
     *
     * @param coinType 虚拟币
     * @throws Exception 执行异常
     */
    public void recharge(SystemCoinType coinType) throws Exception {
        log.debug(coinType.getShortName() + " recharge start");
        BigDecimal minCount = coinType.getMinCount();
        minCount = minCount == null ? BigDecimal.valueOf(0.01) : minCount;
        long time0 = System.currentTimeMillis();

        String etherscanApiKey = redisCryptoHelper.getSystemArgs(ArgsConstant.ETHERSCAN_API_KEY);
        String etherscanApi = redisCryptoHelper.getSystemArgs(ArgsConstant.ETHERSCAN_API_URL);

        if (StringUtils.isEmpty(etherscanApiKey) || StringUtils.isEmpty(etherscanApi)) {
            log.error("system args [etherscanApiKey:{}] or [etherscanApi:{}] was not set", etherscanApiKey, etherscanApi);
            return;
        }

        // 获取用户虚拟币充值地址
        List<FUserVirtualAddress> addresses = getUserCoinAddresses(coinType.getId());

        for (FUserVirtualAddress address : addresses) {
            // 根据地址 包括(api-rinkeby.etherscan.io/api.etherscan.io)
            String apiUrl = new StringBuilder().append(etherscanApi)
                    .append("/api?module=account&action=txlist&startblock=0&endblock=99999999&page=1&offset=100&sort=asc&address=")
                    .append(address.getFadderess())
                    .append("&apikey=" + etherscanApiKey)
                    .toString();

            log.debug("for Address {}, apiUrl {}", address.getFadderess(), apiUrl);
            JSONObject result = JSON.parseObject(HttpPoolingManager.getInstance().get(apiUrl, HttpSSLType.ONEWAY));
            log.debug("Etherscan return {}", result.toString());

            if (result == null || !"1".equals(result.get("status"))) {
                return;
            }
            List<EtherscanTx> txs = JSON.parseArray(result.get("result").toString(), EtherscanTx.class);

            for (EtherscanTx tx : txs) {
                if (tx.getIsError() == 1) {
                    log.error("got an error ether tx");
                    continue;
                }
                if (address.getFadderess().equalsIgnoreCase(tx.getFrom())) {
                    log.debug("this tx is out");
                    continue;
                }
                if (FEE_FROM_ADDRESS.equalsIgnoreCase(tx.getFrom())) {
                    log.debug("this tx is from {} as free", FEE_FROM_ADDRESS);
                    continue;
                }
                BigDecimal eth = new BigDecimal(tx.getValue()).divide(BigDecimal.valueOf(1000000000000000000.), 6,
                        RoundingMode.FLOOR);
                // 判断值是否小于充值最小值
                if (minCount.compareTo(eth) > 0) {
                    if (qVirtualCapitalSmallMapper.countByUniquenumber(tx.getHash()) <= 0) {
                        QVirtualCapitalSmall record = new QVirtualCapitalSmall();
                        record.setQamount(eth);
                        record.setQcoinid(coinType.getId());
                        record.setQcreatetime(Utils.getTimestamp());
                        record.setQnonce(tx.getNonce());
                        record.setQplatform(PlatformEnum.UHHA.getCode());
                        record.setQrechargeaddress(address.getFadderess());
                        record.setQsource(DataSourceEnum.WEB.getCode());
                        record.setQtype(VirtualCapitalOperationTypeEnum.COIN_IN.getCode());
                        record.setQuniquenumber(tx.getHash());
                        record.setTxTime(new Date(tx.getTimeStamp() * 1000));
                        qVirtualCapitalSmallMapper.insert(record);
                    }
                    continue;
                }
                log.debug("hash is {},blockNumber is {},value is {},confirmations is {}", tx.getHash(),
                        tx.getBlockNumber(), eth.doubleValue(), tx.getConfirmations());
                // 查询订单是否存在
                List<FVirtualCapitalOperation> fvirtualcaptualoperations = fVirtualCapitalOperationMapper
                        .selectMoreByTxid(tx.getHash());
                // 订单已经存在
                if (!fvirtualcaptualoperations.isEmpty()) {
                    FVirtualCapitalOperation fVirtualCapitalOperation = fvirtualcaptualoperations.get(0);

                    //充值订单WAIT_0的时候更新订单；提币订单在lock的时候更新订单
                    if (fVirtualCapitalOperation.getFstatus() == VirtualCapitalOperationInStatusEnum.WAIT_0
                            || fVirtualCapitalOperation.getFstatus() == VirtualCapitalOperationOutStatusEnum.OperationLock) {
                        updateRecharge(coinType, fVirtualCapitalOperation, tx, eth);
                    }
                } else {
                    addRecharge(coinType, address, tx, eth);
                }
            }
        }
        log.info("{} recharge finished:{}ms", coinType.getShortName(), System.currentTimeMillis() - time0);
    }

}

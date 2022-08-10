package io.uhha.coin.system.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.uhha.coin.capital.domain.FVirtualCapitalOperation;
import io.uhha.coin.capital.domain.QVirtualCapitalSmall;
import io.uhha.coin.capital.mapper.FVirtualCapitalOperationMapper;
import io.uhha.coin.capital.mapper.QVirtualCapitalSmallMapper;
import io.uhha.coin.common.Enum.DataSourceEnum;
import io.uhha.coin.common.Enum.VirtualCapitalOperationInStatusEnum;
import io.uhha.coin.common.Enum.VirtualCapitalOperationTypeEnum;
import io.uhha.coin.common.Enum.validate.PlatformEnum;
import io.uhha.coin.common.coin.CoinDriver;
import io.uhha.coin.common.coin.CoinDriverFactory;
import io.uhha.coin.common.coin.driver.ETHDriver;
import io.uhha.coin.common.domain.EtherscanLog;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Service("erc20Service")
@Slf4j
public class Erc20Service {

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

    private List<FUserVirtualAddress> getUserEthAddresses(int coinId) {
        List<FUserVirtualAddress> addresses = fUserVirtualAddressMapper.listUserEthAddresses(coinId);
        return addresses;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addRecharge(SystemCoinType coinType, FUserVirtualAddress address, EtherscanLog etherscanLog, double eth) throws Exception {
        FVirtualCapitalOperation fvirtualcaptualoperation = new FVirtualCapitalOperation();
        fvirtualcaptualoperation.setFuid(address.getFuid());
        fvirtualcaptualoperation.setFrechargeaddress(address.getFadderess());
        fvirtualcaptualoperation.setFamount(BigDecimal.valueOf(eth));
        fvirtualcaptualoperation.setFfees(BigDecimal.ZERO);
        fvirtualcaptualoperation.setFblocknumber((int) hexToLong(etherscanLog.getBlockNumber()));
        fvirtualcaptualoperation.setFuniquenumber(etherscanLog.getTransactionHash());
        fvirtualcaptualoperation.setFtype(VirtualCapitalOperationTypeEnum.COIN_IN.getCode());
        fvirtualcaptualoperation.setFcreatetime(Utils.getTimestamp());
        fvirtualcaptualoperation.setFupdatetime(Utils.getTimestamp());
        fvirtualcaptualoperation.setTxTime(new Date(hexToLong(etherscanLog.getTimeStamp()) * 1000));
        fvirtualcaptualoperation.setFcoinid(coinType.getId());
        fvirtualcaptualoperation.setFhasowner(true);
        fvirtualcaptualoperation.setFfees(BigDecimal.ZERO);
        fvirtualcaptualoperation.setFstatus(VirtualCapitalOperationInStatusEnum.WAIT_0);
        fvirtualcaptualoperation.setVersion(0);
        fVirtualCapitalOperationMapper.insert(fvirtualcaptualoperation);
        log.info("addRecharge success coinType {}", coinType.getShortName());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateRecharge(SystemCoinType coinType, FVirtualCapitalOperation fVirtualCapitalOperation, EtherscanLog etherscanLog, double eth) throws Exception {
        fVirtualCapitalOperation.setFupdatetime(Utils.getTimestamp());
        CoinDriver coinDriver = CoinDriverFactory.getDriver(coinType);
        Integer latestBlock = ((ETHDriver) coinDriver).getETCBlockNumber();
        Long confirm = latestBlock - hexToLong(etherscanLog.getBlockNumber());
        fVirtualCapitalOperation.setFconfirmations(confirm.intValue());
        if (confirm > coinType.getConfirmations()) {//创建订单前已经达到确认数
            userwalletRechargeService.txSuccess(fVirtualCapitalOperation, coinType, BigDecimal.valueOf(eth));
        } else {
            fVirtualCapitalOperationMapper.updateByPrimaryKey(fVirtualCapitalOperation);
        }
        log.info("updateRecharge success coinType {}", coinType.getShortName());
    }

    /**
     * 充值
     *
     * @param coinType 虚拟币
     * @throws Exception 执行异常
     */
    public void recharge(SystemCoinType coinType) throws Exception {
        BigDecimal minCount = coinType.getMinCount();
        minCount = minCount == null ? BigDecimal.valueOf(0.01) : minCount;

        long time0 = System.currentTimeMillis();
        log.info(coinType.getShortName() + " recharge start");
        String etherscanApiKey = redisCryptoHelper.getSystemArgs(ArgsConstant.ETHERSCAN_API_KEY);
        String etherscanApi = redisCryptoHelper.getSystemArgs(ArgsConstant.ETHERSCAN_API_URL);

        if (StringUtils.isEmpty(etherscanApiKey) || StringUtils.isEmpty(etherscanApi)) {
            log.error("system args [etherscanApiKey:{}] or [etherscanApi:{}] was not set", etherscanApiKey, etherscanApi);
            return;
        }

        List<FUserVirtualAddress> addresses = getUserEthAddresses(coinType.getId());

        for (FUserVirtualAddress address : addresses) {
            String topic2 = address.getFadderess().replaceFirst("0x", "0x000000000000000000000000");
            String apiUrl = new StringBuilder().append(etherscanApi)
                    .append("/api?module=logs&action=getLogs&fromBlock=0&toBlock=latest")
                    //topic0这个值为TRANSFER EVENT， https://ethereum.stackexchange.com/questions/84284/how-to-get-list-of-transaction-for-erc20-token-address/84285?r=SearchResults#84285
                    .append("&topic0=0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef")
                    .append("&topic2="+topic2)
                    .append("&address=" + coinType.getContractAddress())
                    .append("&apikey=" + etherscanApiKey)
                    .toString();
            log.debug("for Address [{}], ERC20 apiUrl [{}]", address.getFadderess(), apiUrl);
            JSONObject result = JSON.parseObject(HttpPoolingManager.getInstance().get(apiUrl, HttpSSLType.ONEWAY));
            log.debug("Etherscan return:[{}]", result.toString());
            if ("1".equals(result.get("status"))) {
                List<EtherscanLog> logs = JSON.parseArray(result.get("result").toString(), EtherscanLog.class);
                for (EtherscanLog log : logs) {
                    BigInteger data = hexToBigInt(log.getData());
                    BigDecimal eth = Convert.fromWei(data.toString(), Convert.Unit.ETHER);
                    eth = eth.multiply(new BigDecimal("100"));
                    // 判断值是否小于充值最小值
                    if (eth.compareTo(minCount) < 0) {
                        if (qVirtualCapitalSmallMapper.countByUniquenumber(log.getTransactionHash()) <= 0) {
                            QVirtualCapitalSmall record = new QVirtualCapitalSmall();
                            record.setQamount(eth);
                            record.setQcoinid(coinType.getId());
                            record.setQcreatetime(Utils.getTimestamp());
//							record.setQnonce();
                            record.setQplatform(PlatformEnum.UHHA.getCode());
                            record.setQrechargeaddress(address.getFadderess());
                            record.setQsource(DataSourceEnum.WEB.getCode());
                            record.setQtype(VirtualCapitalOperationTypeEnum.COIN_IN.getCode());
                            record.setQuniquenumber(log.getTransactionHash());
                            record.setTxTime(new Date(hexToLong(log.getTimeStamp()) * 1000));
                            qVirtualCapitalSmallMapper.insert(record);
                        }
                        continue;
                    }
                    List<FVirtualCapitalOperation> fvirtualcaptualoperations = this.fVirtualCapitalOperationMapper.selectMoreByTxid(log.getTransactionHash());
                    if (fvirtualcaptualoperations.size() > 0) {//订单已经存在
                        FVirtualCapitalOperation fVirtualCapitalOperation = fvirtualcaptualoperations.get(0);
                        if (fVirtualCapitalOperation.getFstatus() == VirtualCapitalOperationInStatusEnum.WAIT_0) {
                            updateRecharge(coinType, fVirtualCapitalOperation, log, eth.doubleValue());
                        }
                    } else {
                        addRecharge(coinType, address, log, eth.doubleValue());
                    }

                }
            }
        }
        log.info("{} recharge finished {}ms", coinType.getShortName(), System.currentTimeMillis() - time0);
    }

    private BigInteger hexToBigInt(String x) {
        String y = x.replaceFirst("0x", "");
        return new BigInteger(y, 16);
    }

    private long hexToLong(String x) {
        String y = x.replaceFirst("0x", "");
        return Long.parseLong(y, 16);
    }
}

package io.uhha.coin.system.service.impl;


import io.uhha.coin.common.util.ArgsConstant;
import io.uhha.coin.dailylog.domain.FCoinBalance;
import io.uhha.coin.dailylog.mapper.FCoinBalanceMapper;
import io.uhha.coin.common.Enum.SystemCoinSortEnum;
import io.uhha.coin.common.coin.CoinDriver;
import io.uhha.coin.common.coin.CoinDriverFactory;
import io.uhha.coin.common.coin.driver.ETHDriver;
import io.uhha.coin.common.framework.redis.RedisCryptoHelper;
import io.uhha.common.utils.Utils;
import io.uhha.coin.system.domain.SystemCoinType;
import io.uhha.coin.system.mapper.SystemCoinTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service("coinBalanceService")
public class CoinBalanceService {

    private static final Logger logger = LoggerFactory.getLogger(CoinBalanceService.class);
    @Autowired
    private FCoinBalanceMapper fCoinBalanceMapper;
    @Autowired
    private SystemCoinTypeMapper systemCoinTypeMapper;
    @Autowired
    private RedisCryptoHelper redisCryptoHelper;

    public void insertCoinBalance() {
        List<SystemCoinType> systemCoinTypeList = systemCoinTypeMapper.selectAll();
        for (SystemCoinType systemCoinType : systemCoinTypeList) {
            try {
                logger.info(systemCoinType.getShortName() + " start getbalance");
                FCoinBalance fCoinBalance = new FCoinBalance();
                fCoinBalance.setFdate(Utils.getTimestamp());
                fCoinBalance.setFcoinid(systemCoinType.getId());
                CoinDriver coinDriver = CoinDriverFactory.getDriver(systemCoinType);
                BigDecimal balance = new BigDecimal("-1");

                if (systemCoinType.getCoinType().equals(SystemCoinSortEnum.ETH.getCode())) {
                    String ethApi = redisCryptoHelper.getSystemArgs(ArgsConstant.ETHERSCAN_API_URL);
                    if ("ETH".equals(systemCoinType.getShortName())) {
                        balance = ((ETHDriver) coinDriver).getEthBalance(ethApi);//eth查询余额
                    } else {
                        balance = ((ETHDriver) coinDriver).getEthTokenBalance(ethApi, systemCoinType.getEthAccount());//eth代币余额
                    }
                } else {
                    balance = coinDriver.getBalance();
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                String date = sdf.format(new Date());
                if (balance == null || balance.intValue() == -1) {
                    logger.error(systemCoinType.getShortName() + "查询失败");
                    fCoinBalance.setFremark(date + "  " + systemCoinType.getShortName() + "余额查询失败");
                } else {
                    if (systemCoinType.getStatus() == 2) {
                        fCoinBalance.setFremark(date + "  " + systemCoinType.getShortName() + " 禁用 余额为：" + balance);
                    } else {
                        fCoinBalance.setFremark(date + "  " + systemCoinType.getShortName() + " 启用 余额为：" + balance);
                    }
                }
                fCoinBalance.setFbalance(balance);
                fCoinBalance.setFcreatedate(Utils.getTimestamp());
                fCoinBalance.setVersion(0);
                fCoinBalanceMapper.insert(fCoinBalance);
                logger.info(systemCoinType.getShortName() + " end getbalance");
            } catch (Exception e) {
                logger.error(systemCoinType.getShortName() + "没有查询数据");
                e.printStackTrace();
                continue;
            }
        }
    }
}

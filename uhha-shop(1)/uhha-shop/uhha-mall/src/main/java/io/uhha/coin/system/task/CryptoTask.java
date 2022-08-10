package io.uhha.coin.system.task;

import io.uhha.coin.common.Enum.SystemCoinSortEnum;
import io.uhha.coin.common.Enum.SystemCoinStatusEnum;
import io.uhha.coin.common.Enum.SystemCoinTypeEnum;
import io.uhha.util.JobUtils;
import io.uhha.coin.system.domain.SystemCoinType;
import io.uhha.coin.system.service.CoinService;
import io.uhha.coin.system.service.Erc20Service;
import io.uhha.coin.system.service.EthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("cryptoTask")
@Slf4j
public class CryptoTask {

    @Autowired
    private EthService ethService;
    @Autowired
    private Erc20Service erc20Service;
    @Autowired
    private CoinService coinService;

    @Autowired
    private JobUtils jobUtils;

    public void ethRecharge() throws Exception {

        List<SystemCoinType> coinTypes = jobUtils.getCoinTypeList();
        if (coinTypes == null) {
            log.warn("No coin list in redis. return");
            return;
        }
        for (final SystemCoinType coinType : coinTypes) {
            if (!coinType.getType().equals(SystemCoinTypeEnum.COIN.getCode())) {
                continue;
            }
            if (coinType.getStatus().equals(SystemCoinStatusEnum.ABNORMAL.getCode()) || !coinType.getIsRecharge()) {
                continue;
            }
            //ETH类
            if (coinType.getCoinType().equals(SystemCoinSortEnum.ETH.getCode())) {
                //ETH
                if ("ETH".equalsIgnoreCase(coinType.getShortName())) {
                    ethService.recharge(coinType);
                }
                //其他ERC20 Token
                if ("UHHA".equalsIgnoreCase(coinType.getShortName())
                        || "USDT".equalsIgnoreCase(coinType.getShortName())) {
                    erc20Service.recharge(coinType);
                }
            }
        }
    }

    public void btcAutocome() {
        List<SystemCoinType> coinTypes = jobUtils.getCoinTypeList();
        if (coinTypes == null) {
            log.warn("No coin list in redis. return");
            return;
        }
        for (SystemCoinType coinType : coinTypes) {
            if (!coinType.getType().equals(SystemCoinTypeEnum.COIN.getCode())) {
                log.debug("cointType is not a SystemCoinType");
                continue;
            }
            if (coinType.getStatus().equals(SystemCoinStatusEnum.ABNORMAL.getCode()) || !coinType.getIsRecharge()) {
                log.debug("{} coinType status is abnormal or coinType is not recharge", coinType.getShortName());
                continue;
            }
            try {
                if (coinType.getCoinType().equals(SystemCoinSortEnum.BTC.getCode())) {
                    // 定时刷新确认数
                    coinService.updateCoinCome(coinType);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("coincome failed {}", e.getLocalizedMessage());
            }
        }
    }

    public void btcRecharge() {
        List<SystemCoinType> coinTypes = jobUtils.getCoinTypeList();
        if (coinTypes == null) {
            log.warn("No coin list in redis. sleep for 10 seconds.");
            return;
        }
        for (SystemCoinType coinType : coinTypes) {
            if (!coinType.getType().equals(SystemCoinTypeEnum.COIN.getCode())) {
                continue;
            }
            if (coinType.getStatus().equals(SystemCoinStatusEnum.ABNORMAL.getCode()) || !coinType.getIsRecharge()) {
                continue;
            }
            try {
                if (coinType.getCoinType().equals(SystemCoinSortEnum.BTC.getCode())) {
                    coinService.updateRecharge(coinType);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("coin recharge failed {}", e.getLocalizedMessage());
            }
        }
    }

}

package io.uhha.coin.common.coin;

import io.uhha.coin.system.domain.SystemCoinType;
import io.uhha.coin.common.coin.driver.*;

import java.io.Serializable;
import java.math.BigInteger;


/**
 * CoinDriverFactory
 * @author jany
 */
public class CoinDriverFactory implements Serializable {

	/**
	 * 创建CoinDriver
	 * @param CoinSort
	 * @param accessKey
	 * @param secretKey
	 * @param ip
	 * @param port
	 * @param pass
	 * @return
	 */
	public static CoinDriver getDriver(Integer CoinSort, String accessKey, String secretKey, String ip, String port, String pass, BigInteger assetId, String EthAccount) {
		switch (CoinSort) {
			case 1: // UHHA
				return new ETHDriver(ip, port, CoinSort, EthAccount, secretKey);
			case 2: // BTC
				return new BTCDriver(accessKey, secretKey, ip, port, pass, CoinSort);
			case 3: // ETH
				return new ETHDriver(ip, port, CoinSort, EthAccount, secretKey);
			case 4: //USDT
				return new USDTDriver(accessKey, secretKey, ip, port, pass, CoinSort);

			default:
				return null;
		}
	}

	public static CoinDriver getDriver(Integer CoinSort, String accessKey, String secretKey, String ip, String port, String pass, BigInteger assetId, String EthAccount,String shortName) {
		switch (CoinSort) {
			case 1: // UHHA
				return new ETHDriver(ip, port, CoinSort, EthAccount, secretKey);
			case 2: // BTC
				return new BTCDriver(accessKey, secretKey, ip, port, pass, CoinSort);
			case 3: // ETH
				return new ETHDriver(ip, port, CoinSort, EthAccount,secretKey);
			case 4: //USDT
				return new USDTDriver(accessKey, secretKey, ip, port, pass, CoinSort);
			default:
				return null;
		}
	}

	public static CoinDriver getDriver(SystemCoinType coinType){
		switch (coinType.getCoinType()) {
			case 1: // UHHA
				return new ETHDriver(coinType.getIp(), coinType.getPort(),coinType.getSortId(), coinType.getEthAccount(),coinType.getAccessKey());
			case 2: // BTC
				return new BTCDriver(coinType.getAccessKey(), coinType.getSecrtKey(), coinType.getIp(), coinType.getPort(), null, coinType.getCoinType());
			case 3: // ETH
				return new ETHDriver(coinType.getIp(), coinType.getPort(),coinType.getSortId(), coinType.getEthAccount(),coinType.getAccessKey());
			case 4: //USDT
				return new USDTDriver(coinType.getAccessKey(), coinType.getSecrtKey(), coinType.getIp(), coinType.getPort(), null, coinType.getCoinType());
			default:
				return null;
		}
	}
}

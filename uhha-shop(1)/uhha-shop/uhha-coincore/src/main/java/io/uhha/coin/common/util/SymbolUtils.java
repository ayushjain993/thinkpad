package io.uhha.coin.common.util;

import org.apache.commons.lang3.StringUtils;

import io.uhha.coin.system.domain.SystemTradeType;

public class SymbolUtils {

	/**
	 * 获取交易对
	 * 
	 * @param systemTradeType
	 * @return
	 */
	public static String getSymbol(SystemTradeType systemTradeType, String separator) {

		String sellSymbol = systemTradeType.getSellSymbol();
		String buySymbol = systemTradeType.getBuySymbol();
		if (StringUtils.isNotBlank(systemTradeType.getsCoinNickName())) {
			sellSymbol = systemTradeType.getsCoinNickName();

		}
		if (StringUtils.isNotBlank(systemTradeType.getbCoinNickName())) {
			buySymbol = systemTradeType.getbCoinNickName();
		}
		String symbol = (sellSymbol + separator + buySymbol).toLowerCase();

		return symbol;
	}

	/**
	 * 获取交易对
	 * 
	 * @param systemTradeType
	 * @return
	 */
	public static String getSymbol(SystemTradeType systemTradeType) {
		return getSymbol(systemTradeType, "");
	}
	
	

}

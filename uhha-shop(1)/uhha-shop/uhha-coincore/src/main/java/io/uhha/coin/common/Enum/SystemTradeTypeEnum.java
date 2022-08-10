package io.uhha.coin.common.Enum;

/**
 * 交易类型
 * @author LY
 *
 */
public enum SystemTradeTypeEnum {
	BTC(1, "BTC交易区"),
	CNY(2, "QCNY交易区"),
	FUC(3, "FUC交易区"),
	ETH(4, "ETH交易区");

	private Integer code;
	private Object value;

	private SystemTradeTypeEnum(Integer code, Object value) {
		this.code = code;
		this.value = value;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public static String getValueByCode(Integer code) {
		for (SystemTradeTypeEnum coinType : SystemTradeTypeEnum.values()) {
			if (coinType.getCode().equals(code)) {
				return coinType.getValue().toString();
			}
		}
		return null;
	}
}

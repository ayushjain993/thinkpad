package io.uhha.coin.common.Enum;

/**
 * 银行类型枚举
 * @author ZKF
 */
public enum BankCardTypeEnum {

	CREDIT_ARD(0, "信用卡"),
	DEBUT_CARD(1, "储值卡");

	private Integer code;
	private Object value;

	private BankCardTypeEnum(Integer code, Object value) {
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
}

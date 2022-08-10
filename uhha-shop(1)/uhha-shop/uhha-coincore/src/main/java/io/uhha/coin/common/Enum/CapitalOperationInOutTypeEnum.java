package io.uhha.coin.common.Enum;

/**
 * 资金充值提现类型枚举
 * @author ZKF
 */
public enum CapitalOperationInOutTypeEnum {
	
	IN(1, "充值"),
	OUT(2, "提现");

	private Integer code;
	private Object value;

	private CapitalOperationInOutTypeEnum(Integer code, Object value) {
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

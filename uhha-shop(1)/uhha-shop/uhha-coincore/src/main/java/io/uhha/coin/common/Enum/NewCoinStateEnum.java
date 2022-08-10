package io.uhha.coin.common.Enum;

/**
 * 存币理财状态
 * @author LY
 *
 */
public enum NewCoinStateEnum {
	FAVOR(1, "赞成"), OPPOSER(2, "反对");

	private Integer code;
	private Object value;

	private NewCoinStateEnum(Integer code, Object value) {
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
		for (NewCoinStateEnum financesState : NewCoinStateEnum.values()) {
			if (financesState.getCode().equals(code)) {
				return financesState.getValue().toString();
			}
		}
		return null;
	}
}

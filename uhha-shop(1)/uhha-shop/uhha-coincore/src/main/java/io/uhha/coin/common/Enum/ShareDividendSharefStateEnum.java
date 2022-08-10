package io.uhha.coin.common.Enum;

/**
 * 分红状态
 * @author ZGY
 *
 */
public enum ShareDividendSharefStateEnum {
	TOTAL(1, "可用状态分红"), BORROW(2,"理财状态分红"), ALL(3,"全部状态分红");

	private Integer code;
	private Object value;

	private ShareDividendSharefStateEnum(Integer code, Object value) {
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
		for (ShareDividendSharefStateEnum financesState : ShareDividendSharefStateEnum.values()) {
			if (financesState.getCode().equals(code)) {
				return financesState.getValue().toString();
			}
		}
		return null;
	}
}

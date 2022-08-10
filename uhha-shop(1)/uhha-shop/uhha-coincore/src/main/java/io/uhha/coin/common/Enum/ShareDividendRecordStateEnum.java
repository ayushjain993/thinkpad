package io.uhha.coin.common.Enum;

/**
 * 分红设置状态
 * @author LY
 *
 */
public enum ShareDividendRecordStateEnum {
	SUCCESS(1, "成功"), FAIL(2,"失败");

	private Integer code;
	private Object value;

	private ShareDividendRecordStateEnum(Integer code, Object value) {
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
		for (ShareDividendRecordStateEnum financesState : ShareDividendRecordStateEnum.values()) {
			if (financesState.getCode().equals(code)) {
				return financesState.getValue().toString();
			}
		}
		return null;
	}
}

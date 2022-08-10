package io.uhha.coin.common.Enum;

/**
 * 分红设置状态
 * @author LY
 *
 */
public enum ShareDividendStateEnum {
	NOTSTARTED(1, "未开始"), COMPLETED(2, "已完成"), RUNNING(3,"进行中"), FAIL(4,"失败"),
	PERCENTAGENOTSTARTED(5, "百分比分红未开始"), PERCENTAGECOMPLETED(6, "百分比分红已完成"),
	PERCENTAGERUNNING(7, "百分比分红进行中"),PERCENTAGEFAIL(8, "百分比分红失败");

	private Integer code;
	private Object value;

	private ShareDividendStateEnum(Integer code, Object value) {
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
		for (ShareDividendStateEnum financesState : ShareDividendStateEnum.values()) {
			if (financesState.getCode().equals(code)) {
				return financesState.getValue().toString();
			}
		}
		return null;
	}
}

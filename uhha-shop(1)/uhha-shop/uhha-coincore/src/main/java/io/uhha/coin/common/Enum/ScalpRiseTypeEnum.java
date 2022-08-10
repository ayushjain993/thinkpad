package io.uhha.coin.common.Enum;

public enum ScalpRiseTypeEnum {
	RISE(0, "上涨"),		// 上涨
	DOWN(1, "下跌");		// 下跌

	private Integer code;
	private String value;

	private ScalpRiseTypeEnum(int code, String value) {
		this.code = code;
		this.value = value;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public static String getScalpRiseValueByCode(Integer code) {
		for (ScalpRiseTypeEnum scalpRise : ScalpRiseTypeEnum.values()) {
			if (scalpRise.getCode().equals(code)) {
				return scalpRise.getValue().toString();
			}
		}
		return null;
	}
}

package io.uhha.coin.common.Enum;

public enum ScalpStatusEnum {
	FORBIDDEN(0, "禁用"),		// 禁用
	ENABLE(1, "启用");		// 启用

	private Integer code;
	private String value;

	private ScalpStatusEnum(int code, String value) {
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
	
	public static String getScalpStatusValueByCode(Integer code) {
		for (ScalpStatusEnum scalpStatus : ScalpStatusEnum.values()) {
			if (scalpStatus.getCode().equals(code)) {
				return scalpStatus.getValue().toString();
			}
		}
		return null;
	}
}

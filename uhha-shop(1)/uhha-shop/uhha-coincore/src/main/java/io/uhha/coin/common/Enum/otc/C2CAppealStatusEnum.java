package io.uhha.coin.common.Enum.otc;

public enum  C2CAppealStatusEnum {
	APPEALE(1, "已申诉"),
	REJECT(2, "申诉驳回"),
	FINISH(3, "处理完成");

	private Integer code;
	private String value;

	private C2CAppealStatusEnum(int code, String value) {
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


}

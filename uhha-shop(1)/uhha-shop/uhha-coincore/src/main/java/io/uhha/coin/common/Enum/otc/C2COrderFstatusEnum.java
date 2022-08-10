package io.uhha.coin.common.Enum.otc;

public enum C2COrderFstatusEnum {
	SUCCEED(1, "委托订单"),
	ACCOMPLISH(2, "完成订单"),
	CANCEL(3, "取消订单");

	private Integer code;
	private String value;

	private C2COrderFstatusEnum(int code, String value) {
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

	public static String getC2COrderFstatusValueByCode(Integer code) {
		for (C2COrderFstatusEnum C2COrderFstatusEnum : C2COrderFstatusEnum.values()){
			if(C2COrderFstatusEnum.getCode().equals(code)){
				return C2COrderFstatusEnum.getValue().toString();
			}
		}
		return null;
	}
}

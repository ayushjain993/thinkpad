package io.uhha.coin.common.Enum.otc;

public enum OtcBusinessTypeEnum {
	ENTRUST_SELL(1, "委托卖单"),
	ORDER_SELL(2, "摘单卖出"),
	AUTO12(3, "12小时自动转币"),
	CANCEL(4, "取消处理"),
	SUCCESS(5, "订单完成"),
	ACDCANCEL(6, "平台取消");

	private Integer code;
	private String value;

	private OtcBusinessTypeEnum(int code, String value) {
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

	public static String getOtcBusinessTypeValueByCode(Integer code) {
		for (OtcBusinessTypeEnum C2COrderFstatusEnum : OtcBusinessTypeEnum.values()){
			if(C2COrderFstatusEnum.getCode().equals(code)){
				return C2COrderFstatusEnum.getValue().toString();
			}
		}
		return null;
	}
}

package io.uhha.coin.common.Enum.otc;

public enum C2COrderSublistFstatusEnum {
    NONPAYMENT(1, "未支付"),	    	    // 未支付
    PAID(2, "已支付"),		                // 已支付
    AFFIRMPAID(3, "确认已支付"),		    // 确认已支付
    ACCOMPLISH(4, "已完成"),		        // 已完成
    CANCEL(5, "取消处理中"),		        // 已取消
    APPEAL(6, "申诉中"),		            // 申诉中 即冻结状态
    CANCELSUCCESS(7,"取消已完成"),        // 取消已完成
    ABNORMITY(99, "异常订单");		        // 异常订单

    private Integer code;
    private String value;

    private C2COrderSublistFstatusEnum(int code, String value) {
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

    public static String getC2COrderTypeValueByCode(Integer code) {
        for (C2COrderSublistFstatusEnum c2COrderTypeEnum : C2COrderSublistFstatusEnum.values()) {
            if (c2COrderTypeEnum.getCode().equals(code)) {
                return c2COrderTypeEnum.getValue().toString();
            }
        }
        return null;
    }
}

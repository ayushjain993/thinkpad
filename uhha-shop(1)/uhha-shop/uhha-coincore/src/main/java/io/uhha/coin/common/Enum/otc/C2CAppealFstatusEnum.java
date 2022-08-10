package io.uhha.coin.common.Enum.otc;



public enum C2CAppealFstatusEnum {
    APPEAL(1, "已申诉"),	    	    	// 已申诉
    APPEALREJECT(2, "申诉驳回"),		    // 申诉驳回
    ACCOMPLISH(3, "处理完成");		    // 处理完成

    private Integer code;
    private String value;

    private C2CAppealFstatusEnum(int code, String value) {
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
        for (C2CAppealFstatusEnum c2COrderTypeEnum : C2CAppealFstatusEnum.values()) {
            if (c2COrderTypeEnum.getCode().equals(code)) {
                return c2COrderTypeEnum.getValue().toString();
            }
        }
        return null;
    }
}

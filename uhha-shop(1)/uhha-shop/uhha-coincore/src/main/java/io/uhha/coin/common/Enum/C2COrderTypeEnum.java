package io.uhha.coin.common.Enum;

public enum C2COrderTypeEnum {
    BUY(1, "买单"),		// 买单
    SELL(2, "卖单");		// 卖单

    private Integer code;
    private String value;

    private C2COrderTypeEnum(int code, String value) {
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
        for (C2COrderTypeEnum c2COrderTypeEnum : C2COrderTypeEnum.values()) {
            if (c2COrderTypeEnum.getCode().equals(code)) {
                return c2COrderTypeEnum.getValue().toString();
            }
        }
        return null;
    }
}

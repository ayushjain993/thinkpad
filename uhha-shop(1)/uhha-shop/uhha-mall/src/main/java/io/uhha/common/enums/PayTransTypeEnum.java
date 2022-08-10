package io.uhha.common.enums;

/**
 *支付流水类型 1 门店订单 2 订单 3 预存款
 */
public enum PayTransTypeEnum {
    STORE_ORDER("1", "门店订单"),
    ONLINE_ORDER("2","线上订单"),
    PREPAY("3","预存款订单");

    private String code;
    private String description;

    PayTransTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

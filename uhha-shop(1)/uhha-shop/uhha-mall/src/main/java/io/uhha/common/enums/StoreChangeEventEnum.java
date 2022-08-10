package io.uhha.common.enums;

public enum StoreChangeEventEnum {
    /**
     * 创建店铺
     */
    CREATED("创建店铺"),
    /**
     * 更改店铺
     */
    UPDATE("更改店铺"),

    /**
     * 更改店铺类型
     */
    MODIFY_MERCHANT_TYPE("更改店铺类型"),

    /**
     * 更改店铺费率
     */
    MODIFY_MERCHANT_PLARTOFMR_RATE("更改店铺费率")
    ;

    private String description;

    StoreChangeEventEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

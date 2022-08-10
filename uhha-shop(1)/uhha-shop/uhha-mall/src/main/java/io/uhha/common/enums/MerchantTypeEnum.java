package io.uhha.common.enums;

/**
 * 商品类型，普通商品还是付费店铺
 */
public enum MerchantTypeEnum {
    /**
     * 0 普通店铺
     */
    BASIC("0", "普通店铺"),

    /**
     * 付费店铺
     */
    PREMIUM("1", "付费店铺")
    ;


    private String code;
    private String description;

    MerchantTypeEnum(String code, String description) {
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

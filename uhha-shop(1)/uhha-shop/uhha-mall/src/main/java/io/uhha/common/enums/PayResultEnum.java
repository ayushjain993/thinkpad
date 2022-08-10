package io.uhha.common.enums;

/**
 * 充值支付状态 0 未支付 1 支付成功
 */
public enum PayResultEnum {
    /**
     * 0未支付
     */
    NOT_PAID("0", "0未支付"),

    /**
     * 1支付成功
     */
    PAID("1", "1支付成功");

    private String code;
    private String description;

    PayResultEnum(String code, String description) {
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

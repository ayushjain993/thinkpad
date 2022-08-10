package io.uhha.common.enums;

/**
 * 0在线支付  1货到付款
 */
public enum PayTypeEnum {
    /**
     * 0在线支付
     */
    ONLINE_PAYMENT("0", "0在线支付"),

    /**
     * 1货到付款
     */
    CASH_ON_DELVERY("1", "1货到付款");

    private String code;
    private String description;

    PayTypeEnum(String code, String description) {
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

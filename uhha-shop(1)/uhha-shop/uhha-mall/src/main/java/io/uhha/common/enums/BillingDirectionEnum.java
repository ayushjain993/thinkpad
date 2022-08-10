package io.uhha.common.enums;

/**
 * 类型  0 收入 1 支出
 */
public enum BillingDirectionEnum {
    /**
     * 收入
     */
    INCOME("0","收入"),

    /**
     * 支出
     */
    OUTCOME("1", "支出");

    private String code;
    private String description;

    BillingDirectionEnum(String code, String description) {
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

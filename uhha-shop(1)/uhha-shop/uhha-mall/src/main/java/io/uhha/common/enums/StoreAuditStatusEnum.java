package io.uhha.common.enums;

/**
 * 店铺商品审核开关  0 需要审核 1 不需要 默认0
 */
public enum StoreAuditStatusEnum {
    NEED_AUDIT("0", "需要审核"),
    NOT_NEED_AUDIT("1", "不需要审核");

    private String code;
    private String description;

    StoreAuditStatusEnum(String code, String description) {
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

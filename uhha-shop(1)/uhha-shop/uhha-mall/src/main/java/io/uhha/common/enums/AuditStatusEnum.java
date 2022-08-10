package io.uhha.common.enums;

/**
 * 审核状态  0 审核中 1 审核通过 2 审核未通过
 */
public enum AuditStatusEnum {

    UNDER_AUDIT("0","审核中"),
    PASS("1", "审核通过"),
    REJECTED("2", "审核未通过");

    private String code;
    private String description;

    AuditStatusEnum(String code, String description) {
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

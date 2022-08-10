package io.uhha.common.enums;

/**
 * 实名审核状态 0 已提交 1 验证通过  2 已拒绝 默认0
 */
public enum RealnameScreenStatusEnum {

    SUBMITTED("0", "已提交"),
    APPROVED("1", "验证通过"),
    REJECTED("2", "已拒绝")
            ;

    private String code;
    private String description;

    RealnameScreenStatusEnum(String code, String description) {
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

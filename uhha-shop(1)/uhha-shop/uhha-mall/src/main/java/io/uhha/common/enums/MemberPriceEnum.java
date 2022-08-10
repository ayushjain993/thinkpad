package io.uhha.common.enums;

public enum MemberPriceEnum {
    ACTIVATED("0", "启用会员价"),
    DEACTIVATED("1", "不启用会员价");

    private String code;
    private String description;

    MemberPriceEnum(String code, String description) {
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

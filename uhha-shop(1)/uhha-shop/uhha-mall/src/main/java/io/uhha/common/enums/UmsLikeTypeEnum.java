package io.uhha.common.enums;

public enum UmsLikeTypeEnum {
    /**
     * 0 视频
     */
    VIDEO("0", "0 视频"),

    /**
     * 1 已评价
     */
    COMMENT("1", "1 评论");

    private String code;
    private String description;

    UmsLikeTypeEnum(String code, String description) {
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

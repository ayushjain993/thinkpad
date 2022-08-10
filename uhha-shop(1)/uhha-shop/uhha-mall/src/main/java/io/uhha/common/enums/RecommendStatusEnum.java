package io.uhha.common.enums;

public enum RecommendStatusEnum {
    RECOMMEND(1, "推荐"),
    NOT_RECOMMEND(0, "不推荐");

    private Integer code;
    private String description;

    RecommendStatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
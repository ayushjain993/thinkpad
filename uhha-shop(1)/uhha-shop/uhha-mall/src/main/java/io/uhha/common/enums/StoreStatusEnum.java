package io.uhha.common.enums;

import io.uhha.common.utils.StringUtils;

/**
 * 店铺状态枚举
 */
public enum StoreStatusEnum {
    /**
     * 填写资料中
     */
    FILLING("0", "填写资料中"),

    /**
     * 店铺审核中
     */
    UNDER_SCREENING("1", "店铺审核中"),

    /**
     * 审核通过
     */
    APPROVED("2", "审核通过"),

    /**
     * 审核不通过
     */
    REJECTED("3", "审核不通过"),

    /**
     * 店铺关闭
     */
    CLOSED("4", "店铺关闭"),

    /**
     * 店铺禁用
     */
    FROZEN("5", "店铺被禁用");

    public static String getDescription(String code) {
        for (StoreStatusEnum ele : values()) {
            if (StringUtils.equals(ele.getCode(), code)) return ele.getDescription();
        }
        return null;
    }

    StoreStatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    private String code;
    private String description;

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

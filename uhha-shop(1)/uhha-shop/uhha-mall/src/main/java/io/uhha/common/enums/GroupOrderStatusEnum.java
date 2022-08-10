package io.uhha.common.enums;

/**
 * 拼团状态 -1无状态 0未成团 1已成团
 */
public enum GroupOrderStatusEnum {
    INIT("-1","1无状态"),

    NOT_FORMED("0","0未成团"),

    FORMED("1","1已成团"),

    ;

    private String code;
    private String description;

    GroupOrderStatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }
}

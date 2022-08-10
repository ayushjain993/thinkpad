package io.uhha.common.enums;

/**
 * 评价状态  0 未评价 1 已评价  默认为0 未评价
 */
public enum RateStatusEnum {
    /**
     * 0 未评价
     */
    INIT("0", "0 未评价"),

    /**
     * 1 已评价
     */
    RATED("1", "1 已评价");

    private String code;
    private String description;

    RateStatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }
}

package io.uhha.common.enums;

/**
 * 预售订单状态   普通订单 该状态没作用
 * 0 第一阶段未支付
 * 1 第一阶段已支付第二阶段未支付
 * 2 第二阶段已支付   默认0
 */
public enum PresaleStatusEnum {
    /**
     * 0 第一阶段未支付
     */
    STAGE1("0", "第一阶段未支付"),

    /**
     * 1 第一阶段已支付第二阶段未支付
     */
    STAGE2("1", "1 第一阶段已支付第二阶段未支付"),

    /**
     * 2 第二阶段已支付
     */
    STAGE3("2", "第二阶段已支付");

    private String code;
    private String description;

    PresaleStatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }
}

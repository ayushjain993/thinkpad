package io.uhha.common.enums;

import io.uhha.common.utils.StringUtils;

/**
 * 账单记录类型 1 确认收货 2 退款订单 3 退货订单佣金  4 订单关闭（只支付定金） 5 推广订单提成 6 退货订单
 */
public enum BillingRecordTypeEnum {
    /**
     * NULL
     */
    NULL("0", "空"),
    /**
     * 确认收货
     */
    CONFIRM_ORDER_RECEIPT("1", "确认收货"),

    /**
     * 退款订单
     */
    REFUND_ORDER("2", "退款订单"),

    /**
     * 退货订单佣金
     */
    BACK_ORDER_COMMISSION("3", "退货订单佣金"),

    /**
     * 订单关闭（只支付定金）
     */
    CLOSE_ORDER("4", "订单关闭（只支付定金）"),

    /**
     * 推广订单提成
     */
    DISTRIBUTION_COMMISSION_L1("5", "推广订单提成L1"),

    /**
     * 推广订单提成
     */
    DISTRIBUTION_COMMISSION_L2("6", "推广订单提成L2"),

    /**
     * 退货订单
     */
    BACK_ORDER("7", "退货订单"),

    /**
     * 带货佣金用户或者店铺部分
     */
    ORDER_DROPSHIP_USER("8", "带货佣金用户部分"),

    /**
     * 带货佣金平台部分
     */
    ORDER_DROPSHIP_PLATFORM("9", "带货佣金店铺部分"),

    /**
     * 带货佣金
     */
    BACK_ORDER_DROPSHIP("10", "带货佣金退货"),

    /**
     * 确认收货平台收取手续费
     */
    CONFIRM_ORDER_RECEIPT_CATE("11", "确认收货平台收取手续费");


    private String code;
    private String description;

    BillingRecordTypeEnum(String code, String description) {
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

    public static BillingRecordTypeEnum getRecordType(String code) {
        for (BillingRecordTypeEnum ele : values()) {
            if (StringUtils.equals(ele.getCode(), code)) {
                return ele;
            }
        }
        return null;
    }
}

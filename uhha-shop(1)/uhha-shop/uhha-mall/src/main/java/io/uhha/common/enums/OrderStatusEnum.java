package io.uhha.common.enums;

public enum OrderStatusEnum {
    /**
     * 待付款  （用户刚下单）
     */
    PENDING("1","待付款  （用户刚下单）"),

    /**
     * 待发货  （用户付完款 等待商城发货）
      */
    TO_BE_SHIPPED("2","待发货（用户付完款 等待商城发货）"),

    /**
     * 待收货  （商城已经发货 等待用户确认收货）
     */
    TO_BE_RECEIVED("3", "待收货（商城已经发货 等待用户确认收货）"),

    /**
     * 已完成  （用户已经确认收货 订单结束）
     */
    COMPLETED("4", "已完成（用户已经确认收货 订单结束）"),

    /**
     * 取消订单 （用户未付款前取消订单）
     */
    CANCELLED("5","取消订单（用户未付款前取消订单）"),

    /**
     * 退款通过  （用户已经付款但是商城还未发货，用户发出退款申请，商城同意退款）
     */
    REFUND_AGREED("6","退款通过（用户已经付款但是商城还未发货，用户发出退款申请，商城同意退款）"),

    /**
     * 退货通过   （用户已经确认收货后用户发出退货申请，商城同意所有退货申请 ，一个订单可能有多个单品）
     */
    GOODS_RETURN("7","退货通过（用户已经确认收货后用户发出退货申请，商城同意所有退货申请 ，一个订单可能有多个单品）"),

    /**
     * 逻辑删除
     */
    DELETED("8","逻辑删除");

    private String code;
    private String description;

    OrderStatusEnum(String code, String description) {
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

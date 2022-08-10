package io.uhha.common.enums;

/**
 *   退款／退货状态
 *   1:退款申请（用户发送退款请求）
 *   2:退款成功（商家同意退款）
 *   3:退款拒绝（商家拒绝退款）
 *   4:退货申请（用户发起退货请求）
 *   5:退货拒绝（商家拒绝退货）
 *   6:退货审核通过等待用户填写物流（商家审核通过，等待用户寄回商品）
 *   7: 待收货（用户已经寄回商品，等待商家收货确认）
 *   8：退货完成（商家收货并且同意退款给用户）
 *   9:退货失败（商家不同意退款）
 */
public enum BackOrderStatusEnum {
    REFUND_NULL("0", "Null status"),
    REFUND_APPLIED("1","退款申请（用户发送退款请求）"),
    REFUND_SUCCESS("2","退款成功（商家同意退款）"),
    REFUND_REJECTED("3","退款拒绝（商家拒绝退款）"),
    RETURN_APPLIED("4","退货申请（用户发起退货请求）"),
    RETURN_REJECTED("5","退货拒绝（商家拒绝退货）"),
    RETURN_APPROVED_NEEDS_LOGISTIC("6","退货审核通过等待用户填写物流（商家审核通过，等待用户寄回商品）"),
    RETURN_APPROVED_COLLECT("7","待收货（用户已经寄回商品，等待商家收货确认）"),
    RETURN_COMPLETED("8","退货完成（商家收货并且同意退款给用户）"),
    RETURN_FAILED("9","退货失败（商家不同意退款）");

    private String code;
    private String description;

    BackOrderStatusEnum(String code, String description) {
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

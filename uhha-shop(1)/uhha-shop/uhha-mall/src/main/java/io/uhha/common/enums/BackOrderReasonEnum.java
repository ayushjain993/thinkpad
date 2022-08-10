package io.uhha.common.enums;

/**
 *   退款／退货原因
 *   1:不想买了
 *   2:收货人信息有误
 *   3:未按指定时间发货
 *   4:商品质量问题
 *   5:收到商品与描述不符
 *   6:其他
 *   7:系统自动申请
 *
 */
public enum BackOrderReasonEnum {
    REFUND_REASON_DONT_BUY("1","不想买了"),
    REFUND_REASON_RECIEPT_ERROR("2","收货人信息有误"),
    REFUND_REASON_NOT_RECEIVED("3","未按指定时间发货"),
    REFUND_REASON_COMMODITY_QUALITY("4","商品质量问题"),
    REFUND_REASON_COMMODITY_MISMATCH("5","收到商品与描述不符"),
    REFUND_REASON_OTHER("6","其他"),
    REFUND_REASON_SYS_REQ("7","系统自动申请"),;

    private String code;
    private String description;

    BackOrderReasonEnum(String code, String description) {
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

package io.uhha.common.enums;

/**
 * 订单取消原因
 * 1:现在不想买
 * 2:商品价格较贵
 * 3:价格波动
 * 4:商品缺货
 * 5:重复下单
 * 6:收货人信息有误
 * 7:发票信息有误/发票未开
 * 8:送货时间过长
 * 9:其他原因
 * 0:系统取消
 */
public enum CancelReasonEnum {
    DONT_WANT_TO_BUY("1", "1:现在不想买"),
    TOO_EXPENSIVE("2", "2:商品价格较贵"),
    PRICE_CHANGING("3", "3:价格波动"),
    OUT_OF_STOCK("4", "4:商品缺货"),
    DUNPLICATED("5", "5:重复下单"),
    WRONG_RECIEVER("6", "6:收货人信息有误"),
    WRONG_RECIEPT_INFO("7", "7:发票信息有误/发票未开"),
    TOO_LONG_LOGISTIC("8", "8:送货时间过长"),
    OTEHRS("9", "9:其他原因"),
    SYSTEM_CANCEL("0", "0:系统取消")
    ;

    private String code;
    private String description;

    CancelReasonEnum(String code, String description) {
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

package io.uhha.common.enums;

/**
 * 1:订单支付 2:预存款充值 3:门店线下支付
 */
public enum PaymentTypeEnum {

    /**
     * 1订单支付
     */
    ORDER_PAY(1, "1订单支付"),

    /**
     * 2预存款充值
     */
    RECHARGE_PAY(2, "2预存款充值"),

    /**
     * 3门店线下支付
     */
    STORE_ORDER_PAY(3, "3门店线下支付");

    private int code;
    private String description;

    PaymentTypeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

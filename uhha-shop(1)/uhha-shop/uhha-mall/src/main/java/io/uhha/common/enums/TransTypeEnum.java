package io.uhha.common.enums;

/**
 * 交易类型  1:在线充值  2:订单消费  3:订单退款 4:管理员增加 5:管理员减少 6:手续费转余额 7:余额提款
 */
public enum TransTypeEnum {
    /**
     * NULL
     */
    NULL("0","NULL"),

    /**
     * 在线充值
     */
    ONLINE_DEPOSIT("1", "在线充值"),

    /**
     * 订单消费
     */
    ORDER_CONSUME("2", "订单消费"),

    /**
     * 订单退款
     */
    ORDER_REFUND("3", "订单退款"),

    /**
     * 管理员增加
     */
    ADMIN_ADD("4", "管理员增加"),

    /**
     * 管理员减少
     */
    ADMIN_REDUCE("5", "管理员减少"),

    /**
     * 手续费转余额
     */
    COMMISSION2BALANCE("6", "手续费转余额"),

    /**
     * 余额提款
     */
    WITHDRAW("7", "余额提款"),

    /**
     * 余额提款失败
     */
    WITHDRAW_FAIL("8", "余额提款失败"),

    /**
     * 提款结束
     */
    WITHDRAW_FINISHED("9", "提款结束")
    ;

    private String code;
    private String description;

    TransTypeEnum(String code, String description) {
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

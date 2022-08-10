package io.uhha.coin.common.Enum;


/**
 * C2C状态枚举
 */
public enum C2COrderStatusEnum {
    HaveNotPayment(1, "未付款"),				// 未付款
    AlreadyPayment(2, "已付款"),			// 已付款
    Checkup(3, "未审核"),		    // 待审核
    Completes(4, "已完成"),				// 已完成
    OrderLock(5,"锁定"),
    Checked(6,"已审核"),
    Cancel(7,"已取消"),
    NotReceivingMoney(8,"未收款"),
    HaveChecked(9,"已确认");
    private Integer code;
    private String value;

    private C2COrderStatusEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static String getC2COrderStateValueByCode(Integer code) {
        for (C2COrderStatusEnum c2COrderStatusEnum : C2COrderStatusEnum.values()){
            if(c2COrderStatusEnum.getCode().equals(code)){
                return c2COrderStatusEnum.getValue().toString();
            }
        }
        return null;
    }

}

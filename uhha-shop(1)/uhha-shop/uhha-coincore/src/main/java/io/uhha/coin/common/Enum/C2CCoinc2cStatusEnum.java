package io.uhha.coin.common.Enum;


/**
 * C2C状态枚举
 */
public enum C2CCoinc2cStatusEnum {

    FORBBIN(0, "禁用"),
    NORMAL(1, "正常");

    private Integer code;
    private String value;

    private C2CCoinc2cStatusEnum(int code, String value) {
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

}

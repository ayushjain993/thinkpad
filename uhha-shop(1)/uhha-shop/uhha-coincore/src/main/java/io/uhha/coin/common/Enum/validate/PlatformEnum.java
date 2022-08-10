package io.uhha.coin.common.Enum.validate;

/**
 * 验证平台枚举
 */
public enum PlatformEnum {

    UHHA(1, "UHHA"),
    SMS(2, "Store Management System");

    private Integer code;
    private String value;


    PlatformEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static String getValueByCode(Integer code) {
        for (PlatformEnum platformEnum : PlatformEnum.values()) {
            if (platformEnum.getCode().equals(code)) {
                return platformEnum.getValue();
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}

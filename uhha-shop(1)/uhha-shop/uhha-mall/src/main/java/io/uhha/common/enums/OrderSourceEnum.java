package io.uhha.common.enums;

/**
 * 订单来源 1pc  2 h5   3 app 4小程序
 */
public enum OrderSourceEnum {

    PC("1", "pc"),
    H5("2", "h5"),
    APP("3","app"),
    APPLET("4","小程序"),;

    private String code;
    private String description;

    OrderSourceEnum(String code, String description) {
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

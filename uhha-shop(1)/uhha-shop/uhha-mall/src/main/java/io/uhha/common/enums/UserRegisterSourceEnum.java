package io.uhha.common.enums;

/**
 * 用户注册来源 1 pc  2app  3 手机h5 4 管理员后台新增 5applet
 */
public enum UserRegisterSourceEnum {
    PC("1", "pc"),
    H5("2", "app"),
    APP("3","手机h5"),
    BACKEND("4","管理员后台新增"),
    APPLET("5","小程序");

    private String code;
    private String description;

    UserRegisterSourceEnum(String code, String description) {
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

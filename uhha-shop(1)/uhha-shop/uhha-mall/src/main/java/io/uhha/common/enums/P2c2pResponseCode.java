package io.uhha.common.enums;

public enum P2c2pResponseCode {

    NOT_CONFIGURED(-9,"-9 2c2p没开启"),

    USER_NOT_EXIST(-1,"-1  用户不存在"),

    ORDER_NOT_EXIST(-3,"-3 没有待支付订单"),

    FORM_GENERATION_FAILED(-5,"-5 生成表单错误"),

    SITE_NOT_EXIST(-7,"-7 网站地址不存在"),

    PARAMETER_ERROR(-8,"-8 参数检查错误"),

    WRONG_CONFIGURATION(-1,"-8 2c2p配置的参数错误"),

    GENERATE_TRANSCODE_ERROR(-10,"-10 生成交易订单号错误"),

    SUCCESS(1,"1 成功"),
            ;

    private int code;
    private String description;

    P2c2pResponseCode(int code, String description) {
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

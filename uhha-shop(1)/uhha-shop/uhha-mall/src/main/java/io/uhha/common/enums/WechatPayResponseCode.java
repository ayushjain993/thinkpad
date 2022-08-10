package io.uhha.common.enums;

/**
 * -8 没有设置微信支付信息 -9 微信支付没启动 -7 没有配置站点信息 -1 用户不存在 -3 没有待支付订单 -5 微信生存支付信息失败 1 成功
 */
public enum WechatPayResponseCode {
    SUCCESS(1, "1 成功"),
    PAYMENT_CONFIGURATION_NOT_SET(-8, "-8 没有设置微信支付信息"),
    NOT_CONFIGURED(-9,"-9 微信支付没启动"),
    SITE_NOT_EXIST(-7,"-7 没有配置站点信息"),
    USER_NOT_EXIST(-1,"-1  用户不存在"),
    ORDER_NOT_EXIST(-3,"-3 没有待支付订单"),
    WRONG_CONFIGURATION(-5,"-5 微信生存支付信息失败"),
    GENERATE_TRANSCODE_ERROR(-10,"-10 生成交易订单号错误"),
    QR_PAY_NOT_USED(-11, "QR支付未配置")
    ;


    private int code;
    private String description;

    WechatPayResponseCode(int code, String description) {
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

package io.uhha.coin.common.result;

/**
 * Created by ZKF on 2017/4/19.
 */
public enum ResultErrorCode {

    USER_FORBIDDEN(101, "账号异常被冻结，如有疑问请联系客服！"),
    TRADEPASSWORD_AVAILABLE(102, "交易密码修改后24小时内暂停提现！"),
    TRADEPASSWORD_SETTING(103, "请先设置交易密码"),
    USER_IDENTITYAUTH(104, "请先完成实名认证！"),
    USER_BIND_PHONEANDGOOGLE(105, "请先绑定手机或谷歌验证器！"),
    USER_FORBIDDEN_CNY(106, "账号资金操作已冻结，如有疑问请联系客服！"),
    USER_FORBIDDEN_COIN(107, "账号虚拟币操作已冻结，如有疑问请联系客服！"),
    GOOGLE_LIMIT_BEYOND_ERROR(201,"谷歌验证码错误多次，请2小时后再试！"),
    GOOGLE_LIMIT_COUNT_ERROR(202,"谷歌验证码错误！您还有%d次机会！"),
    PHONE_LIMIT_BEYOND_ERROR(203,"手机验证码错误多次，请2小时后再试！"),
    PHONE_LIMIT_COUNT_ERROR(204,"手机验证码错误！您还有%d次机会！"),
    TRADE_LIMIT_BEYOND_ERROR(207,"交易密码错误多次，请2小时后再试！"),
    TRADE_LIMIT_COUNT_ERROR(208,"交易密码错误！您还有%d次机会！"),
    GOOGLE_CHECK_ERROR(209,"谷歌验证码错误，请重试！"),

    END(999, "None"),
    PARAM_ERROR(400,"参数错误");

    private Integer code;
    private String value;

    private ResultErrorCode(Integer code, String value) {
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

package io.uhha.common.enums;

public enum UmsMemberChangeEventEnum {
    /**
     * 创建用户
     */
    CREATED("创建用户"),

    /**
     * 修改登录密码
     */
    LOGIN_PASSWORD_CHANGED("修改登录密码"),

    /**
     * 修改支付密码
     */
    PAY_PASSWORD_CHANGED("修改支付密码"),

    /**
     * 绑定谷歌认证器
     */
    BIND_GOOGLE_AUTH("绑定谷歌认证器"),

    /**
     * 重置绑定谷歌认证器
     */
    RESET_BIND_GOOGLE_AUTH("重置绑定谷歌认证器"),

    /**
     * 密码登陆
     */
    LOGIN_WITH_PASSWORD("密码登陆"),

    /**
     * 验证码登陆
     */
    LOGIN_WITH_OTP("验证码登陆"),

    /**
     * 登出
     */
    LOGOUT("登出"),

    /**
     * 实名认证
     */
    RELE_NAME("实名认证"),

    /**
     * 修改个人资料
     */
    MODIFY_PROFILE("修改个人资料"),

    /**
     * 忘记登陆密码
     */
    FORGOT_LOGIN_PASSWORD("忘记登陆密码"),
    ;

    private String description;

    UmsMemberChangeEventEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

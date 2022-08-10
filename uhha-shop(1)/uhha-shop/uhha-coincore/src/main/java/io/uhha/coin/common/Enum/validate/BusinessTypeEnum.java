package io.uhha.coin.common.Enum.validate;

/**
 * 模板业务类型枚举
 * Created by ZKF on 2017/5/4.
 */
public enum BusinessTypeEnum {

    /**
     * 短信
     */
    SMS_APPLY_API(101, "API申请", "Api application"),
    SMS_BING_MOBILE(102, "绑定手机", "Bind mobile"),
    SMS_UNBIND_MOBILE(103, "解绑手机", "Unbind mobile"),
    SMS_CNY_WITHDRAW(104, "法定货币提现", "Fiat withdraw"),
    SMS_COIN_WITHDRAW(105, "虚拟币提现", "Crypto withdraw"),
    SMS_MODIFY_LOGIN_PASSWORD(106, "修改登录密码", "Modify login password"),
    SMS_MODIFY_TRADE_PASSWORD(107, "修改交易密码", "Modify payment password"),
    SMS_COIN_WITHDRAW_ACCOUNT(108, "虚拟币提现地址设置", "Config crypto address"),
    SMS_FIND_PHONE_PASSWORD(109, "找回登录密码-手机找回", "Find login password by phone"),
    SMS_CNY_ACCOUNT_WITHDRAW(110, "设置人民币提现账号", "Config fiat withdraw account"),
    SMS_WEB_REGISTER(111, "网页端注册帐号", "Web register"),
    SMS_APP_REGISTER(112, "手机端注册帐号","App register"),
    SMS_PUSHASSET(113, "PUSH资产", ""),
    SMS_FINANCES(114, "存币理财",""),
    SMS_TRANSFER(115, "资产转移","Asset transfer"),
    SMS_PRICE_CLOCK(116, "价格闹钟", ""),
    SMS_NEW_IP_LOGIN(117, "新IP登陆",""),
    SMS_CNY_RECHARGE(118, "人民币充值","Fiat recharge"),
    SMS_RISKMANAGE(119, "风控短信", "Risk reminder"),
    SMS_FIND_EMAIL_PASSWORD(120, "找回登录密码-邮箱找回","Find login password by email"),
    SMS_MODIFY_LOGIN_REMIND(121, "修改登录密码-短信提醒", "Modify login password SMS reminder"),
    SMS_MODIFY_TRADE_REMIND(122, "修改交易密码-短信提醒", "Modify payment password SMS reminder"),
    SMS_LOGIN(123,"手机登录验证码", "Login with SMS OTP"),
    SMS_PAYMENT(124,"购买成功-通知用户付款",""),
    SMS_RECEIVABLE(125,"付款成功-通知用户收款",""),
    SMS_MODIFY_API(126, "API修改",""),
    SMS_RESET_API(127, "API重置",""),
    SMS_GOOGLE_DESABLE(128,"解绑谷歌验证", "Disable Google Authenticator"),
    SMS_GOOGLE_CLOSE(129,"关闭谷歌验证", "Turn off Google Authenticator"),
    SMS_GOOGLE_OPEN(130,"开启谷歌验证", "Turn on Google Authenticator"),
    SMS_STORE_REGISTER(131,"商户开户", "Open store"),

    SMS_FAIT_WITHDRAW_NOTIFY(132, "提现通知", "Fiat withdraw reminder"),

    /**
     *    商户修改登录密码
     */
    SMS_STORE_FIND_PHONE_PASSWORD(133,"商户修改登录密码", "Store find login password"),

    /**
     * 邮件
     */
    EMAIL_VALIDATE_BING(201, "绑定邮件", "Binding Email"),
    EMAIL_FIND_PASSWORD(202, "找回登录密码", "Find login password with Email"),
    EMAIL_REGISTER_CODE(203, "注册验证码", "Register code"),
    EMAIL_PRICE_CLOCK(204, "价格闹钟", ""),
    EMAIL_NEW_IP_LOGIN(205, "登录IP异常", ""),
    EMAIL_LOGIN(206,"邮箱登录验证码", "Email login password"),
    EMAIL_PAYMENT(207,"邮箱通知购买成功-通知用户付款", "Payment email reminder"),
    EMAIL_RECEIVABLE(208,"邮箱通知付款成功-通知用户收款", ""),
	APP_EMAIL_FIND_PASSWORD(209, "APP-找回登录密码", "App find login password"),
    EMAIL_APPLY_API(210, "邮箱API申请", ""),
    EMAIL_MODIFY_API(211, "邮箱API修改", ""),
    EMAIL_RESET_API(212, "邮箱API重置", ""),
    EMAIL_GOOGLE_DESABLE(213,"邮箱解绑谷歌验证", "Disable Google Authenticator"),
    EMAIL_GOOGLE_CLOSE(214,"邮箱关闭谷歌验证", "Turn off Google Authenticator"),
    EMAIL_GOOGLE_OPEN(215,"邮箱开启谷歌验证", "Turn on Google Authenticator"),

    /**
     * 用户下单
     */
    ORDER_SUBMITTED(301,"用户下单", "Order submission"),

    /**
     * 用户支付成功
     */
    ORDER_PAID(302,"用户支付成功", "Order paid"),

    /**
     * 发货成功
     */
    ORDER_DELIVER(303,"发货成功", "Order shipped"),

    /**
     * 收货成功
     */
    ORDER_RECEIVED(304,"收货成功", "Order received"),

    /**
     * 退款申请
     */
    ORDER_REFUND_REQ(305,"退款申请", "Refund request"),

    /**
     * 退款成功
     */
    ORDER_REFUND_SUCCEED(306,"退款成功", "Refund succeed"),

    /**
     * 退货申请
     */
    ORDER_RETURN_REQ(307,"退货申请", "Return request"),

    /**
     * 退货成功
     */
    ORDER_RETURN_SUCCEED(308,"退货成功", "Return succeed"),

    /**
     * 订单取消
     */
    ORDER_CANCELLED(309,"订单取消", "Order cancelled"),

    /**
     * 充值订单取消
     */
    ORDER_DEPOSIT_CANCELLED(310,"充值订单取消", "Deposit cancelled"),

    SMS_COMMISSION_TO_BALANCE(311,"用户从推广手续费余额转到可用余额", "Transfer commission to balance"),

    /**
     * 提现审批通过
     */
    WITHDRAW_APPROVED(312,"提现审批通过", "Withdraw approved"),

    /**
     * 提现拒绝
     */
    WITHDRAW_REJECTED(313,"提现拒绝", "Withdraw rejected"),


    /**
     * 提现已给付
     */
    WITHDRAW_RELEASED(314,"提现已给付", "Money sent"),

    /**
     * 兑换积分为Token
     */
    REDEEM_POINTS_TOKEN(315,"兑换积分为Token", "Redeem points to token"),

    /**
     * 视频审核通过
     */
    VIDEO_SCREEN_APPROVED(400,"视频审核通过", "Video screening passed"),

    /**
     * 视频审核拒绝
     */
    VIDEO_SCREEN_REJECTED(401,"视频审核拒绝", "Video screening rejected"),

    /**
     * 实名审核通过
     */
    REALNAME_SCREEN_APPROVED(402,"实名审核通过", "Realname screening passed"),

    /**
     * 实名审核拒绝
     */
    REALNAME_SCREEN_REJECTED(403,"实名审核拒绝", "Realname screening rejected"),

    /**
     * 收到评论
     */
    VIDEO_COMMENTED(404,"视频收到评论", "Video was commented")

    ;


    private Integer code;
    /**
     * Chinese description
     */
    private String value;

    /**
     * English description
     */
    private String eValue;

    BusinessTypeEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    BusinessTypeEnum(Integer code, String value, String eValue) {
        this.code = code;
        this.value = value;
        this.eValue = eValue;
    }

    public static String getCnValueByCode(Integer code) {
        for (BusinessTypeEnum e : BusinessTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e.getValue();
            }
        }
        return null;
    }

    public static String getEnValueByCode(Integer code) {
        for (BusinessTypeEnum e : BusinessTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e.geteValue();
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

    public String geteValue() {
        return eValue;
    }

    public void seteValue(String eValue) {
        this.eValue = eValue;
    }
}

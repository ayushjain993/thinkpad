package io.uhha.coin.common.Enum.validate;


/**
 * 参数类型枚举
 * Created by ZKF on 2017/5/4.
 */
public enum ParameterTypeEnum {

    CODE(1, "#code#", "验证码"),
    DATE(2, "#date#", "时间"),
    SIGN(3, "#sign#", "签名"),
    UUID(4, "#uuid#", "UUID"),
    USER(5, "#user#", "用户名"),
    TYPE(6, "#type#", "充提类型"),
    PRICE(7, "#price#", "价格"),
    USERPRICE(8, "#userPrice#", "用户设置价格"),
    AMOUNT(9, "#amount#", "总量"),
    COIN(10, "#coin#", "币种"),
    DOMAIN(11, "#domain#", "域名"),
    UID(12, "#uid#", "UID"),
    PLATFORM(13, "#platform#", "平台"),
    IPADDRESS(14, "#ipAddress#", "IP地址"),
    language(15,"#language#","语言ID"),
    ORDERNO(16, "#orderNo#","订单号"),
    LOGISTIC_COMPANY_NAME(17, "#logisticCompanyName#","快递公司名称"),
    WAY_BILL_CODE(18, "#wayBillCode#","运单号"),

    //管理员审批相关
    VIDEO_TITLE(30, "#videoTitle#","视频标题"),
    ADMIN_APPROVAL_REASON(31, "#reason#","审批结果留言"),
    //视频评论相关
    COMMENT(32,"#comment#","视频评论")
    ;

    private Integer code;
    private String value;
    private String str;

    ParameterTypeEnum(Integer code, String value, String str) {
        this.code = code;
        this.value = value;
        this.str = str;
    }

    public static String getValueByCode(Integer code) {
        for (ParameterTypeEnum e : ParameterTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e.getValue();
            }
        }
        return null;
    }

    public static String getStrByCode(Integer code) {
        for (ParameterTypeEnum e : ParameterTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e.getStr();
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

    public String getStr() {
        return str;
    }
}

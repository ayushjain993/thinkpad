package io.uhha.util;

/**
 * Created by mj on 17/5/2.
 * 公共常量类
 */
public interface CommonConstant {

    /**
     * 开始页
     */
    String START_ROW_NUM = "startRowNum";

    /**
     * 每页大小
     */
    String PAGE_SIZE = "pageSize";

    /**
     * store端session放置手机验证码的key(代客下单)
     */
    String STORE_SHOPPING_CODE_KEY = "STORE_SHOPPING_CODE_KEY";

    /**
     * store端session放置验证标记key(代客下单)
     */
    String STORE_SHOPPING_MOBILE_KEY = "STORE_SHOPPING_VALIDFLAG_KEY";

    /**
     * 加号替换
     */
    String PLUS_REPLACE = "5lec2shop0";
    /**
     * 从pc端发出
     */
    String FROM_PC = "FROM_PC";

    /**
     * admin端店铺id默认值0
     */
    String ADMIN_STORENAME = "平台";
    Long ADMIN_STOREID = 0L;

    /**
     * 查询的时候不需要带店铺id过滤
     */
    Long QUERY_WITH_NO_STORE = -1L;

    /**
     * 查询的收货不需要带用户id进行过滤
     */
    Long QUERY_WITH_NO_CUSTOMER = -1L;

    /**
     * 评论、回复不根据ISSHOW显示
     */
    Long QUERY_WITH_NO_ISSHOW = -1L;
    /**
     * 评论、回复根据ISSHOW显示
     */
    Long QUERY_WITH_ISSHOW = 1L;

    /**
     * 主题不根据ISUSE显示
     */
    Long QUERY_WITH_NO_ISUSE = -1L;
    /**
     * 主题根据ISUSE显示
     */
    Long QUERY_WITH_ISUSE = 1L;
    /**
     * 文章不根据columnId显示
     */
    Long QUERY_WITH_NO_COLUMNID = -1L;
    /**
     * 文章根据ISRELEASE显示
     */
    Long QUERY_WITH_ISRELEASE = 1L;
    /**
     * 验证通过标记
     */
    String PASS_FLAG = "PASS";

    /**
     * 未登录状态下的会员id
     */
    Long NO_LOGIN_CUSTOMERID = -1L;

    /**
     * 登录成功后把用户信息放入session的key
     */
    String MOBILE_LOGIN_SESSION_KEY = "MOBILE_LOGIN_SESSION_KEY";

    /**
     * 微信授权后把openId放入session的key(微信内置浏览器)
     */
    String MOBILE_OPENID_SESSION_KEY = "MOBILE_OPENID_SESSION_KEY";
    /**
     * 微信授权后把unionId放入session的key(微信内置浏览器)
     */
    String MOBILE_UNIONID_SESSION_KEY = "MOBILE_UNIONID_SESSION_KEY";

    /**
     * mobile端session放置手机验证码的key(注册)
     */
    String MOBILE_REGISTER_CODE_KEY = "MOBILE_REGISTER_CODE_KEY";

    /**
     * mobile端session放置手机验证码的key(忘记密码)
     */
    String MOBILE_FORGETPWD_CODE_KEY = "MOBILE_FORGETPWD_CODE_KEY";
    /**
     * mobile端session放置手机验证码的key(绑定新手机_旧手机验证码)
     */
    String MOBILE_BINDNEWMOBILE_CODE_KEY = "MOBILE_BINDNEWMOBILE_CODE_KEY";
    /**
     * mobile端session放置手机验证码的key(绑定新手机_新手机验证码)
     */
    String MOBILE_BINDNEWMOBILE_NEWMOBILE_CODE_KEY = "MOBILE_BINDNEWMOBILE_NEWMOBILE_CODE_KEY";
    /**
     * mobile端session放置验证标记key(绑定新手机)
     */
    String MOBILE_BINDNEWMOBILE_VALIDFLAG_KEY = "MOBILE_BINDNEWMOBILE_VALIDFLAG_KEY";
    /**
     * mobile端session放置手机验证码的key(更新支付密码)
     */
    String MOBILE_UPDATEPAYPWD_CODE_KEY = "MOBILE_UPDATEPAYPWD_CODE_KEY";
    /**
     * mobile端session放置手机验证码的key(更新密码)
     */
    String MOBILE_UPDATEPWD_CODE_KEY = "MOBILE_UPDATEPWD_CODE_KEY";

    /**
     * 支付宝支付
     */
    String ALI_PAY = "支付宝";
    /**
     * 微信支付
     */
    String WECHAT_PAY = "微信";
    /**
     * 银联支付
     */
    String UNION_PAY = "银联";

    /**
     * 2c2p支付
     */
    String P_2C2P_PAY = "2c2p";

    /**
     * Dlocal支付
     */
    String DLOCAL_PAY = "dlocal";

    /**
     * ZotaPay支付
     */
    String ZOTA_PAY = "zotapay";

    /**
     * 抢购代号
     */
    String PANIC_BUY = "3";

    /**
     * 未过期的活动标记
     */
    String NOT_PAST = "999";

    /**
     * 没有用户id
     */
    Long NO_CUSTOMER_ID = -1L;

    /**
     * 还没使用抢购
     */
    int NO_USE_PANIC = -1;

    /**
     * 没有抢购促销
     */
    int NO_PANIC_MARKETING = -2;

    /**
     * 没有商品试用申请
     */
    int NO_SKU_TRY_APPLY = -1;

    /**
     * 平台关注人数
     */
    int ADMIN_FOLLOW_NUM = 10000;


    /**
     * 小程序端redis放置手机验证码的key(注册)
     */
    String APPLET_REGISTER_CODE_KEY = "APPLET_REGISTER_CODE_KEY";


    /**
     * 与会员价互斥
     */
    long MEMBER_PRICE_EXCLUSION = 2L;

    /**
     * 与商品组合互斥
     */
    long COMBINATION_EXCLUSION = 1L;

    /**
     * 与批发单品互斥
     */
    long BATCH_SKU_EXCLUSION = 3L;


    /**
     * 上传的图片
     */
    String UPLOAD_PIC = "0";

    /**
     * 上传的视频
     */
    String UPLOAD_VIDEO = "1";

    /**
     * 门店订单
     */
    String STORE_ORDER = "1";

    /**
     * 普通订单
     */
    String ORDINARY_ORDER = "2";

    /**
     * 预存款订单
     */
    String PREALSE_ORDER = "3";

    /**
     * 二级分销
     */
    String SECOND_COMMISSION_LEVEL = "2";

    /**
     * 抢购促销图片类型
     */
    String PANIC_MARKETING_PIC_TYPE = "1";

    /**
     * 预售促销图片类型
     */
    String PRESALE_MARKETING_PIC_TYPE = "2";

    /**
     * 拼团促销图片类型
     */
    String GROUP_MARKETING_PIC_TYPE = "3";

    /**
     * 试用促销图片类型
     */
    String TRY_MARKETING_PIC_TYPE = "4";

    /**
     * 秒杀促销显示
     */
    String PANIC_SHOW = "1";

    /**
     * 秒杀促销不显示
     */
    String PANIC_NOT_SHOW = "0";

    /**
     * 预售分类
     */
    String PRESALE_MARKETING_CATE = "1";

    /**
     * 拼团分类
     */
    String GROUP_MARKETING_CATE = "2";

    /**
     * 试用分类
     */
    String TRY_MARKETING_CATE = "3";

    /**
     * 定金预售类型
     */
    String DEPOSIT_PRESALE_TYPE = "1";

    /**
     * 全款预售类型
     */
    String FULL_PRESALE_TYPE = "2";

    /**
     * 系统交易账户
     */
    String SYSTEM_TRADING = "SYSTEM_TRADING";

    /**
     * 系统结算账户
     */
    String SYSTEM_SETTLEMENT = "SYSTEM_SETTLEMENT";

    /**
     * 系统手续费账户
     */
    String SYSTEM_HANDLING_FEE = "SYSTEM_HANDLING_FEE";
    String ADMIN = "admin";

    /**
     * 系统账户号前缀
     */
    public static final String SYSTEM_ACCOUNT_PREFIX = "10";

    /**
     * 商铺账户号前缀
     */
    public static final String STORE_ACCOUNT_PREFIX = "20";

    /**
     * 系个人账户号前缀
     */
    public static final String PERSONAL_ACCOUNT_PREFIX = "60";

    /**
     * 币种前缀
     */
    public static final String CNY_PREFIX = "02";
    public static final String USD_PREFIX = "01";
}

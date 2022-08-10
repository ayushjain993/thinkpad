package io.uhha.common.core.redis;

/**
 * Redis前缀
 *
 * @author TT
 */
public interface RedisConstant {

    /**
     * 行情 key前缀规范
     */
    public static String TICKERE_KEY = "TICKER_";

    /**
     * 行情 key前缀规范
     */
    public static String THREETICKERE_KEY = "THREETICKER_";

    /**
     * kline key前缀规范
     */
    public static String KLINE_KEY = "KLINE_";

    /**
     * lastkline key前缀规范
     */
    public static String LASTKLINE_KEY = "LASTKLINE_";

    /**
     * 买深度 key前缀规范
     */
    public static String BUYDEPTH_KEY = "BUYDEPTH_";

    /**
     * 卖深度 key前缀规范
     */
    public static String SELLDEPTH_KEY = "SELLDEPTH_";

    /**
     * 最新成交 key前缀规范
     */
    public static String SUCCESSENTRUST_KEY = "SUCCESSENTRUST_";

    /**
     * token key前缀规范
     */
    public static String TOKEN_KEY = "TOKEN_";

    /**
     * 用户 key前缀规范
     */
    public static String USER_KEY = "USER_";

    /**
     * 验证 key前缀规范
     */
    public static String VALIDATE_KEY = "VALIDATE_CODE_";


    /**
     * 绑定 key前缀规范
     */
    public static String Bind_KEY = "BIND_CODE_";

    /**
     * 委单 key前缀规范
     */
    public static String ENTRUST_KEY = "ENTRUST_";

    /**
     * 行情 key前缀规范
     */
    public static String MARKET_KEY = "MARKET_";

    /**
     * 定时器 key前缀规范
     */
    public static String JOB_KET = "JOB_";

    /**
     * web文章总页面 key前缀规范
     */
    public static String WEB_ARTICLECOUNT_KET = "ARTICLEPAGECOUNT";

    /**
     * app文章 key前缀规范
     */
    public static String APP_ARTICLECOUNT_KET = "ARTICLEAPPPAGECOUNT";

    /**
     * web文章 key前缀规范
     */
    public static String WEB_ARTICLE_KET = "WEBARTICLE_";

    /**
     * app文章 key前缀规范
     */
    public static String APP_ARTICLE_KET = "APPBARTICLE_";

    /**
     * web文章详情 key前缀规范
     */
    public static String WEB_ARTICLEDETAIL_KET = "WEBARTICLEDETAIL_";

    /**
     * app文章详情 key前缀规范
     */
    public static String APP_ARTICLEDETAIL_KET = "APPARTICLEDETAIL_";

    /**
     * 文章类型列表 key前缀规范
     */
    public static String ARTICLE_TYPELIST_KET = "ARTICLETYPES";

    /**
     * 文章类型 key前缀规范
     */
    public static String ARTICLE_TYPE_KET = "ARTICLETYPE_";

    /**
     * 关于我们列表 key前缀规范
     */
    public static String ABOUT_LIST_KET = "ABOUTS";

    /**
     * 关于我们 key前缀规范
     */
    public static String ABOUT_KET = "ABOUT_";

    /**
     * 关于我们类型列表 key前缀规范
     */
    public static String ABOUT_TYPELIST_KET = "ABOUTTYPES";

    /**
     * 关于我们类型 key前缀规范
     */
    public static String ABOUT_TYPE_KET = "ABOUTTYPE_";

    /**
     * 网站前台所有的系统参数
     */
    public static String ARGS_KET_WEB = "WEBARGSLIST";

    /**
     * 系统参数 key前缀规范
     */
    public static String ARGS_KET = "ARGS_";

    /**
     * 语言类型 key前缀规范
     */
    public static String LANGUAGE_KET = "LANGUAGE_";
    /**
     * 语言类型 key前缀规范
     */
    public static String LANGUAGE_LIST_KET = "LANGUAGELIST_";

    /**
     * 币种类型 key前缀规范
     */
    public static String COIN_LIST_KEY = "COINS";

    /**
     * 币种类型 key前缀规范
     */
    public static String COIN_KEY = "COIN_";

    /**
     * 交易类型 key前缀规范
     */
    public static String TRADE_LIST_KEY = "TRADES";

    /**
     * 交易类型 key前缀规范
     */
    public static String TRADE_KEY = "TRADE_";

    /**
     * 网站基础 key前缀规范
     */
    public static String WEBINFO_KEY = "WEBINFO";


    /**
     * 系统充值银行状态 key前缀规范
     */
    public static String SYS_RECHARGEBANKSTART_KEY = "RECHARGEBANKSTART_";

    /**
     * 系统充值银行类型 key前缀规范
     */
    public static String SYS_RECHARGEBANK_KEY = "RECHARGEBANK_";

    /**
     * 系统充值银行(id) key前缀规范
     */
    public static String SYS_RECHARGEBANKID_KEY = "RECHARGEBANKID_";

    /**
     * 系统提现银行 key前缀规范
     */
    public static String SYS_WITHDRAWBANK_KEY = "WITHDRAWBANK_";

    /**
     * 系统提现银行 key前缀规范
     */
    public static String SYS_WITHDRAWBANKLIST_KEY = "WITHDRAWBANKS";

    /**
     * 人民币提现手续费 key前缀规范
     */
    public static String SYS_WITHDRAWCNYFEES_KEY = "WITHDRAWCNYFEES_";

    /**
     * 虚拟币手续费 key前缀规范(币种+等级)
     */
    public static String SYS_VIRTUALCOINFEES_KEY = "VIRTUALCOINFEES_";

    /**
     * 是否输入交易密码 key前缀规范
     */
    public static String TRADE_NEED_PASSWORD = "TRADENEEDPASSWORD";

    /**
     * 用户登录
     */
    public static String ACCOUNT_LOGIN_TOTAL_KEY = "BCACCOUNT_LOGIN_";


    /**
     * 用户APP登录
     */
    public static String APP_LOGIN_TOTAL_KEY = "BCAPP_LOGIN_";

    /**
     * 用户签名
     */
    public static String ACCOUNT_SIGN__KEY = "BCSIGN_";

    /**
     * 找回密码的信息的标志位
     */
    public static String SETREDISINFO_KEY = "SETREDISINFO_";

    /**
     * 图片验证码的标志位
     */
    public static String VERIFYCODE_KEY = "VERIFYCODE_";

    /**
     * 友情链接标志位
     */
    public static String FRIEND_LINK = "FRIENDLINK_";

    /**
     * API访问限制
     */
    public static String APIACCESSLIMIT = "APIACCESSLIMIT_";

    /**
     * 众筹支付签名
     */
    public static String ICO_PAY_SIGN__KEY = "ICOPAYSIGN_";

    /**
     * 券商
     */
    public static String AGENT_LIST_KEY = "AGENTLIST";

    /**
     * 券商
     */
    public static String AGENT_KEY = "AGENT_";

    /**
     * 微天使
     */
    public static String WEAGNEL_KEY = "WEANGEL_DATA";

    /**
     * 价格闹钟短信
     */
    public static String PRICECLOCK_SMS_KEY = "PRICECLOCK_SMS_KEY";

    /**
     * 价格闹钟邮件
     */
    public static String PRICECLOCK_EMAIL_KEY = "PRICECLOCK_EMAIL_KEY";

    /**
     * CountLimit访问限制
     */
    public static String COUNTLIMIT_KEY = "COUNTLIMIT_KEY_";

    /**
     * 存币理财设置
     */
    public static String FINANCES_KEY = "FINANCES_KEY";

    /**
     * 广告商访问统计
     */
    public static String ADS_SOURCE_KEY = "ADS_SOURCE_";

    /**
     * 广告商缓存
     */
    public static String ADS_KEY = "ADS_";

    /**
     * 广告商缓存
     */
    public static String ADS_LIST_KEY = "ADS_LIST";

    /**
     * ICO缓存
     */
    public static String ICODETAIL_LIST_KEY = "ICODETAIL_LIST_KEY";

    /**
     * ICO缓存
     */
    public static String ICODETAIL_KEY = "ICODETAIL_";
    /**
     * 投资人问卷
     */
    public static String FQAS_USER_KEY = "FQAS_USER_KEY_";

    /**
     * 验证发送明细统计
     */
    public static String STATISTICS_VALIDATE_KEY = "STATISTICS_VALIDATE_";

    /**
     * 邮件
     */
    public static String ACTIVE_EMAIL_SETTING = "ACTIVE_EMAIL_SETTING";
    /**
     * 短信
     */
    public static String ACTIVE_SMS_SETTING = "ACTIVE_SMS_SETTING";
    /**
     * 验证模板
     */
    public static String VALIDATE_TEMPLATE = "VALIDATE_TEMPLATE_";

    /**
     * 代理充值号
     */
    public static String RECHARGEAGENCY_KEY = "RECHARGEAGENCY_KEY";

    /**
     * @author windy
     * UNIQUE_ADDRESS_{coinId} bts钱包类，平台统一地址
     */
    public static String UNIQUE_ADDRESS = "UNIQUE_ADDRESS_";

    /**
     * C2C币种
     */
    public static String C2CCOIN_KEY = "C2CCOIN_KEY";

    /**
     * 注册商家
     */
    public static String C2CSELLER_KEY = "C2CSELLER_KEY";

    /**
     * 刷单参数key
     */
    public static String SCALP_KEY = "SCALP_KEY";

    /**
     * 获取聚合行情(Ticker)
     */
    public static String HUOBI_MERGED = "HUOBI_MERGED";

    /**
     * 获取 Market Depth 数据
     */
    public static String HUOBI_DEPTH = "HUOBI_DEPTH";

    /**
     * 批量获取最近的交易记录
     */
    public static String HUOBI_TRADE = "HUOBI_TRADE";

    /**
     * 批量获取最近的交易记录
     */
    public static String ZHONGBI_TRADE = "ZHONGBI_TRADE";

    /**
     * 获取K线数据
     */
    public static String HUOBI_KLINE = "HUOBI_KLINE";

    /**
     * 中币深度
     */
    public static String ZHONGBI_DEPTH = "ZHONGBI_DEPTH";

    /**
     * 获取K线数据
     */
    public static String ZHONGBI_KLINE = "ZHONGBI_KLINE";
    /**
     * 涨跌
     */
    public static String HUOBI_ROSE = "HUOBI_ROSE";


    /**
     * 涨跌
     */
    public static String ZHONGBI_ROSE = "ZHONGBI_ROSE";
    /**
     * 用户API
     */
    public static String API_USER = "API_USER";

    /**
     * API_symbol
     */
    public static String API_SYMBOL = "API_SYMBOL";

    /**
     * api_fapi实体信息
     */
    public static String API_FAPI = "API_FAPI";


    public static String ZHONGBI_MERGED = "ZHONGBI_MERGED";

    /**
     * 聚合汇率
     */
    public static String RATE_USDT = "RATE_USDT";

    /***
     * 注册邀请排行榜
     */
    public static String REGISTERED_MERGED = "REGISTERED_MERGED";

    /***
     * 公司信息
     */
    public static String COMPANY_MERGED = "COMPANY_MERGED";

    public static String FC2CCOIN_KEY = "FC2CCOIN_KEY";

    /**
     * 新C2C币价格
     */
    public static String FC2CCOINPRICE_KEY = "FC2CCOIN_PRICE_KEY";

    /**
     * 国家
     */
    public static String COUNTRIES = "COUNTRIES";

    /**
     * 物流企业列表
     */
    public static String LOGISTIC_COMPANIES = "LOGISTIC_COMPANIES";


    /**
     * mobile端首页缓存key
     */
    public static String MOBILE_TEMPLATE = "MOBILE_TEMPLATE";

    /**
     * 地区缓存key
     */
    public static String AREA = "AREA";

    /**
     * 基本信息设置缓存key
     */
    public static String BASE_INFO_SET = "BASE_INFO_SET";

    /**
     * pc端首页缓存key
     */
    public static String PC_TEMPLATE = "PC_TEMPLATE";


    // pc端店铺首页模版缓存key
    public static String STORE_PC_TEMPLATE = "STORE_PC_TEMPLATE";

    /**
     * 手机端专题页缓存key
     */
    public static String MOBILE_THEMATIC = "MOBILE_THEMATIC";

    /**
     * PC端专题页缓存key
     */
    public static String PC_THEMATIC = "PC_THEMATIC";

    /**
     * APP端首页模版缓存key
     */
    public static String APP_INDEX_TEMPLATE = "APP_INDEX_TEMPLATE";

    /**
     * APP 专题页缓存
     */
    public static String APP_THEMATIC = "APP_THEMATIC";

    /**
     * APPLET 端首页模版缓存key
     */
    public static String APPLET_INDEX_TEMPLATE = "APPLET_INDEX_TEMPLATE";

    /**
     * APPLET 专题页缓存
     */
    public static String APPLET_THEMATIC = "APPLET_THEMATIC";


    /**
     * 促销设置缓存key
     */
    public static String MARKETING_SETTING = "MARKETING_SETTING";

    /**
     * 微信用户关联缓存key
     */
    public static String WE_CHAT_CUSTOMER_LINK = "WE_CHAT_CUSTOMER_LINK";

    /**
     * app版本
     */
    public static String APP_VERSION = "APP_VERSION";
    /**
     * 社区团购设置
     */
    public static String COMMUNITY_BUY_SETTING = "COMMUNITY_BUY_SETTING";
    /**
     * 社区推广
     */
    public static String COMMUNITY_BUY_INDEX = "COMMUNITY_BUY_INDEX";

    ///////////////////////////////////////////////////////////////////////
    // 推荐商品
    /**
     * 推荐专题KEY
     */
    public static final String RECOMMEND_SUBJECT = "RECOMMEND_SUBJECT";

    /**
     * 推荐品牌KEY
     */
    public static final String RECOMMEND_BRAND = "RECOMMEND_BRAND";

    /**
     * 推荐商品KEY
     */
    public static final String RECOMMEND_PRODUCT = "RECOMMEND_PRODUCT";

    /**
     * 新品KEY
     */
    public static final String NEW_PRODUCT = "NEW_PRODUCT";

    /**
     * BANNERKEY
     */
    public static final String BANNER_AD = "BANNER_AD";

    /**
     * 所有一级分类和二级分类 KEY
     */
    public static final String ALL_1_2_CATEGORY = "ALL_1_2_CATEGORY";

    /**
     * SPU详情
     */
    public static final String SPU_DETAIL = "SPU_DETAIL_";

    /**
     * GOODS详情
     */
    public static final String GOODS_DETAIL = "GOODS_DETAIL_";

    ///////////////////////////////////////////////////////////////////////
    // 短视频
    /**
     * 话题KEY
     */
    public static final String TOPIC_TAG = "TOPIC_";

    /**
     * 今日的PVUV记录
     */
    public static final String PVUV_TODAY = "PVUV_TODAY_";

    //人民币兑美元汇率
    public static final String USD_CNY_RATE = "USD_CNY_RATE";

}

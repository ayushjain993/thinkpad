package io.uhha.order.service.impl;


import com.alibaba.fastjson.JSON;
import io.uhha.common.enums.AlipayResponseCode;
import io.uhha.common.enums.OrderTypeEnum;
import io.uhha.common.enums.P2c2pResponseCode;
import io.uhha.common.enums.PaymentTypeEnum;
import io.uhha.common.exception.ServiceException;
import io.uhha.common.utils.*;
import io.uhha.common.utils.bean.*;
import io.uhha.member.domain.UmsMember;
import io.uhha.member.domain.UmsPreDepositRecord;
import io.uhha.member.domain.WeChatCustomerLink;
import io.uhha.member.service.IUmsMemberService;
import io.uhha.member.service.IUmsPreDepositRecordService;
import io.uhha.member.service.WeChatCustomerLinkService;
import io.uhha.order.OrderPayService;
import io.uhha.order.domain.OmsOrder;
import io.uhha.order.service.IOmsOrderService;
import io.uhha.order.service.OrderServiceApi;
import io.uhha.order.service.PredepositRecordServiceApi;
import io.uhha.order.service.StoreOrderServiceApi;
import io.uhha.order.vo.ConfirmOrderParams;
import io.uhha.order.vo.OrderItem;
import io.uhha.order.vo.PayParam;
import io.uhha.setting.bean.*;
import io.uhha.setting.service.BaseInfoSetService;
import io.uhha.setting.service.ILsPaySettingService;
import io.uhha.store.domain.TStoreOrder;
import io.uhha.store.service.ITStoreOrderService;
import io.uhha.util.CommonConstant;
import io.uhha.util.CommonResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by mj on 17/11/10.
 * 订单支付接口实现
 */
@Service
public class OrderPayServiceImpl implements OrderPayService {

    String appCallBackDomain = "";
    /**
     * 调试日志
     */
    private Logger logger = LoggerFactory.getLogger(OrderPayServiceImpl.class);
    /**
     * 注入订单服务接口
     */
    @Autowired
    private IOmsOrderService orderService;
    /**
     * 注入订单混合服务接口
     */
    @Autowired
    private OrderServiceApi orderServiceApi;
    /**
     * 用户服务接口
     */
    @Autowired
    private IUmsMemberService customerService;
    /**
     * 注入支付设置服务
     */
    @Autowired
    private ILsPaySettingService paySetService;
    /**
     * 注入网站基础信息服务
     */
    @Autowired
    private BaseInfoSetService baseInfoSetService;
    /**
     * 预存款纪录服务接口
     */
    @Autowired
    private IUmsPreDepositRecordService predepositRecordService;
    /**
     * 注入预存款记录服务api接口
     */
    @Autowired
    private PredepositRecordServiceApi predepositRecordServiceApi;
    /**
     * 注入微信用户关联服务
     */
    @Autowired
    private WeChatCustomerLinkService weChatCustomerLinkService;
    /**
     * 注入门店订单服务接口
     */
    @Autowired
    private ITStoreOrderService storeOrderService;
    /**
     * 注入门店订单服务接口
     */
    @Autowired
    private StoreOrderServiceApi storeOrderServiceApi;
    /**
     * 密码工具类
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public int predepositPay(String orderCode, String password, long customerId, int type) {
        logger.debug("predepositPay and orderCode:{} \r\n password:{} \r\n customerId:{} \r\n type:{}", orderCode, password, customerId, type);
        //数据库获取余额支付设置
        PrePaySet prePaySet = paySetService.queryPaySet().getPrePaySet();
        if (!prePaySet.checkIsUse()) {
            logger.error("predepositPay fail due to not used....");
            return -9;
        }

        // 查询用户信息
        UmsMember customer = customerService.queryCustomerInfoById(customerId);

        if (Objects.isNull(customer)) {
            logger.error("predepositPay fail due to member is not ");
            return -1;
        }

        if (Objects.isNull(customer.getPaypassword())) {
            logger.error("predepositPay fail due to member payPassword is null ");
            return -6;
        }

        // 校验用户输入的预存款密码是否正确
        if (!passwordEncoder.matches(password, customer.getPaypassword())) {
            logger.error("predepositPay fail due to password is error...");
            return -2;
        }

        // 订单支付
        if (type == PaymentTypeEnum.ORDER_PAY.getCode()) {

            // 根据订单编号和用户id查询待付款的订单信息
            List<OmsOrder> orders = queryToPayOrders(orderCode, customerId);

            // 没有待支付的订单 则直接返回
            if (CollectionUtils.isEmpty(orders)) {
                logger.error("predepositPay fail due to no order....");
                return -3;
            }

            // 待支付订单的总金额
            BigDecimal orderMoney = getOrdersAllMoeny(orders);

            // 查询用户的总预存款
            BigDecimal customerMoney = predepositRecordService.queryCutomerAllPredeposit(customerId);

            // 判断用户的预存款是否充足
            if (customerMoney.compareTo(orderMoney) < 0) {
                logger.error("predepositPay fail due to member money is not enough ...");
                return -4;
            }

            // 支付成功扣除用户预存款
            predepositRecordService.addPredepositRecord(UmsPreDepositRecord.buildPay(orderMoney, customerId));

            // 修改订单状态
            int result = orderServiceApi.confirmOrderPayed(ConfirmOrderParams.buildCustomerSource(customerId, 1, orders.stream().map(OmsOrder::getOrderCode).collect(Collectors.toList()), "", ""));

            // 修改订单失败 状态回滚
            if (result == 0) {
                throw new ServiceException("R-000008");
            }

            logger.debug("haha ..... pay success......");

            return 1;
        } else if (type == PaymentTypeEnum.STORE_ORDER_PAY.getCode()) {
            // 门店订单支付

            // 根据门店订单编号和用户id查询待付款的订单信息
            List<TStoreOrder> storeOrders = queryToPayStoreOrders(orderCode, customerId);

            // 没有待支付的订单 则直接返回
            if (CollectionUtils.isEmpty(storeOrders)) {
                logger.error("predepositPay fail due to no storeOrders....");
                return -3;
            }

            // 待支付订单的总金额
            BigDecimal orderMoney = storeOrders.stream().map(TStoreOrder::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

            // 查询用户的总预存款
            BigDecimal customerMoney = predepositRecordService.queryCutomerAllPredeposit(customerId);

            // 判断用户的预存款是否充足
            if (customerMoney.compareTo(orderMoney) < 0) {
                logger.error("predepositPay fail due to member money is not enough ...");
                return -4;
            }

            // 支付成功扣除用户预存款
            predepositRecordService.addPredepositRecord(UmsPreDepositRecord.buildPay(orderMoney, customerId));

            // 修改订单的状态为已支付状态
            int result = storeOrderServiceApi.confirmOrderPayed(customerId, storeOrders.stream().map(TStoreOrder::getOrderCode).collect(Collectors.toList()), "1", "", "");

            // 修改订单失败 状态回滚
            if (result == 0) {
                throw new ServiceException("R-000008");
            }

            logger.debug("haha ..... pay success......");

            return 1;
        } else {
            logger.error("predepositPay fail due to type unknow,...");
            return -3;
        }
    }

    @Override
    public CommonResponse<String> aliAppPay(String orderCode, long customerId, int type) {
        logger.debug("aliAppPay and orderCode:{} \r\n customerId:{} \r\n type:{}", orderCode, customerId, type);

        BaseInfoSet infoSet = getSite();
        // 构造支付信息
        return aliCommonPay(orderCode, customerId, type, AliPaySetting.build("",
                infoSet.getSiteBackendDomainWithBias() + "alipaynotify"), (setting, orderinfo) -> AliPayUtils.appPay(setting, orderinfo));
    }

    @Override
    public CommonResponse<String> aliPagePay(String orderCode, long customerId, int type) {
        logger.debug("aliPagePay and orderCode:{} \r\n customerId:{} \r\n type:{}", orderCode, customerId, type);
        BaseInfoSet infoSet = getSite();
        // 构造支付信息
        return aliCommonPay(orderCode, customerId, type, AliPaySetting.build(infoSet.getPcCallBackDomain() + "paysuccess??type=" + type + "&orderCode=" + orderCode, "api/pc/alipaynotify"), (setting, orderinfo) -> AliPayUtils.pagePay(setting, orderinfo));
    }

    @Override
    public CommonResponse<String> aliWapPay(String orderCode, long customerId, int type, String orderType, String orderId) {
        logger.debug("aliWapPay and orderCode:{} \r\n customerId:{} \r\n type:{} \r\n orderType:{} \r\n orderId:{}", orderCode, customerId, type, orderType, orderId);

        // 同步回调地址
        String callBackUrl = "pages/user/money/success?type=" + type;

        // 如果是社区团购订单 则同步回调地址为订单详情页面
        if (OrderTypeEnum.COMMUNITY_GROUP_ORDER.getCode().equalsIgnoreCase(orderType)) {
            callBackUrl = "tocommunityorderdetail.htm?orderId=" + orderId;
        }
        BaseInfoSet infoSet = getSite();
        logger.debug("siteUrl:{} \r\n siteBackendUrl: {} \r\n pcUrl: {}", infoSet.getSiteUrl(), infoSet.getH5CallBackDomain(), infoSet.getPcCallBackDomain());
        // 构造支付信息
        return aliCommonPay(orderCode, customerId, type, AliPaySetting.build(infoSet.getSiteUrlWithBias() + callBackUrl,
                infoSet.getSiteBackendDomainWithBias() + "alipaynotify"), (setting, orderinfo) -> AliPayUtils.wapPay(setting, orderinfo));
    }


    @Override
    public CommonResponse<WechatPayResponse> wechatQRPay(String orderCode, long customerId, String ip, int type) {
        logger.debug("wechatQRPay and orderCode:{} \r\n customerId:{}  \r\n ip:{} \r\n:type:{}", orderCode, customerId, ip, type);

        // 返回结果
        return weChatCommonPay(orderCode, customerId, type, WechatSetting.build(ip, WechatUtils.QR_PAY, "wechatnotify"), (setting, orderinfo) -> {
            PrepayResult prepayResult = WechatUtils.getPrepay(setting, orderinfo);
            //微信生成预支付信息错误
            if (ObjectUtils.isEmpty(prepayResult)) {
                logger.error("wechatQRPay fail due to wechat error....");
                return CommonResponse.build(null, -5);
            }

            logger.debug("wechatQRPay getPayUrl success......");
            WechatPayResponse wechatQRPayResponse = new WechatPayResponse();
            //扫码支付url
            wechatQRPayResponse.setCodeUrl(prepayResult.getCode_url());
            //订单code
            wechatQRPayResponse.setOrderCode(orderCode);
            //订单金额
            wechatQRPayResponse.setOrderMoney(orderinfo.getPrice());

            return CommonResponse.build(wechatQRPayResponse, 1);
        });
    }

    @Override
    public CommonResponse<WechatPayResponse> wechatH5Pay(String orderCode, long customerId, String ip, int type, String orderType, String orderId) {
        logger.debug("wechatH5Pay and orderCode:{} \r\n customerId:{}  \r\n ip:{} \r\n:type:{} \r\n orderType:{} \r\n orderId:{}", orderCode, customerId, ip, type, orderType, orderId);

        return weChatCommonPay(orderCode, customerId, type, WechatSetting.build(ip, WechatUtils.H5_PAY, "wechatnotify"), (setting, orderinfo) -> {
            //获取预支付信息
            PrepayResult prepayResult = WechatUtils.getPrepay(setting, orderinfo);
            //微信生成预支付信息错误
            if (ObjectUtils.isEmpty(prepayResult)) {
                logger.error("wechatH5Pay fail due to wechat error....");
                return CommonResponse.build(null, -5);
            }

            logger.debug("wechatH5Pay getPayUrl success......");
            WechatPayResponse wechatPayResponse = new WechatPayResponse();


            // 同步回调地址
            String callBackUrl = "paysuccess?type=" + type;


            // 如果是社区团购订单 则同步回调地址为订单详情页面
            if ("8".equals(orderType)) {
                callBackUrl = "tocommunityorderdetail.htm?orderId=" + orderId;
            }
            BaseInfoSet infoSet = getSite();
            wechatPayResponse.setMwebUrl(prepayResult.getMweb_url() + "&redirect_url=" + URLEncoder.encode(infoSet.getH5CallBackDomain() + callBackUrl));

            //订单code
            wechatPayResponse.setOrderCode(orderCode);
            //订单金额
            wechatPayResponse.setOrderMoney(orderinfo.getPrice());

            return CommonResponse.build(wechatPayResponse, 1);
        });
    }


    @Override
    public CommonResponse<PrepayResult> wechatAppPay(String orderCode, long customerId, String ip, int type) {
        logger.debug("wechatAppPay and orderCode:{} \r\n customerId:{}  \r\n ip:{} \r\n:type:{}", orderCode, customerId, ip, type);

        return weChatCommonPay(orderCode, customerId, type, WechatSetting.build(ip, WechatUtils.APP_PAY, "wechatnotify"), (setting, orderinfo) -> {
            PrepayResult prepayResult = WechatUtils.getPrepay(setting, orderinfo);
            //微信生成预支付信息错误
            if (ObjectUtils.isEmpty(prepayResult)) {
                logger.error("wechatAppPay fail due to wechat error....");
                return CommonResponse.build(null, -5);
            }

            logger.debug("wechatAppPay getPayUrl success......");
            return CommonResponse.build(prepayResult, 1);
        });
    }

    @Override
    public CommonResponse<PrepayResult> wechatOfficialAccountPay(String orderCode, long customerId, String ip, int type) {
        logger.debug("wechatOfficialAccountPay and orderCode:{} \r\n customerId:{} \r\n ip:{} \r\n:type:{}", orderCode, customerId, ip, type);

        WeChatCustomerLink weChatCustomerLink = weChatCustomerLinkService.queryWeChatCustomerLinkByCustomerId(customerId);
        if (ObjectUtils.isEmpty(weChatCustomerLink) || StringUtils.isEmpty(weChatCustomerLink.getOpenId())) {
            logger.error("wechatOfficialAccountPay fail due to no weChatCustomerLink or openId is null....");
            return CommonResponse.build(null, -10);
        }

        return weChatCommonPay(orderCode, customerId, type, WechatSetting.build(ip, WechatUtils.OFFICIAL_ACCOUNT_PAY, "wechatnotify").addOpenId(weChatCustomerLink.getOpenId()), (setting, orderInfo) -> {
            //获取预支付信息
            PrepayResult prepayResult = WechatUtils.getPrepay(setting, orderInfo);
            //微信生成预支付信息错误
            if (ObjectUtils.isEmpty(prepayResult)) {
                logger.error("wechatOfficialAccountPay fail due to wechat error....");
                return CommonResponse.build(null, -5);
            }

            logger.debug("wechatOfficialAccountPay getPayUrl success......");
            return CommonResponse.build(prepayResult, 1);
        });
    }

    @Override
    public CommonResponse<PrepayResult> wechatAppletPay(String orderCode, long customerId, String ip, int type) {
        logger.debug("wechatAppletPay and orderCode:{} \r\n customerId:{} \r\n ip:{} \r\n:type:{}", orderCode, customerId, ip, type);

     /*   WeChatCustomerLink weChatCustomerLink = weChatCustomerLinkService.queryWeChatCustomerLinkByCustomerId(customerId);
        if (ObjectUtils.isEmpty(weChatCustomerLink) || StringUtils.isEmpty(weChatCustomerLink.getAppletOpenId())) {
            logger.error("wechatAppletPay fail due to no weChatCustomerLink or appletOpenId is null....");
            return CommonResponse.build(null, -10);
        }*/
        UmsMember member = customerService.queryCustomerInfoById(customerId);
        if (member != null && ObjectUtils.isEmpty(member.getAppletOpenId())) {
            logger.error("wechatAppletPay fail due to wechat error not auth....");
            return CommonResponse.build(null, -10);
        }
        return weChatCommonPay(orderCode, customerId, type, WechatSetting.build(ip, WechatUtils.APPLET_PAY, "wechatnotify").addOpenId(member.getAppletOpenId()), (setting, orderInfo) -> {
            //获取预支付信息
            PrepayResult prepayResult = WechatUtils.getPrepay(setting, orderInfo);
            //微信生成预支付信息错误
            if (ObjectUtils.isEmpty(prepayResult)) {
                logger.error("wechatAppletPay fail due to wechat error....");
                return CommonResponse.build(null, -5);
            }
            return CommonResponse.build(prepayResult, 1);
        });
    }

    @Override
    public CommonResponse<String> unionPagePay(String orderCode, long customerId, int type) {
        logger.debug("unionPagePay and orderCode:{} \r\n customerId:{} \r\n type:{}", orderCode, customerId, type);
        //数据库获取银联支付设置
        UnionPaySet unionPaySet = paySetService.queryPaySet().getUnionPaySet();
        if (!unionPaySet.checkIsUse()) {
            logger.error("unionPagePay fail due to not used....");
            return CommonResponse.build("", -9);
        }

        // 查询用户信息
        UmsMember customer = customerService.queryCustomerWithNoPasswordById(customerId);

        if (Objects.isNull(customer)) {
            logger.error("unionPagePay fail due to member is not ");
            return CommonResponse.build("", -1);
        }

        // 获得支付实体
        OrderInfoForPay orderInfoForPay = getOrderInfoForPay(orderCode, customerId, type);

        // 没有支付实体 直接返回
        if (Objects.isNull(orderInfoForPay)) {
            logger.error("unionPagePay fail due to no order....");
            return CommonResponse.build("", -3);
        }

        String siteUrl = getSiteUrl();
        if (StringUtils.isEmpty(siteUrl)) {
            logger.error("unionPagePay fail due to no siteurl....");
            return CommonResponse.build("", -7);
        }

        if (StringUtils.isEmpty(unionPaySet.getMerchantNum())) {
            logger.error("unionPagePay fail due to MerchantNum is empty....");
            return CommonResponse.build("", -8);
        }
        //银联支付设置
        UnionPaySetting unionPaySetting = new UnionPaySetting();
        //商户号
        unionPaySetting.setMerchantNum(unionPaySet.getMerchantNum());
        //前台同步回调地址
        unionPaySetting.setBeforeCallbackUrl(siteUrl + "topaysuccess.htm");
        //异步回调地址
        unionPaySetting.setBackCallbackUrl(siteUrl + "unionpaynotify.htm");
        //返回的html
        String form = null;
        //UnionPayUtils.pagePay(unionPaySetting, orderInfoForPay);

        //生成表单错误
        if (StringUtils.isEmpty(form)) {
            logger.error("unionPagePay fail due to form error....");
            return CommonResponse.build("", -5);
        }
        logger.debug("unionPagePay pay success......");

        return CommonResponse.build(form, 1);
    }

    @Override
    public int aliPayNotify(Map requestParams) {
        logger.debug("aliPayNotify and requestParams:{} ", requestParams);
        //支付宝支付设置(数据库)
        AliPaySet aliPaySet = paySetService.queryPaySet().getAliPaySet();
        //支付宝支付设置
        AliPaySetting aliPaySetting = new AliPaySetting();
        BeanUtils.copyProperties(aliPaySet, aliPaySetting);
        if (!aliPaySetting.checkPayParams()) {
            logger.error("aliPayNotify fail due to no checkPayParams fail....");
            return -2;
        }
        OrderInfoAfterPay orderInfoAfterPay = AliPayUtils.afterPayInfo(aliPaySetting, requestParams);
        if (!orderInfoAfterPay.isSuccess()) {
            logger.error("aliPayNotify fail due to notify fail....");
            return -3;
        }
        return paySuccess(orderInfoAfterPay.getOrderCode(), CommonConstant.QUERY_WITH_NO_CUSTOMER, orderInfoAfterPay.getType(), CommonConstant.ALI_PAY, orderInfoAfterPay.getTransCode());
    }

    @Override
    public int weChatNotify(InputStream inputStream) {
        logger.debug("weChatNotify...... ");
        return weChatCommonNotify(inputStream, paySetService.queryPaySet().getWechatPaySet());
    }

    @Override
    public int weChatAppNotify(InputStream inputStream) {
        logger.debug("weChatAppNotify...... ");
        return weChatCommonNotify(inputStream, paySetService.queryPaySet().getWechatAppPaySet());
    }

    @Override
    public int weChatAppletNotify(InputStream inputStream) {
        logger.debug("weChatAppletNotify...... ");
        return weChatCommonNotify(inputStream, paySetService.queryPaySet().getWechatAppletPaySet());
    }

    /**
     * 微信支付回调
     *
     * @param inputStream  微信输入流
     * @param wechatPaySet 微信设置
     * @return 返回  0失败，大于0成功
     */
    private int weChatCommonNotify(InputStream inputStream, WechatPaySet wechatPaySet) {
        logger.debug("weChatCommonNotifya and wechatPaySet:{}", wechatPaySet);
        WechatSetting wechatSetting = new WechatSetting();
        BeanUtils.copyProperties(wechatPaySet, wechatSetting);
        if (StringUtils.isEmpty(wechatPaySet.getAppId()) || StringUtils.isEmpty(wechatPaySet.getMerchantNum())) {
            logger.error("weChatNotify fail due to no checkPayParams fail....");
            return -2;
        }
        OrderInfoAfterPay orderInfoAfterPay = WechatUtils.afterPayInfo(inputStream, wechatSetting);
        if (!orderInfoAfterPay.isSuccess()) {
            logger.error("weChatNotify fail due to notify fail....");
            return -3;
        }
        return paySuccess(orderInfoAfterPay.getOrderCode(), CommonConstant.QUERY_WITH_NO_CUSTOMER, orderInfoAfterPay.getType(), CommonConstant.WECHAT_PAY, orderInfoAfterPay.getTransCode());
    }

    @Override
    public int unionPayNotify(Map requestParams) {
        logger.debug("unionPayNotify and requestParams:{} ", requestParams);
        OrderInfoAfterPay orderInfoAfterPay = null;
        //UnionPayUtils.afterPayInfo(requestParams);
        if (!orderInfoAfterPay.isSuccess()) {
            logger.error("unionPayNotify fail due to notify fail....");
            return -3;
        }
        return paySuccess(orderInfoAfterPay.getOrderCode(), CommonConstant.QUERY_WITH_NO_CUSTOMER, orderInfoAfterPay.getType(), CommonConstant.UNION_PAY, orderInfoAfterPay.getTransCode());

    }

    @Override
    public CommonResponse<String> p2c2pRedirectPay(String orderCode, long customerId, int type, String orderType, String orderId) {
        //TODO bug fixme
        logger.debug("p2c2pRedirectPay and orderCode:{} \r\n customerId:{} \r\n type:{} \r\n orderType:{} \r\n orderId:{}", orderCode, customerId, type, orderType, orderId);

        // 同步回调地址
        String callBackUrl = "pages/user/money/success?type=" + type+"&out_trade_no="+orderCode;

        // 如果是社区团购订单 则同步回调地址为订单详情页面
        if (OrderTypeEnum.COMMUNITY_GROUP_ORDER.getCode().equalsIgnoreCase(orderType)) {
            callBackUrl = "tocommunityorderdetail.htm?orderId=" + orderId;
        }
        BaseInfoSet infoSet = getSite();
        logger.debug("siteUrl:{} \r\n siteBackendUrl: {} \r\n pcUrl: {}", infoSet.getSiteUrl(), infoSet.getH5CallBackDomain(), infoSet.getPcCallBackDomain());
        // 构造支付信息
        return p2c2pCommonPay(orderCode, customerId, type, P2c2pSetting.build(infoSet.getSiteUrlWithBias() + callBackUrl,
                infoSet.getSiteBackendDomainWithBias() + "p2c2pnotify"), (setting, orderinfo) -> P2c2pPayUtils.wapPay(setting, orderinfo));
    }

    @Override
    public int p2c2pNotify(Map<String, String[]> requestParams) {
        requestParams.entrySet().stream().forEach((Map.Entry entry) -> {
            logger.debug("key:{}, value:{}", entry.getKey(), entry.getValue());
        });
        logger.debug("p2c2pNotify and requestParams:{} ", requestParams);
        //2c2p支付设置(数据库)
        P2c2pPaySet p2c2pPaySet = paySetService.queryPaySet().getP2c2pPaySet();
        //2c2p支付设置
        P2c2pSetting p2c2pSetting = new P2c2pSetting();
        BeanUtils.copyProperties(p2c2pPaySet, p2c2pSetting);
        if (!p2c2pSetting.checkPayParams()) {
            logger.error("p2c2pNotify fail due to no checkPayParams fail....");
            return -2;
        }
        OrderInfoAfterPay orderInfoAfterPay = P2c2pPayUtils.afterPayInfo(p2c2pSetting, requestParams);
        if (orderInfoAfterPay == null || !orderInfoAfterPay.isSuccess()) {
            logger.error("p2c2pNotify fail due to notify fail....");
            return -3;
        }
        return paySuccess(orderInfoAfterPay.getOrderCode(), CommonConstant.QUERY_WITH_NO_CUSTOMER, orderInfoAfterPay.getType(), CommonConstant.P_2C2P_PAY, orderInfoAfterPay.getTransCode());
    }

    @Override
    public CommonResponse<String> dlocalPay(String orderCode, String currency, String token, long customerId, int type, String orderType, String orderId, DLocalPayerObject payer) {
        logger.debug("dlocalPay and orderCode:{} \r\n customerId:{} \r\n type:{} \r\n orderType:{} \r\n orderId:{} \r\n token:{} ", orderCode, customerId, type, orderType, orderId, token);

        // 同步回调地址
        String callBackUrl = "pages/user/money/success?type=" + type+"&out_trade_no="+orderCode;

        // 如果是社区团购订单 则同步回调地址为订单详情页面
        if (OrderTypeEnum.COMMUNITY_GROUP_ORDER.getCode().equalsIgnoreCase(orderType)) {
            callBackUrl = "tocommunityorderdetail.htm?orderId=" + orderId;
        }
        BaseInfoSet infoSet = getSite();
        logger.debug("siteUrl:{} \r\n siteBackendUrl: {} \r\n pcUrl: {}", infoSet.getSiteUrl(), infoSet.getH5CallBackDomain(), infoSet.getPcCallBackDomain());
        // 构造支付信息
        return dlocalCommonPay(orderCode, currency, customerId, type, payer, DlocalSetting.build(infoSet.getSiteUrlWithBias() + callBackUrl,
                infoSet.getSiteBackendDomainWithBias() + "dlocalnotify"), (setting, orderinfo) -> DLocalPayUtils.wapPay(setting, orderinfo));
    }

    @Override
    public int dlocalNotify(Map<String, String[]> requestParams) {
        requestParams.entrySet().stream().forEach((Map.Entry entry) -> {
            logger.debug("key:{}, value:{}", entry.getKey(), entry.getValue());
        });
        logger.debug("dlocalNotify and requestParams:{} ", requestParams);
        //dlocal支付设置(数据库)
        DlocalPaySet dlocalPaySet = paySetService.queryPaySet().getDlocalPaySet();
        //dlocal支付设置
        DlocalSetting dlocalSetting = new DlocalSetting();
        BeanUtils.copyProperties(dlocalPaySet, dlocalSetting);
        if (!dlocalSetting.checkPayParams()) {
            logger.error("dlocalNotify fail due to no checkPayParams fail....");
            return -2;
        }
        OrderInfoAfterPay orderInfoAfterPay = DLocalPayUtils.afterPayInfo(dlocalSetting, requestParams);
        if (orderInfoAfterPay == null || !orderInfoAfterPay.isSuccess()) {
            logger.error("dlocalNotify fail due to notify fail....");
            return -3;
        }
        return paySuccess(orderInfoAfterPay.getOrderCode(), CommonConstant.QUERY_WITH_NO_CUSTOMER, orderInfoAfterPay.getType(), CommonConstant.DLOCAL_PAY, orderInfoAfterPay.getTransCode());
    }

    @Override
    public CommonResponse<String> zotaPay(String orderCode, String currency, long customerId, int type, String orderType, String orderId, ZotaPayPayerObject payer) {
        logger.debug("zotaPay and orderCode:{} \r\n currency:{} \r\n customerId:{} \r\n type:{} \r\n orderType:{} \r\n orderId:{} ", orderCode, currency, customerId, type, orderType, orderId);

        // 同步回调地址
        String callBackUrl = "pages/user/money/success?type=" + type+"&out_trade_no="+orderCode;

        // 如果是社区团购订单 则同步回调地址为订单详情页面
        if (OrderTypeEnum.COMMUNITY_GROUP_ORDER.getCode().equalsIgnoreCase(orderType)) {
            callBackUrl = "tocommunityorderdetail.htm?orderId=" + orderId;
        }
        BaseInfoSet infoSet = getSite();
        logger.debug("siteUrl:{} \r\n siteBackendUrl: {} \r\n pcUrl: {}", infoSet.getSiteUrl(), infoSet.getH5CallBackDomain(), infoSet.getPcCallBackDomain());
        // 构造支付信息
        return zotaPayCommonPay(orderCode, currency, customerId, type, payer, ZotaPaySetting.build(infoSet.getSiteUrlWithBias() + callBackUrl,
                infoSet.getSiteBackendDomainWithBias() + "zotapaynotify"), (setting, orderinfo) -> ZotaPayUtils.depositPay(setting, orderinfo));
    }

    @Override
    public CommonResponse<String> queryZotaPayOrder(String orderID, String merchantOrderID) {
        logger.debug("queryZotaPayOrder and orderID:{} \r\n merchantOrderID:{} ", orderID, merchantOrderID);
        //Zotapay支付设置(数据库)
        ZotaPaySet zotaPaySet = paySetService.queryPaySet().getZotaPaySet();
        //Zotapay支付设置
        ZotaPaySetting zotaPaySetting = new ZotaPaySetting();
        BeanUtils.copyProperties(zotaPaySet, zotaPaySetting);
        if (!zotaPaySetting.checkPayParams()) {
            logger.error("queryZotaPayOrder fail due to no checkPayParams fail....");
            return CommonResponse.build("Invalid Zotapay setting!", -1);
        }
        ZotaPayQueryResponseData responseData = ZotaPayUtils.queryOrderStatus(zotaPaySetting, orderID, merchantOrderID);
        return CommonResponse.build(JSON.toJSONString(responseData), 1);
    }

    @Override
    @Transactional
    public CommonResponse<String> queryAndProcessZotaPayOrder(String orderID, String merchantOrderID) {
        logger.debug("queryAndProcessZotaPayOrder and orderID:{} \r\n merchantOrderID:{} ", orderID, merchantOrderID);
        //Zotapay支付设置(数据库)
        ZotaPaySet zotaPaySet = paySetService.queryPaySet().getZotaPaySet();
        //Zotapay支付设置
        ZotaPaySetting zotaPaySetting = new ZotaPaySetting();
        BeanUtils.copyProperties(zotaPaySet, zotaPaySetting);
        if (!zotaPaySetting.checkPayParams()) {
            logger.error("queryAndProcessZotaPayOrder fail due to no checkPayParams fail....");
            return CommonResponse.build("Invalid Zotapay setting!", -1);
        }
        ZotaPayQueryResponseData responseData = ZotaPayUtils.queryOrderStatus(zotaPaySetting, orderID, merchantOrderID);

        logger.debug(responseData.toString());
        //查询支付平台支付订单并处理
        if ("APPROVED".equalsIgnoreCase(responseData.getStatus())) {
            String customParam;
            customParam = responseData.getCustomParam();
            ZotaPayCustomParam param = JSON.parseObject(customParam, ZotaPayCustomParam.class);

            // 容错处理！
            // 查询订单并更新状态。这里无法得知notify来得快还是前台等待来得快。所以先查查
            List<OmsOrder> orders = orderService.queryOrderByOrderCode(merchantOrderID, CommonConstant.QUERY_WITH_NO_CUSTOMER);
            if (CollectionUtils.isEmpty(orders)) {
                logger.error("paySuccess fail due to no order....");
                return CommonResponse.build("paySuccess fail due to no order", -1);
            }
            boolean isNotPaid = orders.stream().anyMatch(OmsOrder::isNotPaid);
            if(!isNotPaid){
                logger.warn("order status is not PENDING. Most likely orderStatus had been changed with interface like queryAndProcessZotaPayOrder");
                return CommonResponse.build(JSON.toJSONString(responseData), 1);
            }
            paySuccess(merchantOrderID, CommonConstant.QUERY_WITH_NO_CUSTOMER, Integer.parseInt(param.getType()), "zotapay", responseData.getProcessorTransactionID());
            return CommonResponse.build(JSON.toJSONString(responseData), 1);
        } else {
            logger.debug("status: {}", responseData.getStatus());
            return CommonResponse.build(JSON.toJSONString(responseData), -1);
        }
    }

    @Override
    public CommonResponse<String> paypalPay(String orderCode, String currency, long customerId, int type, String orderType, String orderId) {
        logger.debug("paypalPay and orderCode:{} \r\n currency:{} \r\n customerId:{} \r\n type:{} \r\n orderType:{} \r\n orderId:{} ", orderCode, currency, customerId, type, orderType, orderId);

        // 同步回调地址
        String callBackUrl = "pages/user/money/success?type=" + type+"&orderCode="+orderCode;

        // 如果是社区团购订单 则同步回调地址为订单详情页面
        if (OrderTypeEnum.COMMUNITY_GROUP_ORDER.getCode().equalsIgnoreCase(orderType)) {
            callBackUrl = "tocommunityorderdetail.htm?orderId=" + orderId;
        }
        BaseInfoSet infoSet = getSite();
        logger.debug("siteUrl:{} \r\n siteBackendUrl: {} \r\n pcUrl: {}", infoSet.getSiteUrl(), infoSet.getH5CallBackDomain(), infoSet.getPcCallBackDomain());
        // 构造支付信息
        return paypalPayCommonPay(orderCode, currency, customerId, type, PayPalSetting.build(infoSet.getSiteUrlWithBias() + callBackUrl,
                infoSet.getSiteBackendDomainWithBias() + "paypalpaynotify"), (setting, orderinfo) -> PayPalUtils.wapPay(setting, orderinfo));
    }

    @Override
    public CommonResponse<String> paypalCapture(PayParam payParam) {

        //Paypal支付设置(数据库)
        PayPalPaySet payPalPaySet = paySetService.queryPaySet().getPaypalPaySet();

        PayPalSetting payPalSetting = new PayPalSetting();

        // 没有开启Paypal支付 则直接返回
        if (!payPalPaySet.checkIsUse()) {
            logger.error("paypalPayCommonPay fail due to not used....");
            return CommonResponse.build("", AlipayResponseCode.NOT_CONFIGURED.getCode());
        }

        //支付设置
        BeanUtils.copyProperties(payPalPaySet, payPalSetting);

        String result="";
        try {
            result = PayPalUtils.executePayment(payPalSetting,payParam.getOrderId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResponse.build(result, AlipayResponseCode.SUCCESS.getCode());
    }

    @Override
    public int zotaPayNotify(ZotaPayCallbackNotification notification) {
        logger.debug("zotaPayNotify and notification:{} ", notification);
        //zotapay支付设置(数据库)
        ZotaPaySet zotaPaySet = paySetService.queryPaySet().getZotaPaySet();
        //zotapay支付设置
        ZotaPaySetting zotaPaySetting = new ZotaPaySetting();
        BeanUtils.copyProperties(zotaPaySet, zotaPaySetting);
        if (!zotaPaySetting.checkPayParams()) {
            logger.error("zotaPayNotify fail due to no checkPayParams fail....");
            return -2;
        }
        OrderInfoAfterPay orderInfoAfterPay = ZotaPayUtils.afterPayInfo(zotaPaySetting, notification);
        if (orderInfoAfterPay == null || !orderInfoAfterPay.isSuccess()) {
            logger.error("zotaPayNotify fail due to notify fail....");
            return -3;
        }
        return paySuccess(orderInfoAfterPay.getOrderCode(), CommonConstant.QUERY_WITH_NO_CUSTOMER, orderInfoAfterPay.getType(), CommonConstant.ZOTA_PAY, orderInfoAfterPay.getTransCode());
    }

    /**
     * 支付成功后的操作
     *
     * @param orderCode  订单code
     * @param customerId 用户id
     * @param type       1:订单支付    2 预存款充值
     * @param channel    支付方式，预存款充值使用
     * @param transCode  交易流水号
     * @return 0失败，大于0成功
     */
    private int paySuccess(String orderCode, long customerId, int type, String channel, String transCode) {
        logger.debug("paySuccess and orderCode:{} \r\n customerId:{} \r\n type:{} \r\n transCode:{}", orderCode, customerId, type, transCode);
        //订单支付
        if (PaymentTypeEnum.ORDER_PAY.getCode() == type) {
            // 根据订单编号和用户id查询待付款的订单信息
            List<OmsOrder> orders = orderService.queryOrderByOrderCode(orderCode, customerId);
            // 没有待支付的订单 则直接返回
            if (CollectionUtils.isEmpty(orders)) {
                logger.error("paySuccess fail due to no order....");
                return -1;
            }
            //判断订单状态是否为除PENDING之外的其他状态
            boolean isNotPaid = orders.stream().anyMatch(OmsOrder::isNotPaid);
            if(!isNotPaid){
                logger.warn("order status is not PENDING. Most likely orderStatus had been changed with interface like queryAndProcessZotaPayOrder");
                return 1;
            }
            // 修改订单状态
            return orderServiceApi.confirmOrderPayed(ConfirmOrderParams.buildCustomerSource(customerId, 0, orders.stream().map(OmsOrder::getOrderCode).collect(Collectors.toList()), transCode, channel));
        } else if (PaymentTypeEnum.RECHARGE_PAY.getCode() == type) {
            //预存款充值
            return predepositRecordServiceApi.updateStatusSuccessByTransCode(orderCode, CommonConstant.QUERY_WITH_NO_CUSTOMER, channel, transCode);
        } else {
            // 根据订单编号和用户id查询待付款的门店订单信息
            List<TStoreOrder> storeOrders = storeOrderService.queryOrderByOrderCode(orderCode, customerId);

            if (CollectionUtils.isEmpty(storeOrders)) {
                logger.error("paySuccess fail due to no order....");
                return -1;
            }

            //判断订单状态是否为除PENDING之外的其他状态
            boolean isNotPaid = storeOrders.stream().anyMatch(TStoreOrder::isNotPaid);
            if(!isNotPaid){
                logger.warn("order status is not PENDING. Most likely orderStatus had been changed with interface like queryAndProcessZotaPayOrder");
                return 1;
            }

            return storeOrderServiceApi.confirmOrderPayed(customerId, storeOrders.stream().map(TStoreOrder::getOrderCode).collect(Collectors.toList()), "0", transCode, channel);
        }

    }


    /**
     * 获得订单的总金额
     *
     * @param orders 待支付的订单
     * @return 返回等待支付订单的总金额
     */
    private BigDecimal getOrdersAllMoeny(List<OmsOrder> orders) {
        // 是定金预售订单 则返回定金的价格
        if (!CollectionUtils.isEmpty(orders.stream().filter(OmsOrder::isDepositPresaleOrder).collect(Collectors.toList()))) {
            return orders.stream().map(OmsOrder::getPresalePrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return orders.stream().map(OmsOrder::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 查询待付款的订单
     *
     * @param orderCode  订单code
     * @param customerId 用户id
     * @return 返回待付款的订单
     */
    private List<OmsOrder> queryToPayOrders(String orderCode, long customerId) {
        logger.debug("queryToPayOrders and orderCode:{} \r\n customerId:{}", orderCode, customerId);
        return orderService.queryToPayOrder(orderCode, customerId);
    }

    /**
     * 查询待付款的门店订单
     *
     * @param orderCode  订单code
     * @param customerId 用户id
     * @return 返回待付款的门店订单
     */
    private List<TStoreOrder> queryToPayStoreOrders(String orderCode, long customerId) {
        logger.debug("queryToPayStoreOrders and orderCode:{} \r\n customerId:{}", orderCode, customerId);
        return storeOrderService.queryToPayStoreOrder(orderCode, customerId);
    }

    /**
     * 构建预支付订单信息
     *
     * @param goodsName  商品名
     * @param orderCode  订单code
     * @param orderMoney 订单金额
     * @param type       类型 1:订单支付 2:预存款充值
     */
    private OrderInfoForPay buildOrderInfoForPay(String goodsName, String orderCode, BigDecimal orderMoney, int type) {
        OrderInfoForPay orderInfoForPay = new OrderInfoForPay();
        //商品名（取一个就行）
        orderInfoForPay.setGoodsName(goodsName);
        //订单号
        orderInfoForPay.setOrderCode(orderCode);
        //订单金额
        orderInfoForPay.setPrice(orderMoney);
        //支付类型
        orderInfoForPay.setType(type);
        return orderInfoForPay;
    }

    /**
     * 构建PAYPAL预支付订单信息
     *
     * @param goodsName  商品名
     * @param orderCode  订单code
     * @param orderMoney 订单金额
     * @param type       类型 1:订单支付 2:预存款充值
     */
    private OrderInfoForPay buildOrderInfoForPay(String goodsName, String orderCode, BigDecimal orderMoney, int type, String currency) {
        OrderInfoForPay orderInfoForPay = new OrderInfoForPay();
        //商品名（取一个就行）
        orderInfoForPay.setGoodsName(goodsName);
        //订单号
        orderInfoForPay.setOrderCode(orderCode);
        //订单金额
        orderInfoForPay.setPrice(orderMoney);

        orderInfoForPay.setCurrency(currency);
        //支付类型
        orderInfoForPay.setType(type);
        return orderInfoForPay;
    }


    /**
     * 构建DLocal预支付订单信息
     *
     * @param goodsName  商品名
     * @param orderCode  订单code
     * @param currency   币种
     * @param orderMoney 订单金额
     * @param type       类型 1:订单支付 2:预存款充值
     */
    private OrderInfoForPay buildOrderInfoForPay(String goodsName, String orderCode, String currency, BigDecimal orderMoney, int type, DLocalPayerObject payer) {
        OrderInfoForPay orderInfoForPay = new OrderInfoForPay();
        //商品名（取一个就行）
        orderInfoForPay.setGoodsName(goodsName);
        //订单号
        orderInfoForPay.setOrderCode(orderCode);
        //币种
        orderInfoForPay.setCurrency(currency);
        //订单金额
        orderInfoForPay.setPrice(orderMoney);
        //支付类型
        orderInfoForPay.setType(type);
        //DLocal用户支付信息对象
        orderInfoForPay.setDLocalPayerObject(payer);
        return orderInfoForPay;
    }

    /**
     * 构建Zotapay预支付订单信息
     *
     * @param goodsName  商品名
     * @param orderCode  订单code
     * @param currency   币种
     * @param orderMoney 订单金额
     * @param type       类型 1:订单支付 2:预存款充值
     */
    private OrderInfoForPay buildOrderInfoForPay(String goodsName, String orderCode, String currency, BigDecimal orderMoney, int type, ZotaPayPayerObject payer) {
        OrderInfoForPay orderInfoForPay = new OrderInfoForPay();
        //商品名（取一个就行）
        orderInfoForPay.setGoodsName(goodsName);
        //订单号
        orderInfoForPay.setOrderCode(orderCode);
        //币种
        orderInfoForPay.setCurrency(currency);
        //订单金额
        orderInfoForPay.setPrice(orderMoney);
        //支付类型
        orderInfoForPay.setType(type);
        //zotapay用户支付信息对象
        orderInfoForPay.setZotaPayPayerObject(payer);
        return orderInfoForPay;
    }


    /**
     * 获取网站地址
     */
    private String getSiteUrl() {
        BaseInfoSet baseInfoSet = baseInfoSetService.queryBaseInfoSet();
        if (ObjectUtils.isEmpty(baseInfoSet)) {
            return null;
        }
        return baseInfoSet.getSiteUrlWithBias();
    }

    /**
     * 获取网站地址
     */
    private String getSiteBackendDomain() {
        BaseInfoSet baseInfoSet = baseInfoSetService.queryBaseInfoSet();
        if (ObjectUtils.isEmpty(baseInfoSet)) {
            return null;
        }
        return baseInfoSet.getSiteBackendDomainWithBias();
    }

    /**
     * 获取网站地址
     */
    private BaseInfoSet getSite() {
        return baseInfoSetService.queryBaseInfoSet();
    }

    /**
     * 阿里支付公共方法
     *
     * @param orderCode     订单code
     * @param customerId    用户id
     * @param type          类型 1 订单支付 2 预存款充值 3 门店订单支付
     * @param aliPaySetting 支付宝设置实体
     * @param paySupplier   回调获得支付宝支付参数
     * @return -9 支付宝没开启 -1  用户不存在 -3 没有待支付订单 -7 网站地址不存在 -8 支付宝配置的参数错误 1 成功
     */
    private CommonResponse<String> aliCommonPay(String orderCode, long customerId, int type, AliPaySetting aliPaySetting, PaySupplier<AliPaySetting, OrderInfoForPay, String> paySupplier) {

        logger.debug("aliCommonPay and orderCode:{}  \r\n customerId:{} \r\n type:{} \r\n aliPaySetting:{} \r\n", orderCode, customerId, type, aliPaySetting);

        //支付宝支付设置(数据库)
        AliPaySet aliPaySet = paySetService.queryPaySet().getAliPaySet();

        // 没有开启支付宝支付 则直接返回
        if (!aliPaySet.checkIsUse()) {
            logger.error("aliCommonPay fail due to not used....");
            return CommonResponse.build("", AlipayResponseCode.NOT_CONFIGURED.getCode());

        }

        // 查询用户信息
        UmsMember customer = customerService.queryCustomerWithNoPasswordById(customerId);
        if (Objects.isNull(customer)) {
            logger.error("aliCommonPay fail due to member is not ");
            return CommonResponse.build("", AlipayResponseCode.USER_NOT_EXIST.getCode());
        }

        // 订单支付实体
        OrderInfoForPay orderInfoForPay = getOrderInfoForPay(orderCode, customerId, type);

        // 如果没有支付订单 则直接返回
        if (Objects.isNull(orderInfoForPay)) {
            logger.error("aliCommonPay fail due to no order....");
            return CommonResponse.build("", AlipayResponseCode.ORDER_NOT_EXIST.getCode());
        }

        //支付宝支付设置
        BeanUtils.copyProperties(aliPaySet, aliPaySetting);

        // 校验参数
        if (!aliPaySetting.checkPayParams()) {
            logger.error("aliCommonPay fail due to no checkPayParams fail....");
            return CommonResponse.build("", AlipayResponseCode.WRONG_CONFIGURATION.getCode());
        }

        //前台同步回调地址
        aliPaySetting.setBeforeCallbackUrl(aliPaySetting.getBeforeCallbackUrl());
        // aliPaySetting.setBeforeCallbackUrl(siteUrl + aliPaySetting.getBeforeCallbackUrl());
        //异步回调地址
        aliPaySetting.setBackCallbackUrl(aliPaySetting.getBackCallbackUrl());
        logger.info("aliPaySetting=" + aliPaySetting);
        // 获得阿里的支付html
        String aliPayParams = paySupplier.getPayParams(aliPaySetting, orderInfoForPay);

        //生成表单错误
        if (StringUtils.isEmpty(aliPayParams)) {
            logger.error("aliCommonPay fail due to form error....");
            return CommonResponse.build("", AlipayResponseCode.FORM_GENERATION_FAILED.getCode());
        }
        logger.debug("aliCommonPay pay success......");

        return CommonResponse.build(aliPayParams, AlipayResponseCode.SUCCESS.getCode());
    }


    /**
     * 2c2p支付公共方法
     *
     * @param orderCode    订单code
     * @param customerId   用户id
     * @param type         类型 1 订单支付 2 预存款充值 3 门店订单支付
     * @param p2c2pSetting 2c2p设置实体
     * @param paySupplier  回调获得2c2p支付参数
     * @return -9 2c2p没开启 -1  用户不存在 -3 没有待支付订单 -7 网站地址不存在 -8 2c2p配置的参数错误 1 成功
     */
    private CommonResponse<String> p2c2pCommonPay(String orderCode, long customerId, int type, P2c2pSetting p2c2pSetting, PaySupplier<P2c2pSetting, OrderInfoForPay, String> paySupplier) {

        logger.debug("p2c2pCommonPay and orderCode:{}  \r\n customerId:{} \r\n type:{} \r\n aliPaySetting:{} \r\n", orderCode, customerId, type, p2c2pSetting);

        //c2cp设置(数据库)
        P2c2pPaySet p2c2pPaySet = paySetService.queryPaySet().getP2c2pPaySet();

        // 没有开启c2cp支付 则直接返回
        if (!p2c2pPaySet.checkIsUse()) {
            logger.error("p2c2pCommonPay fail due to not used....");
            return CommonResponse.build("", P2c2pResponseCode.NOT_CONFIGURED.getCode());

        }

        // 查询用户信息
        UmsMember customer = customerService.queryCustomerWithNoPasswordById(customerId);
        if (Objects.isNull(customer)) {
            logger.error("p2c2pCommonPay fail due to member is not ");
            return CommonResponse.build("", P2c2pResponseCode.USER_NOT_EXIST.getCode());
        }

        // 订单支付实体
        OrderInfoForPay orderInfoForPay = getOrderInfoForPay(orderCode, customerId, type);

        // 如果没有支付订单 则直接返回
        if (Objects.isNull(orderInfoForPay)) {
            logger.error("p2c2pCommonPay fail due to no order....");
            return CommonResponse.build("", P2c2pResponseCode.ORDER_NOT_EXIST.getCode());
        }

        //2c2p支付设置
        BeanUtils.copyProperties(p2c2pPaySet, p2c2pSetting);

        // 校验参数
        if (!p2c2pSetting.checkPayParams()) {
            logger.error("p2c2pCommonPay fail due to no checkPayParams fail....");
            return CommonResponse.build("", P2c2pResponseCode.WRONG_CONFIGURATION.getCode());
        }

        //前台同步回调地址
//        p2c2pSetting.setBeforeCallbackUrl(p2c2pSetting.getBeforeCallbackUrl());
        // aliPaySetting.setBeforeCallbackUrl(siteUrl + aliPaySetting.getBeforeCallbackUrl());
        //异步回调地址
//        p2c2pSetting.setBackCallbackUrl(p2c2pSetting.getBackCallbackUrl());
//        logger.info("p2c2pSetting="+p2c2pSetting);
        // 获得c2cp支付html
        String p2c2pParams = paySupplier.getPayParams(p2c2pSetting, orderInfoForPay);

        //生成表单错误
        if (StringUtils.isEmpty(p2c2pParams)) {
            logger.error("p2c2pCommonPay fail due to form error....");
            return CommonResponse.build("", P2c2pResponseCode.FORM_GENERATION_FAILED.getCode());
        }
        logger.debug("p2c2pCommonPay pay success......");

        return CommonResponse.build(p2c2pParams, P2c2pResponseCode.SUCCESS.getCode());
    }

    /**
     * dlocal支付公共方法
     *
     * @param orderCode     订单code
     * @param customerId    用户id
     * @param type          类型 1 订单支付 2 预存款充值 3 门店订单支付
     * @param dlocalSetting 支付宝设置实体
     * @param paySupplier   回调获得dlocal支付参数
     * @return -9 支付宝没开启 -1  用户不存在 -3 没有待支付订单 -7 网站地址不存在 -8 支付宝配置的参数错误 1 成功
     */
    private CommonResponse<String> dlocalCommonPay(String orderCode, String currency, long customerId, int type, DLocalPayerObject payer, DlocalSetting dlocalSetting, PaySupplier<DlocalSetting, OrderInfoForPay, String> paySupplier) {

        logger.debug("aliCommonPay and orderCode:{}  \r\n currency: {} \r\n customerId:{} \r\n type:{} \r\n dlocalSetting:{} \r\n", orderCode, currency, customerId, type, dlocalSetting);

        //DLOCAL支付设置(数据库)
        DlocalPaySet dlocalPaySet = paySetService.queryPaySet().getDlocalPaySet();

        // 没有开启支付宝支付 则直接返回
        if (!dlocalPaySet.checkIsUse()) {
            logger.error("aliCommonPay fail due to not used....");
            return CommonResponse.build("", AlipayResponseCode.NOT_CONFIGURED.getCode());

        }

        // 查询用户信息
        UmsMember customer = customerService.queryCustomerWithNoPasswordById(customerId);
        if (Objects.isNull(customer)) {
            logger.error("aliCommonPay fail due to member is not ");
            return CommonResponse.build("", AlipayResponseCode.USER_NOT_EXIST.getCode());
        }

        // 订单支付实体
        OrderInfoForPay orderInfoForPay = getOrderInfoForPay(orderCode, currency, customerId, type, payer);

        // 如果没有支付订单 则直接返回
        if (Objects.isNull(orderInfoForPay)) {
            logger.error("aliCommonPay fail due to no order....");
            return CommonResponse.build("", AlipayResponseCode.ORDER_NOT_EXIST.getCode());
        }

        //支付宝支付设置
        BeanUtils.copyProperties(dlocalPaySet, dlocalSetting);

        // 校验参数
        if (!dlocalSetting.checkPayParams()) {
            logger.error("aliCommonPay fail due to no checkPayParams fail....");
            return CommonResponse.build("", AlipayResponseCode.WRONG_CONFIGURATION.getCode());
        }

        //前台同步回调地址
        dlocalSetting.setBeforeCallbackUrl(dlocalSetting.getBeforeCallbackUrl());
        //异步回调地址
        dlocalSetting.setBackCallbackUrl(dlocalSetting.getBackCallbackUrl());
        logger.info("dlocalSetting=" + dlocalSetting);
        // 获得dlocal的支付html
        String dlocalParams = paySupplier.getPayParams(dlocalSetting, orderInfoForPay);

        //生成表单错误
        if (StringUtils.isEmpty(dlocalParams)) {
            logger.error("aliCommonPay fail due to form error....");
            return CommonResponse.build("", AlipayResponseCode.FORM_GENERATION_FAILED.getCode());
        }
        logger.debug("aliCommonPay pay success......");

        return CommonResponse.build(dlocalParams, AlipayResponseCode.SUCCESS.getCode());
    }

    /**
     * zotapay支付公共方法
     *
     * @param orderCode      订单code
     * @param customerId     用户id
     * @param type           类型 1 订单支付 2 预存款充值 3 门店订单支付
     * @param zotaPaySetting zotapay设置实体
     * @param paySupplier    回调获得zotapay支付参数
     * @return -9 zotapay没开启 -1  用户不存在 -3 没有待支付订单 -7 网站地址不存在 -8 zotapay配置的参数错误 1 成功
     */
    private CommonResponse<String> zotaPayCommonPay(String orderCode, String currency, long customerId, int type, ZotaPayPayerObject payer, ZotaPaySetting zotaPaySetting, PaySupplier<ZotaPaySetting, OrderInfoForPay, String> paySupplier) {

        logger.debug("zotaPayCommonPay and orderCode:{}  \r\n customerId:{} \r\n type:{} \r\n zotaPaySetting:{} \r\n", orderCode, customerId, type, zotaPaySetting);

        //ZOTAPAY支付设置(数据库)
        ZotaPaySet zotaPaySet = paySetService.queryPaySet().getZotaPaySet();

        // 没有开启ZOTAPAY支付 则直接返回
        if (!zotaPaySet.checkIsUse()) {
            logger.error("zotaPayCommonPay fail due to not used....");
            return CommonResponse.build("", AlipayResponseCode.NOT_CONFIGURED.getCode());

        }

        // 查询用户信息
        UmsMember customer = customerService.queryCustomerWithNoPasswordById(customerId);
        if (Objects.isNull(customer)) {
            logger.error("zotaPayCommonPay fail due to member is not ");
            return CommonResponse.build("", AlipayResponseCode.USER_NOT_EXIST.getCode());
        }

        // 订单支付实体
        OrderInfoForPay orderInfoForPay = getOrderInfoForPay(orderCode, currency, customerId, type, payer);

        // 如果没有支付订单 则直接返回
        if (Objects.isNull(orderInfoForPay)) {
            logger.error("zotaPayCommonPay fail due to no order....");
            return CommonResponse.build("", AlipayResponseCode.ORDER_NOT_EXIST.getCode());
        }

        //支付宝支付设置
        BeanUtils.copyProperties(zotaPaySet, zotaPaySetting);

        // 校验参数
        if (!zotaPaySetting.checkPayParams()) {
            logger.error("zotaPayCommonPay fail due to no checkPayParams fail....");
            return CommonResponse.build("", AlipayResponseCode.WRONG_CONFIGURATION.getCode());
        }

        //前台同步回调地址
        zotaPaySetting.setBeforeCallbackUrl(zotaPaySetting.getBeforeCallbackUrl());
        //异步回调地址
        zotaPaySetting.setBackCallbackUrl(zotaPaySetting.getBackCallbackUrl());
        logger.info("zotaPaySetting=" + zotaPaySetting);
        String zotapayParams = paySupplier.getPayParams(zotaPaySetting, orderInfoForPay);

        //生成表单错误
        if (StringUtils.isEmpty(zotapayParams)) {
            logger.error("zotaPayCommonPay fail due to form error....");
            return CommonResponse.build("", AlipayResponseCode.FORM_GENERATION_FAILED.getCode());
        }
        logger.debug("zotaPayCommonPay pay success......");

        return CommonResponse.build(zotapayParams, AlipayResponseCode.SUCCESS.getCode());
    }

    /**
     * paypal支付公共方法
     *
     * @param orderCode      订单code
     * @param customerId     用户id
     * @param type           类型 1 订单支付 2 预存款充值 3 门店订单支付
     * @param payPalSetting zotapay设置实体
     * @param paySupplier    回调获得zotapay支付参数
     * @return -9 zotapay没开启 -1  用户不存在 -3 没有待支付订单 -7 网站地址不存在 -8 zotapay配置的参数错误 1 成功
     */
    private CommonResponse<String> paypalPayCommonPay(String orderCode, String currency, long customerId, int type, PayPalSetting payPalSetting, PaySupplier<PayPalSetting, OrderInfoForPay, String> paySupplier) {

        logger.debug("paypalPayCommonPay and orderCode:{}  \r\n customerId:{} \r\n type:{} \r\n zotaPaySetting:{} \r\n", orderCode, customerId, type, payPalSetting);

        //Paypal支付设置(数据库)
        PayPalPaySet payPalPaySet = paySetService.queryPaySet().getPaypalPaySet();

        // 没有开启Paypal支付 则直接返回
        if (!payPalPaySet.checkIsUse()) {
            logger.error("paypalPayCommonPay fail due to not used....");
            return CommonResponse.build("", AlipayResponseCode.NOT_CONFIGURED.getCode());

        }

        // 查询用户信息
        UmsMember customer = customerService.queryCustomerWithNoPasswordById(customerId);
        if (Objects.isNull(customer)) {
            logger.error("paypalPayCommonPay fail due to member is not ");
            return CommonResponse.build("", AlipayResponseCode.USER_NOT_EXIST.getCode());
        }

        // 订单支付实体
        OrderInfoForPay orderInfoForPay = getOrderInfoForPay(orderCode, customerId, type, currency);

        // 如果没有支付订单 则直接返回
        if (Objects.isNull(orderInfoForPay)) {
            logger.error("paypalPayCommonPay fail due to no order....");
            return CommonResponse.build("", AlipayResponseCode.ORDER_NOT_EXIST.getCode());
        }

        //支付设置
        BeanUtils.copyProperties(payPalPaySet, payPalSetting);

        // 校验参数
        if (!payPalSetting.checkPayParams()) {
            logger.error("paypalPayCommonPay fail due to no checkPayParams fail....");
            return CommonResponse.build("", AlipayResponseCode.WRONG_CONFIGURATION.getCode());
        }

        //前台同步回调地址
        payPalSetting.setBeforeCallbackUrl(payPalSetting.getBeforeCallbackUrl());
        //异步回调地址
        payPalSetting.setBackCallbackUrl(payPalSetting.getBackCallbackUrl());
        logger.info("payPalSetting=" + payPalSetting);
        String paypalParams = paySupplier.getPayParams(payPalSetting, orderInfoForPay);

        //生成表单错误
        if (StringUtils.isEmpty(paypalParams)) {
            logger.error("paypalPayCommonPay fail due to form error....");
            return CommonResponse.build("", AlipayResponseCode.FORM_GENERATION_FAILED.getCode());
        }
        logger.debug("paypalPayCommonPay pay success......");

        return CommonResponse.build(paypalParams, AlipayResponseCode.SUCCESS.getCode());
    }

    /**
     * 获得付款的实体
     *
     * @param orderCode  订单code
     * @param customerId 会员id
     * @param type       类型 1 订单支付 2 预存款充值 3 门店订单支付
     * @return 返回付款实体
     */
    private OrderInfoForPay getOrderInfoForPay(String orderCode, long customerId, int type, String currency) {
        // 订单
        if (type == PaymentTypeEnum.ORDER_PAY.getCode()) {
            // 根据订单编号和用户id查询待付款的订单信息
            List<OmsOrder> orders = orderService.queryToPayOrder(orderCode, customerId, OrderItem.SKUS);

            // 没有待支付的订单直接返回
            if (CollectionUtils.isEmpty(orders)) {
                return null;
            }

            return buildOrderInfoForPay(orders.get(0).getOrderSkuNames(), orderCode, getOrdersAllMoeny(orders), type, currency);

        } else if (type == PaymentTypeEnum.STORE_ORDER_PAY.getCode()) {
            // 门店订单

            // 根据订单编号和用户id查询待支付的门店订单信息
            List<TStoreOrder> storeOrders = storeOrderService.queryToPayStoreOrder(orderCode, customerId, OrderItem.SKUS);

            // 没有待支付的门店订单直接返回
            if (CollectionUtils.isEmpty(storeOrders)) {
                return null;
            }

            return buildOrderInfoForPay(storeOrders.get(0).getOrderSkuNames(), orderCode, storeOrders.stream().map(TStoreOrder::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add), type, currency);
        }

        return null;
    }

    /**
     * 获得付款的实体
     *
     * @param orderCode  订单code
     * @param customerId 会员id
     * @param type       类型 1 订单支付 2 预存款充值 3 门店订单支付
     * @return 返回付款实体
     */
    private OrderInfoForPay getOrderInfoForPay(String orderCode, long customerId, int type) {
        // 订单
        if (type == PaymentTypeEnum.ORDER_PAY.getCode()) {
            // 根据订单编号和用户id查询待付款的订单信息
            List<OmsOrder> orders = orderService.queryToPayOrder(orderCode, customerId, OrderItem.SKUS);

            // 没有待支付的订单直接返回
            if (CollectionUtils.isEmpty(orders)) {
                return null;
            }

            return buildOrderInfoForPay(orders.get(0).getOrderSkuNames(), orderCode, getOrdersAllMoeny(orders), type);

        } else if (type == PaymentTypeEnum.STORE_ORDER_PAY.getCode()) {
            // 门店订单

            // 根据订单编号和用户id查询待支付的门店订单信息
            List<TStoreOrder> storeOrders = storeOrderService.queryToPayStoreOrder(orderCode, customerId, OrderItem.SKUS);

            // 没有待支付的门店订单直接返回
            if (CollectionUtils.isEmpty(storeOrders)) {
                return null;
            }

            return buildOrderInfoForPay(storeOrders.get(0).getOrderSkuNames(), orderCode, storeOrders.stream().map(TStoreOrder::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add), type);
        }

        return null;
    }

    /**
     * 获得Zotapay付款的实体
     *
     * @param orderCode  订单code
     * @param currency   币种
     * @param customerId 会员id
     * @param type       类型 1 订单支付 2 预存款充值 3 门店订单支付
     * @return 返回付款实体
     */
    private OrderInfoForPay getOrderInfoForPay(String orderCode, String currency, long customerId, int type, DLocalPayerObject payer) {
        // 订单
        if (type == PaymentTypeEnum.ORDER_PAY.getCode()) {
            // 根据订单编号和用户id查询待付款的订单信息
            List<OmsOrder> orders = orderService.queryToPayOrder(orderCode, customerId, OrderItem.SKUS);

            // 没有待支付的订单直接返回
            if (CollectionUtils.isEmpty(orders)) {
                return null;
            }

            return buildOrderInfoForPay(orders.get(0).getOrderSkuNames(), orderCode, currency, getOrdersAllMoeny(orders), type, payer);

        } else if (type == PaymentTypeEnum.STORE_ORDER_PAY.getCode()) {
            // 门店订单

            // 根据订单编号和用户id查询待支付的门店订单信息
            List<TStoreOrder> storeOrders = storeOrderService.queryToPayStoreOrder(orderCode, customerId, OrderItem.SKUS);

            // 没有待支付的门店订单直接返回
            if (CollectionUtils.isEmpty(storeOrders)) {
                return null;
            }

            return buildOrderInfoForPay(storeOrders.get(0).getOrderSkuNames(), orderCode, currency, storeOrders.stream().map(TStoreOrder::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add), type, payer);
        }

        return null;
    }

    /**
     * 获得Zotapay付款的实体
     *
     * @param orderCode  订单code
     * @param currency   币种
     * @param customerId 会员id
     * @param type       类型 1 订单支付 2 预存款充值 3 门店订单支付
     * @return 返回付款实体
     */
    private OrderInfoForPay getOrderInfoForPay(String orderCode, String currency, long customerId, int type, ZotaPayPayerObject payer) {
        // 订单
        if (type == PaymentTypeEnum.ORDER_PAY.getCode()) {
            // 根据订单编号和用户id查询待付款的订单信息
            List<OmsOrder> orders = orderService.queryToPayOrder(orderCode, customerId, OrderItem.SKUS);

            // 没有待支付的订单直接返回
            if (CollectionUtils.isEmpty(orders)) {
                return null;
            }

            return buildOrderInfoForPay(orders.get(0).getOrderSkuNames(), orderCode, currency, getOrdersAllMoeny(orders), type, payer);

        } else if (type == PaymentTypeEnum.STORE_ORDER_PAY.getCode()) {
            // 门店订单

            // 根据订单编号和用户id查询待支付的门店订单信息
            List<TStoreOrder> storeOrders = storeOrderService.queryToPayStoreOrder(orderCode, customerId, OrderItem.SKUS);

            // 没有待支付的门店订单直接返回
            if (CollectionUtils.isEmpty(storeOrders)) {
                return null;
            }

            return buildOrderInfoForPay(storeOrders.get(0).getOrderSkuNames(), orderCode, currency, storeOrders.stream().map(TStoreOrder::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add), type, payer);
        }

        return null;
    }


    /**
     * 微信支付公共方法
     *
     * @param orderCode     订单号
     * @param customerId    用户id
     * @param type          支付类型    1 订单支付 2 预存款充值 3 门店订单支付
     * @param wechatSetting 微信设置
     * @param supplier      回调
     * @return -8 没有设置微信支付信息 -9 微信支付没启动 -7 没有配置站点信息 -1 用户不存在 -3 没有待支付订单 -5 微信生存支付信息失败 1 成功
     */
    private <T> CommonResponse<T> weChatCommonPay(String orderCode, long customerId, int type, WechatSetting wechatSetting, PaySupplier<WechatSetting, OrderInfoForPay, CommonResponse<T>> supplier) {

        logger.debug("weChatCommonPay and orderCode:{} \r\n customerId:{}   \r\n:type:{}", orderCode, customerId, type);

        //微信支付设置(数据库)
        WechatPaySet wechatPaySet;

        // 微信app、小程序支付和其他微信支付配置的信息不一样所以这边分开获取微信支付信息
        if (wechatSetting.isAppPay()) {
            wechatPaySet = paySetService.queryPaySet().getWechatAppPaySet();
        } else if (wechatSetting.isAppletPay()) {
            wechatPaySet = paySetService.queryPaySet().getWechatAppletPaySet();
        } else {
            wechatPaySet = paySetService.queryPaySet().getWechatPaySet();
        }

        // 没设置微信支付信息
        if (ObjectUtils.isEmpty(wechatPaySet)) {
            logger.error("wechatQRPay fail due to no wechatPaySet is empty....");
            return CommonResponse.build(null, -8);
        }

        // 微信支付没启用
        if (!wechatPaySet.checkIsUse()) {
            logger.error("wechatQRPay fail due to not used....");
            return CommonResponse.build(null, -9);
        }

        // 网站基本信息
        BaseInfoSet baseInfoSet = baseInfoSetService.queryBaseInfoSet();

        if (ObjectUtils.isEmpty(baseInfoSet) || StringUtils.isEmpty(baseInfoSet.getSiteUrlWithBias()) || StringUtils.isEmpty(baseInfoSet.getSiteName())) {
            logger.error("wechatH5Pay fail due to no baseinfoset....");
            return CommonResponse.build(null, -7);
        }

        // 设置网站名称和网站地址
        wechatSetting.addSiteName(baseInfoSet.getSiteName()).addSiteUrl(baseInfoSet.getSiteUrlWithBias());

        // 设置微信支付成功后回调地址
        wechatSetting.setPayCallback(baseInfoSet.getSiteUrlWithBias() + wechatSetting.getPayCallback());

        BeanUtils.copyProperties(wechatPaySet, wechatSetting);
        logger.info("wechatSetting=" + wechatSetting);

        // 检查参数
        if (!wechatSetting.checkPayParams()) {
            logger.error("wechatQRPay fail due to no checkPayParams fail....");
            return CommonResponse.build(null, -8);
        }

        // 查询用户信息
        UmsMember customer = customerService.queryCustomerWithNoPasswordById(customerId);

        // 用户信息为空 则直接返回
        if (Objects.isNull(customer)) {
            logger.error("weChatCommonPay fail due to member is not ");
            return CommonResponse.build(null, -1);
        }

        // 待支付订单
        OrderInfoForPay orderInfoForPay = getOrderInfoForPay(orderCode, customerId, type);

        // 没有待支付订单直接返回
        if (Objects.isNull(orderInfoForPay)) {
            logger.error("weChatCommonPay fail due to no order....");
            return CommonResponse.build(null, -3);
        }

        return supplier.getPayParams(wechatSetting, orderInfoForPay);
    }
}

package io.uhha.member.service.impl;


import io.uhha.common.enums.AlipayResponseCode;
import io.uhha.common.enums.P2c2pResponseCode;
import io.uhha.common.enums.WechatPayResponseCode;
import io.uhha.common.utils.*;
import io.uhha.common.utils.bean.*;
import io.uhha.member.domain.UmsMember;
import io.uhha.member.domain.UmsPreDepositRecord;
import io.uhha.member.service.IUmsMemberService;
import io.uhha.member.service.IUmsPreDepositRecordService;
import io.uhha.member.service.RechargeService;
import io.uhha.setting.bean.*;
import io.uhha.setting.service.BaseInfoSetService;
import io.uhha.setting.service.ILsPaySettingService;
import io.uhha.util.CommonConstant;
import io.uhha.util.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Objects;

/**
 * 充值服务
 */
@Service
@Slf4j
public class RechargeServiceImpl implements RechargeService {

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
     * 注入预存款服务
     */
    @Autowired
    private IUmsPreDepositRecordService predepositRecordService;

    /**
     * 注入序号生成器
     */
    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    /**
     * 用户服务接口
     */
    @Autowired
    private IUmsMemberService customerService;


    @Override
    public CommonResponse<String> c2cpPay(String transCode, BigDecimal money, int payType, long customerId, int type) {
        log.debug("c2cpPay and transCode:{}\r\n money:{}\r\n customerId:{}", transCode, money, customerId);
        //2c2p支付设置(数据库)
        P2c2pPaySet p2c2pPaySet = paySetService.queryPaySet().getP2c2pPaySet();
        // 查询用户信息
        UmsMember customer = customerService.queryCustomerWithNoPasswordById(customerId);
        if (Objects.isNull(customer)) {
            log.error("c2cpPay fail due to customer is not exist");
            return CommonResponse.build("", P2c2pResponseCode.USER_NOT_EXIST.getCode());
        }
        if (!p2c2pPaySet.checkIsUse()) {
            log.error("c2cpPay fail due to not used....");
            return CommonResponse.build("", P2c2pResponseCode.NOT_CONFIGURED.getCode());
        }

        //获得交易号
        transCode = getTransCode(transCode, money, customerId, CommonConstant.P_2C2P_PAY);
        if (StringUtils.isEmpty(transCode)) {
            log.error("c2cpPay fail due to getTransCode fail ");
            return CommonResponse.build("", P2c2pResponseCode.GENERATE_TRANSCODE_ERROR.getCode());
        }
        //查询网站基本信息
        BaseInfoSet baseInfoSet = baseInfoSetService.queryBaseInfoSet();
        if (ObjectUtils.isEmpty(baseInfoSet.getSiteUrlWithBias())) {
            log.error("c2cpPay fail due to no siteurl....");
            return CommonResponse.build("", P2c2pResponseCode.SITE_NOT_EXIST.getCode());
        }
        //C2cp支付设置
        P2c2pSetting p2c2pSetting = new P2c2pSetting();
        BeanUtils.copyProperties(p2c2pPaySet, p2c2pSetting);
        //前台同步回调地址
        p2c2pSetting.setBeforeCallbackUrl("#/rechargesuccess?transCode=" + transCode + "&money=" + money);
        //异步回调地址
        p2c2pSetting.setBackCallbackUrl(p2c2pSetting.getBackCallbackUrl());

        if (!p2c2pSetting.checkPayParams()) {
            log.error("c2cpPay fail due to no checkPayParams fail....");
            return CommonResponse.build("", P2c2pResponseCode.WRONG_CONFIGURATION.getCode());
        }

        //待付订单信息
        OrderInfoForPay orderInfoForPay = buildOrderInfoForPay(transCode, money, type);

        //返回的html
        String p2c2pParams = P2c2pPayUtils.wapPay(p2c2pSetting, orderInfoForPay);
        //生成表单错误
        if (StringUtils.isEmpty(p2c2pParams)) {
            log.error("c2cpPay fail due to form error....");
            return CommonResponse.build("", P2c2pResponseCode.FORM_GENERATION_FAILED.getCode());
        }
        log.debug("c2cpPay pay success......");
        return CommonResponse.build(p2c2pParams, P2c2pResponseCode.SUCCESS.getCode());
    }

    @Override
    public CommonResponse<String> zotapay(String transCode, BigDecimal money, String currency, int payType, long customerId, int type,  ZotaPayPayerObject payer) {
        log.debug("zotapay and transCode:{}\r\n money:{}\r\n customerId:{}", transCode, money, customerId);
        //2c2p支付设置(数据库)
        ZotaPaySet zotaPaySet = paySetService.queryPaySet().getZotaPaySet();
        // 查询用户信息
        UmsMember customer = customerService.queryCustomerWithNoPasswordById(customerId);
        if (Objects.isNull(customer)) {
            log.error("zotapay fail due to customer is not exist");
            return CommonResponse.build("", P2c2pResponseCode.USER_NOT_EXIST.getCode());
        }
        if (!zotaPaySet.checkIsUse()) {
            log.error("zotapay fail due to not used....");
            return CommonResponse.build("", P2c2pResponseCode.NOT_CONFIGURED.getCode());
        }

        //获得交易号
        transCode = getTransCode(transCode, money, customerId, CommonConstant.ZOTA_PAY);
        if (StringUtils.isEmpty(transCode)) {
            log.error("zotapay fail due to getTransCode fail ");
            return CommonResponse.build("", P2c2pResponseCode.GENERATE_TRANSCODE_ERROR.getCode());
        }
        //查询网站基本信息
        BaseInfoSet baseInfoSet = baseInfoSetService.queryBaseInfoSet();
        if (ObjectUtils.isEmpty(baseInfoSet.getSiteUrlWithBias())) {
            log.error("zotapay fail due to no siteurl....");
            return CommonResponse.build("", P2c2pResponseCode.SITE_NOT_EXIST.getCode());
        }
        //zotapay支付设置
        ZotaPaySetting zotaPaySetting = new ZotaPaySetting();
        BeanUtils.copyProperties(zotaPaySet, zotaPaySetting);

        // 同步回调地址
        String beforeCallBackUrl = "pages/user/account/rechargesuccess?type=2&out_trade_no=" + transCode + "&money=" + money;
        String backCallbackUrl = "zotapaynotify";

        //前台同步回调地址
        zotaPaySetting.setBeforeCallbackUrl(baseInfoSet.getSiteUrlWithBias()+beforeCallBackUrl);
        //后台异步回调地址
        zotaPaySetting.setBackCallbackUrl(baseInfoSet.getSiteBackendDomainWithBias()+backCallbackUrl);

        if (!zotaPaySetting.checkPayParams()) {
            log.error("zotapay fail due to no checkPayParams fail....");
            return CommonResponse.build("", P2c2pResponseCode.WRONG_CONFIGURATION.getCode());
        }

        //待付订单信息
        OrderInfoForPay orderInfoForPay = buildOrderInfoForPay(transCode, money, currency, type, payer);

        //返回的html
        String zotapayParams = ZotaPayUtils.depositPay(zotaPaySetting, orderInfoForPay);
        //生成表单错误
        if (StringUtils.isEmpty(zotapayParams)) {
            log.error("zotapay fail due to form error....");
            return CommonResponse.build("", P2c2pResponseCode.FORM_GENERATION_FAILED.getCode());
        }
        log.debug("zotapay pay success......");
        return CommonResponse.build(zotapayParams, P2c2pResponseCode.SUCCESS.getCode());
    }

    @Override
    public CommonResponse<String> aliPagePay(String transCode, BigDecimal money, int payType, long customerId, int type) {
        log.debug("aliPagePay and transCode:{}\r\n money:{}\r\n customerId:{}", transCode, money, customerId);
        //支付宝支付设置(数据库)
        AliPaySet aliPaySet = paySetService.queryPaySet().getAliPaySet();
        // 查询用户信息
        UmsMember customer = customerService.queryCustomerWithNoPasswordById(customerId);
        if (Objects.isNull(customer)) {
            log.error("aliPagePay fail due to customer is not exist");
            return CommonResponse.build("", AlipayResponseCode.USER_NOT_EXIST.getCode());
        }
        if (!aliPaySet.checkIsUse()) {
            log.error("aliPagePay fail due to not used....");
            return CommonResponse.build("", AlipayResponseCode.NOT_CONFIGURED.getCode());
        }

        //获得交易号
        transCode = getTransCode(transCode, money, customerId, CommonConstant.ALI_PAY);
        if (StringUtils.isEmpty(transCode)) {
            log.error("aliPagePay fail due to getTransCode fail ");
            return CommonResponse.build("", AlipayResponseCode.GENERATE_TRANSCODE_ERROR.getCode());
        }
        //查询网站基本信息
        BaseInfoSet baseInfoSet = baseInfoSetService.queryBaseInfoSet();
        if (ObjectUtils.isEmpty(baseInfoSet.getSiteUrlWithBias())) {
            log.error("aliPagePay fail due to no siteurl....");
            return CommonResponse.build("", AlipayResponseCode.SITE_NOT_EXIST.getCode());
        }
        String siteUrl = baseInfoSet.getSiteUrlWithBias();
        if (StringUtils.isEmpty(aliPaySet.getAppId())) {
            log.error("aliPagePay fail due to no appId....");
            return CommonResponse.build("", AlipayResponseCode.WRONG_CONFIGURATION.getCode());
        }
        //支付宝支付设置
        AliPaySetting aliPaySetting = new AliPaySetting();
        BeanUtils.copyProperties(aliPaySet, aliPaySetting);
        //前台同步回调地址
        aliPaySetting.setBeforeCallbackUrl(siteUrl + "#/rechargesuccess?transCode=" + transCode + "&money=" + money);
        //异步回调地址
        aliPaySetting.setBackCallbackUrl(siteUrl + "api/pc/alipaynotify");

        if (!aliPaySetting.checkPayParams()) {
            log.error("aliPagePay fail due to no checkPayParams fail....");
            return CommonResponse.build("", AlipayResponseCode.WRONG_CONFIGURATION.getCode());
        }

        //待付订单信息
        OrderInfoForPay orderInfoForPay = buildOrderInfoForPay(transCode, money, type);

        //返回的html
        String form = AliPayUtils.pagePay(aliPaySetting, orderInfoForPay);
        //生成表单错误
        if (StringUtils.isEmpty(form)) {
            log.error("aliPagePay fail due to form error....");
            return CommonResponse.build("", AlipayResponseCode.FORM_GENERATION_FAILED.getCode());
        }
        log.debug("aliPagePay pay success......");
        return CommonResponse.build(form, AlipayResponseCode.SUCCESS.getCode());
    }


    @Override
    public CommonResponse<String> aliWapPay(String transCode, BigDecimal money, int payType, long customerId, int type) {
        log.debug("aliWapPay and transCode:{}\r\n money:{}\r\n customerId:{}", transCode, money, customerId);
        //支付宝支付设置(数据库)
        AliPaySet aliPaySet = paySetService.queryPaySet().getAliPaySet();
        // 查询用户信息
        UmsMember customer = customerService.queryCustomerWithNoPasswordById(customerId);
        if (Objects.isNull(customer)) {
            log.error("aliWapPay fail due to customer is not ");
            return CommonResponse.build("", WechatPayResponseCode.USER_NOT_EXIST.getCode());
        }
        if (!aliPaySet.checkIsUse()) {
            log.error("aliWapPay fail due to not used....");
            return CommonResponse.build("", WechatPayResponseCode.NOT_CONFIGURED.getCode());
        }

        //获得交易号
        transCode = getTransCode(transCode, money, customerId, CommonConstant.ALI_PAY);
        if (StringUtils.isEmpty(transCode)) {
            log.error("aliWapPay fail due to getTransCode fail ");
            return CommonResponse.build("", AlipayResponseCode.GENERATE_TRANSCODE_ERROR.getCode());
        }
        //查询网站基本信息
        BaseInfoSet baseInfoSet = baseInfoSetService.queryBaseInfoSet();
        if (ObjectUtils.isEmpty(baseInfoSet.getSiteUrlWithBias())) {
            log.error("aliWapPay fail due to no siteurl....");
            return CommonResponse.build("", AlipayResponseCode.SITE_NOT_EXIST.getCode());
        }
        if (StringUtils.isEmpty(aliPaySet.getAppId())) {
            log.error("aliWapPay fail due to no appId....");
            return CommonResponse.build("", AlipayResponseCode.WRONG_CONFIGURATION.getCode());
        }
        //支付宝支付设置
        AliPaySetting aliPaySetting = new AliPaySetting();
        BeanUtils.copyProperties(aliPaySet, aliPaySetting);
        // 同步回调地址
        String callBackUrl = "pages/user/account/rechargesuccess?transcode=" + transCode + "&money=" + money;
        aliPaySetting.setBeforeCallbackUrl(baseInfoSet.getSiteUrlWithBias() + callBackUrl);
        //异步回调地址
        aliPaySetting.setBackCallbackUrl(baseInfoSet.getSiteBackendDomainWithBias() + "alipaynotify");

        if (!aliPaySetting.checkPayParams()) {
            log.error("aliWapPay fail due to no checkPayParams fail....");
            return CommonResponse.build("", AlipayResponseCode.WRONG_CONFIGURATION.getCode());
        }

        //待付订单信息
        OrderInfoForPay orderInfoForPay = buildOrderInfoForPay(transCode, money, type);

        //返回的html
        String form = AliPayUtils.wapPay(aliPaySetting, orderInfoForPay);
        //生成表单错误
        if (StringUtils.isEmpty(form)) {
            log.error("aliWapPay fail due to form error....");
            return CommonResponse.build("", AlipayResponseCode.FORM_GENERATION_FAILED.getCode());
        }
        log.debug("aliWapPay pay success......");
        return CommonResponse.build(form, AlipayResponseCode.SUCCESS.getCode());
    }

    /**
     * @param transCode
     * @param money      充值金额
     * @param payType    1 公众号 2 h5 3 小程序 4 app 5 pc 支付
     * @param customerId 用户id
     * @param ip         请求真实ip
     * @param type       支付类型 1:订单支付 2:预存款充值
     * @return
     */
    @Override
    public CommonResponse<String> wechatQRPay(String transCode, BigDecimal money, int payType, long customerId, String ip, int type) {
        log.debug("wechatQRPay and transCode:{}\r\n money:{} \r\n customerId:{}", transCode, money, customerId);
        //微信支付设置(数据库)
        WechatPaySet wechatPaySet = null;
        // 查询用户信息
        UmsMember customer = customerService.queryCustomerWithNoPasswordById(customerId);
        //微信支付设置
        WechatSetting wechatSetting = null;
        if (Objects.isNull(customer)) {
            log.error("wechatQRPay fail due to customer is not ");
            return CommonResponse.build("", WechatPayResponseCode.USER_NOT_EXIST.getCode());
        }
        if (payType == 4) {
            wechatPaySet = paySetService.queryPaySet().getWechatAppPaySet();
            wechatSetting = WechatSetting.build(ip, WechatUtils.APP_PAY, "wechatnotify");
        } else if (payType == 3) {
            wechatPaySet = paySetService.queryPaySet().getWechatAppletPaySet();
            if (customer != null && ObjectUtils.isEmpty(customer.getAppletOpenId())) {
                log.error("wechatAppletPay fail due to wechat error not auth...openid is not exist.");
                return CommonResponse.build("", WechatPayResponseCode.WRONG_CONFIGURATION.getCode());
            }
            wechatSetting = WechatSetting.build(ip, WechatUtils.APPLET_PAY, "wechatnotify");
        } else if (payType == 1) {
            wechatPaySet = paySetService.queryPaySet().getWechatPaySet();
            wechatSetting = WechatSetting.build(ip, WechatUtils.OFFICIAL_ACCOUNT_PAY, "wechatnotify");
            if (customer != null && ObjectUtils.isEmpty(customer.getH5OpenId())) {
                log.error("wechatPay fail due to wechat error not auth....");
                return CommonResponse.build("", WechatPayResponseCode.WRONG_CONFIGURATION.getCode());
            }
        } else if (payType == 2) {
            wechatPaySet = paySetService.queryPaySet().getWechatPaySet();
            wechatSetting = WechatSetting.build(ip, WechatUtils.H5_PAY, "wechatnotify");
        } else if (payType == 5) {
            wechatPaySet = paySetService.queryPaySet().getWechatPaySet();
            wechatSetting = WechatSetting.build(ip, WechatUtils.QR_PAY, "wechatnotify");
        }
        if (!wechatPaySet.checkIsUse()) {
            log.error("wechatQRPay fail due to not used....");
            return CommonResponse.build("", WechatPayResponseCode.WRONG_CONFIGURATION.getCode());
        }

        //获得交易号
        transCode = getTransCode(transCode, money, customerId, CommonConstant.WECHAT_PAY);
        if (StringUtils.isEmpty(transCode)) {
            log.error("wechatQRPay fail due to getTransCode fail ");
            return CommonResponse.build("", WechatPayResponseCode.GENERATE_TRANSCODE_ERROR.getCode());
        }
        //查询网站基本信息
        BaseInfoSet baseInfoSet = baseInfoSetService.queryBaseInfoSet();
        if (ObjectUtils.isEmpty(baseInfoSet.getSiteUrlWithBias())) {
            log.error("wechatQRPay fail due to no siteurl....");
            return CommonResponse.build("", WechatPayResponseCode.SITE_NOT_EXIST.getCode());
        }
        String siteUrl = baseInfoSet.getSiteUrlWithBias();
        if (ObjectUtils.isEmpty(wechatPaySet)) {
            log.error("wechatQRPay fail due to no wechatPaySet is empty....");
            return CommonResponse.build("", WechatPayResponseCode.WRONG_CONFIGURATION.getCode());
        }

        BeanUtils.copyProperties(wechatPaySet, wechatSetting);

        //异步回调地址
        wechatSetting.setPayCallback(siteUrl + wechatSetting.getPayCallback());
        if (!wechatSetting.checkPayParams()) {
            log.error("wechatQRPay fail due to no checkPayParams fail....");
            return CommonResponse.build("", WechatPayResponseCode.WRONG_CONFIGURATION.getCode());
        }
        //待付订单信息
        OrderInfoForPay orderInfoForPay = buildOrderInfoForPay(transCode, money, type);
        //获取预支付信息
        PrepayResult prepayResult = WechatUtils.getPrepay(wechatSetting, orderInfoForPay);


        if (payType == 4) {
            //微信生成预支付信息错误
            if (ObjectUtils.isEmpty(prepayResult)) {
                log.error("wechatAppPay fail due to wechat error....");
                return CommonResponse.build("", WechatPayResponseCode.WRONG_CONFIGURATION.getCode());
            }
            log.debug("wechatAppPay getPayUrl success......");
            return CommonResponse.build("", WechatPayResponseCode.WRONG_CONFIGURATION.getCode());
        } else if (payType == 3) {

            //微信生成预支付信息错误
            if (ObjectUtils.isEmpty(prepayResult)) {
                log.error("wechatAppletPay fail due to wechat error....");
                return CommonResponse.build("", WechatPayResponseCode.WRONG_CONFIGURATION.getCode());
            }
            return CommonResponse.build(new JSONObject(prepayResult).toString(), WechatPayResponseCode.SUCCESS.getCode());
        } else if (payType == 1) {
            //微信生成预支付信息错误
            if (ObjectUtils.isEmpty(prepayResult)) {
                log.error("wechatOfficialAccountPay fail due to wechat error....");
                return CommonResponse.build("", WechatPayResponseCode.WRONG_CONFIGURATION.getCode());
            }
            log.debug("wechatOfficialAccountPay getPayUrl success......");
            return CommonResponse.build(new JSONObject(prepayResult).toString(), WechatPayResponseCode.SUCCESS.getCode());
        } else if (payType == 2) {

            //微信生成预支付信息错误
            if (ObjectUtils.isEmpty(prepayResult)) {
                log.error("wechatH5Pay fail due to wechat error....");
                return CommonResponse.build("", WechatPayResponseCode.WRONG_CONFIGURATION.getCode());
            }

            log.debug("wechatH5Pay getPayUrl success......");
            WechatPayResponse wechatPayResponse = new WechatPayResponse();


            // 同步回调地址
            String callBackUrl = "paysuccess?type=" + type;

            BaseInfoSet infoSet = getSite();
            wechatPayResponse.setMwebUrl(prepayResult.getMweb_url() + "&redirect_url=" + URLEncoder.encode(infoSet.getH5CallBackDomain() + callBackUrl));

            //订单code
            wechatPayResponse.setOrderCode(transCode);
            //订单金额
            wechatPayResponse.setOrderMoney(money);
            return CommonResponse.build(new JSONObject(wechatPayResponse).toString(), WechatPayResponseCode.SUCCESS.getCode());
        } else if (payType == 5) {
            //微信生成预支付信息错误
            if (ObjectUtils.isEmpty(prepayResult)) {
                log.error("wechatQRPay fail due to wechat error....");
                return CommonResponse.build("", WechatPayResponseCode.WRONG_CONFIGURATION.getCode());
            }
            log.debug("wechatQRPay gePayUrl success......");
            WechatPayResponseForRecharge wechatQRPayResponseForRecharge = new WechatPayResponseForRecharge();
            //扫码支付url
            wechatQRPayResponseForRecharge.setCodeUrl(prepayResult.getCode_url());
            //订单code
            wechatQRPayResponseForRecharge.setTransCode(transCode);
            //订单金额
            wechatQRPayResponseForRecharge.setMoney(money);

            return CommonResponse.build(new JSONObject(wechatQRPayResponseForRecharge).toString(), WechatPayResponseCode.SUCCESS.getCode());
        }

        return CommonResponse.build("", WechatPayResponseCode.SUCCESS.getCode());
    }

    @Override
    public UmsPreDepositRecord queryDepositByTranscode(String transCode, long customerId) {
        return predepositRecordService.queryPredepositRecordByTransCode(transCode, customerId);
    }


    /**
     * 获取网站地址
     */
    private BaseInfoSet getSite() {
        return baseInfoSetService.queryBaseInfoSet();
    }

    /**
     * 获取交易号
     *
     * @param transCode  交易号
     * @param money      充值金额
     * @param customerId 用户ID
     * @param channel    充值渠道
     * @return
     */
    private String getTransCode(String transCode, BigDecimal money, long customerId, String channel) {
        boolean flag = true;
        if (!StringUtils.isEmpty(transCode)) {
            UmsPreDepositRecord predepositRecord = predepositRecordService.queryPredepositRecordByTransCode(transCode, customerId);
            if (!ObjectUtils.isEmpty(predepositRecord) && !predepositRecord.isPaid() && predepositRecord.getMoney().compareTo(money) == 0) {
                flag = false;
            }
        }
        if (flag) {
            //创建充值记录
            UmsPreDepositRecord predepositRecord = UmsPreDepositRecord.buildRecharge(money, customerId, channel, snowflakeIdWorker.nextId());
            int addNum = predepositRecordService.addPredepositRecord(predepositRecord);
            if (addNum < 1) {
                return null;
            }
            transCode = predepositRecord.getTranscode();
        }
        return transCode;
    }

    /**
     * 构建预支付订单信息
     *
     * @param transCode 交易号
     * @param money     订单金额
     * @param type      类型 1:订单支付 2:预存款充值
     */
    private OrderInfoForPay buildOrderInfoForPay(String transCode, BigDecimal money, int type) {
        OrderInfoForPay orderInfoForPay = new OrderInfoForPay();
        //商品名
        orderInfoForPay.setGoodsName("预存款充值");
        //订单号
        orderInfoForPay.setOrderCode(transCode);
        //订单金额
        orderInfoForPay.setPrice(money);
        //支付类型
        orderInfoForPay.setType(type);
        return orderInfoForPay;
    }

    /**
     * 构建预支付订单信息
     *
     * @param transCode 交易号
     * @param money     订单金额
     * @param type      类型 1:订单支付 2:预存款充值
     */
    private OrderInfoForPay buildOrderInfoForPay(String transCode, BigDecimal money, String currency, int type, ZotaPayPayerObject payer) {
        OrderInfoForPay orderInfoForPay = new OrderInfoForPay();
        //商品名
        orderInfoForPay.setGoodsName("预存款充值");
        //订单号
        orderInfoForPay.setOrderCode(transCode);
        //订单金额
        orderInfoForPay.setPrice(money);
        //币种
        orderInfoForPay.setCurrency(currency);
        //支付类型
        orderInfoForPay.setType(type);
        //zotapay用户支付信息对象
        orderInfoForPay.setZotaPayPayerObject(payer);
        return orderInfoForPay;
    }
}

package io.uhha.settlement.service.impl;

import io.uhha.common.enums.AlipayResponseCode;
import io.uhha.common.utils.ZotaPayUtils;
import io.uhha.common.utils.bean.*;
import io.uhha.member.domain.UmsMember;
import io.uhha.member.service.IUmsMemberService;
import io.uhha.order.service.impl.PaySupplier;
import io.uhha.setting.bean.BaseInfoSet;
import io.uhha.setting.bean.ZotaPaySet;
import io.uhha.setting.service.BaseInfoSetService;
import io.uhha.setting.service.ILsPaySettingService;
import io.uhha.settlement.domain.SettlementWithdrawal;
import io.uhha.settlement.service.ISettlementWithdrawalService;
import io.uhha.settlement.service.IWithdrawPayService;
import io.uhha.util.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
@Slf4j
public class IWithdrawPayServiceImpl implements IWithdrawPayService {

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

    @Autowired
    private ISettlementWithdrawalService withdrawalService;

    @Override
    public int zotaPayoutNotify(ZotaPayCallbackNotification notification) {
        log.debug("zotaPayNotify and notification:{} ", notification);
        //zotapay支付设置(数据库)
        ZotaPaySet zotaPaySet = paySetService.queryPaySet().getZotaPaySet();
        //zotapay支付设置
        ZotaPaySetting zotaPaySetting = new ZotaPaySetting();
        BeanUtils.copyProperties(zotaPaySet, zotaPaySetting);
        if (!zotaPaySetting.checkPayParams()) {
            log.error("zotaPayNotify fail due to no checkPayParams fail....");
            return -2;
        }
        OrderInfoAfterPay orderInfoAfterPay = ZotaPayUtils.afterPayInfo(zotaPaySetting, notification);
        if (orderInfoAfterPay == null || !orderInfoAfterPay.isSuccess()) {
            log.error("zotaPayNotify fail due to notify fail....");
            return -3;
        }
        return payoutSuccess(Long.parseLong(orderInfoAfterPay.getOrderCode()), CommonConstant.QUERY_WITH_NO_CUSTOMER, orderInfoAfterPay.getType(), CommonConstant.ZOTA_PAY, orderInfoAfterPay.getTransCode());
    }

    @Override
    public int zotaPayout(Long withdrawId, String currency, long customerId, int type, String orderType, String orderId, ZotaPayPayerObject payer) {
        log.debug("zotaPay and withdrawId:{} \r\n currency:{} \r\n customerId:{} \r\n type:{} \r\n orderType:{} \r\n orderId:{} ", withdrawId, currency, customerId, type, orderType, orderId);

        // 同步回调地址
        String callBackUrl = "pages/user/money/success?type=" + type;

        BaseInfoSet infoSet = getSite();
        log.debug("siteUrl:{} \r\n siteBackendUrl: {} \r\n pcUrl: {}", infoSet.getSiteUrl(), infoSet.getH5CallBackDomain(), infoSet.getPcCallBackDomain());
        // 构造支付信息
        return zotaPayCommonPay(withdrawId, currency, customerId, type, payer, ZotaPaySetting.build(infoSet.getSiteUrlWithBias() + callBackUrl,
                infoSet.getSiteBackendDomainWithBias() + "zotapaynotify"), (setting, orderinfo) -> ZotaPayUtils.payout(setting, orderinfo));
    }

    @Override
    public ZotaPayQueryResponseData queryZotaPayOrder(String orderID, String merchantOrderID) {
        //Zotapay支付设置(数据库)
        ZotaPaySet zotaPaySet = paySetService.queryPaySet().getZotaPaySet();
        //Zotapay支付设置
        ZotaPaySetting zotaPaySetting = new ZotaPaySetting();
        BeanUtils.copyProperties(zotaPaySet, zotaPaySetting);
        if (!zotaPaySetting.checkPayParams()) {
            log.error("queryZotaPayOrder fail due to no checkPayParams fail....");
            return ZotaPayQueryResponseData.builder()
                    .status("Failed")
                    .build();
        }
        return ZotaPayUtils.queryOrderStatus(zotaPaySetting, orderID, merchantOrderID);
    }

    /**
     * 支付成功后的操作
     *
     * @param withdrawId  订单code
     * @param customerId 用户id
     * @param type       1:订单支付    2 预存款充值
     * @param channel    支付方式，预存款充值使用
     * @param transCode  交易流水号
     * @return 0失败，大于0成功
     */
    private int payoutSuccess(Long withdrawId, long customerId, int type, String channel, String transCode){
        // 根据订单编号和用户id查询待付款的订单信息
        SettlementWithdrawal withdrawal = withdrawalService.selectSettlementWithdrawalById(withdrawId);
        // 没有待支付的订单 则直接返回
        if (ObjectUtils.isEmpty(withdrawal)) {
            log.error("paySuccess fail due to no order....");
            return -1;
        }
        // TODO 修改订单状态
//        return withdrawalService.confirmOrderPayed(withdrawal, transCode, channel);
        return 1;
    }


    /**
     * 获取网站地址
     */
    private BaseInfoSet getSite() {
        return baseInfoSetService.queryBaseInfoSet();
    }

    /**
     * 构建预支付订单信息
     *
     * @param orderName  商品名
     * @param withdrawId  订单code
     * @param orderMoney 订单金额
     * @param type       类型 1:订单支付 2:预存款充值 3: 提款订单
     */
    private OrderInfoForPay buildOrderInfoForPay(String orderName, Long withdrawId, BigDecimal orderMoney, Integer type) {
        OrderInfoForPay orderInfoForPay = new OrderInfoForPay();
        //商品名（取一个就行）
        orderInfoForPay.setGoodsName(orderName);
        //订单号
        orderInfoForPay.setOrderCode(Long.toString(withdrawId));
        //订单金额
        orderInfoForPay.setPrice(orderMoney);
        //支付类型
        orderInfoForPay.setType(type);
        return orderInfoForPay;
    }

    /**
     * 获得付款的实体
     *
     * @return 返回付款实体
     */
    private OrderInfoForPay getOrderInfoForPay(Long withdrawId, String currency, long customerId, int type) {
            // 根据订单编号和用户id查询待付款的订单信息
            SettlementWithdrawal settlementWithdrawal = withdrawalService.selectSettlementWithdrawalById(withdrawId);

            // 没有待支付的订单直接返回
            if (ObjectUtils.isEmpty(settlementWithdrawal)) {
                return null;
            }

            return buildOrderInfoForPay("Withdraw", withdrawId, settlementWithdrawal.getWithdrawalAmount(), type);

    }

    /**
     * zotapay支付公共方法
     *
     * @param withdrawId     提款订单id
     * @param customerId     用户id
     * @param type           类型 1 订单支付 2 预存款充值 3 门店订单支付
     * @param zotaPaySetting zotapay设置实体
     * @param paySupplier    回调获得zotapay支付参数
     * @return -9 zotapay没开启 -1  用户不存在 -3 没有待支付订单 -7 网站地址不存在 -8 zotapay配置的参数错误 1 成功
     */
    private int zotaPayCommonPay(Long withdrawId, String currency, long customerId, int type, ZotaPayPayerObject payer, ZotaPaySetting zotaPaySetting, PaySupplier<ZotaPaySetting, OrderInfoForPay, String> paySupplier) {

        log.debug("zotaPayCommonPay and withdrawId:{}  \r\n customerId:{} \r\n type:{} \r\n zotaPaySetting:{} \r\n", withdrawId, customerId, type, zotaPaySetting);

        //ZOTAPAY支付设置(数据库)
        ZotaPaySet zotaPaySet = paySetService.queryPaySet().getZotaPaySet();

        // 没有开启ZOTAPAY支付 则直接返回
        if (!zotaPaySet.checkIsUse()) {
            log.error("zotaPayCommonPay fail due to not used....");
            return AlipayResponseCode.NOT_CONFIGURED.getCode();
        }

        // 查询用户信息
        UmsMember customer = customerService.queryCustomerWithNoPasswordById(customerId);
        if (Objects.isNull(customer)) {
            log.error("zotaPayCommonPay fail due to member is not ");
            return AlipayResponseCode.USER_NOT_EXIST.getCode();
        }

        // 订单支付实体
        OrderInfoForPay orderInfoForPay = getOrderInfoForPay(withdrawId, currency, customerId, type);

        // 如果没有支付订单 则直接返回
        if (Objects.isNull(orderInfoForPay)) {
            log.error("zotaPayCommonPay fail due to no order....");
            return AlipayResponseCode.ORDER_NOT_EXIST.getCode();
        }

        //支付宝支付设置
        BeanUtils.copyProperties(zotaPaySet, zotaPaySetting);

        // 校验参数
        if (!zotaPaySetting.checkPayParams()) {
            log.error("zotaPayCommonPay fail due to no checkPayParams fail....");
            return AlipayResponseCode.WRONG_CONFIGURATION.getCode();
        }

        //前台同步回调地址
        zotaPaySetting.setBeforeCallbackUrl(zotaPaySetting.getBeforeCallbackUrl());
        //异步回调地址
        zotaPaySetting.setBackCallbackUrl(zotaPaySetting.getBackCallbackUrl());
        log.info("zotaPaySetting=" + zotaPaySetting);
        String zotapayParams = paySupplier.getPayParams(zotaPaySetting, orderInfoForPay);

        //生成表单错误
        if (StringUtils.isEmpty(zotapayParams)) {
            log.error("zotaPayCommonPay fail due to form error....");
            return AlipayResponseCode.FORM_GENERATION_FAILED.getCode();
        }
        log.debug("zotaPayCommonPay pay success......");

        return AlipayResponseCode.SUCCESS.getCode();
    }
}

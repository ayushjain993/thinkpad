package io.uhha.settlement.service;

import io.uhha.common.utils.bean.ZotaPayCallbackNotification;
import io.uhha.common.utils.bean.ZotaPayPayerObject;
import io.uhha.common.utils.bean.ZotaPayQueryResponseData;

/**
 * 提款操作控制器。通过操作支付渠道的api实现对提款订单付款
 */
public interface IWithdrawPayService {
    /**
     * zotapay回调
     *
     * @param notification zotapay回调参数
     * @return 返回码 1:成功 -1:没有订单
     */
    int zotaPayoutNotify(ZotaPayCallbackNotification notification);

    /*
     * zotapay 支付
     *
     * @param orderCode  订单code
     * @param customerId 用户id
     * @param type       支付类型 1:订单支付 2:预存款充值 3:门店线下支付
     * @param orderType  订单类型 主要是社区团购使用
     * @param orderId    订单id
     * @return
     */
    public int zotaPayout(Long withdrawId, String currency, long customerId, int type, String orderType, String orderId, ZotaPayPayerObject payer);

    /**
     * 查询payout订单
     * @param orderID
     * @param merchantOrderID
     * @return
     */
    public ZotaPayQueryResponseData queryZotaPayOrder(String orderID, String merchantOrderID);
}

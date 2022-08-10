package io.uhha.order.vo;

import lombok.Data;

/**
 * Created by mj on 17/11/13.
 * 取消订单参数
 */
@Data
public class DeleteOrderParams {

    /**
     * 会员id
     */
    private long customerId;

    /**
     * 订单id
     */
    private long orderId;

    /**
     * 店铺id
     */
    private long storeId;

    /**
     * 操作人
     */
    private String operationName;

    /**
     * 请求来源 0 用户 1 管理员
     */
    private int source;

    /**
     * 构造请求来源用户的取消订单参数
     *
     * @param customerId 用户id
     * @param orderId    订单id
     * @return 返回取消订单参数实体
     */
    public static DeleteOrderParams buildCustomerSource(long customerId, long orderId) {
        DeleteOrderParams cancelOrderParams = new DeleteOrderParams();
        cancelOrderParams.customerId = customerId;
        cancelOrderParams.orderId = orderId;
        cancelOrderParams.source = 0;
        cancelOrderParams.storeId = -1;
        return cancelOrderParams;
    }

    /**
     * 构造请求来自管理员的取消订单参数
     *
     * @param storeId       店铺id
     * @param orderId       订单id
     * @param operationName 操作人
     * @return 返回取消订单参数实体
     */
    public static DeleteOrderParams buildManagerSource(long storeId, long orderId, String operationName) {
        DeleteOrderParams cancelOrderParams = new DeleteOrderParams();
        cancelOrderParams.storeId = storeId;
        cancelOrderParams.customerId = -1;
        cancelOrderParams.orderId = orderId;
        cancelOrderParams.operationName = operationName;
        cancelOrderParams.source = 1;
        return cancelOrderParams;
    }

    /**
     * 构造请求来自系统自动取消的取消订单参数
     *
     * @param orderId 订单id
     * @param storeId 店铺id
     * @return 返回取消订单参数实体
     */
    public static DeleteOrderParams buildAutoCancelSource(long orderId, long storeId) {
        DeleteOrderParams cancelOrderParams = new DeleteOrderParams();
        cancelOrderParams.storeId = storeId;
        cancelOrderParams.customerId = -1;
        cancelOrderParams.orderId = orderId;
        cancelOrderParams.operationName = "admin";
        cancelOrderParams.source = 1;
        return cancelOrderParams;
    }

    /**
     * 判断是否来用户请求
     *
     * @return 0 返回true  否则返回false
     */
    public boolean isFromCustomer() {
        return this.source == 0;
    }
}

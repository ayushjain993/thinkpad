package io.uhha.order.service;


import io.uhha.order.vo.OrderSettlement;
import io.uhha.order.vo.OrderSettlementRequest;

/**
 * Created by mj on 17/11/2.
 * 结算服务接口
 */
public interface SettlementService {

    /**
     * 订单结算
     *
     * @param orderSettlementRequest 订单结算请求参数
     * @return 返回订单结算实体
     */
    OrderSettlement orderSettlement(OrderSettlementRequest orderSettlementRequest);

}

package io.uhha.common.utils.bean;


import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZotaPayPayoutResponseData {
    /**
     * 商品方的订单id
     */
    String merchantOrderID;
    /**
     * Zotapay generated orderId
     */
    String orderID;
}
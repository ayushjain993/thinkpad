package io.uhha.common.utils.bean;


import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZotaPayDepositResponseData {
    /**
     * 商品方的订单id
     */
    private String merchantOrderID;
    /**
     * Zotapay generated orderId
     */
    private String orderID;

    private String depositUrl;
}
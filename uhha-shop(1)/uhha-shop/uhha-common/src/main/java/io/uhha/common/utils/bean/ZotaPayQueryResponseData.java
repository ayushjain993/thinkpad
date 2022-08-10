package io.uhha.common.utils.bean;


import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZotaPayQueryResponseData {
    String type;
    String status;
    String errorMessage;
    String processorTransactionID;
    /**
     * Zotapay generated orderId
     */
    String orderID;
    /**
     * 商品方的订单id
     */
    String merchantOrderID;
    String amount;
    String currency;
    String customerEmail;
    String customParam;
    Object request;

}
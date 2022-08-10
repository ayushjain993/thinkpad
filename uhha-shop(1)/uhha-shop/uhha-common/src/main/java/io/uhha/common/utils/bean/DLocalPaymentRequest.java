package io.uhha.common.utils.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DLocalPaymentRequest implements Serializable {
    @JSONField(name = "amount")
    BigDecimal amount;

    @JSONField(name = "currency")
    String currency;

    @JSONField(name = "country")
    String country;

    @JSONField(name = "payment_method_id")
    String paymentMethodId;

    @JSONField(name = "payment_method_flow")
    String paymentMethodFlow;

    @JSONField(name = "order_id")
    String orderId;

    @JSONField(name = "notification_url")
    String notificationUrl;

    @JSONField(name = "card")
    DLocalCreditCardObject card;

    @JSONField(name = "payer")
    DLocalPayerObject payer;
}

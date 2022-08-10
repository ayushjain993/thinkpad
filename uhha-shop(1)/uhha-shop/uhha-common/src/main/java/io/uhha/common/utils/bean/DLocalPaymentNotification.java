package io.uhha.common.utils.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

//{
//        "id": "D-4-be8eda8c-5fe7-49dd-8058-4ddaac00611b",
//        "amount": 72.00,
//        "status": "PAID",
//        "status_detail": "The payment was paid.",
//        "status_code": "200",
//        "currency": "USD",
//        "country": "AR",
//        "payment_method_id": "RP",
//        "payment_method_type": "TICKET",
//        "payment_method_flow": "REDIRECT",
//        "payer": {
//        "name": "Nino Deicas",
//        "user_reference": "US-jmh3gb4kj5h34",
//        "email": "buyer@gmail.com",
//        },
//        "order_id": "4m1OdghPUQtg",
//        "notification_url": "http://www.merchant.com/notifications",
//        "created_date": "2019-06-26T15:17:31.000+0000"
//        }

@Data
public class DLocalPaymentNotification implements Serializable {
    @JSONField(name = "id")
    private String id;

    @JSONField(name = "amount")
    private BigDecimal amount;

    @JSONField(name = "status")
    private String status;

    @JSONField(name = "status_detail")
    private String statusDetail;

    @JSONField(name = "status_code")
    private String statusCode;

    @JSONField(name = "country")
    private String country;

    @JSONField(name = "currency")
    private String currency;

    @JSONField(name = "payment_method_id")
    private String paymentMethodId;

    @JSONField(name = "payment_method_type")
    private String paymentMethodType;

    @JSONField(name = "payment_method_flow")
    private String paymentMethodFlow;

    @JSONField(name = "payer")
    private DLocalPayerObject payer;

    @JSONField(name = "order_id")
    private String orderId;

    @JSONField(name = "notification_url")
    private String notificationUrl;

    @JSONField(name = "created_date")
    private String createDate;

}

package io.uhha.common.utils.bean;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

//{
//        "id": "D-4-80ca7fbd-67ad-444a-aa88-791ca4a0c2b2",
//        "amount": 120,
//        "currency": "USD",
//        "payment_method_id": "CARD",
//        "payment_method_type": "CARD",
//        "payment_method_flow": "DIRECT",
//        "country": "BR",
//        "card": {
//        "holder_name": "Thiago Gabriel",
//        "expiration_month": 10,
//        "expiration_year": 2040,
//        "brand": "VI",
//        "last4": "1111"
//        },
//        "created_date": "2018-12-26T20:28:47.000+0000",
//        "approved_date": "2018-12-26T20:28:47.000+0000",
//        "status": "PAID",
//        "status_detail": "The payment was paid",
//        "status_code": "200",
//        "order_id": "657434343",
//        "notification_url": "http://merchant.com/notifications"
//        }

/**
 * DLocal SmartFields 付款响应对象
 * https://dlocal.gitbook.io/dlocal-china/api-dui-jie-wen-dang/payins-api-wen-dang/chuang-jian-fu-kuan/yin-hang-ka-fu-kuan#shi-yong-smart-fields-fa-qi-fu-kuan
 */
@Data
public class DLocalPaymentResponse implements Serializable {
    @JSONField(name = "id")
    private String id;

    @JSONField(name = "amount")
    private BigDecimal amount;

    @JSONField(name = "currency")
    private String currency;

    @JSONField(name = "payment_method_id")
    private String paymentMethodId;

    @JSONField(name = "payment_method_type")
    private String paymentMethodType;

    @JSONField(name = "payment_method_flow")
    private String paymentMethodFlow;

    @JSONField(name = "country")
    private String country;

    @JSONField(name = "card")
    private DLocalCreditCardObject card;

    @JSONField(name = "created_date")
    private String createDate;

    @JSONField(name = "approved_date")
    private String approvedDate;

    @JSONField(name = "status")
    private String status;

    @JSONField(name = "status_detail")
    private String statusDetail;

    @JSONField(name = "status_code")
    private String statusCode;

    @JSONField(name = "order_id")
    private String orderId;

    @JSONField(name = "notification_url")
    private String notificationUrl;
}

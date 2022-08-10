package io.uhha.common.utils.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * curl -X POST \
 *     "https://api.zotapay.com/api/v1/deposit/request/1050/" \
 *     -H "Content-Type: application/json" \
 *     -d '{
 *         "merchantOrderID": "QvE8dZshpKhaOmHY",
 *         "merchantOrderDesc": "Test order",
 *         "orderAmount": "500.00",
 *         "orderCurrency": "THB",
 *         "customerEmail": "customer@email-address.com",
 *         "customerFirstName": "John",
 *         "customerLastName": "Doe",
 *         "customerAddress": "5/5 Moo 5 Thong Nai Pan Noi Beach, Baan Tai, Koh Phangan",
 *         "customerCountryCode": "TH",
 *         "customerCity": "Surat Thani",
 *         "customerZipCode": "84280",
 *         "customerPhone": "+66-77999110",
 *         "customerIP": "103.106.8.104",
 *         "redirectUrl": "https://www.example-merchant.com/payment-return/",
 *         "callbackUrl": "https://www.example-merchant.com/payment-callback/",
 *         "customParam": "{\"UserId\": \"e139b447\"}",
 *         "checkoutUrl": "https://www.example-merchant.com/account/deposit/?uid=e139b447",
 *         "signature": "47d7ed292cf10e689b311ef5573eddbcc8505fe51e20d3f74e6b33756d96800b"
 *     }'
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZotaDepositPayRequest {

    private String merchantOrderID;
    private String merchantOrderDesc;
    private String orderAmount;
    private String orderCurrency;
    private String customerEmail;
    private String customerFirstName;
    private String customerLastName;
    private String customerAddress;
    private String customerCountryCode;
    private String customerCity;
    private String customerState;
    private String customerZipCode;
    private String customerPhone;
    private String customerIP;
    private String customerBankCode;
    private String redirectUrl;
    private String callbackUrl;
    private String checkoutUrl;
    private String customParam;
    private String language;
    private String signature;
}

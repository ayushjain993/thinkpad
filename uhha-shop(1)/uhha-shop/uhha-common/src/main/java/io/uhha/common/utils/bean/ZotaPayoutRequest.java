package io.uhha.common.utils.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Zotapay支付对象
 * https://mg-tools.zotapay.com/#/
 *
 * curl -X POST \
 *     "https://api.zotapay.com/api/v1/payout/request/1050/" \
 *     -H "Content-Type: application/json" \
 *     -d '{
 *         "merchantOrderID": "TbbQzewLWwDW6goc",
 *         "merchantOrderDesc": "Test order",
 *         "orderAmount": "500.00",
 *         "orderCurrency": "THB",
 *         "customerEmail": "customer@email-address.com",
 *         "customerFirstName": "John",
 *         "customerLastName": "Doe",
 *         "customerPhone": "+66-77999110",
 *         "customerIP": "103.106.8.104",
 *         "callbackUrl": "https://www.example-merchant.com/payout-callback/",
 *         "customerBankCode": "BBL",
 *         "customerBankAccountNumber": "100200",
 *         "customerBankAccountName": "John Doe",
 *         "customerBankBranch": "Bank Branch",
 *         "customerBankAddress": "Thong Nai Pan Noi Beach, Baan Tai, Koh Phangan",
 *         "customerBankZipCode": "84280",
 *         "customerBankProvince": "Bank Province",
 *         "customerBankArea": "Bank Area / City",
 *         "customerBankRoutingNumber": "000",
 *         "customParam": "{\"UserId\": \"e139b447\"}",
 *         "checkoutUrl": "https://www.example-merchant.com/account/withdrawal/?uid=e139b447",
 *         "signature": "d04ccb6a14d2c9e6f566766b8158bc4dd5ab6c3bb964a446da92aa61b882d88b"
 *     }'
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZotaPayoutRequest implements Serializable {
    private String merchantOrderID;
    private String merchantOrderDesc;
    private String orderAmount;
    private String orderCurrency;
    private String customerEmail;
    private String customerFirstName;
    private String customerLastName;
    private String customerPhone;
    private String customerIP;
    private String customerPersonalID;
    private String customerBankCode;
    private String customerBankAccountNumber;
    private String customerBankAccountNumberDigit;
    private String customerBankAccountType;
    private String customerBankSwiftCode;
    private String customerBankAccountName;
    private String customerBankBranch;
    private String customerBankBranchDigit;
    private String customerBankAddress;
    private String customerAddress;
    private String customerBankZipCode;
    private String customerBankRoutingNumber;
    private String customerBankProvince;
    private String customerBankArea;
    private String callbackUrl;
    private String customParam;
    private String signature;


}



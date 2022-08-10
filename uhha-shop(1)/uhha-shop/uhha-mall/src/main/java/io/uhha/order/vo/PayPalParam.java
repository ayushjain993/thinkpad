package io.uhha.order.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

@Data
public class PayPalParam extends PayParam implements Serializable {

    /**
     * 币种
     */
    private String currency;

    /**
     * 用户Id，管理UmsMember表中的id
     */
    private Long customerId;

    /**
     * Zotapay要求的付款人信息
     */
    private String customerEmail;
    private String customerFirstName;
    private String customerLastName;
    private String customerAddress;
    private String customerCountryCode;
    private String customerCity;
    private String customerState;
    private String customerZipCode;
    private String customerPhone;
    private String customerBankCode;
    private String customerBankAccountNumber;
    private String customerBankAccountName;
    @JsonIgnore
    private String customerBankBranch;
    @JsonIgnore
    private String customerBankAddress;
    @JsonIgnore
    private String customerBankZipCode;
    @JsonIgnore
    private String customerBankProvince;
    @JsonIgnore
    private String customerBankArea;
    @JsonIgnore
    private String customerBankRoutingNumber;
    @JsonIgnore
    private String customerIP;

}

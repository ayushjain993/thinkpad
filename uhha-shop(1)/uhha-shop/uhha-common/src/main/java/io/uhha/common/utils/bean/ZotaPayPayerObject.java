package io.uhha.common.utils.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZotaPayPayerObject implements Serializable {

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
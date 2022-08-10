package io.uhha.common.utils.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ToString
public class ZotaPayCallbackNotification {
    private String type;
    private String amount;
    private String status;
    private String orderID;
    private String currency;
    private extraData extraData;
    private String signature;
    private String endpointID;
    private String customParam;
    private String errorMessage;
    private String customerEmail;
    private String merchantOrderID;

    private ZotaPayoutRequest originalRequest;
    private String processorTransactionID;

}

@Data
@ToString
class extraData{
    private String ID;
    private String Note;
    private String Amount;
    private String Status;
    private String Currency;
    /**
     * customer email
     */
    private String Customer;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss aaa")
    private Date Datetime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss aaa")
    private Date StatementDate;

    private String selectedBankCode;
    private String selectedBankName;
}

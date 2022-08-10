package io.uhha.common.utils.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class P2c2pPaymentResponse implements Serializable {
    private String merchantID;
    private String invoiceNo;
    private String cardNo;
    private String amount;
    private String currencyCode;
    private String tranRef;
    private String referenceNo;
    private String approvalCode;
    private String eci;
    private String transactionDateTime;
    private String respCode;
    private String respDesc;
}

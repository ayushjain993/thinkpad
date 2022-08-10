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
public class P2c2pTokenResponse implements Serializable {
    private String paymentToken;
    private String webPaymentUrl;
    private String respDesc;
    private String respCode;
}

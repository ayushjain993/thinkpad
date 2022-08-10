package io.uhha.member.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class UmsRedeemVo {
    @NotNull
    private BigDecimal token;

    @NotNull
    private String password;
}

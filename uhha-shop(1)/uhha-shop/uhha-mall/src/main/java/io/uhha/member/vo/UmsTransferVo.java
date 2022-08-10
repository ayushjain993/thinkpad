package io.uhha.member.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class UmsTransferVo {
    @NotNull
    private BigDecimal money;

    @NotNull
    private String password;
}

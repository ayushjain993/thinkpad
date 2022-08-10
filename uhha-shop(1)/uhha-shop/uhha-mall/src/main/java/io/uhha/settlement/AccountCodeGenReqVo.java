package io.uhha.settlement;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class AccountCodeGenReqVo {
    @NotNull
    private String accountType;
    @NotNull
    private Integer amount;
    @NotNull
    @Min(1)
    @Max(100)
    private Integer currency;
}

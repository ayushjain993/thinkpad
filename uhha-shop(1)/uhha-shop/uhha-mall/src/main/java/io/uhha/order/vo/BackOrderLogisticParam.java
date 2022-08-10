package io.uhha.order.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BackOrderLogisticParam {
    @NotNull
    private Long id;
    @NotNull
    private String logisCompanyName;
    @NotNull
    private String waybillCode;
}

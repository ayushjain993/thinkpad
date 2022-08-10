package io.uhha.settlement.model;

import io.uhha.common.enums.WithdrawalStatusEnum;
import lombok.Data;

@Data
public class WithdrawalRequest {
    private Long id;
    private String failReason;
    private WithdrawalStatusEnum status;
}

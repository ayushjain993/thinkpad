package io.uhha.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum WithdrawalStatusEnum {
    UNDER_AUDIT("审核中"),
    PASS("审核通过"),
    REJECTED("审核未通过"),
    WITHDRAWAL_ING("提现中"),
    WITHDRAWAL_FAIL("提现失败"),
    WITHDRAWAL_SUCCESS("提现成功");

    private String description;
}

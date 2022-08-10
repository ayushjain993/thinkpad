package io.uhha.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApplyStatusEnum {
    APPLYING("申请中"), PASS("通过"), REJECT("拒绝");
    private final String desc;
}

package io.uhha.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SettlementTypeEnum {
    AUTO("自动"),MANUAL("手动");
    private final String desc;
}

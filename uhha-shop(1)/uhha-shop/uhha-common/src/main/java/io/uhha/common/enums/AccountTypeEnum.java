package io.uhha.common.enums;

import io.uhha.common.utils.StringUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AccountTypeEnum {
    PERSONAL("个人账户"), SYSTEM("系统账户"), STORE("商户账户");

    public static String getDescription(String name) {
        for (AccountTypeEnum ele : values()) {
            if (StringUtils.equals(ele.name(), name)) return ele.getDesc();
        }
        return null;
    }

    private final String desc;
}

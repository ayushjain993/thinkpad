package io.uhha.common.enums;

import io.uhha.common.utils.StringUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ImUserTypeEnum {
    PERSONAL("个人", "User"), SYSTEM("系统", "Platform Support"), STORE("商户", "Store Support");

    public static String getDescription(String name) {
        for (ImUserTypeEnum ele : values()) {
            if (StringUtils.equals(ele.name(), name)) {
                return ele.getDesc();
            }
        }
        return null;
    }

    private final String desc;
    private final String value;
}

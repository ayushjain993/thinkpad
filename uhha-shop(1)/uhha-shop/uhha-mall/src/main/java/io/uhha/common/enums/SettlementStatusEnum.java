package io.uhha.common.enums;

import io.uhha.common.utils.StringUtils;

/**
 * 结算状态 0 未结算 1 已结算 默认0
 */
public enum SettlementStatusEnum {

    NOT_SETTLED("0","未结算"),
    SETTLED("1", "已结算");

    private String code;
    private String description;

    SettlementStatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static SettlementStatusEnum getStatus(String status) {
        for (SettlementStatusEnum ele : values()) {
            if (StringUtils.equals(ele.getCode(), status)) return ele;
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

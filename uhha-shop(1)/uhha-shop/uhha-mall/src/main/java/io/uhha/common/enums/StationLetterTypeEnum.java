package io.uhha.common.enums;

/**
 * 站内信类型 0 公告 1 交易相关 2 私信
 */
public enum StationLetterTypeEnum {
    /**
     * 公告
     */
    ANNOUNCEMENT("1", "公告"),

    /**
     * 交易相关
     */
    ORDER_RELATED("2", "交易相关"),

    /**
     * 私信
     */
    PRIVATE("3", "私信");

    private String code;
    private String desc;

    StationLetterTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

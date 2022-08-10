package io.uhha.coin.common.Enum.validate;

/**
 * 国际化语言环境枚举
 */
public enum LocaleEnum {

    ZH_CN(1, "简体中文", "zh_CN"),
    ZH_TW(2, "繁体中文", "zh_TW"),
    EN_US(3, "English",  "en_US"),
    KO_KR(4, "한국어",  "ko_KR"),
    JA_JP(5, "日本語",  "ja_JP");
    private Integer code;
    private String value;
    private String name;


    LocaleEnum(Integer code, String value, String name) {
        this.code = code;
        this.value = value;
        this.name = name;
    }

    public static String getValueByCode(Integer code) {
        for (LocaleEnum e : LocaleEnum.values()) {
            if (e.getCode().equals(code)) {
                return e.getValue();
            }
        }
        return null;
    }
    public static String getNameByCode(Integer code) {
        for (LocaleEnum e : LocaleEnum.values()) {
            if (e.getCode().equals(code)) {
                return e.getName();
            }
        }
        return null;
    }
    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}

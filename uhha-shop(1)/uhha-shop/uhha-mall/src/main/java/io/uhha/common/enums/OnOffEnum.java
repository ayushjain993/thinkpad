package io.uhha.common.enums;

public enum OnOffEnum {
    ON ("0"),
    OFF ("1");

    private String code;

    OnOffEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

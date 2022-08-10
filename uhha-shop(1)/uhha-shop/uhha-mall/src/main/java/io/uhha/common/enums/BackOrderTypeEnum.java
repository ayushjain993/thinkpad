package io.uhha.common.enums;

public enum BackOrderTypeEnum {
    RETURN_FUND("1", "退款"),
    RETURN_GOODS("2","退货");

    private String code;
    private String description;

    BackOrderTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
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

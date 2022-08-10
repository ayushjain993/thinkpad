package io.uhha.coin.common.Enum;

/**
 * 虚拟币类型
 *
 * @author LY
 */
public enum ERC20CoinEnum {
    UHHA(1, "UHHA");

    private Integer code;
    private Object value;

    private ERC20CoinEnum(Integer code, Object value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public static String getValueByCode(Integer code) {
        for (ERC20CoinEnum coinType : ERC20CoinEnum.values()) {
            if (coinType.getCode().equals(code)) {
                return coinType.getValue().toString();
            }
        }
        return null;
    }
}

package io.uhha.coin.common.Enum;

/**
 * 虚拟币类型
 *
 * @author LY
 */
public enum SystemCoinSortEnum {
    CNY(1, "法币类"),
    BTC(2, "比特币类"),
    ETH(3, "以太坊类"),
    TOKEN(7, "淘类"),
    USDT(18,"USDT");

    private Integer code;
    private Object value;

    private SystemCoinSortEnum(Integer code, Object value) {
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
        for (SystemCoinSortEnum coinType : SystemCoinSortEnum.values()) {
            if (coinType.getCode().equals(code)) {
                return coinType.getValue().toString();
            }
        }
        return null;
    }
}

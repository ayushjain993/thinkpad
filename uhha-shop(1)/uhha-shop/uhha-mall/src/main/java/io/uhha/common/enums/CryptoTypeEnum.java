package io.uhha.common.enums;

public enum CryptoTypeEnum {
    UHHA(1,"UHHA"),
    BTC(2,"BTC"),
    ETH(3,"ETH"),
    USDT(4,"USDT")

    ;
    int coinId;
    String shortName;

    CryptoTypeEnum(int coinId, String shortName) {
        this.coinId = coinId;
        this.shortName = shortName;
    }

    public int getCoinId() {
        return coinId;
    }

    public void setCoinId(int coinId) {
        this.coinId = coinId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}

package io.uhha.coin.common.coin.enums;

public enum  EndpointsEnums {

    /**
     * 代币网络环境
     * mainnet:主要的以太坊网络, ropsten:测试网, kovan:, rinkeby:, goerli:
     */
    MAINNET(1, "mainnet"),
    ROPSTEN(2, "ropsten"),
    KOVAN(3, "kovan"),
    RINKEBY(4, "rinkeby"),
    GOERLI(5, "goerli"),
    ;

    /**
     * 编号
     */
    private int code;

    /**
     * 名称
     */
    private String name;


    EndpointsEnums(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
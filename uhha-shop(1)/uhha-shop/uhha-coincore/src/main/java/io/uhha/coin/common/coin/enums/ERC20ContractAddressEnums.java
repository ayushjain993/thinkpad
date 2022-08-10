package io.uhha.coin.common.coin.enums;

import java.math.BigInteger;

public enum ERC20ContractAddressEnums {

    // USDT
    USDT(1, "usdt", EndpointsEnums.MAINNET, "1000000000000000000",
            "0xdAC17F958D2ee523a2206206994597C13D831ec7", "erc20.contract.USDTKeyContract"),
    // UHHA
    UHHA(2, "uhha", EndpointsEnums.MAINNET, "1000000000000000000",
            "0xdAC17F958D2ee523a2206206994597C13D831ec7", "io.uhha.coin.common.coin.contract.UhhaNFTContract"),

    ;

    /**
     * 代币地址编号
     */
    private int code;

    /**
     * 代币名称
     */
    private String name;

    /**
     * 代币环境
     */
    private EndpointsEnums endpoints;

    /**
     * 精度
     */
    private BigInteger wei;

    /**
     * 代币地址
     */
    private String  contractAddress;

    /**
     * ERC20 代币 KeyContract 类地址
     */
    private String  contractClassName;

    ERC20ContractAddressEnums(int code, String name, EndpointsEnums endpoints, String wei, String contractAddress, String contractClassName) {
        this.code = code;
        this.name = name;
        this.endpoints = endpoints;
        this.wei = new BigInteger(wei);
        this.contractAddress = contractAddress;
        this.contractClassName = contractClassName;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getWei() {
        return wei;
    }

    public void setWei(BigInteger wei) {
        this.wei = wei;
    }

    public String getContractClassName() {
        return contractClassName;
    }

    public void setContractClassName(String contractClassName) {
        this.contractClassName = contractClassName;
    }

    public EndpointsEnums getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(EndpointsEnums endpoints) {
        this.endpoints = endpoints;
    }
}

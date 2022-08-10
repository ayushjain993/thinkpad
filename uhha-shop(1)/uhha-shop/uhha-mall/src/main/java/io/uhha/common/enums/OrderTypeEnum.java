package io.uhha.common.enums;

/**
 * 订单类型 0 普通订单 1 定金预售订单 2全款预售订单 3 拼团订单 4 众筹全款 5 众筹1元 6 众筹无回报 7 虚拟商品订单 8 社区团购订单 默认0 普通订单
 */
public enum OrderTypeEnum {
    /**
     * 0 普通订单
     */
    ORDINARY_ORDER("0", "普通订单"),

    /**
     * 1 定金预售订单
     */
    DEPOSIT_PRESALE_ORDER("1", "1 定金预售订单"),

    /**
     * 2全款预售订单
     */
    FULL_ADVANCE_ORDER("2", "2 全款预售订单"),

    /**
     * 3 拼团订单
     */
    GROUP_ORDER("3", "3 拼团订单"),

    /**
     * 4 众筹全款
     */
    CROWDSALE_ORDER("4", "4 众筹全款"),

    /**
     * 5 众筹1元
     */
    CROWDSALE_ONE_ORDER("5", "5 众筹1元"),

    /**
     * 6 众筹无回报
     */
    CROWDSALE_NONE_ORDER("6", "6 众筹无回报"),

    /**
     * 7 虚拟商品订单
     */
    VIRTUAL_PRODUCT_ORDER("7", "7 虚拟商品订单"),

    /**
     * 8 社区团购订单
     */
    COMMUNITY_GROUP_ORDER("8", "8 社区团购订单"),

    ;

    private String code;
    private String description;

    OrderTypeEnum(String code, String description) {
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

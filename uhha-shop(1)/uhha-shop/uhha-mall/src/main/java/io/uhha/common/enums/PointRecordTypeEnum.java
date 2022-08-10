package io.uhha.common.enums;

/**
 * 积分纪录来源类型 1 订单使用 2 订单取消 3 操作员修改 4 签到 5 积分商城使用 6 邮箱验证 7 评论 8 注册赠送
 */
public enum PointRecordTypeEnum {

    /**
     * 订单使用积分
     */
    ORDER_CONSUMED("1", "Consume points in order"),

    /**
     * 取消订单返回积分
     */
    ORDER_CANCELED("2","Return points due to order cancellations"),

    /**
     * 操作员修改
     */
    OPERATOR_UPDATE("3","Operator updates"),

    /**
     * 签到
     */
    SIGN("4", "Sign"),

    /**
     * 积分商城使用
     */
    POINT_STORE("5", "Consume in point store"),

    /**
     * 邮箱验证赠送积分
     */
    EMAIL_VERIFY("6","Email binding"),

    /**
     * 评论赠送积分
     */
    ECOMMERCE_COMMENT("7","Commenting"),

    /**
     * 注册赠送积分
     */
    REGISTER("8", "Registration"),

    /**
     * 发布视频
     */
    POST_VIDEO("9","Publish video"),

    /**
     * 视频评论赠送积分
     */
    VIDEO_COMMENT("10","Video commenting"),

    /**
     * 视频点赞赠送积分
     */
    VIDEO_LIKE("11","Like video"),

    /**
     * 评论点赞赠送积分
     */
    COMMENT_LIKE("12","Like comment"),

    /**
     * 兑换Token类型
     */
    REDEEM_ORDER("13", "Redeem Token")

    ;

    private String code;
    private String description;

    PointRecordTypeEnum(String code, String description) {
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

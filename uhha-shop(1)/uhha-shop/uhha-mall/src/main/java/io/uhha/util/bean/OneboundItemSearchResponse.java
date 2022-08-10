package io.uhha.util.bean;

import lombok.Data;

import java.math.BigInteger;

@Data
public class OneboundItemSearchResponse {
    /**
     * 商品标题
     */
    private String title;

    /**
     * 宝贝图片
     */
    private String pic_url;

    /**
     * 优惠价
     */
    private String promotion_price;

    /**
     * 价格
     */
    private Float price;
    private Float price_range;

    /**
     * 销量
     */
    private Integer sales;

    /**
     * 宝贝ID
     */
    private BigInteger  num_iid;
    private BigInteger sample_id;

    /**
     * 卖家昵称
     */
    private String seller_nick;

    /**
     * 邮费
     */
    private Integer post_fee;

    /**
     * 卖家所在区域
     */
    private String area;

    /**
     * 卖家id
     */
    private BigInteger seller_id;

    /**
     * 宝贝链接接
     */
    private String seller_url;

    /**
     * 卖家地址
     */
    private String seller_addr;

    /**
     * 店铺ID
     */
    private Integer shopid;

}

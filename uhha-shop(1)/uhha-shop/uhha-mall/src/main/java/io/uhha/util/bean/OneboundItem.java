package io.uhha.util.bean;

import lombok.Data;

/**
 * https://open.onebound.cn/help/api/yiwugo.item_get.html
 */
@Data
public class OneboundItem {
    /**
     * 宝贝ID
     */
    private String num_iid;

    /**
     * 商品标题
     */
    private String title;

    /**
     * 商品简介
     */
    private String desc_short;

    /**
     * 宝贝图片
     */
    private String pic_url;

    private Integer min_num;

    private Integer num;

    /**
     * EMS费用
     */
    private Float express_fee;

    /**
     *	发货至
     */
    private String shipping_to;

    /**
     * 	卖家昵称
     */
    private String nick;

    /**
     * 宝贝链接
     */
    private String detail_url;

    private String desc;

    /**
     * 卖家ID
     */
    private Integer seller_id;

    /**
     * 销售额
     */
    private Integer sales;

    /**
     * 卖家信息
     */
    private SellerInfo seller_info;

    /**
     * 	店铺ID
     */
    private Integer shop_id;

    /**
     * 属性列表
     */
    private String[] props_alias;

    /**
     * 商品图片列表
     */
    private String[] item_imgs;

    /**
     * 	商品属性
     */
    private String[] props_list;

    /**
     * 商品规格信息列表
     */
    private OneboundSku sku;

    /**
     * 	价格
     */
    private Float price;

}

/**
 * 	{"desc": "卫红女装制衣厂实体经营中高档女装，款式新颖价格实惠，欢迎全国各地的顾客前来咨询订购", "dizhi": "", "nick": "卫红女装制衣厂义乌篁园市场", "seller_name": "", "user_num_id": "", "phone": "", "QQ": ""}
 */
@Data
class SellerInfo{

    private String desc;
    private String dizhi;
    private String nick;
    private String seller_name;
    private String user_num_id;
    private String phone;
    private String QQ;
}




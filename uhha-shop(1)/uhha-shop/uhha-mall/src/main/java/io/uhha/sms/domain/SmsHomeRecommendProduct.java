package io.uhha.sms.domain;

import io.uhha.common.annotation.Excel;
import io.uhha.common.core.domain.BaseEntity;
import io.uhha.common.md5.MD5Utils;
import lombok.*;

import java.math.BigDecimal;

/**
 * 人气推荐商品对象 sms_home_recommend_product
 *
 * @author ruoyi
 * @date 2020-08-06
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SmsHomeRecommendProduct extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 首页推荐产品id
     */
    private Long id;

    /**
     * 产品编号
     */
    @Excel(name = "产品编号SpuId")
    private Long productId;

    /**
     * 产品名称
     */
    @Excel(name = "产品名称")
    private String productName;

    /**
     * 推荐状态
     */
    @Excel(name = "推荐状态 1 推荐 0 不推荐")
    private Integer recommendStatus;

    /**
     * 排序
     */
    @Excel(name = "排序")
    private Integer sort;

    /**
     * 所属店铺
     */
    @Excel(name = "所属店铺")
    private Long storeId;

    /**
     * 价格
     */
    @Excel(name = "价格")
    private BigDecimal price;

    /**
     * 图片
     */
    @Excel(name = "图片")
    private String pic;

    public String md5hash(){
        return MD5Utils.getInstance().createMd5(toString());
    }

}

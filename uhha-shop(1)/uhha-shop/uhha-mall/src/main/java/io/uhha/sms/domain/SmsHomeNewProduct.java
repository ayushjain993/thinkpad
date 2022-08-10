package io.uhha.sms.domain;

import io.uhha.common.annotation.Excel;
import io.uhha.common.core.domain.BaseEntity;
import lombok.*;

import java.math.BigDecimal;

/**
 * 新鲜好物对象 sms_home_new_product
 *
 * @author ruoyi
 * @date 2020-08-06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SmsHomeNewProduct extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
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


}

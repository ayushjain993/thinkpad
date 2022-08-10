package io.uhha.sms.domain;

import io.uhha.common.annotation.Excel;
import io.uhha.common.core.domain.BaseEntity;
import lombok.*;

/**
 * 首页推荐品牌对象 sms_home_brand
 *
 * @author ruoyi
 * @date 2020-08-06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SmsHomeBrand extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Long id;

    /**
     * 品牌id
     */
    @Excel(name = "品牌id")
    private Long brandId;

    /**
     * 品牌名称
     */
    @Excel(name = "品牌名称")
    private String brandName;

    /**
     * 推荐状态
     */
    @Excel(name = "推荐状态")
    private Integer recommendStatus;

    /**
     * 序号
     */
    @Excel(name = "序号")
    private Integer sort;

    /**
     * 所属店铺
     */
    @Excel(name = "所属店铺")
    private Long storeId;

    /**
     * log
     */
    @Excel(name = "logo")
    private String logo;


}

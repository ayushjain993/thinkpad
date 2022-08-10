package io.uhha.marketing.domain;


import io.uhha.goods.domain.PmsSku;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品组合与单品的关联实体类
 * <p>
 * Created by mj on 2017/6/13.
 */
@Data
@ApiModel(description = "商品组合与单品的关联实体")
public class CombinationSku {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private long id;

    /**
     * 商品组合id
     */
    @ApiModelProperty(value = "商品组合id")
    private long combinationId;

    /**
     * 单品id
     */
    @ApiModelProperty(value = "单品id")
    private String skuId;

    /**
     * 单品信息
     */
    @ApiModelProperty(value = "单品信息")
    private PmsSku sku;

}

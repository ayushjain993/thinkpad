package io.uhha.goods.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.uhha.common.annotation.Excel;
import io.uhha.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Objects;

/**
 * 商品关注对象 pms_attention
 *
 * @author mj
 * @date 2020-07-24
 */
@Data
public class PmsAttention extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 所属店铺
     */
    @Excel(name = "所属店铺")
    private Long storeId;

    /**
     * 会员id
     */
    @Excel(name = "会员id")
    private Long customerId;

    /**
     * 商品id
     */
    @Excel(name = "商品id")
    private Long spuId;

    /**
     * 单品id
     */
    @Excel(name = "单品id")
    private String skuId;

    /**
     * 删除标记 0 未删除 1 删除 默认0
     */
    private int delFlag;

    /**
     * 删除时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel(name = "删除时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date delTime;

    /**
     * 单品信息
     */
    @ApiModelProperty(value = "单品信息")
    private PmsSku sku;

    /**
     * 关注单品的评价数量
     */
    @ApiModelProperty(value = "关注单品的评价数量")
    private int commentCount;

    /**
     * 好评数量
     */
    @ApiModelProperty(value = "好评数量")
    private int goodCommentCount;

    /**
     * 构造新增的商品关注实体
     *
     * @param customerId 会员id
     * @param skuId      单品id
     * @param spuId      商品id
     * @param storeId    商铺id
     * @return 返回商品关注实体
     */
    public static PmsAttention buildForAdd(long customerId, String skuId, long spuId, long storeId) {
        PmsAttention attention = new PmsAttention();
        attention.customerId = customerId;
        attention.skuId = skuId;
        attention.spuId = spuId;
        attention.storeId = storeId;
        return attention;
    }

    /**
     * 获得单品的好评率
     *
     * @return 返回单品的好评率
     */
    public int getSkuGoodRate() {
        if (this.commentCount == 0 || this.goodCommentCount == 0) {
            return 0;
        }

        return Integer.parseInt(new BigDecimal(goodCommentCount).divide(new BigDecimal(commentCount), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).setScale(0).toString());
    }

    /**
     * 获取商品关注实体类
     *
     * @param skuId      单品id
     * @param customerId 会员id
     * @return 商品关注实体类
     */
    public PmsAttention getAttentionForCancelAttention(String skuId, long customerId) {
        this.skuId = skuId;
        this.customerId = customerId;
        return this;
    }

    /**
     * 返回单品的名称
     *
     * @return 返回单品名称
     */
    @JsonIgnore
    public String getSkuName() {
        return Objects.nonNull(sku) ? sku.getName() : "";
    }

    /**
     * 返回商品id
     *
     * @return 返回商品id
     */
    @JsonIgnore
    public long spuId() {
        return Objects.nonNull(sku) ? sku.getSpuId() : 0;
    }

    /**
     * 返回默认图片
     *
     * @return 返回默认图片
     */
    @JsonIgnore
    public String getImages() {
        return Objects.nonNull(sku) ? sku.getUrl() : "";
    }

    /**
     * 返回单品价格
     *
     * @return 返回单品价格
     */
    @JsonIgnore
    public BigDecimal getPrice() {
        return Objects.nonNull(sku) ? sku.getPrice() : new BigDecimal(0);
    }
}

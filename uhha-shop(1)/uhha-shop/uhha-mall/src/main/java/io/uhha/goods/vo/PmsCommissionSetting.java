package io.uhha.goods.vo;

import io.uhha.common.annotation.Excel;
import io.uhha.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 设置对象 sms_commission_setting
 * 
 * @author peter
 * @date 2021-09-10
 */
public class PmsCommissionSetting extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** spuId */
    private Long spuId;

    /** 商品名称 */
    @Excel(name = "商品名称")
    private String name;

    /** 销售价(元) */
    @Excel(name = "销售价(元)")
    private BigDecimal price;

    /** 一级分类 */
    @Excel(name = "一级分类")
    private Long firstCateId;

    /** 二级分类 */
    @Excel(name = "二级分类")
    private Long secondCateId;

    /** 三级分类 */
    @Excel(name = "三级分类")
    private Long thirdCateId;

    /** 品牌 */
    @Excel(name = "品牌")
    private Long brandId;

    /** 一级佣金比例 */
    @Excel(name = "一级佣金比例")
    private String commissionRate;

    /** 二级佣金比例 */
    @Excel(name = "二级佣金比例")
    private String sCommissionRate;

    public void setSpuId(Long spuId) 
    {
        this.spuId = spuId;
    }

    public Long getSpuId() 
    {
        return spuId;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setPrice(BigDecimal price) 
    {
        this.price = price;
    }

    public BigDecimal getPrice() 
    {
        return price;
    }
    public void setFirstCateId(Long firstCateId) 
    {
        this.firstCateId = firstCateId;
    }

    public Long getFirstCateId() 
    {
        return firstCateId;
    }
    public void setSecondCateId(Long secondCateId) 
    {
        this.secondCateId = secondCateId;
    }

    public Long getSecondCateId() 
    {
        return secondCateId;
    }
    public void setThirdCateId(Long thirdCateId) 
    {
        this.thirdCateId = thirdCateId;
    }

    public Long getThirdCateId() 
    {
        return thirdCateId;
    }
    public void setBrandId(Long brandId) 
    {
        this.brandId = brandId;
    }

    public Long getBrandId() 
    {
        return brandId;
    }
    public void setCommissionRate(String commissionRate) 
    {
        this.commissionRate = commissionRate;
    }

    public String getCommissionRate() 
    {
        return commissionRate;
    }
    public void setsCommissionRate(String sCommissionRate) 
    {
        this.sCommissionRate = sCommissionRate;
    }

    public String getsCommissionRate() 
    {
        return sCommissionRate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("spuId", getSpuId())
            .append("name", getName())
            .append("price", getPrice())
            .append("firstCateId", getFirstCateId())
            .append("secondCateId", getSecondCateId())
            .append("thirdCateId", getThirdCateId())
            .append("brandId", getBrandId())
            .append("commissionRate", getCommissionRate())
            .append("sCommissionRate", getsCommissionRate())
            .toString();
    }
}

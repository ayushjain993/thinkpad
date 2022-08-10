package io.uhha.goods.domain;

import io.uhha.common.annotation.Excel;
import io.uhha.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 商品导入对象 pms_goods_import
 *
 * @author mj
 * @date 2020-07-24
 */
public class PmsGoodsImport extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 商品名称
     */
    @Excel(name = "商品名称")
    private String name;

    /**
     * 商品副标题
     */
    @Excel(name = "商品副标题")
    private String subTitle;

    /**
     * 商品价格
     */
    @Excel(name = "商品价格")
    private BigDecimal price;

    /**
     * see标题
     */
    @Excel(name = "see标题")
    private String seoTitle;

    /**
     * seo关键字
     */
    @Excel(name = "seo关键字")
    private String seoKeywords;

    /**
     * see描述
     */
    @Excel(name = "see描述")
    private String seoDesc;

    /**
     * 是否发布 0 未发布 1 发布 默认0
     */
    @Excel(name = "是否发布 0 未发布 1 发布 默认0 ")
    private String isRelease;

    /**
     * 删除标记 0 未删除 1 删除 默认0
     */
    private int delFlag;

    /**
     * 来源平台，如淘宝和义乌购
     */
    private String channel;

    /**
     * 天猫商城
     */
    private int tmall;

    /**
     * 对应平台的id
     */
    private String numIid;

    /**
     * 图片链接
     */
    private String picUrl;


    /**
     * 实际链接
     */
    private String orgUrl;


    /**
     * 原始商店id
     */
    private String shopId;

    /**
     * 一级分类id
     */
    private Long firstCateId;

    /**
     * 二级分类id
     */
    private Long secondCateId;

    /**
     * 三级分类id
     */
    private Long thirdCateId;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSeoTitle() {
        return seoTitle;
    }

    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }

    public String getSeoKeywords() {
        return seoKeywords;
    }

    public void setSeoKeywords(String seoKeywords) {
        this.seoKeywords = seoKeywords;
    }

    public String getSeoDesc() {
        return seoDesc;
    }

    public void setSeoDesc(String seoDesc) {
        this.seoDesc = seoDesc;
    }

    public String getIsRelease() {
        return isRelease;
    }

    public void setIsRelease(String isRelease) {
        this.isRelease = isRelease;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getNumIid() {
        return numIid;
    }

    public void setNumIid(String numIid) {
        this.numIid = numIid;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getOrgUrl() {
        return orgUrl;
    }

    public void setOrgUrl(String orgUrl) {
        this.orgUrl = orgUrl;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public Long getFirstCateId() {
        return firstCateId;
    }

    public void setFirstCateId(Long firstCateId) {
        this.firstCateId = firstCateId;
    }

    public Long getSecondCateId() {
        return secondCateId;
    }

    public void setSecondCateId(Long secondCateId) {
        this.secondCateId = secondCateId;
    }

    public Long getThirdCateId() {
        return thirdCateId;
    }

    public void setThirdCateId(Long thirdCateId) {
        this.thirdCateId = thirdCateId;
    }

    public int getTmall() {
        return tmall;
    }

    public void setTmall(int tmall) {
        this.tmall = tmall;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("subTitle", getSubTitle())
                .append("price", getPrice())
                .append("seoTitle", getSeoTitle())
                .append("seoKeywords", getSeoKeywords())
                .append("seoDesc", getSeoDesc())
                .append("isRelease", getIsRelease())
                .append("delFlag", getDelFlag())
                .append("numIid", getNumIid())
                .append("channel", getChannel())
                .toString();
    }
}

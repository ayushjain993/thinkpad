package io.uhha.system.domain;


import io.uhha.common.annotation.Excel;
import io.uhha.common.core.domain.BaseEntity;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 国家表对象 f_country
 *
 * @author uhha
 * @date 2021-10-11
 */
public class FCountry
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 英文名 */
    @Excel(name = "英文名")
    private String englishName;

    /** 中文名 */
    @Excel(name = "中文名")
    private String chineseName;

    /** 区号 */
    @Excel(name = "区号")
    private String areacode;

    private String twoCode;

    private String threeCode;

    private String isoCode;

    private String isEnable;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setEnglishName(String englishName)
    {
        this.englishName = englishName;
    }

    public String getEnglishName()
    {
        return englishName;
    }
    public void setChineseName(String chineseName)
    {
        this.chineseName = chineseName;
    }

    public String getChineseName()
    {
        return chineseName;
    }
    public void setAreacode(String areacode)
    {
        this.areacode = areacode;
    }

    public String getAreacode()
    {
        return areacode;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public String getTwoCode() {
        return twoCode;
    }

    public void setTwoCode(String twoCode) {
        this.twoCode = twoCode;
    }

    public String getThreeCode() {
        return threeCode;
    }

    public void setThreeCode(String threeCode) {
        this.threeCode = threeCode;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("englishName", getEnglishName())
                .append("chineseName", getChineseName())
                .append("areacode", getAreacode())
                .append("twocode", getTwoCode())
                .append("threeCode", getThreeCode())
                .append("isoCode", getIsoCode())
                .append("isEable", getIsEnable())
                .toString();
    }
}

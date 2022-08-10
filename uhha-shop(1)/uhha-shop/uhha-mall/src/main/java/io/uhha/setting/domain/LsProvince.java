package io.uhha.setting.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.uhha.common.annotation.Excel;
import io.uhha.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.List;

/**
 * 区域省对象 ls_province
 *
 * @author mj
 * @date 2020-07-29
 */
@Data
public class LsProvince extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 该市所属的国家id  对应f_country 中的id
     */
    @Excel(name = "该市所属的国家id  对应f_country 中的id")
    private Long countryId;

    /**
     * 省份的名称
     */
    @Excel(name = "省份的名称")
    private String name;

    /**
     * 排序
     */
    @Excel(name = "排序")
    private Long sort;

    /**
     * 删除标记 0 未删除 1删除
     */
    private int delFlag;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date modifyTime;

    /**
     * 删除时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel(name = "删除时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date delTime;
    /**
     * 省下面的市
     */
    @ApiModelProperty(value = "省下面的市")
    private List<LsCity> child;

    /**
     * 设置添加省份的默认
     *
     * @return 返回省份
     */
    public LsProvince setDefaultValuesForAdd() {
        this.delFlag = 0;
        return this;
    }

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

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getDelTime() {
        return delTime;
    }

    public void setDelTime(Date delTime) {
        this.delTime = delTime;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("countryId", getCountryId())
                .append("name", getName())
                .append("sort", getSort())
                .append("delFlag", getDelFlag())
                .append("createTime", getCreateTime())
                .append("modifyTime", getModifyTime())
                .append("delTime", getDelTime())
                .toString();
    }
}

package io.uhha.setting.domain;

import io.uhha.common.annotation.Excel;
import io.uhha.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 第三方SKU来源设置对象 ls_sku_source_setting
 *
 * @author mj
 * @date 2020-07-28
 */
public class LsSkuSourceSetting extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 支付方式 1:OneBound
     */
    @Excel(name = "来源 1:OneBound ")
    private String codetype;

    /**
     * 字段名称
     */
    @Excel(name = "字段名称")
    private String columnName;

    /**
     * 字段值
     */
    @Excel(name = "字段值")
    private String columnValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodetype() {
        return codetype;
    }

    public void setCodetype(String codetype) {
        this.codetype = codetype;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(String columnValue) {
        this.columnValue = columnValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("codetype", getCodetype())
                .append("columnName", getColumnName())
                .append("columnValue", getColumnValue())
                .toString();
    }
}

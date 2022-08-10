package io.uhha.setting.domain;

import io.uhha.common.annotation.Excel;
import io.uhha.setting.bean.PaySet;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 平台费用设置对象 ls_platform_fee_setting
 * 
 * @author peter
 * @date 2022-01-26
 */
public class LsPlatformFeeSetting implements Serializable
{

    /** 主键id	 */
    private Long id;

    /** 字段名称 */
    @Excel(name = "字段名称")
    private String columnName;

    /** 字段值 */
    @Excel(name = "字段值")
    private String columnValue;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setColumnName(String columnName) 
    {
        this.columnName = columnName;
    }

    public String getColumnName() 
    {
        return columnName;
    }
    public void setColumnValue(String columnValue) 
    {
        this.columnValue = columnValue;
    }

    public String getColumnValue() 
    {
        return columnValue;
    }

    public static LsPlatformFeeSetting getPlatformFeeSetting(LsPlatformFeeSetting setting, String columnValue, String columnName){
        setting.setColumnName(columnName);
        setting.setColumnValue(columnValue);
        return setting;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("columnName", getColumnName())
            .append("columnValue", getColumnValue())
            .toString();
    }
}

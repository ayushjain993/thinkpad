package io.uhha.setting.bean;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "第三方Sku来源配置实体")
public class SkuSourceSetting {
    /**
     * 主键id
     */
    private long id;
    /**
     * 来源 1:OneBound
     */
    private String codeType;
    /**
     * 字段名称
     */
    private String columnName;
    /**
     * 字段值
     */
    private String columnValue;

    /**
     * 组装数据-用于向数据库插入数据
     *
     * @param skuSourceSetting      数据库映射对象
     * @param codeType    第三方来源设置类型 1:OneBound
     * @param columnValue 字段的值
     * @param columnName  字段名称
     * @return paySet对象
     */
    public static SkuSourceSetting getSkuSourceSet(SkuSourceSetting skuSourceSetting, String codeType, String columnValue, String columnName) {
        skuSourceSetting.setCodeType(codeType);
        skuSourceSetting.setColumnName(columnName);
        skuSourceSetting.setColumnValue(columnValue);
        return skuSourceSetting;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
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
}

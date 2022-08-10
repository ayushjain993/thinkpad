package io.uhha.setting.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.uhha.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import io.uhha.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 银行配置信息对象 ls_bank_setting
 * 
 * @author uhha
 * @date 2022-03-12
 */
public class LsBankSetting
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 服务国家id */
    @Excel(name = "服务国家id")
    private Long countryId;

    /** 服务国家 */
    @Excel(name = "服务国家")
    private String country;

    /** 银行名称 */
    @Excel(name = "银行名称")
    private String bankName;

    /** swift代码 */
    @Excel(name = "swift代码")
    private String swiftCode;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 银行地址 */
    @Excel(name = "银行地址")
    private String bankAddress;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCountryId(Long countryId) 
    {
        this.countryId = countryId;
    }

    public Long getCountryId() 
    {
        return countryId;
    }
    public void setCountry(String country) 
    {
        this.country = country;
    }

    public String getCountry() 
    {
        return country;
    }
    public void setBankName(String bankName) 
    {
        this.bankName = bankName;
    }

    public String getBankName() 
    {
        return bankName;
    }
    public void setSwiftCode(String swiftCode) 
    {
        this.swiftCode = swiftCode;
    }

    public String getSwiftCode() 
    {
        return swiftCode;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setBankAddress(String bankAddress) 
    {
        this.bankAddress = bankAddress;
    }

    public String getBankAddress() 
    {
        return bankAddress;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("countryId", getCountryId())
            .append("country", getCountry())
            .append("bankName", getBankName())
            .append("swiftCode", getSwiftCode())
            .append("status", getStatus())
            .append("bankAddress", getBankAddress())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .toString();
    }
}

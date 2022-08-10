package io.uhha.settlement.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.uhha.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import io.uhha.common.core.domain.BaseEntity;

/**
 * 账户号码池对象 account_code
 * 
 * @author uhha
 * @date 2022-02-23
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountCode
{
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 占用状态 0 未被占用 1已占用 */
    @Excel(name = "占用状态 0 未被占用 1已占用")
    private Integer status;

    /** 1 USD 2 CNY */
    @Excel(name = "1 USD 2 CNY")
    private Integer currency;

    /** 账号 */
    @Excel(name = "账号")
    private String accountNo;

    /** 账号 */
    @Excel(name = "账号类型")
    private String accountType;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getStatus()
    {
        return status;
    }
    public void setCurrency(Integer currency)
    {
        this.currency = currency;
    }

    public Integer getCurrency()
    {
        return currency;
    }
    public void setAccountNo(String accountNo) 
    {
        this.accountNo = accountNo;
    }

    public String getAccountNo() 
    {
        return accountNo;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("status", getStatus())
            .append("currency", getCurrency())
            .append("accountNo", getAccountNo())
            .toString();
    }
}

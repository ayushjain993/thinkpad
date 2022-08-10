package io.uhha.settlement.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.uhha.common.annotation.Excel;
import io.uhha.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 结算账户对象 settlement_account
 *
 * @author uhha
 * @date 2021-12-31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettlementAccount {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 账户编码
     */
    @Excel(name = "账户编码")
    private String accountCode;

    /**
     * 账户类型
     */
    @Excel(name = "账户类型")
    private String accountType;

    @Excel(name = "账户子类型")
    private String childType;

    /**
     * 结算账户名称
     */
    @Excel(name = "结算账户名称")
    private String accountName;

    /**
     * 关联id
     */
    @Excel(name = "关联id")
    private Long associateId;

    /**
     * 结算时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel(name = "结算时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date settlementTime;

    /**
     * 结算前余额
     */
    @Excel(name = "结算前余额")
    private BigDecimal settlementBeforeBalance;

    /**
     * 结算后余额
     */
    @Excel(name = "结算后余额")
    private BigDecimal settlementAfterBalance;

    /**
     * 冻结余额
     */
    @Excel(name = "冻结余额")
    private BigDecimal frozenBalance;

    /**
     * 可用余额
     */
    @Excel(name = "可用余额")
    private BigDecimal availableBalance;

    /**
     * 结算类型
     */
    @Excel(name = "结算类型")
    private String settlementType;

    /**
     * 提现费率
     */
    @Excel(name = "提现费率")
    private BigDecimal withdrawalRate;


    /**
     * 删除标记  0未删除 1删除 默认0
     */
    private String delFlag;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

}

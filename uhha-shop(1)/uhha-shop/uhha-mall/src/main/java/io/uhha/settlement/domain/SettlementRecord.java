package io.uhha.settlement.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.uhha.common.annotation.Excel;
import io.uhha.order.domain.OmsBillingRecords;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 结算记录对象 settlement_record
 *
 * @author uhha
 * @date 2021-12-31
 */
@Data
@Accessors(chain = true)
public class SettlementRecord {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 账户唯一code
     */
    @Excel(name = "账户唯一code")
    private String accountCode;

    /**
     * 关联的账户号
     */
    @Excel(name = "关联的账户号")
    private String relatedAccountCode;

    /**
     * 进账
     */
    @Excel(name = "进账")
    private BigDecimal inBilling;

    /**
     * 出账
     */
    @Excel(name = "出账")
    private BigDecimal outBilling;

    /**
     * 结算状态
     */
    @Excel(name = "结算状态")
    private String settlementStaus;
    @Excel(name = "结算时间")
    private Date settlementTime;

    /**
     * 账单记录类型
     *
     * @see io.uhha.common.enums.BillingRecordTypeEnum
     */
    @Excel(name = "账单记录类型")
    private String recordType;

    /**
     * 删除标记  0未删除 1删除 默认0
     */
    private String delFlag;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    private String createBy;

    private String omsBillingRecords;
}

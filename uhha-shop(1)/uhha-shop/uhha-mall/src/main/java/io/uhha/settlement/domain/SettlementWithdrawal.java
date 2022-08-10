package io.uhha.settlement.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.uhha.common.annotation.Excel;
import lombok.Data;

/**
 * 提现对象 settlement_withdrawal
 *
 * @author uhha
 * @date 2022-01-04
 */
@Data
public class SettlementWithdrawal {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 提现单号
     */
    @Excel(name = "提现单号")
    private String withdrawalCode;

    /**
     * 门店名称
     */
    @Excel(name = "门店名称")
    private String storeName;

    /**
     * 提现金额
     */
    @Excel(name = "提现金额")
    private BigDecimal withdrawalAmount;

    /**
     * 商户实收
     */
    @Excel(name = "商户实收")
    private BigDecimal realStoreInAmount;

    /**
     * 店铺id
     */
    @Excel(name = "店铺id")
    private Long storeId;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private String status;

    /**
     * 付款时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "付款时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date payTime;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    private String failReason;

    private String createBy;
}

package io.uhha.settlement.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.uhha.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 提现对象 settlement_withdrawal
 *
 * @author uhha
 * @date 2022-01-04
 */
@Data
public class SettlementWithdrawalRequest {
    private static final long serialVersionUID = 1L;

    /**
     * 门店名称
     */
    private String storeName;

    /**
     * 提现金额
     */
    private BigDecimal withdrawalAmount;

    /**
     * 商户实收
     */
    private BigDecimal realStoreInAmount;

    /**
     * 店铺id
     */
    private Long storeId;

    private String childType;

    private String createBy;



}

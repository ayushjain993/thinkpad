package io.uhha.order.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettlementStatics {
    /**
     * 结算中金额，实际结算额绝对值累加
     */
    private BigDecimal settlingAmount = BigDecimal.ZERO;;

    /**
     * 结算中订单金额，只计算确认订单的订单额
     */
    private BigDecimal settlingPrice = BigDecimal.ZERO;

    /**
     * 结算中运费
     */
    private BigDecimal settlingFreight = BigDecimal.ZERO;

    /**
     * 结算中预计平台留存
     */
    private BigDecimal settlingPlatformRetained = BigDecimal.ZERO;

    /**
     * 已结算金额，实际结算额绝对值累加
     */
    private BigDecimal settledAmount = BigDecimal.ZERO;


    /**
     * 已结算订单金额，只计算确认订单的订单额
     */
    private BigDecimal settledPrice = BigDecimal.ZERO;

    /**
     * 已结算运费
     */
    private BigDecimal settledFreight = BigDecimal.ZERO;

    /**
     * 已结算平台留存
     */
    private BigDecimal settledPlatformRetained = BigDecimal.ZERO;

    /**
     * 已冻结额
     */
    private BigDecimal freezeAmount = BigDecimal.ZERO;
}

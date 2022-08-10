package io.uhha.settlement.service;

import io.uhha.order.domain.OmsBillingRecords;
import io.uhha.settlement.domain.SettlementAccount;

import java.math.BigDecimal;
import java.util.Map;

public interface ISettlementManager {

    /**
     * 自动结算任务，将资金导入指定的结算账户，并记录
     * @param storeId 店铺Id
     * @param dateRange 实际范围
     * @return 是否执行成功
     */
    boolean autoSettlementJob(Long storeId, String operator, Map<String, Object> dateRange);

    /**
     * 新增结算账户
     * @param settlementAccount 结算账户
     * @return 是否成功
     */
    boolean insertSettlementAccount(SettlementAccount settlementAccount);

    public boolean transfer(OmsBillingRecords omsBillingRecord, SettlementAccount from, SettlementAccount to, BigDecimal amount);
}

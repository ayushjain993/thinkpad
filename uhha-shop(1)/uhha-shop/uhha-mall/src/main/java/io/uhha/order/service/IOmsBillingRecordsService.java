package io.uhha.order.service;

import io.uhha.common.enums.SettlementStatusEnum;
import io.uhha.order.domain.OmsBillingRecords;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 账单记录Service接口
 *
 * @author mj
 * @date 2020-07-24
 */
public interface IOmsBillingRecordsService {
    /**
     * 查询账单记录
     *
     * @param id 账单记录ID
     * @return 账单记录
     */
    OmsBillingRecords selectOmsBillingRecordsById(Long id);

    /**
     * 查询账单记录列表
     *
     * @param omsBillingRecords 账单记录
     * @return 账单记录集合
     */
    List<OmsBillingRecords> selectOmsBillingRecordsList(OmsBillingRecords omsBillingRecords);

    /**
     * 新增账单记录
     *
     * @param omsBillingRecords 账单记录
     * @return 结果
     */
    int insertOmsBillingRecords(OmsBillingRecords omsBillingRecords);

    /**
     * 修改账单记录
     *
     * @param omsBillingRecords 账单记录
     * @return 结果
     */
    int updateOmsBillingRecords(OmsBillingRecords omsBillingRecords);

    /**
     * 批量删除账单记录
     *
     * @param ids 需要删除的账单记录ID
     * @return 结果
     */
    int deleteOmsBillingRecordsByIds(Long[] ids);

    /**
     * 删除账单记录信息
     *
     * @param id 账单记录ID
     * @return 结果
     */
    int deleteOmsBillingRecordsById(Long id);

    /**
     * 查询账单
     *
     * @return
     */
    List<OmsBillingRecords> queryByQueryDateAndSettlementStatus(SettlementStatusEnum settlementStatus, Long storeId, Map<String, Object> dateRange);

    /**
     * 更新状态
     *
     * @param records
     */
    void updateBatchOmsBillingRecords(List<OmsBillingRecords> records);
}

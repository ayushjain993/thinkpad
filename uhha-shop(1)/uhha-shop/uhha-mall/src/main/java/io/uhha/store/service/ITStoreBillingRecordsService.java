package io.uhha.store.service;

import io.uhha.store.domain.TStoreBillingRecords;

import java.util.List;

/**
 * 门店账单收入支出Service接口
 *
 * @author mj
 * @date 2020-07-28
 */
public interface ITStoreBillingRecordsService {
    /**
     * 查询门店账单收入支出
     *
     * @param id 门店账单收入支出ID
     * @return 门店账单收入支出
     */
    public TStoreBillingRecords selectTStoreBillingRecordsById(Long id);

    /**
     * 查询门店账单收入支出列表
     *
     * @param tStoreBillingRecords 门店账单收入支出
     * @return 门店账单收入支出集合
     */
    public List<TStoreBillingRecords> selectTStoreBillingRecordsList(TStoreBillingRecords tStoreBillingRecords);

    /**
     * 新增门店账单收入支出
     *
     * @param tStoreBillingRecords 门店账单收入支出
     * @return 结果
     */
    public int insertTStoreBillingRecords(TStoreBillingRecords tStoreBillingRecords);

    /**
     * 修改门店账单收入支出
     *
     * @param tStoreBillingRecords 门店账单收入支出
     * @return 结果
     */
    public int updateTStoreBillingRecords(TStoreBillingRecords tStoreBillingRecords);

    /**
     * 批量删除门店账单收入支出
     *
     * @param ids 需要删除的门店账单收入支出ID
     * @return 结果
     */
    public int deleteTStoreBillingRecordsByIds(Long[] ids);

    /**
     * 删除门店账单收入支出信息
     *
     * @param id 门店账单收入支出ID
     * @return 结果
     */
    public int deleteTStoreBillingRecordsById(Long id);
}

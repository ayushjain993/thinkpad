package io.uhha.order.service;

import io.uhha.order.domain.OmsCommissionRecords;
import io.uhha.util.PageHelper;

import java.util.List;
import java.util.Map;

/**
 * 佣金记录Service接口
 *
 * @author mj
 * @date 2020-07-24
 */
public interface IOmsCommissionRecordsService {
    /**
     * 查询佣金记录
     *
     * @param id 佣金记录ID
     * @return 佣金记录
     */
    public OmsCommissionRecords selectOmsCommissionRecordsById(Long id);

    public PageHelper<OmsCommissionRecords> queryCommissionRecords(PageHelper<OmsCommissionRecords> pageHelper, OmsCommissionRecords.QueryCriteria queryCriteria);


    public String queryCommissionMoney(Map<String, Object> params);
    /**
     * 查询佣金记录列表
     *
     * @param omsCommissionRecords 佣金记录
     * @return 佣金记录集合
     */
    public List<OmsCommissionRecords> selectOmsCommissionRecordsList(OmsCommissionRecords omsCommissionRecords);

    /**
     * 新增佣金记录
     *
     * @param omsCommissionRecords 佣金记录
     * @return 结果
     */
    public int insertOmsCommissionRecords(OmsCommissionRecords omsCommissionRecords);

    /**
     * 修改佣金记录
     *
     * @param omsCommissionRecords 佣金记录
     * @return 结果
     */
    public int updateOmsCommissionRecords(OmsCommissionRecords omsCommissionRecords);

    /**
     * 批量删除佣金记录
     *
     * @param ids 需要删除的佣金记录ID
     * @return 结果
     */
    public int deleteOmsCommissionRecordsByIds(Long[] ids);

    /**
     * 删除佣金记录信息
     *
     * @param id 佣金记录ID
     * @return 结果
     */
    public int deleteOmsCommissionRecordsById(Long id);

    /**
     * 自动将当天的到期的冻结手续费转为可用手续费
     */
    public void autoUpdateCommissionFrozenToBalance();
}

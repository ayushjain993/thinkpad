package io.uhha.order.service.impl;

import io.uhha.common.utils.DateUtils;
import io.uhha.member.service.IUmsMemberService;
import io.uhha.order.domain.OmsCommissionRecords;
import io.uhha.order.domain.OmsOrder;
import io.uhha.order.domain.OmsOrderSetting;
import io.uhha.order.mapper.OmsCommissionRecordsMapper;
import io.uhha.order.service.IOmsCommissionRecordsService;
import io.uhha.order.service.IOmsOrderService;
import io.uhha.order.service.IOmsOrderSettingService;
import io.uhha.util.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 佣金记录Service业务层处理
 *
 * @author mj
 * @date 2020-07-24
 */
@Service("commissionRecord")
@Slf4j
public class OmsCommissionRecordsServiceImpl implements IOmsCommissionRecordsService {
    @Autowired
    private OmsCommissionRecordsMapper omsCommissionRecordsMapper;

    @Autowired
    private IUmsMemberService umsMemberService;


    /**
     * 注入订单设置服务
     */
    @Autowired
    private IOmsOrderSettingService orderSettingService;

    @Autowired
    private IOmsOrderService omsOrderService;

    /**
     * 查询佣金记录
     *
     * @param id 佣金记录ID
     * @return 佣金记录
     */
    @Override
    public OmsCommissionRecords selectOmsCommissionRecordsById(Long id) {
        return omsCommissionRecordsMapper.selectOmsCommissionRecordsById(id);
    }

    @Override
    public PageHelper<OmsCommissionRecords> queryCommissionRecords(PageHelper<OmsCommissionRecords> pageHelper, OmsCommissionRecords.QueryCriteria queryCriteria) {
        return pageHelper.setListDates(omsCommissionRecordsMapper.queryCommissionRecords(pageHelper.getQueryParams(queryCriteria.getQueryMap(), omsCommissionRecordsMapper.queryCommissionRecordsCount(queryCriteria.getQueryMap()))));
    }
    @Override
    public String queryCommissionMoney(Map<String, Object> params){
        return omsCommissionRecordsMapper.queryCommissionMoney(params);
    }
    /**
     * 查询佣金记录列表
     *
     * @param omsCommissionRecords 佣金记录
     * @return 佣金记录
     */
    @Override
    public List<OmsCommissionRecords> selectOmsCommissionRecordsList(OmsCommissionRecords omsCommissionRecords) {
        return omsCommissionRecordsMapper.selectOmsCommissionRecordsList(omsCommissionRecords);
    }

    /**
     * 新增佣金记录
     *
     * @param omsCommissionRecords 佣金记录
     * @return 结果
     */
    @Override
    public int insertOmsCommissionRecords(OmsCommissionRecords omsCommissionRecords) {
        omsCommissionRecords.setCreateTime(DateUtils.getNowDate());
        return omsCommissionRecordsMapper.insertOmsCommissionRecords(omsCommissionRecords);
    }

    /**
     * 修改佣金记录
     *
     * @param omsCommissionRecords 佣金记录
     * @return 结果
     */
    @Override
    public int updateOmsCommissionRecords(OmsCommissionRecords omsCommissionRecords) {
        return omsCommissionRecordsMapper.updateOmsCommissionRecords(omsCommissionRecords);
    }

    /**
     * 批量删除佣金记录
     *
     * @param ids 需要删除的佣金记录ID
     * @return 结果
     */
    @Override
    public int deleteOmsCommissionRecordsByIds(Long[] ids) {
        return omsCommissionRecordsMapper.deleteOmsCommissionRecordsByIds(ids);
    }

    /**
     * 删除佣金记录信息
     *
     * @param id 佣金记录ID
     * @return 结果
     */
    @Override
    public int deleteOmsCommissionRecordsById(Long id) {
        return omsCommissionRecordsMapper.deleteOmsCommissionRecordsById(id);
    }


    /**
     * 自动将当天的到期的冻结手续费转为可用手续费
     */
    @Override
    @Transactional
    public void autoUpdateCommissionFrozenToBalance() {
        //查询当天的到期的手续费
        log.debug("autoUpdateCommissionFrozenToBalance......");
        OmsOrderSetting orderSetting = orderSettingService.selectOmsOrderSettingById(1L);
        if (ObjectUtils.isEmpty(orderSetting)) {
            log.error("autoUpdateCommissionFrozenToBalance fail : no orderSetting");
            return;
        }
        List<OmsCommissionRecords> commissionRecords = omsCommissionRecordsMapper.queryOmsCommissionRecordsForSettle(orderSetting.getAutoConfirm());

        commissionRecords.forEach(x->{
            umsMemberService.updateCustomerFrozenCommission2Commission(x.getCustomerId(),x.getMoney());
        });
    }
}

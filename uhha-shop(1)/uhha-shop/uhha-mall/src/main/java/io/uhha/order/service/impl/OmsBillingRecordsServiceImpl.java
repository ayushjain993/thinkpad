package io.uhha.order.service.impl;

import io.uhha.common.enums.BillingRecordTypeEnum;
import io.uhha.common.enums.SettlementStatusEnum;
import io.uhha.common.utils.DateUtils;
import io.uhha.order.domain.OmsBillingRecords;
import io.uhha.order.domain.OmsOrder;
import io.uhha.order.mapper.OmsBillingRecordsMapper;
import io.uhha.order.mapper.OmsOrderMapper;
import io.uhha.order.service.IOmsBillingRecordsService;
import io.uhha.order.service.IOmsOrderService;
import io.uhha.order.service.IOmsOrderSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 账单记录Service业务层处理
 *
 * @author mj
 * @date 2020-07-24
 */
@Service
public class OmsBillingRecordsServiceImpl implements IOmsBillingRecordsService {
    @Autowired
    private OmsBillingRecordsMapper omsBillingRecordsMapper;
    @Autowired
    private IOmsOrderService omsOrderService;
    @Autowired
    private OmsOrderMapper omsOrderMapper;
    @Autowired
    private IOmsOrderSkuService orderSkuService;


    /**
     * 查询账单记录
     *
     * @param id 账单记录ID
     * @return 账单记录
     */
    @Override
    public OmsBillingRecords selectOmsBillingRecordsById(Long id) {
        return omsBillingRecordsMapper.selectOmsBillingRecordsById(id);
    }

    /**
     * 查询账单记录列表
     *
     * @param omsBillingRecords 账单记录
     * @return 账单记录
     */
    @Override
    public List<OmsBillingRecords> selectOmsBillingRecordsList(OmsBillingRecords omsBillingRecords) {
        return omsBillingRecordsMapper.selectOmsBillingRecordsList(omsBillingRecords);
    }

    /**
     * 新增账单记录
     *
     * @param omsBillingRecords 账单记录
     * @return 结果
     */
    @Override
    public int insertOmsBillingRecords(OmsBillingRecords omsBillingRecords) {
        omsBillingRecords.setCreateTime(DateUtils.getNowDate());
        return omsBillingRecordsMapper.insertOmsBillingRecords(omsBillingRecords);
    }

    /**
     * 修改账单记录
     *
     * @param omsBillingRecords 账单记录
     * @return 结果
     */
    @Override
    public int updateOmsBillingRecords(OmsBillingRecords omsBillingRecords) {
        return omsBillingRecordsMapper.updateOmsBillingRecords(omsBillingRecords);
    }

    /**
     * 批量删除账单记录
     *
     * @param ids 需要删除的账单记录ID
     * @return 结果
     */
    @Override
    public int deleteOmsBillingRecordsByIds(Long[] ids) {
        return omsBillingRecordsMapper.deleteOmsBillingRecordsByIds(ids);
    }

    /**
     * 删除账单记录信息
     *
     * @param id 账单记录ID
     * @return 结果
     */
    @Override
    public int deleteOmsBillingRecordsById(Long id) {
        return omsBillingRecordsMapper.deleteOmsBillingRecordsById(id);
    }


    @Override
    public List<OmsBillingRecords> queryByQueryDateAndSettlementStatus(SettlementStatusEnum settlementStatus, Long storeId, Map<String, Object> dateRange) {
        OmsBillingRecords omsBillingRecords = new OmsBillingRecords();
        omsBillingRecords.setStoreId(storeId);
        omsBillingRecords.setStatus(settlementStatus.getCode());
        omsBillingRecords.setParams(dateRange);
        List<OmsBillingRecords> billingRecords = omsBillingRecordsMapper.selectOmsBillingRecordsList(omsBillingRecords);
        billingRecords.forEach(omsBillingRecord -> {
            OmsOrder omsOrder;
            if(BillingRecordTypeEnum.BACK_ORDER.getCode().equalsIgnoreCase(omsBillingRecord.getRecordType()) ||
                    BillingRecordTypeEnum.REFUND_ORDER.getCode().equalsIgnoreCase(omsBillingRecord.getRecordType())){
                omsOrder = omsOrderMapper.queryOrderByBackCode(omsBillingRecord.getOrderCode());
            }else{
                omsOrder = omsOrderMapper.queryCommunityOrdersByOrderCode(omsBillingRecord.getOrderCode());
            }
            omsOrder.setOrderSkus(orderSkuService.queryByOrderId(omsOrder.getId()));
            omsBillingRecord.setOmsOrder(omsOrder);
        });
        return billingRecords;
    }

    @Override
    public void updateBatchOmsBillingRecords(List<OmsBillingRecords> records) {
        records.forEach(this::updateOmsBillingRecords);
    }
}

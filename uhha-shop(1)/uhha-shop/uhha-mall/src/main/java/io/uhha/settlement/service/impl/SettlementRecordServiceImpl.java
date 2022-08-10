package io.uhha.settlement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.uhha.common.utils.DateUtils;
import io.uhha.settlement.domain.SettlementRecord;
import io.uhha.settlement.mapper.SettlementRecordMapper;
import io.uhha.settlement.service.ISettlementRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 结算记录Service业务层处理
 *
 * @author uhha
 * @date 2021-12-31
 */
@Service
public class SettlementRecordServiceImpl extends ServiceImpl<SettlementRecordMapper, SettlementRecord> implements ISettlementRecordService
{
    @Autowired
    private SettlementRecordMapper settlementRecordMapper;

    /**
     * 查询结算记录
     *
     * @param id 结算记录主键
     * @return 结算记录
     */
    @Override
    public SettlementRecord selectSettlementRecordById(Long id)
    {
        return settlementRecordMapper.selectSettlementRecordById(id);
    }

    /**
     * 查询结算记录列表
     *
     * @param settlementRecord 结算记录
     * @return 结算记录
     */
    @Override
    public List<SettlementRecord> selectSettlementRecordList(SettlementRecord settlementRecord)
    {
        return settlementRecordMapper.selectSettlementRecordList(settlementRecord);
    }

    /**
     * 新增结算记录
     *
     * @param settlementRecord 结算记录
     * @return 结果
     */
    @Override
    public int insertSettlementRecord(SettlementRecord settlementRecord)
    {
        settlementRecord.setCreateTime(DateUtils.getNowDate());
        return settlementRecordMapper.insertSettlementRecord(settlementRecord);
    }

    /**
     * 修改结算记录
     *
     * @param settlementRecord 结算记录
     * @return 结果
     */
    @Override
    public int updateSettlementRecord(SettlementRecord settlementRecord)
    {
        return settlementRecordMapper.updateSettlementRecord(settlementRecord);
    }

    /**
     * 批量删除结算记录
     *
     * @param ids 需要删除的结算记录主键
     * @return 结果
     */
    @Override
    public int deleteSettlementRecordByIds(Long[] ids)
    {
        return settlementRecordMapper.deleteSettlementRecordByIds(ids);
    }

    /**
     * 删除结算记录信息
     *
     * @param id 结算记录主键
     * @return 结果
     */
    @Override
    public int deleteSettlementRecordById(Long id)
    {
        return settlementRecordMapper.deleteSettlementRecordById(id);
    }
}

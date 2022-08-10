package io.uhha.settlement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.uhha.settlement.domain.SettlementRecord;

import java.util.List;

/**
 * 结算记录Mapper接口
 *
 * @author uhha
 * @date 2021-12-31
 */
public interface SettlementRecordMapper extends BaseMapper<SettlementRecord>
{
    /**
     * 查询结算记录
     *
     * @param id 结算记录主键
     * @return 结算记录
     */
    public SettlementRecord selectSettlementRecordById(Long id);

    /**
     * 查询结算记录列表
     *
     * @param settlementRecord 结算记录
     * @return 结算记录集合
     */
    public List<SettlementRecord> selectSettlementRecordList(SettlementRecord settlementRecord);

    /**
     * 新增结算记录
     *
     * @param settlementRecord 结算记录
     * @return 结果
     */
    public int insertSettlementRecord(SettlementRecord settlementRecord);

    /**
     * 修改结算记录
     *
     * @param settlementRecord 结算记录
     * @return 结果
     */
    public int updateSettlementRecord(SettlementRecord settlementRecord);

    /**
     * 删除结算记录
     *
     * @param id 结算记录主键
     * @return 结果
     */
    public int deleteSettlementRecordById(Long id);

    /**
     * 批量删除结算记录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSettlementRecordByIds(Long[] ids);
}

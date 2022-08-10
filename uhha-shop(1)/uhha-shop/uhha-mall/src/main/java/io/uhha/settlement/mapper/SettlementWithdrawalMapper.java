package io.uhha.settlement.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.uhha.settlement.domain.SettlementWithdrawal;

/**
 * 提现Mapper接口
 *
 * @author uhha
 * @date 2022-01-04
 */
public interface SettlementWithdrawalMapper extends BaseMapper<SettlementWithdrawal>
{
    /**
     * 查询提现
     *
     * @param id 提现主键
     * @return 提现
     */
    public SettlementWithdrawal selectSettlementWithdrawalById(Long id);

    /**
     * 查询提现列表
     *
     * @param settlementWithdrawal 提现
     * @return 提现集合
     */
    public List<SettlementWithdrawal> selectSettlementWithdrawalList(SettlementWithdrawal settlementWithdrawal);

    /**
     * 新增提现
     *
     * @param settlementWithdrawal 提现
     * @return 结果
     */
    public int insertSettlementWithdrawal(SettlementWithdrawal settlementWithdrawal);

    /**
     * 修改提现
     *
     * @param settlementWithdrawal 提现
     * @return 结果
     */
    public int updateSettlementWithdrawal(SettlementWithdrawal settlementWithdrawal);

    /**
     * 删除提现
     *
     * @param id 提现主键
     * @return 结果
     */
    public int deleteSettlementWithdrawalById(Long id);

    /**
     * 批量删除提现
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSettlementWithdrawalByIds(Long[] ids);
}

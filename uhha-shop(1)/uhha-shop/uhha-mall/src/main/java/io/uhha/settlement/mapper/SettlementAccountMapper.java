package io.uhha.settlement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.uhha.settlement.domain.SettlementAccount;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 结算账户Mapper接口
 *
 * @author uhha
 * @date 2021-12-31
 */
@Mapper
public interface SettlementAccountMapper extends BaseMapper<SettlementAccount>
{
    /**
     * 查询结算账户
     *
     * @param id 结算账户主键
     * @return 结算账户
     */
    public SettlementAccount selectSettlementAccountById(Long id);

    /**
     * 查询结算账户列表
     *
     * @param settlementAccount 结算账户
     * @return 结算账户集合
     */
    public List<SettlementAccount> selectSettlementAccountList(SettlementAccount settlementAccount);

    /**
     * 新增结算账户
     *
     * @param settlementAccount 结算账户
     * @return 结果
     */
    public int insertSettlementAccount(SettlementAccount settlementAccount);

    /**
     * 修改结算账户
     *
     * @param settlementAccount 结算账户
     * @return 结果
     */
    public int updateSettlementAccount(SettlementAccount settlementAccount);

    /**
     * 删除结算账户
     *
     * @param id 结算账户主键
     * @return 结果
     */
    public int deleteSettlementAccountById(Long id);

    /**
     * 批量删除结算账户
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSettlementAccountByIds(Long[] ids);
}

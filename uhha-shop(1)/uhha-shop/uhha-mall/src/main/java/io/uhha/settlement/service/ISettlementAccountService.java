package io.uhha.settlement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.uhha.common.enums.AccountTypeEnum;
import io.uhha.settlement.domain.SettlementAccount;
import io.uhha.settlement.model.AccountUser;

import java.util.List;

/**
 * 结算账户Service接口
 *
 * @author uhha
 * @date 2021-12-31
 */
public interface ISettlementAccountService extends IService<SettlementAccount> {
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
     * 批量删除结算账户
     *
     * @param ids 需要删除的结算账户主键集合
     * @return 结果
     */
    public int deleteSettlementAccountByIds(Long[] ids);

    /**
     * 删除结算账户信息
     *
     * @param id 结算账户主键
     * @return 结果
     */
    public int deleteSettlementAccountById(Long id);

    /**
     * 查询结算账户
     *
     * @param accountType
     * @param associateId
     * @return
     */
    SettlementAccount querySettlementAccountByStoreId(String accountType, String childType, Long associateId);

    /**
     * 查询结算账户
     *
     * @param accountCode
     * @return
     */
    SettlementAccount querySettlementAccountByAccountCode(String accountCode);

    /**
     * 查询用户列表
     *
     * @param accountType
     * @return
     */
    List<AccountUser> selectAccountUserByAccountType(AccountTypeEnum accountType);
}

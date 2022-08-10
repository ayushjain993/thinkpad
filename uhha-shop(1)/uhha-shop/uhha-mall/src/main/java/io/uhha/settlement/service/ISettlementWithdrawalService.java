package io.uhha.settlement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.uhha.settlement.domain.SettlementWithdrawal;
import io.uhha.settlement.model.SettlementWithdrawalRequest;
import io.uhha.settlement.model.WithdrawalPayRequest;
import io.uhha.settlement.model.WithdrawalRequest;

import java.util.List;

/**
 * 提现Service接口
 *
 * @author uhha
 * @date 2022-01-04
 */
public interface ISettlementWithdrawalService extends IService<SettlementWithdrawal> {
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
    public int insertSettlementWithdrawal(SettlementWithdrawalRequest settlementWithdrawal);

    /**
     * 修改提现
     *
     * @param settlementWithdrawal 提现
     * @return 结果
     */
    public int updateSettlementWithdrawal(SettlementWithdrawal settlementWithdrawal);


    /**
     * 审核提现
     *
     * @param settlementWithdrawal 提现
     * @return 结果
     */
    public int applyStatusUpdate(SettlementWithdrawal settlementWithdrawal);

    /**
     * 批量删除提现
     *
     * @param ids 需要删除的提现主键集合
     * @return 结果
     */
    public int deleteSettlementWithdrawalByIds(Long[] ids);

    /**
     * 删除提现信息
     *
     * @param id 提现主键
     * @return 结果
     */
    public int deleteSettlementWithdrawalById(Long id);

    /**
     * 检查权限
     *
     * @param storeId
     * @param ids
     */
    void checkPremssion(long storeId, Long... ids);

    /**
     * 审核体现记录
     *
     * @param withdrawalRequest
     * @return
     */
    Boolean auditWithdrawal(WithdrawalRequest withdrawalRequest);

    /**
     * 付款
     *
     * @param withdrawalPayRequest
     * @return
     */
    Boolean withdrawalPay(WithdrawalPayRequest withdrawalPayRequest);
}

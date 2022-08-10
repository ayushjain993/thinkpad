package io.uhha.settlement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.lang.Assert;
import io.uhha.common.enums.AccountTypeEnum;
import io.uhha.common.enums.WithdrawalStatusEnum;
import io.uhha.common.exception.CustomException;
import io.uhha.common.utils.DateUtils;
import io.uhha.common.utils.StringUtils;
import io.uhha.common.utils.uuid.IdUtils;
import io.uhha.setting.bean.PlatformFeeSet;
import io.uhha.setting.service.ILsPlatformFeeSettingService;
import io.uhha.settlement.domain.SettlementAccount;
import io.uhha.settlement.domain.SettlementWithdrawal;
import io.uhha.settlement.mapper.SettlementWithdrawalMapper;
import io.uhha.settlement.model.SettlementWithdrawalRequest;
import io.uhha.settlement.model.WithdrawalPayRequest;
import io.uhha.settlement.model.WithdrawalRequest;
import io.uhha.settlement.service.ISettlementAccountService;
import io.uhha.settlement.service.ISettlementWithdrawalService;
import io.uhha.store.domain.TStoreInfo;
import io.uhha.store.service.ITStoreInfoService;
import io.uhha.util.CommonConstant;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 提现Service业务层处理
 *
 * @author uhha
 * @date 2022-01-04
 */
@Service
public class SettlementWithdrawalServiceImpl extends ServiceImpl<SettlementWithdrawalMapper, SettlementWithdrawal> implements ISettlementWithdrawalService {

    @Autowired
    private ISettlementAccountService settlementAccountService;

    @Autowired
    private ITStoreInfoService storeInfoService;

    @Autowired
    private ILsPlatformFeeSettingService lsPlatformFeeSettingService;


    /**
     * 查询提现
     *
     * @param id 提现主键
     * @return 提现
     */
    @Override
    public SettlementWithdrawal selectSettlementWithdrawalById(Long id) {
        return this.baseMapper.selectSettlementWithdrawalById(id);
    }

    /**
     * 查询提现列表
     *
     * @param settlementWithdrawal 提现
     * @return 提现
     */
    @Override
    public List<SettlementWithdrawal> selectSettlementWithdrawalList(SettlementWithdrawal settlementWithdrawal) {
        return this.baseMapper.selectSettlementWithdrawalList(settlementWithdrawal);
    }

    /**
     * 新增提现
     *
     * @param settlementWithdrawalRequest 提现
     * @return 结果
     */
    @Override
    public int insertSettlementWithdrawal(SettlementWithdrawalRequest settlementWithdrawalRequest) {
        Assert.notNull(settlementWithdrawalRequest, "提现信息不能为空");
        Assert.notNull(settlementWithdrawalRequest.getStoreId(), "店铺id不能为空");
        Assert.notNull(settlementWithdrawalRequest.getWithdrawalAmount(), "提现金额不能为空");
        Long storeId = settlementWithdrawalRequest.getStoreId();
        SettlementWithdrawal settlementWithdrawal = new SettlementWithdrawal();
        BeanUtils.copyProperties(settlementWithdrawalRequest, settlementWithdrawal);
        AccountTypeEnum accountType;
        TStoreInfo tStoreInfo = null;
        String childType = null;
        if (CommonConstant.ADMIN_STOREID.equals(storeId)) {
            accountType = AccountTypeEnum.SYSTEM;
            if (StringUtils.isBlank(settlementWithdrawalRequest.getChildType())) {
                childType = CommonConstant.SYSTEM_TRADING;
            }else{
                childType = settlementWithdrawalRequest.getChildType();
            }
        } else {
            accountType = AccountTypeEnum.STORE;
            tStoreInfo = storeInfoService.checkStore(storeId);
        }

        SettlementAccount settlementAccount = settlementAccountService.querySettlementAccountByStoreId(accountType.name(), childType, storeId);
        if (settlementAccount == null) {
            throw new CustomException("结算账户不存在");
        }
        BigDecimal withdrawRate = Optional.ofNullable(settlementAccount).map(SettlementAccount::getWithdrawalRate).orElse(BigDecimal.ZERO);
        if (withdrawRate.compareTo(BigDecimal.ZERO) <= 0) {
            PlatformFeeSet platformFeeSet = lsPlatformFeeSettingService.queryPlatformFeeSet();
            withdrawRate = Optional.ofNullable(platformFeeSet).map(PlatformFeeSet::getDefaultWithdrawCommissionRate).orElse(BigDecimal.ZERO);
        }
        //手续费
        BigDecimal realStoreInAmount = settlementWithdrawal.getWithdrawalAmount().multiply(BigDecimal.ONE.subtract(withdrawRate));
        if (realStoreInAmount.compareTo(settlementAccount.getAvailableBalance()) > 0) {
            throw new CustomException("用户可提现金额(加费率)不能大于账户可用金额");
        }
        settlementWithdrawal.setWithdrawalCode(IdUtils.fastSimpleUUID());
        settlementWithdrawal.setCreateTime(DateUtils.getNowDate());
        settlementWithdrawal.setStoreName(Optional.ofNullable(tStoreInfo).map(TStoreInfo::getStoreName).orElse("平台"));
        settlementWithdrawal.setRealStoreInAmount(realStoreInAmount);
        settlementWithdrawal.setStatus(WithdrawalStatusEnum.UNDER_AUDIT.name());
        return this.baseMapper.insertSettlementWithdrawal(settlementWithdrawal);
    }

    /**
     * 修改提现
     *
     * @param settlementWithdrawal 提现
     * @return 结果
     */
    @Override
    public int updateSettlementWithdrawal(SettlementWithdrawal settlementWithdrawal) {
        Assert.notNull(settlementWithdrawal, "提现信息不能为空");
        Assert.notNull(settlementWithdrawal.getStoreId(), "店铺id不能为空");
        Assert.notNull(settlementWithdrawal.getWithdrawalAmount(), "提现金额不能为空");
        Long storeId = settlementWithdrawal.getStoreId();
        TStoreInfo tStoreInfo = storeInfoService.checkStore(storeId);
        checkPremssion(storeId, settlementWithdrawal.getId());
        AccountTypeEnum accountType;
        String childType = null;
        if (CommonConstant.ADMIN_STOREID.equals(storeId)) {
            accountType = AccountTypeEnum.SYSTEM;
            childType = "SYSTEM_TRADING";
        } else {
            accountType = AccountTypeEnum.STORE;
            checkStatus(settlementWithdrawal.getId());
        }
        SettlementAccount settlementAccount = settlementAccountService.querySettlementAccountByStoreId(accountType.name(), childType, storeId);

        if (settlementAccount == null) {
            throw new CustomException("结算账户不存在");
        }
        //手续费
        settlementWithdrawal.setRealStoreInAmount(checkWithfrawalAmount(settlementWithdrawal, settlementAccount));
        settlementWithdrawal.setUpdateTime(DateUtils.getNowDate());
        settlementWithdrawal.setStoreName(tStoreInfo.getStoreName());
        return this.baseMapper.updateSettlementWithdrawal(settlementWithdrawal);
    }

    /**
     * 修改提现
     *
     * @param settlementWithdrawal 提现
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int applyStatusUpdate(SettlementWithdrawal settlementWithdrawal) {
        Assert.notNull(settlementWithdrawal, "提现信息不能为空");
        Assert.notNull(settlementWithdrawal.getStoreId(), "店铺id不能为空");
        Assert.notNull(settlementWithdrawal.getStatus(), "status不能为空");
        Long storeId = settlementWithdrawal.getStoreId();

        AccountTypeEnum accountType;
        String childType = null;
        if (CommonConstant.ADMIN_STOREID.equals(storeId)) {
            accountType = AccountTypeEnum.SYSTEM;
            childType = CommonConstant.SYSTEM_TRADING;
        } else {
            accountType = AccountTypeEnum.STORE;
        }
        SettlementAccount settlementAccount = settlementAccountService.querySettlementAccountByStoreId(accountType.name(), childType, storeId);
        if (settlementAccount == null) {
            throw new CustomException("结算账户不存在");
        }
        WithdrawalStatusEnum withdrawalStatus = WithdrawalStatusEnum.valueOf(settlementWithdrawal.getStatus());
        switch (withdrawalStatus) {
            case PASS:
                //手续费
                checkWithfrawalAmount(settlementWithdrawal, settlementAccount);
                settlementAccount.setFrozenBalance(settlementWithdrawal.getWithdrawalAmount());
                BigDecimal availableBalance = settlementAccount.getAvailableBalance().subtract(settlementWithdrawal.getWithdrawalAmount());
                settlementAccount.setAvailableBalance(availableBalance);
                break;
            case REJECTED:
                //检测拒绝状态
                checkRejectedAction(settlementWithdrawal);
                break;
            case WITHDRAWAL_FAIL:
                //检测拒绝状态
                checkRejectedAction(settlementWithdrawal);
                BigDecimal frozenBalance = settlementAccount.getFrozenBalance().subtract(settlementWithdrawal.getWithdrawalAmount());
                settlementAccount.setFrozenBalance(frozenBalance);
                availableBalance = settlementAccount.getAvailableBalance().add(settlementWithdrawal.getWithdrawalAmount());
                settlementAccount.setAvailableBalance(availableBalance);
                break;
            case WITHDRAWAL_SUCCESS:
                //手续费
                checkWithfrawalAmount(settlementWithdrawal, settlementAccount);
                BigDecimal subtract = settlementAccount.getSettlementAfterBalance().subtract(settlementAccount.getFrozenBalance());
                frozenBalance = settlementAccount.getFrozenBalance().subtract(settlementWithdrawal.getWithdrawalAmount());
                settlementAccount.setFrozenBalance(frozenBalance);
                settlementAccount.setSettlementAfterBalance(subtract);
                break;
            default:
                break;

        }
        settlementAccountService.updateById(settlementAccount);
        settlementWithdrawal.setUpdateTime(DateUtils.getNowDate());
        return this.baseMapper.updateSettlementWithdrawal(settlementWithdrawal);
    }

    private BigDecimal checkWithfrawalAmount(SettlementWithdrawal settlementWithdrawal, SettlementAccount settlementAccount) {
        BigDecimal realStoreInAmount = settlementWithdrawal.getWithdrawalAmount().multiply(BigDecimal.ONE.subtract(settlementAccount.getWithdrawalRate()));
        if (realStoreInAmount.compareTo(settlementAccount.getAvailableBalance()) > 0) {
            throw new CustomException("用户可提现金额(加费率)不能大于账户可用金额");
        }
        return realStoreInAmount;
    }

    private void checkRejectedAction(SettlementWithdrawal settlementWithdrawal) {
        if (Objects.equals(WithdrawalStatusEnum.REJECTED, settlementWithdrawal.getStatus()) && StringUtils.isBlank(settlementWithdrawal.getFailReason())) {
            throw new CustomException("请填写失败原因");
        }
    }

    @Override
    public Boolean auditWithdrawal(WithdrawalRequest withdrawalRequest) {
        Assert.notNull(withdrawalRequest, "审核申请不能为空");
        Assert.notNull(withdrawalRequest.getId(), "id不能为空");
        Assert.notNull(withdrawalRequest.getStatus(), "status不能为空");
        if (Objects.equals(WithdrawalStatusEnum.REJECTED, withdrawalRequest.getStatus()) && StringUtils.isBlank(withdrawalRequest.getFailReason())) {
            throw new CustomException("请填写失败原因");
        }
        return lambdaUpdate().set(StringUtils.isNotBlank(withdrawalRequest.getFailReason()), SettlementWithdrawal::getFailReason, withdrawalRequest.getFailReason())
                .set(SettlementWithdrawal::getStatus, withdrawalRequest.getStatus())
                .eq(SettlementWithdrawal::getId, withdrawalRequest.getId()).update();
    }

    /**
     * 批量删除提现
     *
     * @param ids 需要删除的提现主键
     * @return 结果
     */
    @Override
    public int deleteSettlementWithdrawalByIds(Long[] ids) {
        checkStatus(ids);
        return this.baseMapper.deleteSettlementWithdrawalByIds(ids);
    }

    private void checkStatus(Long... ids) {
        boolean res = lambdaQuery().in(SettlementWithdrawal::getId, ids).list().stream().anyMatch(settlementWithdrawal -> !StringUtils.equals(WithdrawalStatusEnum.UNDER_AUDIT.name(), settlementWithdrawal.getStatus()));
        if (res) {
            throw new CustomException("已过审核后的提现记录不能操作");
        }
    }

    /**
     * 删除提现信息
     *
     * @param id 提现主键
     * @return 结果
     */
    @Override
    public int deleteSettlementWithdrawalById(Long id) {
        return this.baseMapper.deleteSettlementWithdrawalById(id);
    }

    @Override
    public void checkPremssion(long storeId, Long... ids) {
        boolean res = lambdaQuery().in(SettlementWithdrawal::getId, ids).list().stream().anyMatch(settlementWithdrawal -> !settlementWithdrawal.getStoreId().equals(storeId));
        if (res) {
            throw new CustomException("您没有权限操作该数据");
        }
    }

    @Override
    public Boolean withdrawalPay(WithdrawalPayRequest withdrawalPayRequest) {
        Assert.notNull(withdrawalPayRequest, "付款请求体不能为空");
        Assert.notNull(withdrawalPayRequest.getId(), "id不能为空");
        SettlementWithdrawal settlementWithdrawal = getById(withdrawalPayRequest.getId());
        //付款
        settlementWithdrawal.setStatus(WithdrawalStatusEnum.WITHDRAWAL_ING.name());
        return updateById(settlementWithdrawal);
    }

}

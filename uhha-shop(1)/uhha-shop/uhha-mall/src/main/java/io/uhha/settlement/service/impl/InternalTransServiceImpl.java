package io.uhha.settlement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.lang.Assert;
import io.uhha.common.enums.AccountTypeEnum;
import io.uhha.common.enums.BillingDirectionEnum;
import io.uhha.common.enums.MerchantTypeEnum;
import io.uhha.common.exception.CustomException;
import io.uhha.common.utils.DateUtils;
import io.uhha.common.utils.StringUtils;
import io.uhha.order.domain.OmsBillingRecords;
import io.uhha.order.domain.OmsOrder;
import io.uhha.order.domain.OmsOrderSku;
import io.uhha.setting.bean.PlatformFeeSet;
import io.uhha.setting.service.ILsPlatformFeeSettingService;
import io.uhha.settlement.domain.InternalTrans;
import io.uhha.settlement.domain.SettlementAccount;
import io.uhha.settlement.mapper.InternalTransMapper;
import io.uhha.settlement.service.IInternalTransService;
import io.uhha.settlement.service.ISettlementAccountService;
import io.uhha.store.domain.TStoreInfo;
import io.uhha.store.service.ITStoreInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 转移记录Service业务层处理
 *
 * @author uhha
 * @date 2021-12-31
 */
@Service
@Slf4j
public class InternalTransServiceImpl extends ServiceImpl<InternalTransMapper, InternalTrans> implements IInternalTransService {

    @Resource
    private InternalTransMapper internalTransMapper;

    @Autowired
    private ISettlementAccountService settlementAccountService;

    @Autowired
    private ITStoreInfoService storeInfoService;

    @Autowired
    private ILsPlatformFeeSettingService lsPlatformFeeSettingService;


    /**
     * 查询转移记录
     *
     * @param id 转移记录主键
     * @return 转移记录
     */
    @Override
    public InternalTrans selectInternalTransById(Long id) {
        return internalTransMapper.selectInternalTransById(id);
    }

    /**
     * 查询转移记录列表
     *
     * @param internalTrans 转移记录
     * @return 转移记录
     */
    @Override
    public List<InternalTrans> selectInternalTransList(InternalTrans internalTrans) {
        return internalTransMapper.selectInternalTransList(internalTrans);
    }

    /**
     * 新增转移记录
     *
     * @param internalTrans 转移记录
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertInternalTrans(InternalTrans internalTrans) {
        Assert.notNull(internalTrans, "转移信息不能为空");
        Assert.notNull(internalTrans.getFormAccountCode(), "formAccountCode不能为空");
        Assert.notNull(internalTrans.getToAccountCode(), "toAccountCode不能为空");
        Assert.notNull(internalTrans.getTransferAmount(), "transferAmount不能为空");
        Assert.notNull(internalTrans.getOperateType(), "operateType不能为空");
        Assert.notNull(internalTrans.getTransferType(), "操作类型不能为空");
        SettlementAccount toAccount = settlementAccountService.querySettlementAccountByAccountCode(internalTrans.getToAccountCode());
        if (toAccount == null) {
            throw new CustomException("目标账号为空");
        }
        SettlementAccount fromAccount = settlementAccountService.querySettlementAccountByAccountCode(internalTrans.getFormAccountCode());
        if (fromAccount == null) {
            throw new CustomException("源账号为空");
        }

        BigDecimal diffBalance = internalTrans.getTransferAmount();
        if (StringUtils.equals(internalTrans.getOperateType(), BillingDirectionEnum.INCOME.getCode())) {

            if (diffBalance.compareTo(fromAccount.getAvailableBalance()) > 0) {
                throw new CustomException("源账户可用金额不足");
            }
            BigDecimal settlementAfterBalance = toAccount.getSettlementAfterBalance();
            toAccount.setSettlementBeforeBalance(settlementAfterBalance);

            BigDecimal afterBalance = settlementAfterBalance.add(diffBalance);
            toAccount.setSettlementAfterBalance(afterBalance);
            toAccount.setAvailableBalance(toAccount.getAvailableBalance().add(diffBalance));

            BigDecimal settlementfromAccountAfterBalance = fromAccount.getSettlementAfterBalance();
            fromAccount.setSettlementBeforeBalance(settlementfromAccountAfterBalance);

            BigDecimal afterfromAccountBalance = settlementfromAccountAfterBalance.subtract(diffBalance);
            fromAccount.setSettlementAfterBalance(afterfromAccountBalance);
            fromAccount.setAvailableBalance(fromAccount.getAvailableBalance().subtract(diffBalance));


        } else if (StringUtils.equals(internalTrans.getOperateType(), BillingDirectionEnum.OUTCOME.getCode())) {
            if (diffBalance.compareTo(toAccount.getAvailableBalance()) > 0) {
                throw new CustomException("目标账户可用金额不足");
            }

            BigDecimal settlementAfterBalance = fromAccount.getSettlementAfterBalance();
            fromAccount.setSettlementBeforeBalance(settlementAfterBalance);

            BigDecimal afterBalance = settlementAfterBalance.add(diffBalance);
            fromAccount.setSettlementAfterBalance(afterBalance);
            fromAccount.setAvailableBalance(fromAccount.getAvailableBalance().add(diffBalance));

            BigDecimal settlementfromAccountAfterBalance = toAccount.getSettlementAfterBalance();
            toAccount.setSettlementBeforeBalance(settlementfromAccountAfterBalance);

            BigDecimal afterfromAccountBalance = settlementfromAccountAfterBalance.subtract(diffBalance);
            toAccount.setSettlementAfterBalance(afterfromAccountBalance);
            toAccount.setAvailableBalance(toAccount.getAvailableBalance().subtract(diffBalance));
        }
        settlementAccountService.updateById(toAccount);
        settlementAccountService.updateById(fromAccount);
        internalTrans.setCreateTime(DateUtils.getNowDate());
        internalTrans.setTransferTime(DateUtils.getNowDate());
        return internalTransMapper.insertInternalTrans(internalTrans);
    }

    @Override
    public void settlement(InternalTrans internalTrans, OmsBillingRecords obs) {
        Assert.notNull(internalTrans, "转移信息不能为空");
        Assert.notNull(internalTrans.getFormAccountCode(), "formAccountCode不能为空");
        Assert.notNull(internalTrans.getToAccountCode(), "toAccountCode不能为空");
        Assert.notNull(internalTrans.getTransferAmount(), "transferAmount不能为空");
        Assert.notNull(internalTrans.getOperateType(), "operateType不能为空");
        Assert.notNull(internalTrans.getTransferType(), "操作类型不能为空");
        SettlementAccount toAccount = settlementAccountService.querySettlementAccountByAccountCode(internalTrans.getToAccountCode());
        if (toAccount == null) {
            throw new CustomException("目标账号为空");
        }


        SettlementAccount fromAccount = settlementAccountService.querySettlementAccountByAccountCode(internalTrans.getFormAccountCode());
        if (fromAccount == null) {
            throw new CustomException("源账号为空");
        }
        if (StringUtils.equals(internalTrans.getOperateType(), BillingDirectionEnum.INCOME.getCode())) {
            //获取转移金额
            BigDecimal transferAmount = getInComeTransferAmount(internalTrans, toAccount);

            if (transferAmount.compareTo(fromAccount.getAvailableBalance()) > 0) {
                throw new CustomException("源账户可用金额不足");
            }
            BigDecimal settlementAfterBalance = toAccount.getSettlementAfterBalance();
            toAccount.setSettlementBeforeBalance(settlementAfterBalance);

            BigDecimal afterBalance = settlementAfterBalance.add(transferAmount);
            toAccount.setSettlementAfterBalance(afterBalance);
            toAccount.setAvailableBalance(toAccount.getAvailableBalance().add(transferAmount));

            BigDecimal settlementfromAccountAfterBalance = fromAccount.getSettlementAfterBalance();
            fromAccount.setSettlementBeforeBalance(settlementfromAccountAfterBalance);

            BigDecimal afterfromAccountBalance = settlementfromAccountAfterBalance.subtract(transferAmount);
            fromAccount.setSettlementAfterBalance(afterfromAccountBalance);
            fromAccount.setAvailableBalance(fromAccount.getAvailableBalance().subtract(transferAmount));
            //带货结算
            dropshipSettlement(obs, toAccount, fromAccount);
        } else if (StringUtils.equals(internalTrans.getOperateType(), BillingDirectionEnum.OUTCOME.getCode())) {
            //获取转移金额
            BigDecimal transferAmount = getOutComeTransferAmount(internalTrans, toAccount);

            if (transferAmount.compareTo(toAccount.getAvailableBalance()) > 0) {
                throw new CustomException("目标账户可用金额不足");
            }

            BigDecimal settlementAfterBalance = fromAccount.getSettlementAfterBalance();
            fromAccount.setSettlementBeforeBalance(settlementAfterBalance);

            BigDecimal afterBalance = settlementAfterBalance.add(transferAmount);
            fromAccount.setSettlementAfterBalance(afterBalance);
            fromAccount.setAvailableBalance(fromAccount.getAvailableBalance().add(transferAmount));

            BigDecimal settlementfromAccountAfterBalance = toAccount.getSettlementAfterBalance();
            toAccount.setSettlementBeforeBalance(settlementfromAccountAfterBalance);

            BigDecimal afterfromAccountBalance = settlementfromAccountAfterBalance.subtract(transferAmount);
            toAccount.setSettlementAfterBalance(afterfromAccountBalance);
            toAccount.setAvailableBalance(toAccount.getAvailableBalance().subtract(transferAmount));
        }
        settlementAccountService.updateById(toAccount);
        settlementAccountService.updateById(fromAccount);
        internalTrans.setCreateTime(DateUtils.getNowDate());
        internalTrans.setTransferTime(DateUtils.getNowDate());
        internalTransMapper.insertInternalTrans(internalTrans);

    }

    private BigDecimal getInComeTransferAmount(InternalTrans internalTrans, SettlementAccount toAccount) {
        AccountTypeEnum accountType = AccountTypeEnum.valueOf(toAccount.getAccountType());
        BigDecimal transferAmount = internalTrans.getTransferAmount();
        switch (accountType) {
            case STORE:
                //服务费率
                BigDecimal serviceRate = getServiceRate(toAccount);
                transferAmount = internalTrans.getTransferAmount().multiply(BigDecimal.ONE.subtract(serviceRate));
                break;
            case PERSONAL:
                break;
            case SYSTEM:
                break;
            default:
                break;
        }
        return transferAmount;
    }

    private BigDecimal getOutComeTransferAmount(InternalTrans internalTrans, SettlementAccount toAccount) {
        //扣除平台手续费
        //扣除店铺获取费用
        //扣除用户手续费
        AccountTypeEnum accountType = AccountTypeEnum.valueOf(toAccount.getAccountType());
        BigDecimal transferAmount = internalTrans.getTransferAmount();
        switch (accountType) {
            case STORE:
                //服务费率
                BigDecimal serviceRate = getServiceRate(toAccount);
                transferAmount = internalTrans.getTransferAmount().multiply(BigDecimal.ONE.subtract(serviceRate));
                break;
            case PERSONAL:
                break;
            case SYSTEM:
                break;
            default:
                break;
        }
        return transferAmount;
    }

    private void dropshipSettlement(OmsBillingRecords obs, SettlementAccount toAccount, SettlementAccount fromAccount) {
        OmsOrder omsOrder = obs.getOmsOrder();
        List<OmsOrderSku> omsOrderSkus = Optional.ofNullable(omsOrder).map(OmsOrder::getOrderSkus).orElse(null);
        if (obs != null && !CollectionUtils.isEmpty(omsOrderSkus)) {
            List<OmsOrderSku> omsOrderSkuList = omsOrderSkus.stream().filter(omsOrderSku -> "1".equals(omsOrderSku.getIsDropship())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(omsOrderSkuList)) {
                Long customerId = obs.getOmsOrder().getCustomerId();
                PlatformFeeSet platformFeeSet = lsPlatformFeeSettingService.queryPlatformFeeSet();
                BigDecimal defaultDropshipPlatformPart = Optional.ofNullable(platformFeeSet).map(PlatformFeeSet::getDefaultDropshipPlatformPart).orElse(BigDecimal.ZERO);
                BigDecimal defaultDropshipPart = Optional.ofNullable(platformFeeSet).map(PlatformFeeSet::getDefaultDropshipRate).orElse(BigDecimal.ZERO);
                if (!ObjectUtils.isEmpty(customerId) && customerId != 0) {
                    SettlementAccount customSettlementAccount = settlementAccountService.querySettlementAccountByStoreId(AccountTypeEnum.PERSONAL.name(), null, customerId);
                    if (customSettlementAccount == null) {
                        log.error("会员id为{}，订单号为{}结算账户为空总价为{},结算费率为", customerId, obs.getOmsOrder().getOrderCode());
                        throw new CustomException("用户结算账户为空,会员id为" + customerId);
                    }
                    omsOrderSkuList.forEach(omsOrderSku -> {
                        BigDecimal dropshipServiceRate = Optional.ofNullable(omsOrderSku.getCateRate()).orElse(BigDecimal.ZERO);
                        if (dropshipServiceRate.compareTo(BigDecimal.ZERO) <= 0) {
                            dropshipServiceRate = defaultDropshipPart;
                        }
                        BigDecimal price = omsOrderSku.getSkuPrice();
                        //平台手续费
                        dropshipSettlement(toAccount.getAccountCode(), fromAccount.getAccountCode(), defaultDropshipPlatformPart, price);
                        //用户带货手续费
                        dropshipSettlement(toAccount.getAccountCode(), customSettlementAccount.getAccountCode(), dropshipServiceRate, price);

                    });
                }


            }
        }
    }

    private void dropshipSettlement(String formAccountCode, String toAccountCode, BigDecimal dropshipServiceRate, BigDecimal price) {
        BigDecimal userTransferAmount = price.multiply(dropshipServiceRate);
        InternalTrans internalTrans1 = new InternalTrans();
        internalTrans1.setTransferAmount(userTransferAmount);
        internalTrans1.setToAccountCode(toAccountCode);
        internalTrans1.setFormAccountCode(formAccountCode);
        internalTrans1.setOperateType(BillingDirectionEnum.INCOME.getCode());
        internalTrans1.setTransferType("账单结算");
        insertInternalTrans(internalTrans1);
    }

    private BigDecimal getServiceRate(SettlementAccount toAccount) {
        BigDecimal serviceRate = BigDecimal.ZERO;
        TStoreInfo tStoreInfo = storeInfoService.queryStoreInfo(toAccount.getAssociateId());
        if (tStoreInfo == null) {
            throw new CustomException("结算账户code为" + toAccount.getAccountCode() + "店铺不存在");
        }
        PlatformFeeSet platformFeeSet = lsPlatformFeeSettingService.queryPlatformFeeSet();
        if (tStoreInfo.getMerchantType().equals(MerchantTypeEnum.BASIC.getCode()) && !ObjectUtils.isEmpty(platformFeeSet.getDefaultBasicStoreServiceRate())) {
            serviceRate = platformFeeSet.getDefaultBasicStoreServiceRate();
        } else if (tStoreInfo.getMerchantType().equals(MerchantTypeEnum.PREMIUM.getCode()) && !ObjectUtils.isEmpty(platformFeeSet.getDefaultPremiumStoreServiceRate()))
            serviceRate = platformFeeSet.getDefaultPremiumStoreServiceRate();
        return serviceRate;
    }

    /**
     * 修改转移记录
     *
     * @param internalTrans 转移记录
     * @return 结果
     */
    @Override
    public int updateInternalTrans(InternalTrans internalTrans) {
        return internalTransMapper.updateInternalTrans(internalTrans);
    }

    /**
     * 批量删除转移记录
     *
     * @param ids 需要删除的转移记录主键
     * @return 结果
     */
    @Override
    public int deleteInternalTransByIds(Long[] ids) {
        return internalTransMapper.deleteInternalTransByIds(ids);
    }

    /**
     * 删除转移记录信息
     *
     * @param id 转移记录主键
     * @return 结果
     */
    @Override
    public int deleteInternalTransById(Long id) {
        return internalTransMapper.deleteInternalTransById(id);
    }
}

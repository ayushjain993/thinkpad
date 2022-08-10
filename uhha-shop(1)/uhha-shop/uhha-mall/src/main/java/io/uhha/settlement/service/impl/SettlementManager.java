package io.uhha.settlement.service.impl;

import com.alibaba.fastjson.JSONObject;
import io.uhha.coin.common.match.MathUtils;
import io.uhha.common.constant.Constant;
import io.uhha.common.enums.*;
import io.uhha.common.utils.DateUtils;
import io.uhha.common.utils.StringUtils;
import io.uhha.order.domain.OmsBillingRecords;
import io.uhha.order.domain.OmsOrder;
import io.uhha.order.service.IOmsBillingRecordsService;
import io.uhha.setting.bean.PlatformFeeSet;
import io.uhha.setting.service.ILsPlatformFeeSettingService;
import io.uhha.settlement.domain.InternalTrans;
import io.uhha.settlement.domain.SettlementAccount;
import io.uhha.settlement.domain.SettlementRecord;
import io.uhha.settlement.service.*;
import io.uhha.store.domain.TStoreInfo;
import io.uhha.store.service.ITStoreInfoService;
import io.uhha.util.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SettlementManager implements ISettlementManager {

    @Autowired
    private IOmsBillingRecordsService omsBillingRecordsService;
    @Autowired
    private ISettlementRecordService settlementRecordService;
    @Autowired
    private ISettlementAccountService settlementAccountService;
    @Autowired
    private IInternalTransService internalTransService;
    @Autowired
    private ILsPlatformFeeSettingService lsPlatformFeeSettingService;
    @Autowired
    private ITStoreInfoService storeInfoService;
    @Autowired
    private IAccountCodeService accountCodeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean autoSettlementJob(Long storeId, String operator, Map<String, Object> dateRange) {
        List<OmsBillingRecords> omsBillingRecordsList = omsBillingRecordsService.queryByQueryDateAndSettlementStatus(SettlementStatusEnum.NOT_SETTLED, storeId, dateRange);

        if (CollectionUtils.isEmpty(omsBillingRecordsList)) {
            log.warn("no omsBillingRecordsList.");
            return true;
        }
        omsBillingRecordsList
                .stream()
                .collect(Collectors.groupingBy(OmsBillingRecords::getOrderCode, Collectors.toList()))
                .forEach((orderCode, omsBillingRecords) -> {
                    AccountTypeEnum accountType = AccountTypeEnum.SYSTEM;
                    String childType = CommonConstant.SYSTEM_SETTLEMENT;
                    SettlementAccount settlementAccount = settlementAccountService.querySettlementAccountByStoreId(accountType.name(), childType, CommonConstant.ADMIN_STOREID);

                    if (settlementAccount == null) {
                        log.error("no settlement account.");
                        return;
                    }
//                    if (!checkOrderBalance(omsBillingRecords)) {
//                        log.error("omsBillingRecords are not balance.");
//                        return;
//                    }
                    omsBillingRecords.forEach(x -> handleOmsBillingRecord(settlementAccount, x, operator));

                });
        return true;
    }

    /**
     * 检查订单数据是否平衡
     *
     * @param omsBillingRecords
     * @return
     */
    private boolean checkOrderBalance(List<OmsBillingRecords> omsBillingRecords) {
        if (org.apache.commons.collections4.CollectionUtils.isEmpty(omsBillingRecords)) {
            log.warn("omsBillingRecords is empty!");
            return true;
        }
        BigDecimal cumulative = omsBillingRecords.stream().map(OmsBillingRecords::getOrderActualPrice).reduce(BigDecimal.ZERO, (a, b) -> MathUtils.add(a.abs(), b.abs()));

        return cumulative.compareTo(omsBillingRecords.get(0).getOrderPrice()) == 0;
    }

    private void handleOmsBillingRecord(SettlementAccount settlementAccount, OmsBillingRecords omsBillingRecord, String operator) {
        //账单类型
        BillingRecordTypeEnum recordType = BillingRecordTypeEnum.getRecordType(omsBillingRecord.getRecordType());
        OmsOrder omsOrder = omsBillingRecord.getOmsOrder();
        Optional.ofNullable(omsOrder).ifPresent(omsOrder1 -> {
            if (recordType == null) {
                log.error("invalid recordType data: {}", recordType);
                return;
            }
            BigDecimal orderActualPrice = omsBillingRecord.getOrderActualPrice();
            Long storeId = omsBillingRecord.getStoreId();
            String accountType = "";
            String childType = "";

            switch (recordType) {
                case CONFIRM_ORDER_RECEIPT:
                    if (omsOrder1.isPlatform()) {
                        //钱从订单记录表转入结算账户，直接更新结算表
                        updateSettlementAccount(orderActualPrice, settlementAccount);
                        //更改结算记录表为已结算
                        SettlementRecord settlementRecord = addSettlementRecord(omsBillingRecord, settlementAccount.getAccountCode(),"", orderActualPrice, BigDecimal.ZERO, BillingRecordTypeEnum.CONFIRM_ORDER_RECEIPT.getCode());
                        settlementRecord.setSettlementStaus(SettlementStatusEnum.SETTLED.getCode());
                        settlementRecordService.updateById(settlementRecord);
                    } else {
                        childType = null;
                        SettlementAccount storeSettlementAccount = settlementAccountService.querySettlementAccountByStoreId(AccountTypeEnum.STORE.name(), childType, storeId);
                        if (storeSettlementAccount == null) {
                            log.error("store settlement account is not found.");
                            return;
                        }
                        //计算平台服务费并转账
                        TStoreInfo tStoreInfo = storeInfoService.queryStoreInfo(storeId);
                        if (tStoreInfo == null) {
                            log.error("store with storeId = {} was not found.", storeId);
                            return;
                        }
                        PlatformFeeSet platformFeeSet = lsPlatformFeeSettingService.queryPlatformFeeSet();
                        BigDecimal serviceRate = BigDecimal.ZERO;
                        if (StringUtils.equals(MerchantTypeEnum.BASIC.getCode(), tStoreInfo.getMerchantType())) {
                            serviceRate = Optional.ofNullable(platformFeeSet.getDefaultBasicStoreServiceRate()).orElse(BigDecimal.ZERO);
                        } else if (StringUtils.equals(MerchantTypeEnum.PREMIUM.getCode(), tStoreInfo.getMerchantType())) {
                            serviceRate = Optional.ofNullable(platformFeeSet.getDefaultPremiumStoreServiceRate()).orElse(BigDecimal.ZERO);
                        }
                        //转店铺所得部分转给店铺
                        BigDecimal userTransferAmount = omsBillingRecord.getOrderActualPrice().multiply(BigDecimal.ONE.subtract(serviceRate));
                        updateSettlementAccount(userTransferAmount, settlementAccount);
                        //登记结算记录
                        SettlementRecord settlementRecord = addSettlementRecord(omsBillingRecord, storeSettlementAccount.getAccountCode(),"", userTransferAmount, BigDecimal.ZERO, BillingRecordTypeEnum.CONFIRM_ORDER_RECEIPT.getCode());
                        settlementRecord.setSettlementStaus(SettlementStatusEnum.SETTLED.getCode());
                        settlementRecordService.updateById(settlementRecord);

                        //转系统手续费到系统账户
                        accountType = AccountTypeEnum.SYSTEM.name();
                        childType = CommonConstant.SYSTEM_HANDLING_FEE;
                        SettlementAccount commissionFeeSettlementAccount = settlementAccountService.querySettlementAccountByStoreId(accountType, childType, CommonConstant.ADMIN_STOREID);
                        if (commissionFeeSettlementAccount == null) {
                            log.error("SettlementAccount for system account, handle fee was not found");
                            return;
                        }
                        //钱从订单记录表转入结算账户，直接更新结算表
                        updateSettlementAccount(serviceRate, commissionFeeSettlementAccount);
                        //登记结算记录
                        SettlementRecord platSettlementRecord = addSettlementRecord(omsBillingRecord, accountType,"", serviceRate, BigDecimal.ZERO, BillingRecordTypeEnum.CONFIRM_ORDER_RECEIPT_CATE.getCode());
                        platSettlementRecord.setSettlementStaus(SettlementStatusEnum.SETTLED.getCode());
                        settlementRecordService.updateById(platSettlementRecord);
                    }

                    break;
                case DISTRIBUTION_COMMISSION_L1:
                    //实际资金流转已在确认收货时发放
                    accountType = AccountTypeEnum.PERSONAL.name();
                    childType = null;
                    if (orderActualPrice.compareTo(BigDecimal.ZERO) < 0) {
                        orderActualPrice = orderActualPrice.negate();
                    }
                    SettlementAccount personalSettlementAccount1 = new SettlementAccount();

                    //没有推荐费的推广订单是不存在的
                    if (BigDecimal.ZERO.compareTo(omsOrder1.getOrderCommission()) == 0
                            && (BigDecimal.ZERO.compareTo(omsOrder1.getOrderSCommission()) == 0)) {
                        log.error("invalid distribution billing record \r\n billingRecord:{}", omsBillingRecord);
                        return;
                    }

                    if (omsOrder1.hasRecommendedCommission()) {
                        //1级用户
                        personalSettlementAccount1 = settlementAccountService.querySettlementAccountByStoreId(accountType, childType, omsOrder1.getRecommended());
                        //使用结算账户发佣金
                        transfer(omsBillingRecord, settlementAccount, personalSettlementAccount1, orderActualPrice);
                    }
                    break;
                case DISTRIBUTION_COMMISSION_L2:
                    //实际资金流转已在确认收货时发放
                    accountType = AccountTypeEnum.PERSONAL.name();
                    childType = null;
                    if (orderActualPrice.compareTo(BigDecimal.ZERO) < 0) {
                        orderActualPrice = orderActualPrice.negate();
                    }
                    SettlementAccount personalSettlementAccount2 = new SettlementAccount();

                    //没有推荐费的推广订单是不存在的
                    if (BigDecimal.ZERO.compareTo(omsOrder1.getOrderCommission()) == 0
                            && (BigDecimal.ZERO.compareTo(omsOrder1.getOrderSCommission()) == 0)) {
                        log.error("invalid distribution billing record \r\n billingRecord:{}", omsBillingRecord);
                        return;
                    }

                    if (omsOrder1.hasSRecommondedCommission()) {
                        //2级用户
                        personalSettlementAccount2 = settlementAccountService.querySettlementAccountByStoreId(accountType, childType, omsOrder1.getSRecommended());
                        //使用结算账户发佣金
                        transfer(omsBillingRecord, settlementAccount, personalSettlementAccount2, orderActualPrice);
                    }
                    break;
                case ORDER_DROPSHIP_USER:
                    //实际资金流转已在确认收货时发放
                    accountType = AccountTypeEnum.PERSONAL.name();
                    childType = null;
                    if (orderActualPrice.compareTo(BigDecimal.ZERO) < 0) {
                        orderActualPrice = orderActualPrice.negate();
                    }
                    SettlementAccount dropshipSettlementAccount = settlementAccountService.querySettlementAccountByStoreId(accountType, childType, omsOrder1.getDistributeUserId());
                    if (dropshipSettlementAccount == null) {
                        log.error("SettlementAccount for distributeUseId = {} was not found", omsOrder1.getDistributeUserId());
                        return;
                    }
                    //使用结算账户发佣金
                    transfer(omsBillingRecord, settlementAccount, dropshipSettlementAccount, orderActualPrice);
                    break;
                case ORDER_DROPSHIP_PLATFORM:
                    //实际资金流转已在确认收货时发放
                    accountType = AccountTypeEnum.SYSTEM.name();
                    childType = CommonConstant.SYSTEM_HANDLING_FEE;
                    if (orderActualPrice.compareTo(BigDecimal.ZERO) < 0) {
                        orderActualPrice = orderActualPrice.negate();
                    }
                    SettlementAccount commissionFeeSettlementAccount = settlementAccountService.querySettlementAccountByStoreId(accountType, childType, CommonConstant.ADMIN_STOREID);
                    if (commissionFeeSettlementAccount == null) {
                        log.error("SettlementAccount for system account, handle fee was not found");
                        return;
                    }
                    //使用结算账户发佣金
                    transfer(omsBillingRecord, settlementAccount, commissionFeeSettlementAccount, orderActualPrice);
                    break;
                case REFUND_ORDER:
                    BigDecimal refundAmount = omsBillingRecord.getOrderActualPrice();
                    if (refundAmount.compareTo(BigDecimal.ZERO) < 0) {
                        refundAmount = refundAmount.negate();
                    }
                    accountType = AccountTypeEnum.PERSONAL.name();
                    childType = null;
                    SettlementAccount customerSettlementAccount = settlementAccountService.querySettlementAccountByStoreId(accountType, childType, omsOrder1.getCustomerId());
                    if (customerSettlementAccount == null) {
                        log.error("SettlementAccount for user account :{} was not found", omsOrder1.getCustomerId());
                        return;
                    }
                    if (omsOrder1.isPlatform()) {
                        //钱从订单记录表转入结算账户，直接更新结算表
                        updateSettlementAccount(orderActualPrice, settlementAccount);
                        //使用系统结算账户发退款
                        transfer(omsBillingRecord,settlementAccount, customerSettlementAccount, refundAmount);
                    } else {
                        childType = null;
                        SettlementAccount storeSettlementAccount = settlementAccountService.querySettlementAccountByStoreId(AccountTypeEnum.STORE.name(), childType, omsBillingRecord.getStoreId());
                        //平台服务费
                        TStoreInfo tStoreInfo = storeInfoService.queryStoreInfo(omsBillingRecord.getStoreId());

                        if (storeSettlementAccount != null && tStoreInfo != null) {
                            PlatformFeeSet platformFeeSet = lsPlatformFeeSettingService.queryPlatformFeeSet();
                            BigDecimal serviceRate = BigDecimal.ZERO;
                            if (StringUtils.equals(MerchantTypeEnum.BASIC.getCode(), tStoreInfo.getMerchantType())) {
                                serviceRate = platformFeeSet.getDefaultBasicStoreServiceRate();
                            } else if (StringUtils.equals(MerchantTypeEnum.PREMIUM.getCode(), tStoreInfo.getMerchantType())) {
                                serviceRate = platformFeeSet.getDefaultPremiumStoreServiceRate();
                            }
                            //从店铺结算账户中出用户退款
                            BigDecimal userTransferAmount = refundAmount.multiply(BigDecimal.ONE.subtract(serviceRate));
                            //转店铺所得部分转给店铺
                            updateSettlementAccount(userTransferAmount, storeSettlementAccount);
                            //使用店铺账户发退款
                            transfer(omsBillingRecord,storeSettlementAccount, customerSettlementAccount, userTransferAmount);

                            //从平台结算账户中出用户退款
                            BigDecimal platformAmount = refundAmount.subtract(userTransferAmount);
                            transfer(omsBillingRecord, settlementAccount, customerSettlementAccount, platformAmount);
                        }
                    }
                    break;
                case BACK_ORDER_COMMISSION:
                    if (orderActualPrice.compareTo(BigDecimal.ZERO) < 0) {
                        orderActualPrice = orderActualPrice.negate();
                    }
                    accountType = AccountTypeEnum.PERSONAL.name();
                    childType = null;
                    SettlementAccount customerSettlementAccount1 = settlementAccountService.querySettlementAccountByStoreId(accountType, childType, omsOrder1.getCustomerId());
                    if (customerSettlementAccount1 == null) {
                        log.error("SettlementAccount for user account :{} was not found", omsOrder1.getCustomerId());
                        return;
                    }

                    accountType = AccountTypeEnum.PERSONAL.name();
                    childType = null;
                    //没有推荐费的推广订单是不存在的
                    if (BigDecimal.ZERO.compareTo(omsOrder1.getOrderCommission()) == 0
                            && (BigDecimal.ZERO.compareTo(omsOrder1.getOrderSCommission()) == 0)) {
                        log.error("invalid distribution billing record \r\n billingRecord:{}", omsBillingRecord);
                        return;
                    }

                    SettlementAccount personalCommissionSettlementAccount = new SettlementAccount();

                    if (omsOrder1.getOrderCommission() != null) {
                        //1级用户
                        personalCommissionSettlementAccount = settlementAccountService.querySettlementAccountByStoreId(accountType, childType, omsOrder1.getRecommended());
                        transfer(omsBillingRecord, personalCommissionSettlementAccount, customerSettlementAccount1, orderActualPrice);
                    }
                    if (omsOrder1.getOrderSCommission() != null) {
                        //2级用户
                        personalCommissionSettlementAccount = settlementAccountService.querySettlementAccountByStoreId(accountType, childType, omsOrder1.getSRecommended());
                        transfer(omsBillingRecord, personalCommissionSettlementAccount, customerSettlementAccount1, orderActualPrice);
                    }
                    break;
                case BACK_ORDER_DROPSHIP:
                    if (orderActualPrice.compareTo(BigDecimal.ZERO) < 0) {
                        orderActualPrice = orderActualPrice.negate();
                    }
                    SettlementAccount customerSettlementAccount2 = settlementAccountService.querySettlementAccountByStoreId(AccountTypeEnum.PERSONAL.name(), null, omsOrder1.getCustomerId());
                    if (customerSettlementAccount2 == null) {
                        log.error("SettlementAccount for user account :{} was not found", omsOrder1.getCustomerId());
                        return;
                    }
                    //用户退还带货佣金
                    if (omsOrder1.getDistributeUserId() != null) {
                        SettlementAccount distributeSettleAccount = settlementAccountService.querySettlementAccountByStoreId(AccountTypeEnum.PERSONAL.name(), null, omsOrder1.getDistributeUserId());

                        if (distributeSettleAccount != null) {
                            //从用户的账户中退还带货手续费
                            transfer(omsBillingRecord, distributeSettleAccount, customerSettlementAccount2, orderActualPrice);
                        }
                    } else {
                        //系统退还系统部分的带货佣金
                        childType = CommonConstant.SYSTEM_HANDLING_FEE;
                        SettlementAccount sysCommissionSettlementAccount = settlementAccountService.querySettlementAccountByStoreId(AccountTypeEnum.SYSTEM.name(), childType, CommonConstant.ADMIN_STOREID);
                        //从用户的账户中退还带货手续费
                        transfer(omsBillingRecord, sysCommissionSettlementAccount, customerSettlementAccount2, orderActualPrice);
                    }
                    break;
                default:
                    break;
            }
            //更改账单表为已结算
            omsBillingRecord.setStatus(SettlementStatusEnum.SETTLED.getCode());
            omsBillingRecord.setSettlementTime(DateUtils.getNowDate());
            omsBillingRecord.setUpdateBy(operator);
            omsBillingRecordsService.updateOmsBillingRecords(omsBillingRecord);
        });
    }


    /**
     * 新增结算记录
     *
     * @param omsBillingRecord
     * @param accountCode
     * @param inBilling
     * @param outBilling
     * @return
     */
    private SettlementRecord addSettlementRecord(OmsBillingRecords omsBillingRecord, String accountCode, String relatedAccountCode, BigDecimal inBilling, BigDecimal outBilling, String recordType) {
        SettlementRecord settlementRecord = new SettlementRecord();
        settlementRecord.setInBilling(inBilling);
        settlementRecord.setOutBilling(outBilling);
        settlementRecord.setAccountCode(accountCode);
        if(BillingRecordTypeEnum.CONFIRM_ORDER_RECEIPT.getCode().equalsIgnoreCase(recordType)){
            settlementRecord.setRelatedAccountCode("CONFIRM RECEIPT");
        }else{
            settlementRecord.setRelatedAccountCode(relatedAccountCode);
        }
        settlementRecord.setCreateBy(Constant.SYSTEM);
        settlementRecord.setRecordType(recordType);
        settlementRecord.setSettlementTime(DateUtils.getNowDate());
        settlementRecord.setCreateTime(DateUtils.getNowDate());
        settlementRecord.setSettlementTime(DateUtils.getNowDate());
        settlementRecord.setSettlementStaus(SettlementStatusEnum.NOT_SETTLED.getCode());
        settlementRecord.setOmsBillingRecords(JSONObject.toJSONString(omsBillingRecord));
        settlementRecordService.save(settlementRecord);
        return settlementRecord;
    }

    /**
     * 更新结算账户余额
     *
     * @param orderActualPrice
     * @param settlementAccount
     */
    private void updateSettlementAccount(BigDecimal orderActualPrice, SettlementAccount settlementAccount) {
        BigDecimal settlementAfterBalance = settlementAccount.getSettlementAfterBalance();
        BigDecimal availableBalance = settlementAccount.getAvailableBalance();
        settlementAccount.setSettlementBeforeBalance(settlementAfterBalance);
        settlementAccount.setSettlementAfterBalance(settlementAfterBalance.add(orderActualPrice));
        settlementAccount.setAvailableBalance(availableBalance.add(orderActualPrice));
        settlementAccountService.updateById(settlementAccount);
    }

    @Override
    public boolean insertSettlementAccount(SettlementAccount settlementAccount) {
        String accountCode = accountCodeService.allocateAccountCode(settlementAccount.getAccountType());
        if (io.uhha.common.utils.StringUtils.isEmpty(accountCode)) {
            return false;
        }
        settlementAccount.setAccountCode(accountCode);
        int insert = settlementAccountService.insertSettlementAccount(settlementAccount);
        return insert > 0;
    }

    @Override
    public boolean transfer(OmsBillingRecords omsBillingRecord, SettlementAccount fromAccount, SettlementAccount toAccount, BigDecimal amount) {
        //记录入账
        SettlementRecord distributeSettleRecord1 = addSettlementRecord(omsBillingRecord, toAccount.getAccountCode(), fromAccount.getAccountCode(), amount, BigDecimal.ZERO, omsBillingRecord.getRecordType());
        distributeSettleRecord1.setSettlementStaus(SettlementStatusEnum.SETTLED.getCode());
        settlementRecordService.updateById(distributeSettleRecord1);
        BigDecimal settlementAfterBalance1 = toAccount.getSettlementAfterBalance();
        toAccount.setSettlementBeforeBalance(settlementAfterBalance1);

        BigDecimal afterBalance1 = settlementAfterBalance1.add(amount);
        toAccount.setSettlementAfterBalance(afterBalance1);
        toAccount.setAvailableBalance(toAccount.getAvailableBalance().add(amount));

        BigDecimal settlementfromAccountAfterBalance1 = fromAccount.getSettlementAfterBalance();
        fromAccount.setSettlementBeforeBalance(settlementfromAccountAfterBalance1);

        BigDecimal afterfromAccountBalance1 = settlementfromAccountAfterBalance1.subtract(amount);
        fromAccount.setSettlementAfterBalance(afterfromAccountBalance1);
        fromAccount.setAvailableBalance(fromAccount.getAvailableBalance().subtract(amount));
        settlementAccountService.updateById(toAccount);
        settlementAccountService.updateById(fromAccount);

        //记录出账
        SettlementRecord distributeSettleRecord2 = addSettlementRecord(omsBillingRecord, fromAccount.getAccountCode(), toAccount.getAccountCode(), BigDecimal.ZERO, amount, omsBillingRecord.getRecordType());
        distributeSettleRecord2.setSettlementStaus(SettlementStatusEnum.SETTLED.getCode());
        settlementRecordService.updateById(distributeSettleRecord2);

        return false;
    }
}

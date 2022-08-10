package io.uhha.order.domain;

import cn.hutool.core.math.MathUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.uhha.coin.common.match.MathUtils;
import io.uhha.common.annotation.Excel;
import io.uhha.common.core.domain.BaseEntity;
import io.uhha.common.enums.BillingDirectionEnum;
import io.uhha.common.enums.BillingRecordTypeEnum;
import io.uhha.setting.bean.PlatformFeeSet;
import io.uhha.util.CommonConstant;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 账单记录对象 oms_billing_records
 *
 * @author mj
 * @date 2020-07-24
 */
@Data
@Accessors(chain = true)
public class OmsBillingRecords extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 订单或者退单的code
     */
    @Excel(name = "订单或者退单的code")
    private String orderCode;

    /**
     * 账单进出类型 0 进 1 出
     *
     * @see io.uhha.common.enums.BillingDirectionEnum
     */
    @Excel(name = "账单进出类型 0 进 1 出")
    private String type;

    /**
     * 店铺id
     */
    @Excel(name = "店铺id")
    private Long storeId;

    /**
     * 店铺名称
     */
    @Excel(name = "店铺名称")
    private String storeName;

    /**
     * 账单记录类型
     *
     * @see io.uhha.common.enums.BillingRecordTypeEnum
     */
    @Excel(name = "账单记录类型")
    private String recordType;

    /**
     * 订单实付金额
     */
    @Excel(name = "订单实付金额")
    private BigDecimal orderPrice;

    /**
     * 订单实退金额
     */
    @Excel(name = "订单实退金额")
    private BigDecimal orderBackPrice;

    /**
     * 订单佣金
     */
    @Excel(name = "订单佣金")
    private BigDecimal orderCommission;

    /**
     * 订单实际进出金额
     */
    @Excel(name = "订单实际进出金额")
    private BigDecimal orderActualPrice;

    /**
     * 结算状态 0 未结算 1 已结算 默认0
     *
     * @see io.uhha.common.enums.SettlementStatusEnum
     */
    @Excel(name = "结算状态 0 未结算 1 已结算 默认0 ")
    private String status;

    /**
     * 结算时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date settlementTime;

    private OmsOrder omsOrder;

    /**
     * 构造确认收货的账单记录
     *
     * @param order 订单记录
     * @return 返回账单记录
     */
    public static OmsBillingRecords buildForConfirmOrder(OmsOrder order) {


        // 如果没有订单信息 或者订单下没有单品信息 则直接返回
        if (Objects.isNull(order)) {
            return null;
        }

        // 账单记录
        OmsBillingRecords billingRecord = new OmsBillingRecords();
        billingRecord.orderCode = order.getOrderCode();
        billingRecord.type = BillingDirectionEnum.INCOME.getCode();
        billingRecord.storeId = order.getStoreId();
        billingRecord.storeName = order.getStoreName();
        billingRecord.recordType = BillingRecordTypeEnum.CONFIRM_ORDER_RECEIPT.getCode();
        billingRecord.orderPrice = order.getPrice();
        // 订单是众筹订单的时候只有全款订单需要佣金  抽奖和无回报不需要
        if (!order.isPlatform() && order.isNeedCommission()) {
            billingRecord.orderCommission = calcOrderCommission(order.getOrderSkus());
        } else {
            billingRecord.orderCommission = BigDecimal.ZERO;
        }
        billingRecord.orderActualPrice = billingRecord.orderPrice.subtract(billingRecord.orderCommission);
        return billingRecord;
    }

    /**
     * 构造分销订单的账单记录
     *
     * @param order 订单记录
     * @return 返回账单记录
     */
    public static OmsBillingRecords buildForRecommondOrder(OmsOrder order) {
        // 如果没有订单信息 则直接返回
        if (Objects.isNull(order)) {
            return null;
        }
        // 账单记录
        OmsBillingRecords billingRecord = new OmsBillingRecords();
        billingRecord.orderCode = order.getOrderCode();
        billingRecord.type = BillingDirectionEnum.OUTCOME.getCode();
        billingRecord.storeId = order.getStoreId();
        billingRecord.storeName = order.getStoreName();
        billingRecord.recordType = BillingRecordTypeEnum.DISTRIBUTION_COMMISSION_L1.getCode();
        billingRecord.orderPrice = order.getPrice();
        billingRecord.orderCommission = order.getOrderCommission();
        billingRecord.orderActualPrice = BigDecimal.ZERO.subtract(billingRecord.getOrderCommission());
        return billingRecord;

    }

    /**
     * 构造带货订单的账单记录（用户所得部分）
     *
     * @param order 订单记录
     * @return 返回账单记录
     */
    public static OmsBillingRecords buildForDropshipOrderUser(OmsOrder order, BigDecimal orderCommission) {
        // 如果没有订单信息 则直接返回
        if (Objects.isNull(order)) {
            return null;
        }
        // 账单记录
        OmsBillingRecords billingRecord = new OmsBillingRecords();
        billingRecord.orderCode = order.getOrderCode();
        billingRecord.type = BillingDirectionEnum.OUTCOME.getCode();
        billingRecord.storeId = order.getStoreId();
        billingRecord.storeName = order.getStoreName();
        billingRecord.recordType = BillingRecordTypeEnum.ORDER_DROPSHIP_USER.getCode();
        billingRecord.orderPrice = order.getPrice();
        billingRecord.orderCommission = orderCommission;
        billingRecord.orderActualPrice = BigDecimal.ZERO.subtract(billingRecord.getOrderCommission());
        return billingRecord;

    }

    /**
     * 构造带货订单的账单记录（平台所得部分）
     *
     * @param order 订单记录
     * @return 返回账单记录
     */
    public static OmsBillingRecords buildForDropshipOrderPlatform(OmsOrder order, BigDecimal orderCommission) {
        // 如果没有订单信息 则直接返回
        if (Objects.isNull(order)) {
            return null;
        }
        // 账单记录
        OmsBillingRecords billingRecord = new OmsBillingRecords();
        billingRecord.orderCode = order.getOrderCode();
        billingRecord.type = BillingDirectionEnum.INCOME.getCode();
        billingRecord.storeId = order.getStoreId();
        billingRecord.storeName = order.getStoreName();
        billingRecord.recordType = BillingRecordTypeEnum.ORDER_DROPSHIP_PLATFORM.getCode();
        billingRecord.orderPrice = order.getPrice();
        billingRecord.orderCommission = orderCommission;
        billingRecord.orderActualPrice = BigDecimal.ZERO.subtract(billingRecord.getOrderCommission());
        return billingRecord;

    }

    public static OmsBillingRecords buildForReturnDropshipOrder(OmsBackOrder backOrder, BigDecimal defaultDropshipPart) {

        // 平台退货没有佣金直接返回
        if (Objects.isNull(backOrder) || backOrder.isPlatform()) {
            return null;
        }

        // 账单记录
        OmsBillingRecords billingRecord = new OmsBillingRecords();
        billingRecord.orderCode = backOrder.getBackCode();
        billingRecord.type = BillingDirectionEnum.INCOME.getCode();
        billingRecord.storeId = backOrder.getStoreId();
        billingRecord.storeName = backOrder.getStoreName();
        billingRecord.recordType = BillingRecordTypeEnum.BACK_ORDER_DROPSHIP.getCode();
        billingRecord.orderPrice = backOrder.getRealBackPrice();
        billingRecord.orderCommission = calcOrderDropshipForReturn(backOrder.getOrderSkus(), defaultDropshipPart);
        billingRecord.orderActualPrice = billingRecord.orderCommission;
        return billingRecord;
    }

    private static BigDecimal calcOrderDropshipForReturn(List<OmsOrderSku> orderSkus, BigDecimal defaultDropshipPart) {
        BigDecimal userTransferAmount = BigDecimal.ZERO;
        orderSkus.stream().filter(omsOrderSku -> "1".equals(omsOrderSku.getIsDropship())).forEach(omsOrderSku -> {
            BigDecimal dropshipServiceRate = Optional.ofNullable(omsOrderSku.getCateRate()).orElse(BigDecimal.ZERO);
            if (dropshipServiceRate.compareTo(BigDecimal.ZERO) <= 0) {
                dropshipServiceRate = Optional.ofNullable(defaultDropshipPart).orElse(BigDecimal.ZERO);
            }
            BigDecimal price = omsOrderSku.getSkuPrice();
            //用户带货手续费
            userTransferAmount.add(price.multiply(dropshipServiceRate));
        });
        return userTransferAmount;
    }


    /**
     * 构造二级分销订单的账单记录
     *
     * @param order 订单记录
     * @return 返回账单记录
     */
    public static OmsBillingRecords buildForSRecommondOrder(OmsOrder order) {
        // 如果没有订单信息 则直接返回
        if (Objects.isNull(order)) {
            return null;
        }
        // 账单记录
        OmsBillingRecords billingRecord = new OmsBillingRecords();
        billingRecord.orderCode = order.getOrderCode();
        billingRecord.type = BillingDirectionEnum.OUTCOME.getCode();
        billingRecord.storeId = order.getStoreId();
        billingRecord.storeName = order.getStoreName();
        billingRecord.recordType = BillingRecordTypeEnum.DISTRIBUTION_COMMISSION_L2.getCode();
        billingRecord.orderPrice = order.getPrice();
        billingRecord.orderCommission = order.getOrderSCommission();
        billingRecord.orderActualPrice = BigDecimal.ZERO.subtract(billingRecord.getOrderCommission());
        return billingRecord;

    }

    /**
     * 构造退款的账单记录
     *
     * @param backOrder 退款信息
     * @return 返回账单记录
     */
    public static OmsBillingRecords buildForRefundOrder(OmsBackOrder backOrder) {
        if (Objects.isNull(backOrder)) {
            return null;
        }

        // 账单记录
        OmsBillingRecords billingRecord = new OmsBillingRecords();
        billingRecord.orderCode = backOrder.getBackCode();
        billingRecord.type = CommonConstant.ADMIN_STOREID.equals(backOrder.getStoreId()) ? BillingDirectionEnum.OUTCOME.getCode() : BillingDirectionEnum.INCOME.getCode();
        billingRecord.storeId = backOrder.getStoreId();
        billingRecord.storeName = backOrder.getStoreName();
        billingRecord.recordType = BillingRecordTypeEnum.REFUND_ORDER.getCode();
        billingRecord.orderBackPrice = backOrder.getBackPrice();
        billingRecord.orderActualPrice = CommonConstant.ADMIN_STOREID.equals(backOrder.getStoreId()) ? billingRecord.orderBackPrice.negate() : billingRecord.orderBackPrice;
        return billingRecord;
    }

    /**
     * 构造退货的账单记录
     *
     * @param backOrder 退款信息
     * @return 返回账单记录
     */
    public static OmsBillingRecords buildForReturnOrder(OmsBackOrder backOrder) {

        // 平台退货没有佣金直接返回
        if (Objects.isNull(backOrder) || backOrder.isPlatform()) {
            return null;
        }

        // 账单记录
        OmsBillingRecords billingRecord = new OmsBillingRecords();
        billingRecord.orderCode = backOrder.getBackCode();
        billingRecord.type = BillingDirectionEnum.INCOME.getCode();
        billingRecord.storeId = backOrder.getStoreId();
        billingRecord.storeName = backOrder.getStoreName();
        billingRecord.recordType = BillingRecordTypeEnum.BACK_ORDER_COMMISSION.getCode();
        billingRecord.orderPrice = backOrder.getRealBackPrice();
        billingRecord.orderCommission = calcOrderCommissionForRetrun(backOrder.getOrderSkus(), backOrder.getRealBackPrice());
        billingRecord.orderActualPrice = billingRecord.orderCommission;
        return billingRecord;
    }

    /**
     * 构造退货的账单支出记录
     *
     * @param backOrder 退款信息
     * @return 返回账单信息
     */
    public static OmsBillingRecords buildForReturnOrderExpenditure(OmsBackOrder backOrder) {

        if (Objects.isNull(backOrder)) {
            return null;
        }

        // 账单记录
        OmsBillingRecords billingRecord = new OmsBillingRecords();
        billingRecord.orderCode = backOrder.getBackCode();
        billingRecord.type = BillingDirectionEnum.OUTCOME.getCode();
        billingRecord.storeId = backOrder.getStoreId();
        billingRecord.storeName = backOrder.getStoreName();
        billingRecord.recordType = BillingRecordTypeEnum.BACK_ORDER.getCode();
        billingRecord.orderBackPrice = backOrder.getRealBackPrice();
        billingRecord.orderActualPrice = BigDecimal.ZERO.subtract(billingRecord.orderBackPrice);
        return billingRecord;
    }


    /**
     * 构造取消预售订单的账单记录
     *
     * @param order 订单
     * @return 返回预售订单的账单记录
     */
    public static OmsBillingRecords buildForCanclePresaleOrder(OmsOrder order) {
        if (Objects.isNull(order)) {
            return null;
        }
        // 账单记录
        OmsBillingRecords billingRecord = new OmsBillingRecords();
        billingRecord.orderCode = order.getOrderCode();
        billingRecord.type = BillingDirectionEnum.INCOME.getCode();
        billingRecord.storeId = order.getStoreId();
        billingRecord.storeName = order.getStoreName();
        billingRecord.recordType = BillingRecordTypeEnum.CLOSE_ORDER.getCode();
        billingRecord.orderPrice = order.getPrice().subtract(order.getPresalePrice());
        // 平台订单不需要佣金
        if (order.isPlatform()) {
            billingRecord.orderCommission = BigDecimal.ZERO;
        } else {
            billingRecord.orderCommission = calcOrderCommission(order.getOrderSkus());
        }
        billingRecord.orderActualPrice = billingRecord.getOrderPrice().subtract(billingRecord.getOrderCommission());
        return billingRecord;
    }

    /**
     * 计算订单佣金
     *
     * @param orderSkus 订单下的单品信息
     * @return 返回订单佣金
     */
    private static BigDecimal calcOrderCommission(List<OmsOrderSku> orderSkus) {
        return orderSkus.stream().map(orderSku -> orderSku.getPrice().multiply(orderSku.getCateRate())).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 计算退货订单佣金
     *
     * @param orderSkus 退单下的的单品信息
     * @param realPrice 实腿金额
     * @return 返回订单佣金
     */
    private static BigDecimal calcOrderCommissionForRetrun(List<OmsOrderSku> orderSkus, BigDecimal realPrice) {

        if (CollectionUtils.isEmpty(orderSkus)) {
            return BigDecimal.ZERO;
        }

        // 如果只有一个单品的话 直接实退金额*佣金比例
        if (orderSkus.size() == 1) {
            return realPrice.multiply(orderSkus.get(0).getCateRate());
        } else {

            // 退货单品的总价格
            BigDecimal allPrice = orderSkus.stream().map(orderSku -> orderSku.getUnitPrice().multiply(new BigDecimal(orderSku.getReturnNum()))).reduce(BigDecimal.ZERO, BigDecimal::add);

            // 否则多余一个单品的话 则算实际退单金额分摊到每个单品的金额*佣金比例
            return orderSkus.stream().map(orderSku -> {
                return (orderSku.getUnitPrice().multiply(new BigDecimal(orderSku.getReturnNum())).divide(allPrice, 10, RoundingMode.HALF_EVEN)).multiply(realPrice).multiply(orderSku.getCateRate()).setScale(2, RoundingMode.HALF_EVEN);
            }).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }
}

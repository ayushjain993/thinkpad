package io.uhha.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.uhha.coin.common.match.MathUtils;
import io.uhha.common.annotation.Excel;
import io.uhha.common.core.domain.BaseEntity;
import io.uhha.common.enums.TransTypeEnum;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 会员预存款记录对象 ums_pre_deposit_record
 *
 * @author mj
 * @date 2020-07-25
 */
@Data
public class UmsPreDepositRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 会员id
     */
    @Excel(name = "会员id")
    private Long customerId;

    /**
     * 交易类型  1:在线充值  2:订单消费  3:订单退款 4:管理员增加 5:管理员减少 6:手续费转余额 7:余额提款
     * @see io.uhha.common.enums.TransTypeEnum
     */
    @Excel(name = "交易类型  1:在线充值  2:订单消费  3:订单退款 4:管理员增加 5:管理员减少 6:手续费转余额 7:余额提款")
    private String transType;

    /**
     * 金额
     */
    @Excel(name = "金额")
    private BigDecimal money;

    /**
     * 当前总金额
     */
    @Excel(name = "当前总金额")
    private BigDecimal currentMoney;

    /**
     * 冻结金额
     */
    @Excel(name = "冻结金额")
    private BigDecimal frozen;

    /**
     * 充值支付状态 0 未支付 1 支付成功
     */
    @Excel(name = "充值支付状态 0 未支付 1 支付成功")
    private String status;

    private String remark;

    /**
     * 交易号
     */
    @Excel(name = "交易号")
    private String transcode;

    /**
     * 构造充值的实体
     *
     * @param money      金额
     * @param customerId 用户id
     * @param channel    支付渠道
     * @param randomCode 随机数
     */
    public static UmsPreDepositRecord buildRecharge(BigDecimal money, long customerId, String channel, long randomCode) {
        UmsPreDepositRecord predepositRecord = new UmsPreDepositRecord();
        predepositRecord.money = money;
        predepositRecord.customerId = customerId;
        predepositRecord.transType = TransTypeEnum.ONLINE_DEPOSIT.getCode();
        predepositRecord.transcode = "Re" + randomCode;
        predepositRecord.status = "0";
        predepositRecord.remark = "在线充值-" + channel;
        return predepositRecord;
    }

    /**
     * 构造手续费转账为余额实体
     *
     * @param money      金额
     * @param customerId 用户id
     * @param randomCode 随机数
     */
    public static UmsPreDepositRecord buildCommissionTransfer(BigDecimal money, long customerId, long randomCode) {
        UmsPreDepositRecord predepositRecord = new UmsPreDepositRecord();
        predepositRecord.money = money;
        predepositRecord.customerId = customerId;
        predepositRecord.transType = TransTypeEnum.COMMISSION2BALANCE.getCode();
        predepositRecord.transcode = "Re" + randomCode;
        predepositRecord.status = "1";
        predepositRecord.remark = "推广手续费转余额";
        return predepositRecord;
    }

    /**
     * 构造手续费转账为余额实体
     *
     * @param money      金额
     * @param customerId 用户id
     * @param randomCode 随机数
     */
    public static UmsPreDepositRecord buildWithdraw(BigDecimal money, long customerId, long randomCode) {
        UmsPreDepositRecord predepositRecord = new UmsPreDepositRecord();
        predepositRecord.money = money;
        predepositRecord.customerId = customerId;
        predepositRecord.transType = TransTypeEnum.WITHDRAW.getCode();
        predepositRecord.transcode = "Wi" + randomCode;
        predepositRecord.status = "0";
        predepositRecord.remark = "提现";
        return predepositRecord;
    }

    /**
     * 构造手续费转账为余额失败实体
     *
     * @param money      金额
     * @param customerId 用户id
     * @param transcode 单号
     */
    public static UmsPreDepositRecord buildWithdrawFail(BigDecimal money, long customerId, String transcode) {
        UmsPreDepositRecord predepositRecord = new UmsPreDepositRecord();
        predepositRecord.money = money;
        predepositRecord.customerId = customerId;
        predepositRecord.transType = TransTypeEnum.WITHDRAW_FAIL.getCode();
        predepositRecord.transcode = transcode;
        predepositRecord.status = "1";
        predepositRecord.remark = "提现失败";
        return predepositRecord;
    }

    /**
     * 构造手续费转账为余额失败实体
     *
     * @param money      金额
     * @param customerId 用户id
     * @param transcode 单号
     */
    public static UmsPreDepositRecord buildWithdrawRelease(BigDecimal money, long customerId, String transcode) {
        UmsPreDepositRecord predepositRecord = new UmsPreDepositRecord();
        predepositRecord.money = money;
        predepositRecord.customerId = customerId;
        predepositRecord.transType = TransTypeEnum.WITHDRAW_FINISHED.getCode();
        predepositRecord.transcode = transcode;
        predepositRecord.status = "1";
        predepositRecord.remark = "已打款";
        return predepositRecord;
    }

    /**
     * 构造管理员操作的实体
     *
     * @param money      金额
     * @param customerId 用户id
     * @param randomCode 随机数
     */
    public static UmsPreDepositRecord buildManageChange(BigDecimal money, long customerId, long randomCode) {
        UmsPreDepositRecord predepositRecord = new UmsPreDepositRecord();
        predepositRecord.customerId = customerId;

        predepositRecord.transcode = "Re" + randomCode;
        predepositRecord.status = "1";
        if (money.compareTo(new BigDecimal(0)) > 0) {
            predepositRecord.transType = TransTypeEnum.ADMIN_ADD.getCode();
            predepositRecord.remark = "管理员增加";
        } else {
            predepositRecord.transType = TransTypeEnum.ADMIN_REDUCE.getCode();
            predepositRecord.remark = "管理员减少";
        }
        predepositRecord.money = money.abs();
        return predepositRecord;
    }

    /**
     * 构造支付的实体
     *
     * @param money      金额
     * @param customerId 用户id
     */
    public static UmsPreDepositRecord buildPay(BigDecimal money, long customerId) {
        UmsPreDepositRecord predepositRecord = new UmsPreDepositRecord();
        predepositRecord.money = money;
        predepositRecord.customerId = customerId;
        predepositRecord.transType = TransTypeEnum.ORDER_CONSUME.getCode();
        predepositRecord.transcode = "Re" + new SimpleDateFormat("yyyyMMddHHmmss", Locale.UK).format(new Date());
        predepositRecord.status = "1";
        predepositRecord.remark = "订单支付";
        return predepositRecord;
    }

    /**
     * 构造订单退单的实体
     *
     * @param money      退单金额
     * @param customerId 用户id
     * @return 返回预存款实体
     */
    public static UmsPreDepositRecord buildOrderBack(BigDecimal money, long customerId) {
        UmsPreDepositRecord predepositRecord = new UmsPreDepositRecord();
        predepositRecord.money = money;
        predepositRecord.customerId = customerId;
        predepositRecord.transType = TransTypeEnum.ORDER_REFUND.getCode();
        predepositRecord.transcode = "Re" + new SimpleDateFormat("yyyyMMddHHmmss", Locale.UK).format(new Date());
        predepositRecord.status = "1";
        predepositRecord.remark = "订单退款";
        return predepositRecord;
    }

    /**
     * 是否是收入
     */
    @JsonIgnore
    public boolean isIncome() {
        return TransTypeEnum.ONLINE_DEPOSIT.getCode().equals(this.transType)
                || TransTypeEnum.ORDER_REFUND.getCode().equals(this.transType)
                || TransTypeEnum.ADMIN_ADD.getCode().equals(this.transType)
                || TransTypeEnum.COMMISSION2BALANCE.getCode().equals(this.transType)
                || TransTypeEnum.WITHDRAW_FAIL.getCode().equals(this.transType)
        ;
    }

    /**
     * 是否是支出
     */
    @JsonIgnore
    public boolean isExpenditure() {
        return TransTypeEnum.ORDER_CONSUME.getCode().equals(this.transType)
                || TransTypeEnum.ADMIN_REDUCE.getCode().equals(this.transType)
                || TransTypeEnum.WITHDRAW.getCode().equals(this.transType)
                ;
    }

    /**
     * 判断是否支付 已支付返回true
     */
    @JsonIgnore
    public boolean isPaid() {
        return TransTypeEnum.ONLINE_DEPOSIT.getCode().equals(this.status);
    }

    /**
     * 判断当前金额是否小于0
     */
    @JsonIgnore
    public boolean isCurrentMoneyMinus() {
        if(this.currentMoney!=null) {
            return this.currentMoney.compareTo(BigDecimal.ZERO) < 0;
        }
        return false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getCurrentMoney() {
        return currentMoney;
    }

    public void setCurrentMoney(BigDecimal currentMoney) {
        this.currentMoney = currentMoney;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTranscode() {
        return transcode;
    }

    public void setTranscode(String transcode) {
        this.transcode = transcode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("customerId", getCustomerId())
                .append("transType", getTransType())
                .append("money", getMoney())
                .append("currentMoney", getCurrentMoney())
                .append("remark", getRemark())
                .append("status", getStatus())
                .append("transcode", getTranscode())
                .append("createTime", getCreateTime())
                .toString();
    }
}

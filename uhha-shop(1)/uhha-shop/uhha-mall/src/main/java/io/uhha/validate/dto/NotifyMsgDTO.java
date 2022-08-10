package io.uhha.validate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * 消息发送数据传输对象
 * Created by ZKF on 2017/5/4.
 */
@Builder
@AllArgsConstructor
public class NotifyMsgDTO implements Serializable {

    private static final long serialVersionUID = 6833810848619586287L;
    /**
     * 平台
     * @see io.uhha.coin.common.Enum.validate.PlatformEnum
     */
    private Integer platformType;
    /**
     *    发送类型
     *    @see io.uhha.coin.common.Enum.validate.SendTypeEnum
     */
    private Integer sendType;

    /**
     * 业务类型
     * @see io.uhha.coin.common.Enum.validate.BusinessTypeEnum
      */
    private Integer businessType;
    /**
     * 语言类型
     * @see io.uhha.coin.common.Enum.validate.LocaleEnum
     */
    private Integer languageType;
    // UUID
    private String uuid;
    // 验证码
    private String code;

    // 扩展参数
    // 价格
    private BigDecimal price;
    // 用户价格
    private BigDecimal userPrice;
    // 数量
    private BigDecimal amount;
    // 用户id
    private Long uid;
    // 充提类型
    private String type;
    // 币种
    private String coin;
    // 手机
    private String phone;
    // 邮箱
    private String email;
    // 用户登录名
    private String username;
    // 用户IP
    private String ip;
    //订单号
    private String orderNo;

    //系统使用版本
    private Integer systemVersion;

    /**
     * 运单号
     */
    private String waybillCode;

    /**
     * 视频标题
     */
    private String videoTitle;

    /**
     * 管理员审批意见
     */
    private String reason;

    /**
     * 视频评论
     */
    private String comment;


    /**
     * 快递公司名称
     */
    private String logisticCompanyName;

    public NotifyMsgDTO() {
        this.uuid = UUID.randomUUID().toString();
    }

    public Integer getPlatformType() {
        return platformType;
    }

    public void setPlatformType(Integer platformType) {
        this.platformType = platformType;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public Integer getLanguageType() {
        return languageType;
    }

    public void setLanguageType(Integer languageType) {
        this.languageType = languageType;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getUserPrice() {
        return userPrice;
    }

    public void setUserPrice(BigDecimal userPrice) {
        this.userPrice = userPrice;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCoin() {
        return coin;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(Integer systemVersion) {
        this.systemVersion = systemVersion;
    }

    public String getWaybillCode() {
        return waybillCode;
    }

    public void setWaybillCode(String waybillCode) {
        this.waybillCode = waybillCode;
    }

    public String getLogisticCompanyName() {
        return logisticCompanyName;
    }

    public void setLogisticCompanyName(String logisticCompanyName) {
        this.logisticCompanyName = logisticCompanyName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "ValidateSendDTO{" +
                "platformType=" + platformType +
                ", sendType=" + sendType +
                ", businessType=" + businessType +
                ", languageType=" + languageType +
                ", uuid='" + uuid + '\'' +
                ", code='" + code + '\'' +
                ", price=" + price +
                ", userPrice=" + userPrice +
                ", amount=" + amount +
                ", uid=" + uid +
                ", type='" + type + '\'' +
                ", coin='" + coin + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", ip='" + ip + '\'' +
                ", orderNo='" + orderNo + '\'' +
                '}';
    }
}

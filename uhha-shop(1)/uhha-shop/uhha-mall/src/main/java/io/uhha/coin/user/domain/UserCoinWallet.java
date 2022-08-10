package io.uhha.coin.user.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户钱包
 * @author LY
 *
 */
public class UserCoinWallet implements Serializable{
	
	private static final long serialVersionUID = 1L;
	// 主键ID
	private Integer id;
	// 用户ID
    private Long uid;
    // 币种ID
    private Integer coinId;
    // 可用
    private BigDecimal total;
    // 冻结
    private BigDecimal frozen;
    // 理财
    private BigDecimal borrow;
    // ico
    private BigDecimal ico;
    // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date gmtCreate;
    // 更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date gmtModified;
    
    // 扩展字段
    private String loginName;
    private String nickName;
    private String realName;
    private String coinName;
    private String shortName;
    private String logo;

    private BigDecimal sumNumber;
    private int countuid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getCoinId() {
        return coinId;
    }

    public void setCoinId(Integer coinId) {
        this.coinId = coinId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getFrozen() {
        return frozen;
    }

    public void setFrozen(BigDecimal frozen) {
        this.frozen = frozen;
    }

    public BigDecimal getBorrow() {
        return borrow;
    }

    public void setBorrow(BigDecimal borrow) {
        this.borrow = borrow;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public BigDecimal getIco() {
        return ico;
    }

    public void setIco(BigDecimal ico) {
        this.ico = ico;
    }

    public BigDecimal getSumNumber() {
        return sumNumber;
    }

    public void setSumNumber(BigDecimal sumNumber) {
        this.sumNumber = sumNumber;
    }

    public int getCountuid() {
        return countuid;
    }

    public void setCountuid(int countuid) {
        this.countuid = countuid;
    }

    @Override
    public String toString() {
        return "UserCoinWallet{" +
                "id=" + id +
                ", uid=" + uid +
                ", coinId=" + coinId +
                ", total=" + total +
                ", frozen=" + frozen +
                ", borrow=" + borrow +
                ", ico=" + ico +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", loginName='" + loginName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", realName='" + realName + '\'' +
                ", coinName='" + coinName + '\'' +
                ", shortName='" + shortName + '\'' +
                ", logo='" + logo + '\'' +
                '}';
    }
}
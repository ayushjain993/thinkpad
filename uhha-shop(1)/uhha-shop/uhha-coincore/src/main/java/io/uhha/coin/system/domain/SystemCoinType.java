package io.uhha.coin.system.domain;

import io.uhha.coin.common.Enum.SystemCoinSortEnum;
import io.uhha.coin.common.Enum.SystemCoinStatusEnum;
import io.uhha.coin.common.Enum.SystemCoinTypeEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * 币种类型表
 */
public class SystemCoinType implements Serializable {
    private static final long serialVersionUID = 1L;
    // 主键ID
    private Integer id;
    // 排序
    private Integer sortId;
    // 名称
    private String name;
    // 简称
    private String shortName;
    // 符号
    private String symbol;
    // 类型
    private Integer type;
    /**
     * 钱包类型
     * @see SystemCoinSortEnum
      */
    private Integer coinType;
    /**
     *  状态
     * @see SystemCoinStatusEnum
     */
    private Integer status;
    // 是否提现
    private Boolean isWithdraw;
    // 是否充值
    private Boolean isRecharge;
    // 风控阀值
    private BigDecimal riskNum;
    // 是否PUSH
    private Boolean isPush;
    // 是否理财
    private Boolean isFinances;
    // 钱包链接IP
    private String ip;
    // 钱包链接端口
    private String port;
    // 钱包链接user
    private String accessKey;
    // 钱包链接pass
    private String secrtKey;
    // 钱包keystore
    private String keystore;
    // ETH账户
    private String ethAccount;

    //ERC20合约地址
    private String contractAddress;
    // 资产ID
    private BigInteger assetId;
    // 网络手续费
    private BigDecimal networkFee;
    // 充值到账确认数(可在后台进行设置)
    private Integer confirmations;
    // WEB站LOGO
    private String webLogo;
    // APP站LOGO
    private String appLogo;
    // 创建时间
    private Date gmtCreate;
    // 更新时间
    private Date gmtModified;
    // 硬币简介
    private String coinIntroduce;
    // 版本号
    private Integer version;
    //
    private Long fadminid;
    //币种优势
    private String coinAdvantage;
    // 币种名称全称
    private String fullName;
    // 扩展字段
    private String typeName;
    private String statusName;
    private String coinTypeName;
    private String fadminname;
    // 附加字段1:最小充值额度
    private BigDecimal minCount;

    public String getFadminname() {
        return fadminname;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSortId() {
        return sortId;
    }

    public void setSortId(Integer sortId) {
        this.sortId = sortId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCoinType() {
        return coinType;
    }

    public void setCoinType(Integer coinType) {
        this.coinType = coinType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getIsWithdraw() {
        return isWithdraw;
    }

    public void setIsWithdraw(Boolean withdraw) {
        isWithdraw = withdraw;
    }

    public Boolean getIsRecharge() {
        return isRecharge;
    }

    public void setIsRecharge(Boolean recharge) {
        isRecharge = recharge;
    }

    public BigDecimal getRiskNum() {
        return riskNum;
    }

    public void setRiskNum(BigDecimal riskNum) {
        this.riskNum = riskNum;
    }

    public Boolean getIsPush() {
        return isPush;
    }

    public void setIsPush(Boolean push) {
        isPush = push;
    }

    public Boolean getIsFinances() {
        return isFinances;
    }

    public void setIsFinances(Boolean finances) {
        isFinances = finances;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecrtKey() {
        return secrtKey;
    }

    public void setSecrtKey(String secrtKey) {
        this.secrtKey = secrtKey;
    }

    public BigInteger getAssetId() {
        return assetId;
    }

    public void setAssetId(BigInteger assetId) {
        this.assetId = assetId;
    }

    public BigDecimal getNetworkFee() {
        return networkFee;
    }

    public void setNetworkFee(BigDecimal networkFee) {
        this.networkFee = networkFee;
    }

    public Integer getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(Integer confirmations) {
        this.confirmations = confirmations;
    }

    public String getWebLogo() {
        return webLogo;
    }

    public void setWebLogo(String webLogo) {
        this.webLogo = webLogo;
    }

    public String getAppLogo() {
        return appLogo;
    }

    public void setAppLogo(String appLogo) {
        this.appLogo = appLogo;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getTypeName() {
        return SystemCoinTypeEnum.getValueByCode(this.type);
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getStatusName() {
        return SystemCoinStatusEnum.getValueByCode(this.status);
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getCoinTypeName() {
        return SystemCoinSortEnum.getValueByCode(this.coinType);
    }

    public void setCoinTypeName(String coinTypeName) {
        this.coinTypeName = coinTypeName;
    }

    public String getEthAccount() {
        return ethAccount;
    }

    public void setEthAccount(String ethAccount) {
        this.ethAccount = ethAccount;
    }

    public String getCoinIntroduce() {
        return coinIntroduce;
    }

    public void setCoinIntroduce(String coinIntroduce) {
        this.coinIntroduce = coinIntroduce;
    }


    public Long getFadminid() {
        return fadminid;
    }

    public void setFadminid(Long fadminid) {
        this.fadminid = fadminid;
    }

    public String getCoinAdvantage() {
        return coinAdvantage;
    }

    public void setCoinAdvantage(String coinAdvantage) {
        this.coinAdvantage = coinAdvantage;
    }

    public String getFullName() { return fullName; }

    public void setFullName(String fullName) { this.fullName = fullName; }

	public BigDecimal getMinCount() {
		return minCount;
	}

	public void setMinCount(BigDecimal minCount) {
		this.minCount = minCount;
	}

    public String getKeystore() {
        return keystore;
    }

    public void setKeystore(String keystore) {
        this.keystore = keystore;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    @Override
    public String toString() {
        return "SystemCoinType{" +
                "id=" + id +
                ", sortId=" + sortId +
                ", name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", symbol='" + symbol + '\'' +
                ", type=" + type +
                ", coinType=" + coinType +
                ", status=" + status +
                ", isWithdraw=" + isWithdraw +
                ", isRecharge=" + isRecharge +
                ", riskNum=" + riskNum +
                ", isPush=" + isPush +
                ", isFinances=" + isFinances +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", accessKey='" + accessKey + '\'' +
                ", secrtKey='" + secrtKey + '\'' +
                ", keystore='" + keystore + '\'' +
                ", ethAccount='" + ethAccount + '\'' +
                ", contractAddress='" + contractAddress + '\'' +
                ", assetId=" + assetId +
                ", networkFee=" + networkFee +
                ", confirmations=" + confirmations +
                ", webLogo='" + webLogo + '\'' +
                ", appLogo='" + appLogo + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", coinIntroduce='" + coinIntroduce + '\'' +
                ", version=" + version +
                ", fadminid=" + fadminid +
                ", coinAdvantage='" + coinAdvantage + '\'' +
                ", fullName='" + fullName + '\'' +
                ", typeName='" + typeName + '\'' +
                ", statusName='" + statusName + '\'' +
                ", coinTypeName='" + coinTypeName + '\'' +
                ", fadminname='" + fadminname + '\'' +
                ", minCount=" + minCount +
                '}';
    }
}
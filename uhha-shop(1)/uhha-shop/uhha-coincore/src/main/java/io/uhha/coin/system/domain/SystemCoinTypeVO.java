package io.uhha.coin.system.domain;

import io.uhha.coin.common.Enum.SystemCoinSortEnum;
import io.uhha.coin.common.Enum.SystemCoinStatusEnum;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 币种表前端展示实体
 * @author LY
 *
 */
public class SystemCoinTypeVO implements Serializable{
	private static final long serialVersionUID = 1L;
	
    private Integer id;

    private String name;
	// 类型
	private Integer type;

    private String shortname;

    private String weblogo;

    private String applogo;

    private String coinIntroduce;

	//币种优势
	private String coinAdvantage;

    private String symbol;
	// 是否提现
	private Boolean isWithdraw;
	// 是否充值
	private Boolean isRecharge;

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

	//网络手续费
	private BigDecimal networkFee;

	/**
	 * 未使用地址数
	 */
	private Integer notused;

	/**
	 * 使用地址数
	 */
	private Integer used;


	public String getCoinIntroduce() {
		return coinIntroduce;
	}

	public void setCoinIntroduce(String coinIntroduce) {
		this.coinIntroduce = coinIntroduce;
	}

	public BigDecimal getNetworkFee() {
		return networkFee;
	}

	public void setNetworkFee(BigDecimal networkFee) {
		this.networkFee = networkFee;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public String getWeblogo() {
		return weblogo;
	}

	public void setWeblogo(String weblogo) {
		this.weblogo = weblogo;
	}

	public String getApplogo() {
		return applogo;
	}

	public void setApplogo(String applogo) {
		this.applogo = applogo;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCoinAdvantage() {
		return coinAdvantage;
	}

	public void setCoinAdvantage(String coinAdvantage) {
		this.coinAdvantage = coinAdvantage;
	}

	public Integer getUsed() {
		return used;
	}

	public void setUsed(Integer used) {
		this.used = used;
	}

	public Integer getNotused() {
		return notused;
	}

	public void setNotused(Integer notused) {
		this.notused = notused;
	}

	public Integer getCoinType() {
		return coinType;
	}

	public void setCoinType(Integer coinType) {
		this.coinType = coinType;
	}
}
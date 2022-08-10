package io.uhha.coin.system.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 交易列表
 * 
 * @author jany
 */
public class SystemTradeTypeVO implements Serializable {
	private static final long serialVersionUID = 1726766727357266668L;
	// 主键id
	private Integer id;
    // 交易类型
    private Integer type;
    // 买方id
    private Integer buyCoinId;
    private String buySymbol;
	private String buyName;
    private String buyShortName;
    private String buyWebLogo;
    // 卖方id
    private Integer sellCoinId;
    private String sellSymbol;
	private String sellName;
    private String sellShortName;
    private String sellWebLogo;
    // 是否交易
    private Boolean isShare;
	// 价格波动
	private BigDecimal priceWave;
	// 最小下单
	private BigDecimal minCount;
	// 最大下单
	private BigDecimal maxCount;
	// 最小价格
	private BigDecimal minPrice;
	// 最大价格
	private BigDecimal maxPrice;
	// 数量偏移百分比
	private String amountOffset;
	// 价格偏移量
	private String priceOffset;
	// 交易小数位
	private String digit;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBuyCoinId() {
		return buyCoinId;
	}

	public void setBuyCoinId(Integer buyCoinId) {
		this.buyCoinId = buyCoinId;
	}

	public Integer getSellCoinId() {
		return sellCoinId;
	}

	public void setSellCoinId(Integer sellCoinId) {
		this.sellCoinId = sellCoinId;
	}

	public BigDecimal getPriceWave() {
		return priceWave;
	}

	public void setPriceWave(BigDecimal priceWave) {
		this.priceWave = priceWave;
	}

	public BigDecimal getMinCount() {
		return minCount;
	}

	public void setMinCount(BigDecimal minCount) {
		this.minCount = minCount;
	}

	public BigDecimal getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(BigDecimal maxCount) {
		this.maxCount = maxCount;
	}

	public BigDecimal getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}

	public BigDecimal getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}

	public String getAmountOffset() {
		return amountOffset;
	}

	public void setAmountOffset(String amountOffset) {
		this.amountOffset = amountOffset;
	}

	public String getPriceOffset() {
		return priceOffset;
	}

	public void setPriceOffset(String priceOffset) {
		this.priceOffset = priceOffset;
	}

	public String getDigit() {
		return digit;
	}

	public void setDigit(String digit) {
		this.digit = digit;
	}

	public String getBuySymbol() {
		return buySymbol;
	}

	public void setBuySymbol(String buySymbol) {
		this.buySymbol = buySymbol;
	}

	public String getBuyShortName() {
		return buyShortName;
	}

	public void setBuyShortName(String buyShortName) {
		this.buyShortName = buyShortName;
	}

	public String getSellSymbol() {
		return sellSymbol;
	}

	public void setSellSymbol(String sellSymbol) {
		this.sellSymbol = sellSymbol;
	}

	public String getSellShortName() {
		return sellShortName;
	}

	public void setSellShortName(String sellShortName) {
		this.sellShortName = sellShortName;
	}

	public Boolean getIsShare() {
		return isShare;
	}

	public void setIsShare(Boolean isShare) {
		this.isShare = isShare;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getBuyWebLogo() {
		return buyWebLogo;
	}

	public void setBuyWebLogo(String buyWebLogo) {
		this.buyWebLogo = buyWebLogo;
	}

	public String getSellWebLogo() {
		return sellWebLogo;
	}

	public void setSellWebLogo(String sellWebLogo) {
		this.sellWebLogo = sellWebLogo;
	}

	public String getBuyName() {
		return buyName;
	}

	public void setBuyName(String buyName) {
		this.buyName = buyName;
	}

	public String getSellName() {
		return sellName;
	}

	public void setSellName(String sellName) {
		this.sellName = sellName;
	}

}
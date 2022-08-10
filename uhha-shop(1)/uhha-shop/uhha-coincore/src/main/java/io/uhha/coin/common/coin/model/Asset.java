package io.uhha.coin.common.coin.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Asset implements Serializable {
	
	private String name;
	private String assetref;
	private BigDecimal qty;

	public String getAssetref() {
		return assetref;
	}
	public void setAssetref(String assetref) {
		this.assetref = assetref;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}
}
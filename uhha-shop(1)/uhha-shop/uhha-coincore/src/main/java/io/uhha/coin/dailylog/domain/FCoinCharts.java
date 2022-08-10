package io.uhha.coin.dailylog.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class FCoinCharts implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String coinShortName;
	private BigDecimal free;
	private BigDecimal borrow;
	private BigDecimal frozen;
	private BigDecimal total;
	public BigDecimal getFree() {
		return free;
	}
	public void setFree(BigDecimal free) {
		this.free = free;
	}
	public BigDecimal getFrozen() {
		return frozen;
	}
	public void setFrozen(BigDecimal frozen) {
		this.frozen = frozen;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getBorrow() {
		return borrow;
	}

	public void setBorrow(BigDecimal borrow) {
		this.borrow = borrow;
	}

	public String getCoinShortName() {
		return coinShortName;
	}

	public void setCoinShortName(String coinShortName) {
		this.coinShortName = coinShortName;
	}
}

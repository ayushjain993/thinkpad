package io.uhha.coin.common.dto.coin;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 币种总数统计
 * @author ZGY
 */
public class FCoinSumLog implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	// 主键ID
	private Integer fid;
	// 虚拟币名称
	private String coinShortName;
	// 虚拟币id
	private Integer coinid;
	// 可用
	private BigDecimal total;
	// 冻结
	private BigDecimal frozen;
	// 理财
	private BigDecimal borrow;
	// 总数
	private BigDecimal coinsum;
	// 创建时间
	private Date fcreatetime;

	public Integer getFid() {
		return fid;
	}

	public void setFid(Integer fid) {
		this.fid = fid;
	}

	public String getCoinShortName() {
		return coinShortName;
	}

	public void setCoinShortName(String coinShortName) {
		this.coinShortName = coinShortName;
	}

	public Integer getCoinid() {
		return coinid;
	}

	public void setCoinid(Integer coinid) {
		this.coinid = coinid;
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

	public BigDecimal getCoinsum() {
		return coinsum;
	}

	public void setCoinsum(BigDecimal coinsum) {
		this.coinsum = coinsum;
	}

	public Date getFcreatetime() {
		return fcreatetime;
	}

	public void setFcreatetime(Date fcreatetime) {
		this.fcreatetime = fcreatetime;
	}
}
package io.uhha.coin.capital.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 虚拟币最小充值记录
 * 
 * @author lgp
 */
public class QVirtualCapitalSmall implements Serializable {
	private static final long serialVersionUID = 1L;
	//
	private Integer qid;
	// 虚拟币ID
	private Integer qcoinid;
	// 数量
	private BigDecimal qamount;
	// 类型
	private Integer qtype;
	// 充值地址
	private String qrechargeaddress;
	// 交易ID
	private String quniquenumber;
	// 创建时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date qcreatetime;
	// Tx时间
	private Date txTime;
	// 随机数(ETC)
	private Integer qnonce;
	// 来源
	private Integer qsource;
	// 订单来源平台
	private Integer qplatform;

	public Integer getQid() {
		return qid;
	}

	public void setQid(Integer qid) {
		this.qid = qid;
	}

	public Integer getQcoinid() {
		return qcoinid;
	}

	public void setQcoinid(Integer qcoinid) {
		this.qcoinid = qcoinid;
	}

	public BigDecimal getQamount() {
		return qamount;
	}

	public void setQamount(BigDecimal qamount) {
		this.qamount = qamount;
	}

	public Integer getQtype() {
		return qtype;
	}

	public void setQtype(Integer qtype) {
		this.qtype = qtype;
	}

	public String getQrechargeaddress() {
		return qrechargeaddress;
	}

	public void setQrechargeaddress(String qrechargeaddress) {
		this.qrechargeaddress = qrechargeaddress;
	}

	public String getQuniquenumber() {
		return quniquenumber;
	}

	public void setQuniquenumber(String quniquenumber) {
		this.quniquenumber = quniquenumber;
	}

	public Date getQcreatetime() {
		return qcreatetime;
	}

	public void setQcreatetime(Date qcreatetime) {
		this.qcreatetime = qcreatetime;
	}

	public Date getTxTime() {
		return txTime;
	}

	public void setTxTime(Date txTime) {
		this.txTime = txTime;
	}

	public Integer getQnonce() {
		return qnonce;
	}

	public void setQnonce(Integer qnonce) {
		this.qnonce = qnonce;
	}

	public Integer getQsource() {
		return qsource;
	}

	public void setQsource(Integer qsource) {
		this.qsource = qsource;
	}

	public Integer getQplatform() {
		return qplatform;
	}

	public void setQplatform(Integer qplatform) {
		this.qplatform = qplatform;
	}
}
package io.uhha.coin.user.domain;

import java.io.Serializable;
import java.util.Date;

import io.uhha.coin.common.Enum.CountLimitTypeEnum;

/**
 * 用户限制表
 * @author LY
 */
public class FLogCountlimit implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	// 主键ID
	private Integer fid;
	// IP地址
	private String fip;
	// 创建时间
	private Date fcreatetime;
	// 数量
	private Integer fcount;
	// 类型
	private Integer ftype;
	// 版本号
	private Integer version;
	
	private String ftype_s;

	public Integer getFid() {
		return fid;
	}

	public void setFid(Integer fid) {
		this.fid = fid;
	}

	public String getFip() {
		return fip;
	}

	public void setFip(String fip) {
		this.fip = fip == null ? null : fip.trim();
	}

	public Date getFcreatetime() {
		return fcreatetime;
	}

	public void setFcreatetime(Date fcreatetime) {
		this.fcreatetime = fcreatetime;
	}

	public Integer getFcount() {
		return fcount;
	}

	public void setFcount(Integer fcount) {
		this.fcount = fcount;
	}

	public Integer getFtype() {
		return ftype;
	}

	public void setFtype(Integer ftype) {
		this.ftype = ftype;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getFtype_s() {
		return CountLimitTypeEnum.getEnumString(ftype);
	}

	public void setFtype_s(String ftype_s) {
		this.ftype_s = ftype_s;
	}
}
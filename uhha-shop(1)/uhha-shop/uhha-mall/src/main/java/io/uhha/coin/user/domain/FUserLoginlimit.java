package io.uhha.coin.user.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户登陆限制表
 * @author LY
 */
public class FUserLoginlimit implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	// 主键ID
	private Integer fid;
	// 用户ip
	private Integer fuid;
	// IP地址
	private String fip;
	// 创建时间
	private Date fcreatetime;
	// 数量
	private Integer fcount;
	// 版本号
	private Integer version;


	public Integer getFid() {
		return fid;
	}

	public void setFid(Integer fid) {
		this.fid = fid;
	}

	public Integer getFuid() {
		return fuid;
	}

	public void setFuid(Integer fuid) {
		this.fuid = fuid;
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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
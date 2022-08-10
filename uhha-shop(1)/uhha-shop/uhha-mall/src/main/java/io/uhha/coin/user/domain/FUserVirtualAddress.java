package io.uhha.coin.user.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户虚拟币充值地址
 * @author LY
 */
public class FUserVirtualAddress implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	// 主键ID
	private Integer fid;
	// 虚拟币ID
	private Integer fcoinid;
	// 虚拟币地址
	private String fadderess;
	// 用户ID
	private Long fuid;
	// 创建时间
	private Date fcreatetime;
	
	private String fshortname;

	public Integer getFid() {
		return fid;
	}

	public void setFid(Integer fid) {
		this.fid = fid;
	}

	public Integer getFcoinid() {
		return fcoinid;
	}

	public void setFcoinid(Integer fcoinid) {
		this.fcoinid = fcoinid;
	}

	public String getFadderess() {
		return fadderess;
	}

	public void setFadderess(String fadderess) {
		this.fadderess = fadderess == null ? null : fadderess.trim();
	}

	public Long getFuid() {
		return fuid;
	}

	public void setFuid(Long fuid) {
		this.fuid = fuid;
	}

	public Date getFcreatetime() {
		return fcreatetime;
	}

	public void setFcreatetime(Date fcreatetime) {
		this.fcreatetime = fcreatetime;
	}

	public String getFshortname() {
		return fshortname;
	}

	public void setFshortname(String fshortname) {
		this.fshortname = fshortname;
	}
	
	
}
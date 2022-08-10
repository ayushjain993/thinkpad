package io.uhha.coin.user.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户银行卡列表
 * @author LY
 */
public class FUserBankinfo implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	private Integer fid;
	/**
	 * 用户ID
	 */
	private Long fuid;
	/**
	 * 银行卡名称
	 */
	private String fname;

	/**
	 * 内部银行id
	 */
	private Long fbankid;
	/**
	 * 银行卡号
	 */
	private String fbanknumber;
	/**
	 * 银行卡类型, 0 信用卡 1 储蓄卡
	 */
	private Integer fbanktype;
	
	private String fbanktype_s;
	/**
	 * 创建时间
	 */
	private Date fcreatetime;
	/**
	 * 状态 0启用 1停用
	 */
	private Integer fstatus;
	/**
	 * 版本号
	 */
	private Integer version;
	/**
	 * 是否初始化
	 */
	private Boolean init;
	/**
	 * 开户地址
	 */
	private String faddress;
	/**
	 * 开户名
	 */
	private String frealname;

	// 国家 id
	private Long countryid;
	// 国家
	private String fcountry;
	// 省
	private String fprov;
	// 市
	private String fcity;
	// 区
	private String fdist;

	// 类型
	@Deprecated
	private Integer ftype;

	public String getFcountry() {
		return fcountry;
	}

	public void setFcountry(String fcountry) {
		this.fcountry = fcountry;
	}

	public String getFbanktype_s() {
		return fbanktype_s;
	}

	public void setFbanktype_s(String fbanktype_s) {
		this.fbanktype_s = fbanktype_s;
	}

	public Integer getFid() {
		return fid;
	}

	public void setFid(Integer fid) {
		this.fid = fid;
	}

	public Long getFuid() {
		return fuid;
	}

	public void setFuid(Long fuid) {
		this.fuid = fuid;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname == null ? null : fname.trim();
	}

	public String getFbanknumber() {
		return fbanknumber;
	}

	public void setFbanknumber(String fbanknumber) {
		this.fbanknumber = fbanknumber == null ? null : fbanknumber.trim();
	}

	public Integer getFbanktype() {
		return fbanktype;
	}

	public void setFbanktype(Integer fbanktype) {
		this.fbanktype = fbanktype;
	}

	public Date getFcreatetime() {
		return fcreatetime;
	}

	public void setFcreatetime(Date fcreatetime) {
		this.fcreatetime = fcreatetime;
	}

	public Integer getFstatus() {
		return fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Boolean getInit() {
		return init;
	}

	public void setInit(Boolean init) {
		this.init = init;
	}

	public String getFaddress() {
		return faddress;
	}

	public void setFaddress(String faddress) {
		this.faddress = faddress == null ? null : faddress.trim();
	}

	public String getFrealname() {
		return frealname;
	}

	public void setFrealname(String frealname) {
		this.frealname = frealname == null ? null : frealname.trim();
	}

	public String getFprov() {
		return fprov;
	}

	public void setFprov(String fprov) {
		this.fprov = fprov == null ? null : fprov.trim();
	}

	public String getFcity() {
		return fcity;
	}

	public void setFcity(String fcity) {
		this.fcity = fcity == null ? null : fcity.trim();
	}

	public Integer getFtype() {
		return ftype;
	}

	public void setFtype(Integer ftype) {
		this.ftype = ftype;
	}

	public String getFdist() {
		return fdist;
	}

	public void setFdist(String fdist) {
		this.fdist = fdist == null ? null : fdist.trim();
	}

	public Long getFbankid() {
		return fbankid;
	}

	public void setFbankid(Long fbankid) {
		this.fbankid = fbankid;
	}

	public Long getCountryid() {
		return countryid;
	}

	public void setCountryid(Long countryid) {
		this.countryid = countryid;
	}
}
package io.uhha.coin.common.dto.log;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 注册邀请奖励记录
 * @author ZGY
 */
public class FUserRegisterRewardRecord implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	// 主键ID
	private Integer fid;
	// 数量
	private BigDecimal famount;
	// 虚拟币ID
	private Integer fcoinid;
	// 用户ID
	private Integer fuid;
	//关系人Id(注册用户为邀请人id,邀请用户为注册人id)
	private Integer frelationid;
	// 0注册,1为邀请
	private Integer ftype;
	// 状态, 0未到账, 1已到账
	private Integer fstatus;
	// 审核人
	private Integer fcreatorid;
	// 描述
	private String finfo;
	// 创建时间
	private Date fcreatetime;
	// 更新时间
	private Date fupdatetime;
	// 版本号
	private Integer version;

	public Integer getFid() {
		return fid;
	}

	public void setFid(Integer fid) {
		this.fid = fid;
	}

	public BigDecimal getFamount() {
		return famount;
	}

	public void setFamount(BigDecimal famount) {
		this.famount = famount;
	}

	public Integer getFcoinid() {
		return fcoinid;
	}

	public void setFcoinid(Integer fcoinid) {
		this.fcoinid = fcoinid;
	}

	public Integer getFuid() {
		return fuid;
	}

	public void setFuid(Integer fuid) {
		this.fuid = fuid;
	}

	public Integer getFrelationid() {
		return frelationid;
	}

	public void setFrelationid(Integer frelationid) {
		this.frelationid = frelationid;
	}

	public Integer getFtype() {
		return ftype;
	}

	public void setFtype(Integer ftype) {
		this.ftype = ftype;
	}

	public Integer getFstatus() {
		return fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	public Integer getFcreatorid() {
		return fcreatorid;
	}

	public void setFcreatorid(Integer fcreatorid) {
		this.fcreatorid = fcreatorid;
	}

	public String getFinfo() {
		return finfo;
	}

	public void setFinfo(String finfo) {
		this.finfo = finfo;
	}

	public Date getFcreatetime() {
		return fcreatetime;
	}

	public void setFcreatetime(Date fcreatetime) {
		this.fcreatetime = fcreatetime;
	}

	public Date getFupdatetime() {
		return fupdatetime;
	}

	public void setFupdatetime(Date fupdatetime) {
		this.fupdatetime = fupdatetime;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}
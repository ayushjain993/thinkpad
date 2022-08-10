package io.uhha.coin.common.domain;

import io.uhha.coin.common.Enum.ScalpStatusEnum;

import java.io.Serializable;
import java.util.Date;

public class FSignUpAddress implements Serializable {
	private static final long serialVersionUID = 8735837042417629874L;
	private Integer fid;
	private String sourceAddress;
	private Integer status;
	private String status_s;
	private Date createtime;
	private Date modifiedtime;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getFid() {
		return fid;
	}

	public void setFid(Integer fid) {
		this.fid = fid;
	}

	public String getSourceAddress() {
		return sourceAddress;
	}

	public void setSourceAddress(String sourceAddress) {
		this.sourceAddress = sourceAddress;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setStatus_s(String status_s) {
		this.status_s = status_s;
	}

	public String getStatus_s() {
		return status_s = ScalpStatusEnum.getScalpStatusValueByCode(status);
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getModifiedtime() {
		return modifiedtime;
	}

	public void setModifiedtime(Date modifiedtime) {
		this.modifiedtime = modifiedtime;
	}
}

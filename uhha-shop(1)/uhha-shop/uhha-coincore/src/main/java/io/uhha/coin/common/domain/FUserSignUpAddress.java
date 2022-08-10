package io.uhha.coin.common.domain;

import java.io.Serializable;
import java.util.Date;

public class FUserSignUpAddress implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer fid;
	private Integer fuid;
	private Integer faddressid;
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

	public Integer getFuid() {
		return fuid;
	}

	public void setFuid(Integer fuid) {
		this.fuid = fuid;
	}

	public Integer getFaddressid() {
		return faddressid;
	}

	public void setFaddressid(Integer faddressid) {
		this.faddressid = faddressid;
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

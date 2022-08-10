package io.uhha.coin.common.dto.log;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import io.uhha.coin.common.Enum.LogAdminActionEnum;

/**
 * 后台用户行为日志
 * @author TT
 */
public class FLogAdminAction implements Serializable {
	
	private static final long serialVersionUID = 1L;

	// uuid
	private String uuid;
	// 主键ID
    private Integer fid;
    // 用户ID
    private Long fadminid;
    // 用户ID
    private Long fuid;
    // 类型
    private Integer ftype;
    // 数据类型
    private Integer fdatatype;
    // 数据类型
    private Integer fcapitaltype;
    // 数据内容
    private BigDecimal fdata;
    // 描述
    private String fcontent;
    // IP
    private String fip;
    // 更新时间
    private Date fupdatetime;
    // 创建时间
    private Date fcreatetime;
    
    private String ftype_s;

    public FLogAdminAction() {
		this.uuid = UUID.randomUUID().toString();
    }
    
    public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public Long getFadminid() {
		return fadminid;
	}

	public void setFadminid(Long fadminid) {
		this.fadminid = fadminid;
	}

	public Long getFuid() {
        return fuid;
    }

    public void setFuid(Long fuid) {
        this.fuid = fuid;
    }

    public Integer getFtype() {
        return ftype;
    }

    public void setFtype(Integer ftype) {
        this.ftype = ftype;
    }

    public String getFcontent() {
        return fcontent;
    }

    public void setFcontent(String fcontent) {
        this.fcontent = fcontent == null ? null : fcontent.trim();
    }
    
	public Integer getFdatatype() {
		return fdatatype;
	}

	public void setFdatatype(Integer fdatatype) {
		this.fdatatype = fdatatype;
	}

	public Integer getFcapitaltype() {
		return fcapitaltype;
	}

	public void setFcapitaltype(Integer fcapitaltype) {
		this.fcapitaltype = fcapitaltype;
	}

	public BigDecimal getFdata() {
		return fdata;
	}

	public void setFdata(BigDecimal fdata) {
		this.fdata = fdata;
	}

	public String getFip() {
        return fip;
    }

    public void setFip(String fip) {
        this.fip = fip == null ? null : fip.trim();
    }

	public Date getFupdatetime() {
		return fupdatetime;
	}

	public void setFupdatetime(Date fupdatetime) {
		this.fupdatetime = fupdatetime;
	}

	public Date getFcreatetime() {
		return fcreatetime;
	}

	public void setFcreatetime(Date fcreatetime) {
		this.fcreatetime = fcreatetime;
	}

	public String getFtype_s() {
		ftype_s = LogAdminActionEnum.getValueByCode(ftype);
		return ftype_s;
	}

	public void setFtype_s(String ftype_s) {
		this.ftype_s = ftype_s;
	}
}
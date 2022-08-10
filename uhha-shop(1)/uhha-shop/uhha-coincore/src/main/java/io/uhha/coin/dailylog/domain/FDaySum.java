package io.uhha.coin.dailylog.domain;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
/**
 * 每日资金存量
 * @author TT
 */
public class FDaySum implements Serializable {
    private static final long serialVersionUID = 4587049943186470827L;
    // 主键ID
    private Integer fid;
    // 币种ID
    private Integer fcoinid;
    // 总
    private BigDecimal ftotle;
    // 冻结
    private BigDecimal frozen;
    // 时间
    private Date fcreatetime;
    // 币种名称
    private String fcoinname;

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

    public BigDecimal getFtotle() {
        return ftotle;
    }

    public void setFtotle(BigDecimal ftotle) {
        this.ftotle = ftotle;
    }

    public BigDecimal getFrozen() {
        return frozen;
    }

    public void setFrozen(BigDecimal frozen) {
        this.frozen = frozen;
    }

	public Date getFcreatetime() {
		return fcreatetime;
	}

	public void setFcreatetime(Date fcreatetime) {
		this.fcreatetime = fcreatetime;
	}

	public String getFcoinname() {
		return fcoinname;
	}

	public void setFcoinname(String fcoinname) {
		this.fcoinname = fcoinname;
	}
	
	
}
package io.uhha.coin.dailylog.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class FCoinBalance implements Serializable {

    private Integer fid;
    private Date fdate;
    private Integer fcoinid;
    private String fcoinname;
    private BigDecimal fbalance;
    private Date fcreatedate;
    private String fremark;
    private Integer version;

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public Date getFdate() {
        return fdate;
    }

    public void setFdate(Date fdate) {
        this.fdate = fdate;
    }

    public Integer getFcoinid() {
        return fcoinid;
    }

    public void setFcoinid(Integer fcoinid) {
        this.fcoinid = fcoinid;
    }

    public BigDecimal getFbalance() {
        return fbalance;
    }

    public void setFbalance(BigDecimal fbalance) {
        this.fbalance = fbalance;
    }

    public Date getFcreatedate() {
        return fcreatedate;
    }

    public void setFcreatedate(Date fcreatedate) {
        this.fcreatedate = fcreatedate;
    }

    public String getFremark() {
        return fremark;
    }

    public void setFremark(String fremark) {
        this.fremark = fremark;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getFcoinname() {
        return fcoinname;
    }

    public void setFcoinname(String fcoinname) {
        this.fcoinname = fcoinname;
    }
}

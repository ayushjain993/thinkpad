package io.uhha.coin.capital.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.uhha.coin.common.Enum.CapitalOperationInStatus;
import io.uhha.coin.common.Enum.CapitalOperationOutStatus;
import io.uhha.coin.common.Enum.CapitalOperationTypeEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Token类充提记录
 *
 * @author LY
 */
public class FTokenCapitalOperation implements Serializable {

    private static final long serialVersionUID = 1L;

    // 主键ID
    private Integer fid;
    // 用户ID
    private Long fuid;
    // 币种id
    private Integer fcoinid;
    // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date fcreatetime;
    // 数量
    private BigDecimal famount;
    // 充值或提现 CapitalOperationTypeEnum，汇款or提现
    private Integer finouttype;

    private String faccount;
    // 充值类型
    private Integer ftype;
    private String ftype_s;
    // 状态
    private Integer fstatus;// CapitalOperationInStatus||CapitalOperationOutStatus
    private String fstatus_s;
    // 备注
    private String fremark;
    // 更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date fupdatetime;
    // 审核管理员ID
    private Long fadminid;
    // 手续费
    private BigDecimal ffees;
    // 版本号
    private Integer version;

    /********** 扩展字段 ************/

    // 登录名
    private String floginname;
    // 昵称
    private String fnickname;
    // 真实姓名
    private String frealname;
    // 审核人
    private String fadminname;

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

    public Integer getFcoinid() {
        return fcoinid;
    }

    public String getFaccount() {
        return faccount;
    }

    public void setFaccount(String faccount) {
        this.faccount = faccount;
    }

    public void setFcoinid(Integer fcoinid) {
        this.fcoinid = fcoinid;
    }

    public Date getFcreatetime() {
        return fcreatetime;
    }

    public void setFcreatetime(Date fcreatetime) {
        this.fcreatetime = fcreatetime;
    }

    public BigDecimal getFamount() {
        return famount;
    }

    public void setFamount(BigDecimal famount) {
        this.famount = famount;
    }

    public Integer getFinouttype() {
        return finouttype;
    }

    public void setFinouttype(Integer finouttype) {
        this.finouttype = finouttype;
    }

    public Integer getFtype() {
        return ftype;
    }

    public void setFtype(Integer ftype) {
        this.ftype = ftype;
    }

    public String getFtype_s() {
        return ftype_s;
    }

    public void setFtype_s(String ftype_s) {
        this.ftype_s = ftype_s;
    }

    public Integer getFstatus() {
        return fstatus;
    }

    public void setFstatus(Integer fstatus) {
        this.fstatus = fstatus;
    }

    public String getFstatus_s() {
        int status = this.getFstatus();
        if(this.getFinouttype()== CapitalOperationTypeEnum.TOKEN_IN){
            return CapitalOperationInStatus.getEnumString(status);
        }
        return CapitalOperationOutStatus.getEnumString(status);
    }

    public void setFstatus_s(String fstatus_s) {
        this.fstatus_s = fstatus_s;
    }

    public String getFremark() {
        return fremark;
    }

    public void setFremark(String fremark) {
        this.fremark = fremark;
    }

    public Date getFupdatetime() {
        return fupdatetime;
    }

    public void setFupdatetime(Date fupdatetime) {
        this.fupdatetime = fupdatetime;
    }

    public Long getFadminid() {
        return fadminid;
    }

    public void setFadminid(Long fadminid) {
        this.fadminid = fadminid;
    }

    public BigDecimal getFfees() {
        return ffees;
    }

    public void setFfees(BigDecimal ffees) {
        this.ffees = ffees;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
    public String getFloginname() {
        return floginname;
    }

    public void setFloginname(String floginname) {
        this.floginname = floginname;
    }

    public String getFnickname() {
        return fnickname;
    }

    public void setFnickname(String fnickname) {
        this.fnickname = fnickname;
    }

    public String getFrealname() {
        return frealname;
    }

    public void setFrealname(String frealname) {
        this.frealname = frealname;
    }

    public String getFadminname() {
        return fadminname;
    }

    public void setFadminname(String fadminname) {
        this.fadminname = fadminname;
    }
}
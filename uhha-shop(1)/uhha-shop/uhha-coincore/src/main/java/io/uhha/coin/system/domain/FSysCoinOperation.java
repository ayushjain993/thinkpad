package io.uhha.coin.system.domain;


import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.uhha.common.annotation.Excel;
import io.uhha.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 系统钱包操作日志对象 f_sys_coin_operation
 *
 * @author uhha
 * @date 2021-10-05
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FSysCoinOperation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** fid */
    private Long fid;

    /** 币id */
    @Excel(name = "币id")
    private Integer fcoinid;

    /** 数额 */
    @Excel(name = "数额")
    private BigDecimal famount;

    /** 手续费 */
    @Excel(name = "手续费")
    private BigDecimal ffees;

    /** 类型 */
    @Excel(name = "类型")
    private Integer ftype;

    /**
     * 状态
     * @see io.uhha.coin.common.Enum.VirtualCapitalOperationInStatusEnum
     * @see io.uhha.coin.common.Enum.VirtualCapitalOperationOutStatusEnum
     */
    @Excel(name = "状态")
    private Integer fstatus;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date fcreatetime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date fupdatetime;

    /** 源地址 */
    @Excel(name = "源地址")
    private String ffromaddress;

    /** 目的地址 */
    @Excel(name = "目的地址")
    private String ftoaddress;

    /** 上链时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel(name = "上链时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date txtime;

    /** txid */
    @Excel(name = "txid")
    private String txid;

    /** 网络确认数 */
    @Excel(name = "网络确认数")
    private Long fconfirmations;

    /** 区块号 */
    @Excel(name = "区块号")
    private Long fblocknumber;

    /** 管理员id */
    @Excel(name = "管理员id")
    private Long fadminid;

    /** 版本 */
    @Excel(name = "版本")
    private Integer version;

    /** nonce */
    @Excel(name = "nonce")
    private Integer fnonce;

    public void setFid(Long fid)
    {
        this.fid = fid;
    }

    public Long getFid()
    {
        return fid;
    }

    public Integer getFcoinid() {
        return fcoinid;
    }

    public void setFcoinid(Integer fcoinid) {
        this.fcoinid = fcoinid;
    }

    public void setFamount(BigDecimal famount)
    {
        this.famount = famount;
    }

    public BigDecimal getFamount()
    {
        return famount;
    }
    public void setFfees(BigDecimal ffees)
    {
        this.ffees = ffees;
    }

    public BigDecimal getFfees()
    {
        return ffees;
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

    public void setFcreatetime(Date fcreatetime)
    {
        this.fcreatetime = fcreatetime;
    }

    public Date getFcreatetime()
    {
        return fcreatetime;
    }
    public void setFupdatetime(Date fupdatetime)
    {
        this.fupdatetime = fupdatetime;
    }

    public Date getFupdatetime()
    {
        return fupdatetime;
    }
    public void setFfromaddress(String ffromaddress)
    {
        this.ffromaddress = ffromaddress;
    }

    public String getFfromaddress()
    {
        return ffromaddress;
    }
    public void setFtoaddress(String ftoaddress)
    {
        this.ftoaddress = ftoaddress;
    }

    public String getFtoaddress()
    {
        return ftoaddress;
    }
    public void setTxtime(Date txtime)
    {
        this.txtime = txtime;
    }

    public Date getTxtime()
    {
        return txtime;
    }
    public void setTxid(String txid)
    {
        this.txid = txid;
    }

    public String getTxid()
    {
        return txid;
    }
    public void setFconfirmations(Long fconfirmations)
    {
        this.fconfirmations = fconfirmations;
    }

    public Long getFconfirmations()
    {
        return fconfirmations;
    }
    public void setFblocknumber(Long fblocknumber)
    {
        this.fblocknumber = fblocknumber;
    }

    public Long getFblocknumber()
    {
        return fblocknumber;
    }
    public void setFadminid(Long fadminid)
    {
        this.fadminid = fadminid;
    }

    public Long getFadminid()
    {
        return fadminid;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getFnonce() {
        return fnonce;
    }

    public void setFnonce(Integer fnonce) {
        this.fnonce = fnonce;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("fid", getFid())
                .append("fcoinid", getFcoinid())
                .append("famount", getFamount())
                .append("ffees", getFfees())
                .append("ftype", getFtype())
                .append("fstatus", getFstatus())
                .append("fcreatetime", getFcreatetime())
                .append("fupdatetime", getFupdatetime())
                .append("ffromaddress", getFfromaddress())
                .append("ftoaddress", getFtoaddress())
                .append("txtime", getTxtime())
                .append("txid", getTxid())
                .append("fconfirmations", getFconfirmations())
                .append("fblocknumber", getFblocknumber())
                .append("fadminid", getFadminid())
                .append("version", getVersion())
                .append("fnonce", getFnonce())
                .toString();
    }
}
package io.uhha.coin.capital.domain;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.uhha.coin.common.Enum.validate.PlatformEnum;
import io.uhha.coin.common.Enum.DataSourceEnum;
import io.uhha.coin.common.Enum.VirtualCapitalOperationInStatusEnum;
import io.uhha.coin.common.Enum.VirtualCapitalOperationOutStatusEnum;
import io.uhha.coin.common.Enum.VirtualCapitalOperationTypeEnum;
import io.uhha.common.utils.Utils;

import java.math.BigDecimal;
/**
 * 虚拟币充提记录
 * @author LY
 */
public class FVirtualCapitalOperation implements Serializable {

	private static final long serialVersionUID = 1L;

	// 主ID
	private Integer fid;
	// 用户ID
	private Long fuid;
	// 虚拟币ID
	private Integer fcoinid;
	// 数量
	private BigDecimal famount;
	// 手续费
	private BigDecimal ffees;

	/**
	 * 类型
	 * @see io.uhha.coin.common.Enum.VirtualCapitalOperationTypeEnum
 	 */
	private Integer ftype;
	private String ftype_s;

	/**
	 * 状态
	 * @see io.uhha.coin.common.Enum.VirtualCapitalOperationInStatusEnum
	 * @see io.uhha.coin.common.Enum.VirtualCapitalOperationOutStatusEnum
 	 */
	private Integer fstatus;
	private String fstatus_s;
	// 提现地址
	private String fwithdrawaddress;

	//用户提现地址外键
	private Integer faddressWithdrawId;

    public Integer getFaddressWithdrawId() {
        return faddressWithdrawId;
    }

    public void setFaddressWithdrawId(Integer faddressWithdrawId) {
        this.faddressWithdrawId = faddressWithdrawId;
    }

    // 充值地址
	private String frechargeaddress;
	// 交易ID
	private String funiquenumber;
	// 确认数
	private Integer fconfirmations;
	//
	private Boolean fhasowner;
	// 区块数
	private Integer fblocknumber;
	// 创建时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date fcreatetime;
	// 更新时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date fupdatetime;
	// Tx时间
	private Date txTime;
	// 版本号
	private Integer version;
	// 审核人
	private Long fadminid;
	// BTC网络手续费
	private BigDecimal fbtcfees;
	// 随机数(ETC)
	private Integer fnonce;
	/**
	 * 来源
	 * @see io.uhha.coin.common.Enum.DataSourceEnum
	 */
	private Integer fsource;
	private String fsource_s;
	/**
	 * 订单来源平台
	 * @see io.uhha.coin.common.Enum.validate.PlatformEnum
 	 */

	private Integer fplatform;
	private String fplatform_s;
	
	/********** 扩展字段 ************/

	/**
	 * 登录名
	 */
	private String floginname;
	/**
	 * 昵称
 	 */
	private String fnickname;
	/**
	 * 真实姓名
 	 */
	private String frealname;
	/**
	 * 审核人
 	 */
	private String fadminname;
	/**
	 * 虚拟币名称
	 */
	private String fcoinname;
	/**
	 * 平台互转
	 */
	private Integer isFromOurself;

	public Integer getIsFromOurself() {
		return isFromOurself;
	}

	public void setIsFromOurself(Integer isFromOurself) {
		this.isFromOurself = isFromOurself;
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

	public Integer getFcoinid() {
		return fcoinid;
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

	public BigDecimal getFfees() {
		return ffees;
	}

	public void setFfees(BigDecimal ffees) {
		this.ffees = ffees;
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

	public Date getFupdatetime() {
		return fupdatetime;
	}

	public void setFupdatetime(Date fupdatetime) {
		this.fupdatetime = fupdatetime;
	}

	public Date getTxTime() {
		return txTime;
	}

	public void setTxTime(Date txTime) {
		this.txTime = txTime;
	}

	public String getFwithdrawaddress() {
		return fwithdrawaddress;
	}

	public void setFwithdrawaddress(String fwithdrawaddress) {
		this.fwithdrawaddress = fwithdrawaddress == null ? null : fwithdrawaddress.trim();
	}

	public String getFrechargeaddress() {
		return frechargeaddress;
	}

	public void setFrechargeaddress(String frechargeaddress) {
		this.frechargeaddress = frechargeaddress == null ? null : frechargeaddress.trim();
	}

	public String getFuniquenumber() {
		return funiquenumber;
	}

	public void setFuniquenumber(String funiquenumber) {
		this.funiquenumber = funiquenumber == null ? null : funiquenumber.trim();
	}

	public Integer getFconfirmations() {
		return fconfirmations;
	}

	public void setFconfirmations(Integer fconfirmations) {
		this.fconfirmations = fconfirmations;
	}

	public Boolean getFhasowner() {
		return fhasowner;
	}

	public void setFhasowner(Boolean fhasowner) {
		this.fhasowner = fhasowner;
	}

	public Integer getFblocknumber() {
		return fblocknumber;
	}

	public void setFblocknumber(Integer fblocknumber) {
		this.fblocknumber = fblocknumber;
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

	public String getFcoinname() {
		return fcoinname;
	}

	public void setFcoinname(String fcoinname) {
		this.fcoinname = fcoinname;
	}

	public Long getFadminid() {
		return fadminid;
	}

	public void setFadminid(Long fadminid) {
		this.fadminid = fadminid;
	}

	public String getFtype_s() {
		return VirtualCapitalOperationTypeEnum.getValueByCode(this.ftype);
	}

	public void setFtype_s(String ftype_s) {
		this.ftype_s = ftype_s;
	}

	public String getFstatus_s() {
		if (this.ftype == VirtualCapitalOperationTypeEnum.COIN_IN.getCode()) {
			return VirtualCapitalOperationInStatusEnum.getEnumString(this.fstatus);
		} else if (this.ftype == VirtualCapitalOperationTypeEnum.COIN_OUT.getCode()) {
			return VirtualCapitalOperationOutStatusEnum.getEnumString(this.fstatus);
		}
		return null;
	}

	public void setFstatus_s(String fstatus_s) {
		this.fstatus_s = fstatus_s;
	}

	public BigDecimal getFbtcfees() {
		return fbtcfees;
	}

	public void setFbtcfees(BigDecimal fbtcfees) {
		this.fbtcfees = fbtcfees;
	}

	public Integer getFnonce() {
		return fnonce;
	}

	public void setFnonce(Integer fnonce) {
		this.fnonce = fnonce;
	}

	public Integer getFsource() {
		return fsource;
	}

	public void setFsource(Integer fsource) {
		this.fsource = fsource;
	}

	public String getFsource_s() {
		this.fsource_s = DataSourceEnum.getValueByCode(this.fsource);
		return fsource_s;
	}

	public void setFsource_s(String fsource_s) {
		this.fsource_s = fsource_s;
	}

	public Integer getFplatform() {
		return fplatform;
	}

	public void setFplatform(Integer fplatform) {
		this.fplatform = fplatform;
	}

	public String getFplatform_s() {
		this.fplatform_s = PlatformEnum.getValueByCode(this.fplatform);
		return fplatform_s;
	}

	public void setFplatform_s(String fplatform_s) {
		this.fplatform_s = fplatform_s;
	}

	/**
	 * 创建兑换Token的操作对象
	 * @param uid
	 * @param fcoinid
	 * @param amount
	 * @param address
	 * @param fplatform
	 * @return
	 */
	public static FVirtualCapitalOperation buildForRedeem(Long uid, int fcoinid, BigDecimal amount, String address, int fplatform){
		FVirtualCapitalOperation tovirtualcaptualoperation = new FVirtualCapitalOperation();
		tovirtualcaptualoperation.setFuid(uid);
		tovirtualcaptualoperation.setFcoinid(fcoinid);
		tovirtualcaptualoperation.setFamount(amount);
		tovirtualcaptualoperation.setFfees(BigDecimal.ZERO);
		tovirtualcaptualoperation.setFbtcfees(BigDecimal.ZERO);
		tovirtualcaptualoperation.setFhasowner(true);
		tovirtualcaptualoperation.setFstatus(VirtualCapitalOperationInStatusEnum.SUCCESS);// 收款成功
		tovirtualcaptualoperation.setFtype(VirtualCapitalOperationTypeEnum.COIN_IN.getCode());// 收款
		tovirtualcaptualoperation.setFuniquenumber("[平台互转]" + Utils.UUID());
		tovirtualcaptualoperation.setFrechargeaddress(address);
		tovirtualcaptualoperation.setFupdatetime(Utils.getTimestamp());
		tovirtualcaptualoperation.setFcreatetime(Utils.getTimestamp());
		tovirtualcaptualoperation.setVersion(0);
		tovirtualcaptualoperation.setFplatform(fplatform);
		return tovirtualcaptualoperation;
	}
}
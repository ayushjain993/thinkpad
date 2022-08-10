/**
 * 
 */
package io.uhha.coin.common.dto.msg;

import java.io.Serializable;
import java.util.Date;

/**
 * @author XinSai
 *
 */
public class MsgInform implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4178111118472133051L;
	
	private Integer id;                           //id
	
	private String source;					  //来源id
	
	private String msgType;						  //短信类型 1.购买成功通知用户付款 2.出售成功通知用户收款
	
	private String mobile;						  //手机号
	
	private String actCode;						  //短信模版参数
	
	private String isSend;					      //是否发送成功
	
	private Date sendTime; 						  //发送时间
	
	private Date validTime;						  //短信有效时间
	
	private String remark;						  //备注
	
	private String createBy;				      //创建人
	
	private Date createTime;					  //创建时间
	
	private String updateBy;					  //更新人
	
	private Date updateTime;					  //更新时间

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the msgType
	 */
	public String getMsgType() {
		return msgType;
	}

	/**
	 * @param msgType the msgType to set
	 */
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return the actCode
	 */
	public String getActCode() {
		return actCode;
	}

	/**
	 * @param actCode the actCode to set
	 */
	public void setActCode(String actCode) {
		this.actCode = actCode;
	}

	/**
	 * @return the isSend
	 */
	public String getIsSend() {
		return isSend;
	}

	/**
	 * @param isSend the isSend to set
	 */
	public void setIsSend(String isSend) {
		this.isSend = isSend;
	}

	/**
	 * @return the sendTime
	 */
	public Date getSendTime() {
		return sendTime;
	}

	/**
	 * @param sendTime the sendTime to set
	 */
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	/**
	 * @return the validTime
	 */
	public Date getValidTime() {
		return validTime;
	}

	/**
	 * @param validTime the validTime to set
	 */
	public void setValidTime(Date validTime) {
		this.validTime = validTime;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the createBy
	 */
	public String getCreateBy() {
		return createBy;
	}

	/**
	 * @param createBy the createBy to set
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the updateBy
	 */
	public String getUpdateBy() {
		return updateBy;
	}

	/**
	 * @param updateBy the updateBy to set
	 */
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	/**
	 * @return the updateTime
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}

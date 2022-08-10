package io.uhha.coin.common.util;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.*;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import io.uhha.coin.common.Enum.LogAdminActionEnum;
import io.uhha.coin.common.Enum.LogUserActionEnum;
import io.uhha.coin.common.dto.log.FLogAdminAction;
import io.uhha.coin.common.dto.log.FLogUserAction;
import io.uhha.common.exception.BCException;
import io.uhha.coin.common.framework.redis.RedisCryptoHelper;
import io.uhha.coin.common.mq.MQConstant;
import io.uhha.coin.common.mq.MQTopic;
import io.uhha.common.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Component("mqSend")
public class MQSend {

	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(MQSend.class);
	
	@Resource(name="userActionProducer")
	private Producer userActionProducer;

	@Resource(name="adminActionProducer")
	private Producer adminActionProducer;

	@Autowired
	private RedisCryptoHelper redisCryptoHelper;

	/**
	 * 发送 MQ_USER_ACTION
	 * @param fuid
	 * @param action
	 * @throws BCException
	 */
	public void SendUserAction(Long fuid, LogUserActionEnum action) throws BCException {
		SendUserAction(fuid, action, null,null, "", BigDecimal.ZERO,"" ,null);
	}
	
	/**
	 * 发送 MQ_USER_ACTION
	 * @param fuid
	 * @param action
	 * @throws BCException
	 */
	public void SendUserAction(Long fuid, LogUserActionEnum action, String fip) throws BCException {
		SendUserAction(fuid, action, null,null, fip, BigDecimal.ZERO,"" ,null);
	}
	
	/**
	 * 发送 MQ_USER_ACTION
	 * @param fuid
	 * @param action
	 * @throws BCException
	 */
	public void SendUserAction(Long fuid, LogUserActionEnum action, String fip, int fdatatype, String fcontent) throws BCException {
		SendUserAction(fuid, action, fdatatype,null, fip, BigDecimal.ZERO,fcontent ,null);
	}
	
	/**
	 * 发送 MQ_USER_ACTION
	 * @param fuid
	 * @param action
	 * @param data
	 * @throws BCException
	 */
	public void SendUserAction(Long fuid, LogUserActionEnum action, BigDecimal data, String ip) throws BCException {
		SendUserAction(fuid, action, null,data,ip, BigDecimal.ZERO,"" ,null);
	}

	/**
	 * 发送 MQ_USER_ACTION
	 * @param fuid
	 * @param action
	 * @param data
	 * @throws BCException
	 */
	public void SendUserAction(Long fuid, LogUserActionEnum action, int fdatatype, BigDecimal data, String ip) throws BCException {
		SendUserAction(fuid, action, fdatatype,data,ip, BigDecimal.ZERO,"" ,null);
	}
	
	/**
	 * 发送 MQ_USER_ACTION
	 * @param fuid
	 * @param action
	 * @param data
	 * @throws BCException
	 */
	public void SendUserAction(Long fuid, LogUserActionEnum action, int fdatatype, BigDecimal data, BigDecimal ffees, String ip) throws BCException {
		SendUserAction(fuid, action, fdatatype,data,ip, ffees,"" ,null);
	}
	
	/**
	 * 发送 MQ_USER_ACTION
	 * @param fuid
	 * @param action
	 * @param data
	 * @throws BCException
	 */
	public void SendUserAction(Long fuid, LogUserActionEnum action, int fdatatype, BigDecimal data, String fcontent, String ip) throws BCException {
		SendUserAction(fuid, action, fdatatype,data,ip, BigDecimal.ZERO,fcontent ,null);
	}
	
	/**
	 * 发送 MQ_USER_ACTION
	 * @param fuid
	 * @param action
	 * @param data
	 * @throws BCException
	 */
	public void SendUserAction(Long fuid, LogUserActionEnum action, int fdatatype, int fcapitaltype, BigDecimal data) throws BCException {
		SendUserAction(fuid, action, fdatatype,data,"", BigDecimal.ZERO,"" ,fcapitaltype);
	}

	public void SendUserAction(Long uid, LogUserActionEnum action, Integer dataType, BigDecimal data, String ip, BigDecimal fees,String content , Integer capitalType ) throws BCException {
		FLogUserAction fLogUserAction = new FLogUserAction();
		fLogUserAction.setFuid(uid);
		fLogUserAction.setFtype(action.getCode());
		fLogUserAction.setFdatatype(dataType);
		fLogUserAction.setFcapitaltype(capitalType);
		fLogUserAction.setFdata(data);
		fLogUserAction.setFfees(fees);
		fLogUserAction.setFcontent(content);
		fLogUserAction.setFip(ip);
		Message message = new Message(MQTopic.USER_ACTION, MQConstant.TAG_USER_ACTION, JSON.toJSONString(fLogUserAction).getBytes());
		message.setKey("USER_ACTION_" + uid + "_" + action + "_" + ip + "_" + dataType + "_" + data);
		//enabled?
		ProducerBean bean = (ProducerBean)userActionProducer;
		if("1".equalsIgnoreCase(redisCryptoHelper.getSystemArgs("isMQEnabled"))) {
			userActionProducer.sendAsync(message, new SendCallback() {
				@Override
				public void onSuccess(SendResult sendResult) {
				}

				@Override
				public void onException(OnExceptionContext context) {
					logger.error("MQ : SendUserAction failed");
				}
			});
		}else{
			logger.warn("isMQEnabled set to 0, no sending to MQ");
		}
	}


	/**
	 * 发送用户日志
	 * @param fuid 用户id
	 * @param action 操作
	 * @param fdatatype 数据类型
	 * @param data 数据
	 * @throws BCException 队列发送异常
	 */
	public void SendUserAction(long fuid, LogUserActionEnum action, int fdatatype, BigDecimal data) throws BCException {
		FLogUserAction fLogUserAction = new FLogUserAction();
		fLogUserAction.setFuid(fuid);
		fLogUserAction.setFtype(action.getCode());
		fLogUserAction.setFdatatype(fdatatype);
		fLogUserAction.setFdata(data);
		Message message = new Message(MQTopic.USER_ACTION, MQConstant.TAG_USER_ACTION, JSON.toJSONString(fLogUserAction).getBytes());
		message.setKey("USER_ACTION_" + fuid + "_" + action + "_" + fdatatype + "_" + data);
		//enabled?
		ProducerBean bean = (ProducerBean)userActionProducer;
		if("1".equalsIgnoreCase(redisCryptoHelper.getSystemArgs("isMQEnabled"))) {
			userActionProducer.sendAsync(message, new SendCallback() {
				@Override
				public void onSuccess(SendResult sendResult) {
				}

				@Override
				public void onException(OnExceptionContext context) {
					logger.error("useracttion mq send failed");
				}
			});
		}else{
			logger.warn("isMQEnabled set to 0, no sending to MQ");
		}
	}

	/**
	 * 发送用户日志
	 * @param fuid 用户id
	 * @param action 操作
	 * @param fdatatype 数据类型
	 * @param data 数据
	 * @param ffees 费率
	 * @throws BCException 队列发送异常
	 */
	public void SendUserAction(long fuid, LogUserActionEnum action, int fdatatype, BigDecimal data, BigDecimal ffees) throws BCException {
		FLogUserAction fLogUserAction = new FLogUserAction();
		fLogUserAction.setFuid(fuid);
		fLogUserAction.setFtype(action.getCode());
		fLogUserAction.setFdatatype(fdatatype);
		fLogUserAction.setFdata(data);
		fLogUserAction.setFfees(ffees);
		Message message = new Message(MQTopic.USER_ACTION, MQConstant.TAG_USER_ACTION, JSON.toJSONString(fLogUserAction).getBytes());
		message.setKey("USER_ACTION_" + fuid + "_" + action + "_" + fdatatype + "_" + data);
		//enabled?
		if("1".equalsIgnoreCase(redisCryptoHelper.getSystemArgs("isMQEnabled"))) {
			userActionProducer.sendAsync(message, new SendCallback() {
				@Override
				public void onSuccess(SendResult sendResult) {
				}

				@Override
				public void onException(OnExceptionContext context) {
					logger.error("useracttion mq send failed");
				}
			});
		}else{
			logger.warn("isMQEnabled set to 0, no sending to MQ");
		}
	}


	/**
	 * 发送用户日志
	 * @param fuid 用户id
	 * @param action 操作
	 * @param fdatatype 数据类型
	 * @param data 数据
	 * @param ffees 费率
	 * @param fbtcfees 网络手续费
	 * @throws BCException 队列发送异常
	 */
	public void SendUserAction(long fuid, LogUserActionEnum action, int fdatatype, BigDecimal data, BigDecimal ffees, BigDecimal fbtcfees) throws BCException {
		FLogUserAction fLogUserAction = new FLogUserAction();
		fLogUserAction.setFuid(fuid);
		fLogUserAction.setFtype(action.getCode());
		fLogUserAction.setFdatatype(fdatatype);
		fLogUserAction.setFdata(data);
		fLogUserAction.setFfees(ffees);
		fLogUserAction.setFbtcfees(fbtcfees);
		Message message = new Message(MQTopic.USER_ACTION, MQConstant.TAG_USER_ACTION, JSON.toJSONString(fLogUserAction).getBytes());
		message.setKey("USER_ACTION_" + fuid + "_" + action + "_" + fdatatype + "_" + data);
		//enabled?
		ProducerBean bean = (ProducerBean)userActionProducer;
		if("1".equalsIgnoreCase(redisCryptoHelper.getSystemArgs("isMQEnabled"))) {
			userActionProducer.sendAsync(message, new SendCallback() {
				@Override
				public void onSuccess(SendResult sendResult) {
				}

				@Override
				public void onException(OnExceptionContext context) {
					logger.error("useracttion mq send failed");
				}
			});
		}else{
			logger.warn("isMQEnabled set to 0, no sending to MQ");
		}
	}

	/**
	 * 发送 MQ_USER_ACTION
	 * @param action
	 * @throws BCException
	 */
	public void SendAdminAction(Long fadminid, LogAdminActionEnum action,String ip) {
		FLogAdminAction fLogAdminAction = new FLogAdminAction();
		fLogAdminAction.setFadminid(fadminid);
		fLogAdminAction.setFtype(action.getCode());
		fLogAdminAction.setFip(ip);
		fLogAdminAction.setFcreatetime(Utils.getTimestamp());
		fLogAdminAction.setFupdatetime(Utils.getTimestamp());
		Message message = new Message(MQTopic.ADMIN_ACTION, MQConstant.TAG_ADMIN_ACTION, JSON.toJSONString(fLogAdminAction).getBytes());
		message.setKey("ADMIN_ACTION_" + fadminid + "_" + action);
		//enabled?
		if("1".equalsIgnoreCase(redisCryptoHelper.getSystemArgs("isMQEnabled"))) {
			adminActionProducer.sendAsync(message, new SendCallback() {
				@Override
				public void onSuccess(SendResult sendResult) {
				}

				@Override
				public void onException(OnExceptionContext context) {
					logger.error("MQ : SendAdminAction failed");
				}
			});
		}else{
			logger.warn("isMQEnabled set to 0, no sending to MQ");
		}
	}

	/**
	 * 发送管理员日志
	 * @param fadminid 管理员id
	 * @param fuid 用户id
	 * @param fdatatype 数据类型
	 * @param data 数据
	 * @param action 操作
	 * @throws BCException 队列发送异常
	 */
	public void SendAdminAction(Long fadminid, Long fuid, LogAdminActionEnum action, int fdatatype, BigDecimal data) {
		FLogAdminAction fLogAdminAction = new FLogAdminAction();
		fLogAdminAction.setFadminid(fadminid);
		fLogAdminAction.setFuid(fuid);
		fLogAdminAction.setFtype(action.getCode());
		fLogAdminAction.setFdatatype(fdatatype);
		fLogAdminAction.setFupdatetime(Utils.getTimestamp());
		fLogAdminAction.setFcreatetime(Utils.getTimestamp());
		Message message = new Message(MQTopic.ADMIN_ACTION, MQConstant.TAG_ADMIN_ACTION, JSON.toJSONString(fLogAdminAction).getBytes());
		message.setKey("ADMIN_ACTION_" + fadminid + "_" +fuid + "_" + action);
		//enabled?
		if("1".equalsIgnoreCase(redisCryptoHelper.getSystemArgs("isMQEnabled"))) {
			adminActionProducer.sendAsync(message, new SendCallback() {
				@Override
				public void onSuccess(SendResult sendResult) {
				}

				@Override
				public void onException(OnExceptionContext context) {
					logger.error("adminacttion mq send failed");
				}
			});
		}else{
			logger.warn("isMQEnabled set to 0, no sending to MQ");
		}
	}
}

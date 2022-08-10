package io.uhha.coin.common.framework.mq;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

public class MQSendHelper {

	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(MQSendHelper.class);

	/**
	 * 委单队列
	 */
	private BlockingQueue<Message> mqQueue = new LinkedTransferQueue<>();
	
	/**
	 * 发送
	 */
	private Producer producer;

	public void setProducer(Producer producer) {
		this.producer = producer;
	}

	@PostConstruct
	public void init() {
		Thread thread = new Thread(new Work());
		thread.setName("MQSendHelper");
		thread.start();
	}
	
	public void offer(String topic, String tags, String key, Object object) {
		if(!mqQueue.offer(new Message(topic, tags, key, JSON.toJSONString(object).getBytes()))) {
			logger.error("queue offer failed : " + topic);
		}
	}

	public boolean send(String topic, String tags, String key, Object object) {
		Message message = new Message(topic, tags, key, JSON.toJSONString(object).getBytes());
		try {
			producer.send(message);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	class Work implements Runnable {
		public void run() {
			while (true) {
				Message msg = null;
				try {
					msg = mqQueue.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(msg==null){
					continue;
				}
				final Message msgf = msg;
				producer.sendAsync(msg, new SendCallback() {
        			@Override
        			public void onSuccess(SendResult sendResult) {
        			}
        			@Override
        			public void onException(OnExceptionContext context) {
        				if (!mqQueue.offer(msgf)){
            				logger.error("queue onException failed : {}_{}", msgf.getMsgID(), msgf.getBody());
        				}
        			}
        		});
			}
		}
	}
}

package io.uhha.im.server.component.push;


import io.uhha.im.server.model.Message;

/*
 * 消息发送实接口
 *
 */
public interface IMMessagePusher {

	/*
	 * 向用户发送消息
	 *
	 * @param msg
	 */
	void push(Message msg);

}

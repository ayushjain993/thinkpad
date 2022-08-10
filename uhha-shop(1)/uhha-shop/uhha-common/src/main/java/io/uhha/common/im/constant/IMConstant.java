package io.uhha.common.im.constant;

/**
 * 常量
 */
public interface IMConstant {

	/**
	 消息头长度为3个字节，第一个字节为消息类型，第二，第三字节 转换int后为消息长度
	 */
	byte DATA_HEADER_LENGTH = 3;

	byte DATA_TYPE_PONG  = 0;
	byte DATA_TYPE_PING  = 1;
	byte DATA_TYPE_MESSAGE = 2;
	byte DATA_TYPE_SENT = 3;
	byte DATA_TYPE_REPLY = 4;

	String CLIENT_CONNECT_CLOSED = "client_closed";

}

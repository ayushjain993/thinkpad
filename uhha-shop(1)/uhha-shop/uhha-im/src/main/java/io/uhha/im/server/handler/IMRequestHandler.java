package io.uhha.im.server.handler;

/**
 *  请求处理接口,所有的请求实现必须实现此接口
 */
import io.netty.channel.Channel;
import io.uhha.im.server.model.SentBody;

public interface IMRequestHandler {

	/**
	 * 处理收到客户端从长链接发送的数据
	 */
	void process(Channel channel, SentBody body);
}

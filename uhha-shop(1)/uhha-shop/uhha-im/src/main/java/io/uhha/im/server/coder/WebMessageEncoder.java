package io.uhha.im.server.coder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.uhha.im.server.model.Transportable;

import java.util.List;

/**
 * websocket发送消息前编码
 */
public class WebMessageEncoder extends MessageToMessageEncoder<Transportable> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Transportable data, List<Object> out){
		byte[] body = data.getBody();
		ByteBufAllocator allocator = ctx.channel().config().getAllocator();
		ByteBuf buffer = allocator.buffer(body.length + 1);
		buffer.writeByte(data.getType());
		buffer.writeBytes(body);
		out.add(new BinaryWebSocketFrame(buffer));
	}
}

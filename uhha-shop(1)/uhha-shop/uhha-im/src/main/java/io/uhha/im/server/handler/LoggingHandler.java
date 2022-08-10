package io.uhha.im.server.handler;


import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.logging.LogLevel;

@ChannelHandler.Sharable
public class LoggingHandler extends io.netty.handler.logging.LoggingHandler {

	public LoggingHandler() {
		super(LogLevel.INFO);
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		ctx.fireChannelRegistered();
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		ctx.fireChannelUnregistered();
	}

	@Override
	public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		ctx.deregister(promise);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.fireChannelReadComplete();
	}

	@Override
	public void flush(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
}

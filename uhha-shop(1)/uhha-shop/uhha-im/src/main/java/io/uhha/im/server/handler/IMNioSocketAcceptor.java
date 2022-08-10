package io.uhha.im.server.handler;

import io.uhha.im.server.coder.AppMessageDecoder;
import io.uhha.im.server.coder.AppMessageEncoder;
import io.uhha.im.server.coder.WebMessageDecoder;
import io.uhha.im.server.coder.WebMessageEncoder;
import io.uhha.common.im.constant.IMConstant;
import io.uhha.common.im.constant.ChannelAttr;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.uhha.im.server.model.Ping;
import io.uhha.im.server.model.SentBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Sharable
public class IMNioSocketAcceptor extends SimpleChannelInboundHandler<SentBody>{
	private static final Logger LOGGER = LoggerFactory.getLogger(IMNioSocketAcceptor.class);

    private static final int PONG_TIME_OUT_COUNT = 3;
	private final ThreadFactory bossThreadFactory;
	private final ThreadFactory workerThreadFactory;

	private EventLoopGroup appBossGroup;
	private EventLoopGroup appWorkerGroup;

	private EventLoopGroup webBossGroup;
	private EventLoopGroup webWorkerGroup;

	private final Integer appPort;
	private final Integer webPort;
	private final IMRequestHandler outerRequestHandler;

	private final ChannelHandler loggingHandler = new LoggingHandler();

	/**
	 *  读空闲时间(秒)
	 */
	public final Duration writeIdle = Duration.ofSeconds(45);

	/**
	 *  写接空闲时间(秒)
	 */
	public final Duration readIdle = Duration.ofSeconds(60);

	public IMNioSocketAcceptor(Builder builder){
		this.webPort = builder.webPort;
		this.appPort = builder.appPort;
		this.outerRequestHandler = builder.outerRequestHandler;

		bossThreadFactory = r -> {
			Thread thread = new Thread(r);
			thread.setName("nio-boss-");
			return thread;
		};
		workerThreadFactory = r -> {
			Thread thread = new Thread(r);
			thread.setName("nio-worker-");
			return thread;
		};

	}

	private void createWebEventGroup(){
		if (isLinuxSystem()){
			webBossGroup = new EpollEventLoopGroup(bossThreadFactory);
			webWorkerGroup = new EpollEventLoopGroup(workerThreadFactory);
		}else {
			webBossGroup = new NioEventLoopGroup(bossThreadFactory);
			webWorkerGroup = new NioEventLoopGroup(workerThreadFactory);
		}
	}

	private void createAppEventGroup(){
		if (isLinuxSystem()){
			appBossGroup = new EpollEventLoopGroup(bossThreadFactory);
			appWorkerGroup = new EpollEventLoopGroup(workerThreadFactory);
		}else {
			appBossGroup = new NioEventLoopGroup(bossThreadFactory);
			appWorkerGroup = new NioEventLoopGroup(workerThreadFactory);
		}
	}

	public void bind() {

		if (appPort != null){
			bindAppPort();
		}

		if (webPort != null){
			bindWebPort();
		}
	}

	public void destroy(EventLoopGroup bossGroup , EventLoopGroup workerGroup) {
		if(bossGroup != null && !bossGroup.isShuttingDown() && !bossGroup.isShutdown() ) {
			try {bossGroup.shutdownGracefully();}catch(Exception ignore) {}
		}

		if(workerGroup != null && !workerGroup.isShuttingDown() && !workerGroup.isShutdown() ) {
			try {workerGroup.shutdownGracefully();}catch(Exception ignore) {}
		}
	}

    public void destroy() {
    	this.destroy(appBossGroup,appWorkerGroup);
		this.destroy(webBossGroup,webWorkerGroup);
	}

	private void bindAppPort(){
		createAppEventGroup();
		ServerBootstrap bootstrap = createServerBootstrap(appBossGroup,appWorkerGroup);
		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch){
				ch.pipeline().addLast(new AppMessageDecoder());
				ch.pipeline().addLast(new AppMessageEncoder());
				ch.pipeline().addLast(loggingHandler);
				ch.pipeline().addLast(new IdleStateHandler(readIdle.getSeconds(), writeIdle.getSeconds(), 0, TimeUnit.SECONDS));
				ch.pipeline().addLast(IMNioSocketAcceptor.this);
			}
		});

		ChannelFuture channelFuture = bootstrap.bind(appPort).syncUninterruptibly();
		channelFuture.channel().newSucceededFuture().addListener(future -> {
			String logBanner = "\n\n" +
					"* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n" +
					"*                                                                                   *\n" +
					"*                                                                                   *\n" +
					"*                   App Socket Server started on port {}.                        *\n" +
					"*                                                                                   *\n" +
					"*                                                                                   *\n" +
					"* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n";
			LOGGER.info(logBanner, appPort);
		});
		channelFuture.channel().closeFuture().addListener(future -> this.destroy(appBossGroup,appWorkerGroup));
	}

	private void bindWebPort(){
		createWebEventGroup();
		ServerBootstrap bootstrap = createServerBootstrap(webBossGroup,webWorkerGroup);
		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			public void initChannel(SocketChannel ch){
				ch.pipeline().addLast(new HttpServerCodec());
				ch.pipeline().addLast(new ChunkedWriteHandler());
				ch.pipeline().addLast(new HttpObjectAggregator(65536));
				ch.pipeline().addLast(new WebSocketServerProtocolHandler("/",false));
				ch.pipeline().addLast(new WebMessageDecoder());
				ch.pipeline().addLast(new WebMessageEncoder());
				ch.pipeline().addLast(loggingHandler);
				ch.pipeline().addLast(new IdleStateHandler(readIdle.getSeconds(), writeIdle.getSeconds(), 0, TimeUnit.SECONDS));
				ch.pipeline().addLast(IMNioSocketAcceptor.this);
			}

		});

		ChannelFuture channelFuture = bootstrap.bind(webPort).syncUninterruptibly();
		channelFuture.channel().newSucceededFuture().addListener(future -> {
			String logBanner = "\n\n" +
					"* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n" +
					"*                                                                                   *\n" +
					"*                                                                                   *\n" +
					"*                   Websocket Server started on port {}.                         *\n" +
					"*                                                                                   *\n" +
					"*                                                                                   *\n" +
					"* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n";
			LOGGER.info(logBanner, webPort);
		});
		channelFuture.channel().closeFuture().addListener(future -> this.destroy(webBossGroup,webWorkerGroup));
	}

	private ServerBootstrap createServerBootstrap(EventLoopGroup bossGroup,EventLoopGroup workerGroup){
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup);
		bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
		bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.channel(isLinuxSystem() ? EpollServerSocketChannel.class : NioServerSocketChannel.class);
		return bootstrap;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, SentBody body) {
		/*
		 * 有业务层去处理其他的sentBody
		 */
		outerRequestHandler.process(ctx.channel(), body);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		ctx.channel().attr(ChannelAttr.ID).set(ctx.channel().id().asShortText());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {

		if (ctx.channel().attr(ChannelAttr.UID) == null){
			return;
		}

		SentBody body = new SentBody();
		body.setKey(IMConstant.CLIENT_CONNECT_CLOSED);
		outerRequestHandler.process(ctx.channel(), body);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt){

		if (! (evt instanceof IdleStateEvent)){
			return;
		}

		IdleStateEvent idleEvent = (IdleStateEvent) evt;

		String uid = ctx.channel().attr(ChannelAttr.UID).get();

		/*
		 * 关闭未认证的连接
		 */
		if (idleEvent.state() == IdleState.WRITER_IDLE && uid == null) {
			ctx.close();
			return;
		}

		/*
		 * 已经认证的连接发送心跳请求
		 */
		if (idleEvent.state() == IdleState.WRITER_IDLE && uid != null) {

			Integer pingCount = ctx.channel().attr(ChannelAttr.PING_COUNT).get();
			ctx.channel().attr(ChannelAttr.PING_COUNT).set(pingCount == null ? 1 : pingCount + 1);

			ctx.channel().writeAndFlush(Ping.getInstance());

			return;
		}

		/*
		 * 如果心跳请求发出30秒内没收到响应，则关闭连接
		 */
		Integer pingCount = ctx.channel().attr(ChannelAttr.PING_COUNT).get();
		if (idleEvent.state() == IdleState.READER_IDLE && pingCount != null && pingCount >= PONG_TIME_OUT_COUNT) {
			ctx.close();
			LOGGER.info("{} pong timeout.",ctx.channel());
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		LOGGER.warn("EXCEPTION",cause);
	}


	public static class Builder{

		private Integer appPort;
		private Integer webPort;
		private IMRequestHandler outerRequestHandler;

		public Builder setAppPort(Integer appPort) {
			this.appPort = appPort;
			return this;
		}

		public Builder setWebsocketPort(Integer port) {
			this.webPort = port;
			return this;
		}

		/**
		 * 设置应用层的sentBody处理handler
		 */
		public Builder setOuterRequestHandler(IMRequestHandler outerRequestHandler) {
			this.outerRequestHandler = outerRequestHandler;
			return this;
		}

		public IMNioSocketAcceptor build(){
			return new IMNioSocketAcceptor(this);
		}

	}

	private boolean isLinuxSystem(){
		String osName = System.getProperty("os.name").toLowerCase();
		return osName.contains("linux");
	}

}

package io.uhha.im.server.config;

import io.uhha.im.server.component.handler.annotation.IMHandler;
import io.uhha.im.server.config.properties.IMProperties;
import io.uhha.im.server.group.SessionGroup;
import io.uhha.im.server.group.TagSessionGroup;
import io.uhha.im.server.handler.IMNioSocketAcceptor;
import io.uhha.im.server.handler.IMRequestHandler;
import io.uhha.im.server.model.SentBody;
import io.uhha.im.service.SessionService;
import io.netty.channel.Channel;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(IMProperties.class)
public class IMConfig implements IMRequestHandler, ApplicationListener<ApplicationStartedEvent> {

	@Resource
	private ApplicationContext applicationContext;

	@Resource
	private SessionService sessionService;

	private final HashMap<String,IMRequestHandler> handlerMap = new HashMap<>();

	@Bean
	public SessionGroup sessionGroup() {
		return new SessionGroup();
	}

	@Bean
	public TagSessionGroup tagSessionGroup() {
		return new TagSessionGroup();
	}


	@Bean(destroyMethod = "destroy")
	public IMNioSocketAcceptor getNioSocketAcceptor(IMProperties properties) {

		return new IMNioSocketAcceptor.Builder()
				.setAppPort(properties.getAppPort())
				.setWebsocketPort(properties.getWebsocketPort())
				.setOuterRequestHandler(this)
				.build();

	}

	@Override
	public void process(Channel channel, SentBody body) {

        IMRequestHandler handler = handlerMap.get(body.getKey());

		if(handler == null) {return ;}

		handler.process(channel, body);

	}
	/*
	 * springboot启动完成之后再启动cim服务的，避免服务正在重启时，客户端会立即开始连接导致意外异常发生.
	 */
	@Override
	public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {

		Map<String, IMRequestHandler> beans =  applicationContext.getBeansOfType(IMRequestHandler.class);

		for (Map.Entry<String, IMRequestHandler> entry : beans.entrySet()) {

			IMRequestHandler handler = entry.getValue();

			IMHandler annotation = handler.getClass().getAnnotation(IMHandler.class);

			if (annotation != null){
				handlerMap.put(annotation.key(),handler);
			}
		}


		applicationContext.getBean(IMNioSocketAcceptor.class).bind();

		sessionService.deleteLocalhost();
	}
}

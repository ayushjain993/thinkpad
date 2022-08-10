package io.uhha.im.server.component.handler;

import io.uhha.im.domain.Session;
import io.uhha.im.server.component.handler.annotation.IMHandler;
import io.uhha.im.server.component.redis.SignalRedisTemplate;
import io.uhha.common.im.constant.ChannelAttr;
import io.uhha.im.server.group.SessionGroup;
import io.uhha.im.server.handler.IMRequestHandler;
import io.uhha.im.server.model.ReplyBody;
import io.uhha.im.server.model.SentBody;
import io.uhha.im.service.SessionService;
import io.netty.channel.Channel;
import org.springframework.http.HttpStatus;

import javax.annotation.Resource;

/**
 * 客户长连接 账户绑定实现
 */
@IMHandler(key = "client_bind")
public class BindHandler implements IMRequestHandler {

    @Resource
    private SessionService sessionService;

    @Resource
    private SessionGroup sessionGroup;

    @Resource
    private SignalRedisTemplate signalRedisTemplate;

    @Override
    public void process(Channel channel, SentBody body) {

        ReplyBody reply = new ReplyBody();
        reply.setKey(body.getKey());
        reply.setCode(HttpStatus.OK.value());
        reply.setTimestamp(System.currentTimeMillis());

        String uid = body.get("uid");
        String deviceId = body.get("deviceId");
        Session session = sessionService.getSessionByUidAndDeviceId(uid, deviceId);
        if (session == null) {
            session = new Session();
            session.setUid(uid);
            session.setNid(channel.attr(ChannelAttr.ID).get());
            session.setDeviceId(deviceId);
            session.setChannel(body.get("channel"));
            session.setDeviceName(body.get("deviceName"));
            session.setAppVersion(body.get("appVersion"));
            session.setOsVersion(body.get("osVersion"));
            session.setLanguage(body.get("language"));

            /*
             *存储到数据库
             */
            sessionService.add(session);
        } else {
            session.setBindTime(System.currentTimeMillis());
            sessionService.updateById(session);
        }
        channel.attr(ChannelAttr.UID).set(uid);
        channel.attr(ChannelAttr.CHANNEL).set(session.getChannel());
        channel.attr(ChannelAttr.DEVICE_ID).set(session.getDeviceId());
        channel.attr(ChannelAttr.LANGUAGE).set(session.getLanguage());
        /*
         * 添加到内存管理
         */
        sessionGroup.add(channel);

        /*
         *向客户端发送bind响应
         */
        channel.writeAndFlush(reply);

        /*
         * 发送上线事件到集群中的其他实例，控制其他设备下线
         */
        signalRedisTemplate.bind(session);

    }
}

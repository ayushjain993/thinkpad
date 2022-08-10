package io.uhha.im.server.component.handler;

import io.uhha.im.domain.Session;
import io.uhha.im.server.component.handler.annotation.IMHandler;
import io.uhha.common.im.constant.ChannelAttr;
import io.uhha.im.server.group.SessionGroup;
import io.uhha.im.server.handler.IMRequestHandler;
import io.uhha.im.server.model.SentBody;
import io.uhha.im.service.SessionService;
import io.netty.channel.Channel;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 连接断开时，更新用户相关状态
 */
@IMHandler(key = "client_closed")
public class ClosedHandler implements IMRequestHandler {

    @Resource
    private SessionService sessionService;

    @Resource
    private SessionGroup sessionGroup;

    @Override
    public void process(Channel channel, SentBody message) {

        String uid = channel.attr(ChannelAttr.UID).get();

        if (uid == null) {
            return;
        }

        String nid = channel.attr(ChannelAttr.ID).get();

        sessionGroup.remove(channel);
        sessionService.delete(uid, nid);
    }

}

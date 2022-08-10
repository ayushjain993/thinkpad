package io.uhha.im.server.component.handler;

import io.netty.channel.Channel;
import io.uhha.common.utils.bean.BeanUtils;
import io.uhha.im.server.component.handler.annotation.IMHandler;
import io.uhha.im.server.component.push.DefaultMessagePusher;
import io.uhha.im.server.group.SessionGroup;
import io.uhha.im.server.handler.IMRequestHandler;
import io.uhha.im.server.model.ReplyBody;
import io.uhha.im.server.model.SentBody;
import io.uhha.im.service.ImUserFriendService;
import io.uhha.im.service.MessageService;
import io.uhha.im.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.annotation.Resource;

/**
 * 客户长连接 账户绑定实现
 */
@IMHandler(key = "client_message")
public class MessageHandler implements IMRequestHandler {

    @Resource
    private SessionService sessionService;

    @Resource
    private SessionGroup sessionGroup;

    @Resource
    private DefaultMessagePusher defaultMessagePusher;

    @Autowired
    private ImUserFriendService userFriendService;

    @Autowired
    private MessageService messageService;

    @Override
    public void process(Channel channel, SentBody body) {

        ReplyBody reply = new ReplyBody();
        reply.setKey(body.getKey());
        reply.setCode(HttpStatus.OK.value());
        reply.setTimestamp(System.currentTimeMillis());
        io.uhha.im.server.model.Message message = new io.uhha.im.server.model.Message();
        message.setSender(body.get("sender"));
        message.setReceiver(body.get("receiver"));
        message.setAction(body.get("action"));
        message.setType(body.getInteger("type"));
        message.setContent(body.get("content"));
        message.setFormat(body.get("format"));
        message.setTitle(body.get("title"));
        message.setExtra(body.get("extra"));
        message.setId(System.currentTimeMillis());
        userFriendService.bindRelationship(message.getSender(), message.getReceiver());
        io.uhha.im.domain.Message msg = new io.uhha.im.domain.Message();
        BeanUtils.copyBeanProp(msg, message);
        msg.setReadStatus(Boolean.FALSE);
        messageService.save(msg);
        defaultMessagePusher.push(message);
        channel.writeAndFlush(reply);

    }
}

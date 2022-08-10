package io.uhha.im.server.component.message;

import io.uhha.common.utils.bean.BeanUtils;
import io.uhha.im.server.group.SessionGroup;
import io.uhha.im.server.model.Message;
import io.uhha.im.server.util.JSONUtils;
import io.uhha.im.service.ImUserFriendService;
import io.uhha.im.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * 集群环境下，监听redis队列，广播消息到每个实例进行推送
 * 如果使用MQ的情况也，最好替换为MQ消息队列
 */
@Component
public class PushMessageListener implements MessageListener {

    @Resource
    private SessionGroup sessionGroup;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onMessage(org.springframework.data.redis.connection.Message redisMessage, byte[] bytes) {
        Message message = JSONUtils.fromJson(redisMessage.getBody(), Message.class);
        String uid = message.getReceiver();
        sessionGroup.write(uid, message);
    }
}

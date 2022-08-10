package io.uhha.im.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.uhha.im.domain.ImUserFriend;
import io.uhha.im.domain.Message;
import io.uhha.im.mapper.MessageMapper;
import io.uhha.im.model.ImMessageRequest;
import io.uhha.im.service.MessageService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.web3j.abi.datatypes.Int;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    private QueryWrapper<Message> getQueryWrapper(String userId, String friendId, Integer type) {
        return new QueryWrapper<Message>()
                .eq("sender", userId)
                .eq("receiver", friendId)
                .eq(ObjectUtils.isNotEmpty(type), "type", type)
                .orderByDesc("timestamp");
    }

    @Override
    public List<Message> listMessage(ImMessageRequest imMessageRequest) {
        List<Message> sendMessages = this.list(getQueryWrapper(imMessageRequest.getUserId(), imMessageRequest.getFriendId(), imMessageRequest.getType()));
        List<Message> receiveMessages = this.list(getQueryWrapper(imMessageRequest.getFriendId(), imMessageRequest.getUserId(), imMessageRequest.getType()));
        receiveMessages = receiveMessages.stream().map(message -> message.setReadStatus(Boolean.TRUE)).collect(Collectors.toList());
        List<Long> messageIds = receiveMessages.stream().map(Message::getId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(messageIds)) {
            lambdaUpdate().set(Message::getReadStatus, Boolean.TRUE).in(Message::getId, messageIds).update();
        }
        List<Message> records = Stream.concat(sendMessages.stream(), receiveMessages.stream()).sorted(Comparator.comparing(Message::getTimestamp)).collect(Collectors.toList());

        return records;
    }

    @Override
    public Long getNoReadCount(ImUserFriend imUserFriend) {
        return lambdaQuery().eq(Message::getSender, imUserFriend.getFriendId()).eq(Message::getReceiver, imUserFriend.getUserId()).eq(Message::getReadStatus, Boolean.FALSE).count();
    }

    @Override
    public String getLastMessage(ImUserFriend imUserFriend) {
        Message msg = lambdaQuery().select(Message::getContent).and(wq -> {
          wq.eq(Message::getSender, imUserFriend.getFriendId()).eq(Message::getReceiver, imUserFriend.getUserId());
        }).or(wq->{
            wq.eq(Message::getSender, imUserFriend.getUserId()).eq(Message::getReceiver, imUserFriend.getFriendId());
        }).orderByDesc(Message::getTimestamp).last("limit 1").one();
        return Optional.ofNullable(msg).map(Message::getContent).orElse(null);
    }


}

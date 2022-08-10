package io.uhha.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.uhha.im.domain.ImUserFriend;
import io.uhha.im.domain.Message;
import io.uhha.im.model.ImMessageRequest;

import java.util.List;

public interface MessageService extends IService<Message> {

    /**
     * 查询消息
     *
     * @param imMessageRequest
     * @return
     */
    List<Message> listMessage(ImMessageRequest imMessageRequest);

    /**
     * 获取未读数量
     *
     * @param imUserFriend
     * @return
     */
    Long getNoReadCount(ImUserFriend imUserFriend);

    /**
     * 获取最新消息
     *
     * @param imUserFriend
     * @return
     */
    String getLastMessage(ImUserFriend imUserFriend);
}

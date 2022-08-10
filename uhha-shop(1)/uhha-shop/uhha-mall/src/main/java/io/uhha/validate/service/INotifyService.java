package io.uhha.validate.service;

import io.uhha.validate.dto.NotifyMsgDTO;

public interface INotifyService {
    /**
     * 发送消息
     * @param vs 消息传输对象
     */
    public Boolean updateSend(NotifyMsgDTO vs);


}

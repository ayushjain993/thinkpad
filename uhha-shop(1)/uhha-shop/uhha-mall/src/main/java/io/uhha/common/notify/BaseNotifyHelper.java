package io.uhha.common.notify;

import io.uhha.member.domain.UmsMember;
import io.uhha.member.service.IUmsMemberService;
import io.uhha.order.domain.OmsOrder;
import io.uhha.order.service.IOmsOrderService;
import io.uhha.setting.domain.LsStationLetter;
import io.uhha.validate.dto.NotifyMsgDTO;
import io.uhha.validate.service.INotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class BaseNotifyHelper {
    @Autowired
    private INotifyService notifyService;

    @Autowired
    private IUmsMemberService memberService;

    @Autowired
    private IOmsOrderService omsOrderService;

    /**
     * 发送邮件
     *
     * @param sendDTO 验证实体 {@link NotifyMsgDTO}
     */
    protected Boolean mailSendBase(NotifyMsgDTO sendDTO) {
        if (!notifyService.updateSend(sendDTO)) {
            log.error("Fail to send message \r\n :{}", sendDTO);
            return false;
        }
        return true;
    }

    /**
     * 新版发送通知短信调用
     */
    protected boolean sendBaseSms(NotifyMsgDTO send) {
        if (!notifyService.updateSend(send)) {
            log.error("Fail to send message \r\n :{}", send);
            return false;
        }
        return true;
    }

    /**
     * 发送站内信
     */
    protected boolean sendStationLetter(NotifyMsgDTO send){
        if (!notifyService.updateSend(send)) {
            log.error("Fail to send message \r\n :{}", send);
            return false;
        }
        return true;

    }

    /**
     * 通过uid获取用户的电话号码
     * @param uid
     * @return
     */
    protected UmsMember getUmsMemberByUid(Long uid) {
        UmsMember customer = memberService.selectUmsMemberById(uid);
        if (customer == null) {
            log.error("no phoneNumber bind for user : {}", uid);
            return customer;
        }
        return customer;
    }


    protected OmsOrder getOmsOrderById(Long orderId) {
        OmsOrder omsOrder = omsOrderService.selectOmsOrderById(orderId);
        if (omsOrder == null) {
            log.error("no OmsOrder found for order: {}", orderId);
            return omsOrder;
        }
        return omsOrder;
    }
}

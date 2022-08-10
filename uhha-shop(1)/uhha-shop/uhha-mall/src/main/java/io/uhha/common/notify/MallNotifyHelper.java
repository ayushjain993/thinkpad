package io.uhha.common.notify;

import io.uhha.coin.common.Enum.validate.BusinessTypeEnum;
import io.uhha.coin.common.Enum.validate.LocaleEnum;
import io.uhha.coin.common.Enum.validate.PlatformEnum;
import io.uhha.coin.common.Enum.validate.SendTypeEnum;
import io.uhha.common.enums.AuditStatusEnum;
import io.uhha.member.domain.UmsMember;
import io.uhha.member.domain.UmsWithdraw;
import io.uhha.member.vo.UmsRedeemVo;
import io.uhha.member.vo.UmsTransferVo;
import io.uhha.member.vo.UmsWithdrawVo;
import io.uhha.order.domain.OmsBackOrder;
import io.uhha.order.domain.OmsOrder;
import io.uhha.order.service.IOmsBackOrderService;
import io.uhha.order.vo.*;
import io.uhha.setting.service.ILsStationLetterService;
import io.uhha.shortvideo.domain.ShortVideo;
import io.uhha.shortvideo.domain.ShortVideoComment;
import io.uhha.shortvideo.service.IShortVideoCommentService;
import io.uhha.shortvideo.service.IShortVideoService;
import io.uhha.shortvideo.vo.ShortVideoVo;
import io.uhha.validate.dto.NotifyMsgDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class MallNotifyHelper extends BaseNotifyHelper {

    @Autowired
    private IOmsBackOrderService backOrderService;

    @Autowired
    private ILsStationLetterService stationLetterService;

    @Autowired
    private IShortVideoService shortVideoService;

    @Autowired
    private IShortVideoCommentService shortVideoCommentService;

    public boolean redeemPointsToToken(UmsRedeemVo umsRedeemVo, Long uid){
        if(uid==null){
            log.error("invalid parameter: uid={}", uid);
            return false;
        }
        UmsMember member = getUmsMemberByUid(uid);
        if(member==null){
            log.error("user not found for uid: {}", uid);
            return false;
        }
        //发送短消息
        boolean ret1;
        NotifyMsgDTO smsNotifyMsgDTO = NotifyMsgDTO.builder()
                .uid(member.getId())
                .price(umsRedeemVo.getToken())
                .businessType(BusinessTypeEnum.REDEEM_POINTS_TOKEN.getCode())
                .languageType(LocaleEnum.EN_US.getCode())
                .platformType(PlatformEnum.UHHA.getCode())
                .sendType(SendTypeEnum.SMS_TEXT.getCode())
                .phone(member.getMobile())
                .build();
        ret1 = sendBaseSms(smsNotifyMsgDTO);

        //发送邮件
        boolean ret2;
        if(member.getEmail()==null){
            log.error("user doesn't config email. uid= {}", uid);
            ret2 = false;
        }
        NotifyMsgDTO emailNotifyMsgDTO = NotifyMsgDTO.builder()
                .uid(member.getId())
                .email(member.getEmail())
                .price(umsRedeemVo.getToken())
                .businessType(BusinessTypeEnum.REDEEM_POINTS_TOKEN.getCode())
                .languageType(LocaleEnum.EN_US.getCode())
                .platformType(PlatformEnum.UHHA.getCode())
                .sendType(SendTypeEnum.EMAIL.getCode())
                .build();
        ret2 = mailSendBase(emailNotifyMsgDTO);

        //发送站内信
        boolean ret3;
        NotifyMsgDTO slNotifyMsgDTO = NotifyMsgDTO.builder()
                .uid(member.getId())
                .price(umsRedeemVo.getToken())
                .businessType(BusinessTypeEnum.REDEEM_POINTS_TOKEN.getCode())
                .languageType(LocaleEnum.EN_US.getCode())
                .platformType(PlatformEnum.UHHA.getCode())
                .sendType(SendTypeEnum.STATION_LETTER.getCode())
                .build();
        ret3 = sendStationLetter(slNotifyMsgDTO);
        return ret1 & ret2 & ret3;
    }

    public boolean commissionTransferToBalance(UmsTransferVo umsTransferVo, Long uid){


        if(uid==null){
            log.error("invalid parameter: uid={}", uid);
            return false;
        }
        UmsMember member = getUmsMemberByUid(uid);
        if(member==null){
            log.error("user not found for uid: {}", uid);
            return false;
        }
        //发送短消息
        boolean ret1;
        NotifyMsgDTO smsNotifyMsgDTO = NotifyMsgDTO.builder()
                .uid(member.getId())
                .price(umsTransferVo.getMoney())
                .businessType(BusinessTypeEnum.SMS_COMMISSION_TO_BALANCE.getCode())
                .languageType(LocaleEnum.EN_US.getCode())
                .platformType(PlatformEnum.UHHA.getCode())
                .sendType(SendTypeEnum.SMS_TEXT.getCode())
                .phone(member.getMobile())
                .build();
        ret1 = sendBaseSms(smsNotifyMsgDTO);

        //发送邮件
        boolean ret2;
        if(member.getEmail()==null){
            log.error("user doesn't config email. uid= {}", uid);
            ret2 = false;
        }
        NotifyMsgDTO emailNotifyMsgDTO = NotifyMsgDTO.builder()
                .uid(member.getId())
                .email(member.getEmail())
                .price(umsTransferVo.getMoney())
                .businessType(BusinessTypeEnum.SMS_COMMISSION_TO_BALANCE.getCode())
                .languageType(LocaleEnum.EN_US.getCode())
                .platformType(PlatformEnum.UHHA.getCode())
                .sendType(SendTypeEnum.EMAIL.getCode())
                .build();
        ret2 = mailSendBase(emailNotifyMsgDTO);

        //发送站内信
        boolean ret3;
        NotifyMsgDTO slNotifyMsgDTO = NotifyMsgDTO.builder()
                .uid(member.getId())
                .price(umsTransferVo.getMoney())
                .businessType(BusinessTypeEnum.SMS_COMMISSION_TO_BALANCE.getCode())
                .languageType(LocaleEnum.EN_US.getCode())
                .platformType(PlatformEnum.UHHA.getCode())
                .sendType(SendTypeEnum.STATION_LETTER.getCode())
                .build();
        ret3 = sendStationLetter(slNotifyMsgDTO);
        return ret1 & ret2 & ret3;
    }

    /**
     * 申请提现
     * @param withdrawVo
     * @return
     */
    public boolean applyWithdrawNotify(UmsWithdraw withdrawVo){

        Long uid = withdrawVo.getCustomerId();

        if(uid==null){
            log.error("invalid parameter: uid={}", uid);
            return false;
        }
        UmsMember member = getUmsMemberByUid(uid);
        if(member==null){
            log.error("user not found for uid: {}", uid);
            return false;
        }
        //发送短消息
        boolean ret1;
        NotifyMsgDTO smsNotifyMsgDTO = NotifyMsgDTO.builder()
                .uid(member.getId())
                .price(withdrawVo.getMoney())
                .businessType(BusinessTypeEnum.SMS_FAIT_WITHDRAW_NOTIFY.getCode())
                .languageType(LocaleEnum.EN_US.getCode())
                .platformType(PlatformEnum.UHHA.getCode())
                .sendType(SendTypeEnum.SMS_TEXT.getCode())
                .phone(member.getMobile())
                .build();
        ret1 = sendBaseSms(smsNotifyMsgDTO);

        //发送邮件
        boolean ret2;
        if(member.getEmail()==null){
            log.error("user doesn't config email. uid= {}", uid);
            ret2 = false;
        }
        NotifyMsgDTO emailNotifyMsgDTO = NotifyMsgDTO.builder()
                .uid(member.getId())
                .email(member.getEmail())
                .price(withdrawVo.getMoney())
                .businessType(BusinessTypeEnum.SMS_FAIT_WITHDRAW_NOTIFY.getCode())
                .languageType(LocaleEnum.EN_US.getCode())
                .platformType(PlatformEnum.UHHA.getCode())
                .sendType(SendTypeEnum.EMAIL.getCode())
                .build();
        ret2 = mailSendBase(emailNotifyMsgDTO);

        //发送站内信
        boolean ret3;
        NotifyMsgDTO slNotifyMsgDTO = NotifyMsgDTO.builder()
                .uid(member.getId())
                .price(withdrawVo.getMoney())
                .businessType(BusinessTypeEnum.SMS_FAIT_WITHDRAW_NOTIFY.getCode())
                .languageType(LocaleEnum.EN_US.getCode())
                .platformType(PlatformEnum.UHHA.getCode())
                .sendType(SendTypeEnum.STATION_LETTER.getCode())
                .build();
        ret3 = sendStationLetter(slNotifyMsgDTO);
        return ret1 & ret2 & ret3;
    }

    public boolean withdrawStatusUpdate(BigDecimal money, Long uid, String status){

        int bizType = 0;
        if("1".equals(status)){
            bizType = BusinessTypeEnum.WITHDRAW_APPROVED.getCode();
        }else if("2".equals(status)){
            bizType = BusinessTypeEnum.WITHDRAW_REJECTED.getCode();
        }else if("3".equalsIgnoreCase(status)){
            bizType = BusinessTypeEnum.WITHDRAW_RELEASED.getCode();
        }else{
            log.error("invalid status type:{}", status);
            return false;
        }

        if(uid==null){
            log.error("invalid parameter: uid={}", uid);
            return false;
        }
        UmsMember member = getUmsMemberByUid(uid);
        if(member==null){
            log.error("user not found for uid: {}", uid);
            return false;
        }

        //发送短消息
        boolean ret1;
        NotifyMsgDTO smsNotifyMsgDTO = NotifyMsgDTO.builder()
                .uid(member.getId())
                .price(money)
                .businessType(bizType)
                .languageType(LocaleEnum.EN_US.getCode())
                .platformType(PlatformEnum.UHHA.getCode())
                .sendType(SendTypeEnum.SMS_TEXT.getCode())
                .phone(member.getMobile())
                .build();
        ret1 = sendBaseSms(smsNotifyMsgDTO);

        //发送邮件
        boolean ret2;
        if(member.getEmail()==null){
            log.error("user doesn't config email. uid= {}", uid);
            ret2 = false;
        }
        NotifyMsgDTO emailNotifyMsgDTO = NotifyMsgDTO.builder()
                .uid(member.getId())
                .email(member.getEmail())
                .price(money)
                .businessType(bizType)
                .languageType(LocaleEnum.EN_US.getCode())
                .platformType(PlatformEnum.UHHA.getCode())
                .sendType(SendTypeEnum.EMAIL.getCode())
                .build();
        ret2 = mailSendBase(emailNotifyMsgDTO);

        //发送站内信
        boolean ret3;
        NotifyMsgDTO slNotifyMsgDTO = NotifyMsgDTO.builder()
                .uid(member.getId())
                .price(money)
                .businessType(bizType)
                .languageType(LocaleEnum.EN_US.getCode())
                .platformType(PlatformEnum.UHHA.getCode())
                .sendType(SendTypeEnum.STATION_LETTER.getCode())
                .build();
        ret3 = sendStationLetter(slNotifyMsgDTO);
        return ret1 & ret2 & ret3;
    }

    public boolean moneyReleased(BigDecimal money, Long uid){
        return true;
    }

    /**
     * 按设计要求同时发送短信和电子邮件
     *
     * @param submitOrderParams 提交订单参数
     * @param submitOrderResponse 确认订单返回值
     * @return 消息是否发送成功
     */
    public boolean orderSubmitted(SubmitOrderParams submitOrderParams, SubmitOrderResponse submitOrderResponse) {
        Long uid = submitOrderParams.getCustomerId();

        if(uid==null){
            log.error("invalid parameter: uid={}", uid);
            return false;
        }
        UmsMember member = getUmsMemberByUid(uid);
        if(member==null){
            log.error("user not found for uid: {}", uid);
            return false;
        }
        //发送短消息
        boolean ret1;
        NotifyMsgDTO smsNotifyMsgDTO = NotifyMsgDTO.builder()
                .uid(member.getId())
                .price(submitOrderResponse.getOrderMoney())
                .businessType(BusinessTypeEnum.ORDER_SUBMITTED.getCode())
                .languageType(LocaleEnum.EN_US.getCode())
                .platformType(PlatformEnum.UHHA.getCode())
                .sendType(SendTypeEnum.SMS_TEXT.getCode())
                .orderNo(submitOrderResponse.getOrderCode())
                .phone(member.getMobile())
                .build();
        ret1 = sendBaseSms(smsNotifyMsgDTO);

        //发送邮件
        boolean ret2;
        if(member.getEmail()==null){
            log.error("user doesn't config email. uid= {}", uid);
            ret2 = false;
        }
        NotifyMsgDTO emailNotifyMsgDTO = NotifyMsgDTO.builder()
                .uid(member.getId())
                .email(member.getEmail())
                .price(submitOrderResponse.getOrderMoney())
                .businessType(BusinessTypeEnum.ORDER_SUBMITTED.getCode())
                .languageType(LocaleEnum.EN_US.getCode())
                .platformType(PlatformEnum.UHHA.getCode())
                .sendType(SendTypeEnum.EMAIL.getCode())
                .orderNo(submitOrderResponse.getOrderCode())
                .build();
        ret2 = mailSendBase(emailNotifyMsgDTO);

        //发送站内信
        boolean ret3;
        NotifyMsgDTO slNotifyMsgDTO = NotifyMsgDTO.builder()
                .uid(member.getId())
                .price(submitOrderResponse.getOrderMoney())
                .businessType(BusinessTypeEnum.ORDER_SUBMITTED.getCode())
                .languageType(LocaleEnum.EN_US.getCode())
                .platformType(PlatformEnum.UHHA.getCode())
                .sendType(SendTypeEnum.STATION_LETTER.getCode())
                .orderNo(submitOrderResponse.getOrderCode())
                .build();
        ret3 = sendStationLetter(slNotifyMsgDTO);
        return ret1 & ret2 & ret3;
    }

    /**
     * 确认收货
     * @param orderId 订单号
     * @param customerId 客户id
     * @return 消息是否发送成功
     */
    public boolean confirmReceipt(long orderId, long customerId) {
        return sendOrderNotifies(orderId, customerId, BusinessTypeEnum.ORDER_RECEIVED);
    }

    /**
     * 订单已支付
     * @param confirmOrderParams 确认订单参数
     * @return 消息是否发送成功
     */
    public boolean confirmOrderPayed(ConfirmOrderParams confirmOrderParams) {
        Long uid = confirmOrderParams.getCustomerId();
        List<String> orderCodes = confirmOrderParams.getOrderCodes();

        return sendOrderNotifies(orderCodes, uid, BusinessTypeEnum.ORDER_PAID);
    }


    /**
     * 发货通知
     * @param orderId
     * @param waybillCode
     * @param companyName
     * @return
     */
    public boolean deliverOrder(long orderId, String waybillCode, String companyName){
        return sendOrderNotifies(orderId, waybillCode, companyName, BusinessTypeEnum.ORDER_DELIVER);
    }

    public boolean cancelOrder(CancelOrderParams cancelOrderParams) {
        Long uid = cancelOrderParams.getCustomerId();
        Long orderId = cancelOrderParams.getOrderId();

        return sendOrderNotifies(orderId, uid, BusinessTypeEnum.ORDER_CANCELLED);
    }

    public boolean cancelDepositPreSaleOrder(CancelOrderParams cancelOrderParams) {
        Long uid = cancelOrderParams.getCustomerId();
        Long orderId = cancelOrderParams.getOrderId();

        return sendOrderNotifies(orderId, uid, BusinessTypeEnum.ORDER_DEPOSIT_CANCELLED);
    }

    /**
     * 申请退款
     *
     * @param applyBackOrderParams 申请退款参数
     * @return 是否发送成功
     */
    public boolean applyRefundOrder(ApplyBackOrderParams applyBackOrderParams) {
        Long uid = applyBackOrderParams.getCustomerId();
        Long orderId = applyBackOrderParams.getOrderId();
        return sendOrderNotifies(orderId, uid, BusinessTypeEnum.ORDER_REFUND_REQ);

    }

    /**
     * 同意退款
     *
     * @param backOrderId 退单号
     * @param message 消息
     * @return 是否发送成功
     */
    public boolean agreeToRefund(Long backOrderId, String message) {
        OmsBackOrder omsBackOrder = backOrderService.selectOmsBackOrderById(backOrderId);
        Long uid = omsBackOrder.getCustomerId();
        String orderCode = omsBackOrder.getOrderCode();
        List<String> orderCodes = new ArrayList<>();
        orderCodes.add(orderCode);
        return sendOrderNotifies(orderCodes, uid, BusinessTypeEnum.ORDER_REFUND_SUCCEED);
    }

    /**
     * 申请退货
     *
     * @param applyReturnParams 申请退货参数
     * @return 是否发送成功
     */
    public boolean applyReturnOrder(ApplyReturnParams applyReturnParams) {
        Long uid = applyReturnParams.getCustomerId();
        Long orderId = applyReturnParams.getOrderId();
        return sendOrderNotifies(orderId, uid, BusinessTypeEnum.ORDER_REFUND_REQ);
    }


    /**
     * 确认退货完成
     *
     * @param backOrderId 退单号
     * @param message 消息
     * @param money 金额
     * @return 是否发送成功
     */
    public boolean confirmReturn(Long backOrderId, String message, BigDecimal money) {
        OmsBackOrder omsBackOrder = backOrderService.selectOmsBackOrderById(backOrderId);
        Long uid = omsBackOrder.getCustomerId();
        Long orderId = omsBackOrder.getOrderId();
        return sendOrderNotifies(orderId, uid, BusinessTypeEnum.ORDER_RETURN_SUCCEED);
    }

    /**
     * 管理员通过或者拒绝视频的审批
     *
     * @param shortVideo 视频
     * @param message 消息
     * @return 是否发送成功
     */
    public boolean adminApproveRejectVideo(ShortVideo shortVideo, String message, String status) {

        if(AuditStatusEnum.PASS.getCode().equals(status)){
            return sendVideoAdminNotifies(shortVideo, "", BusinessTypeEnum.VIDEO_SCREEN_APPROVED);
        }else if(AuditStatusEnum.REJECTED.getCode().equals(status)){
            return sendVideoAdminNotifies(shortVideo, message, BusinessTypeEnum.VIDEO_SCREEN_REJECTED);
        }else{
            log.error("invalid status: {} for adminApproveRejectVideo", status);
            return true;
        }
    }

    /**
     * 管理员通过或者拒绝实名的审批
     *
     * @param uid 用户id
     * @param message 消息
     * @return 是否发送成功
     */
    public boolean adminApproveRejectRealname(Long uid, String message, String status) {

        UmsMember member = getUmsMemberByUid(uid);
        if(member==null){
            log.error("user not found for uid: {}", uid);
            return false;
        }

        if(AuditStatusEnum.PASS.getCode().equals(status)){
            return sendRealnameAdminNotifies(member, "", BusinessTypeEnum.REALNAME_SCREEN_APPROVED);
        }else if(AuditStatusEnum.REJECTED.getCode().equals(status)){
            return sendRealnameAdminNotifies(member, message, BusinessTypeEnum.REALNAME_SCREEN_REJECTED);
        }else{
            log.error("invalid status: {} for adminApproveRejectRealname", status);
            return true;
        }
    }

    /**
     * 批量发送用户评论视频通知
     *
     * @return 是否发送成功
     */
    public void sendVideoCommentNotify(List<ShortVideoComment> commentList) {

        commentList.forEach(comment->{
            String videoId = comment.getVideoId();
            ShortVideoVo shortVideoVo = shortVideoService.getShortVideoById(videoId);
            UmsMember member = getUmsMemberByUid(shortVideoVo.getUid());
            if(member==null){
                log.error("user not found for uid: {}", shortVideoVo.getUid());
                return;
            }
            //向视频作者发送站内信
            NotifyMsgDTO slNotifyMsgDTO = NotifyMsgDTO.builder()
                    .uid(member.getId())
                    .username(member.getUsername())
                    .comment(comment.getContent())
                    .videoTitle(shortVideoVo.getContent())
                    .businessType(BusinessTypeEnum.VIDEO_COMMENTED.getCode())
                    .languageType(LocaleEnum.EN_US.getCode())
                    .platformType(PlatformEnum.UHHA.getCode())
                    .sendType(SendTypeEnum.STATION_LETTER.getCode())
                    .build();
            sendStationLetter(slNotifyMsgDTO);
        });

    }

    private boolean sendOrderNotifies(Long orderId, Long uid, BusinessTypeEnum orderBusinessType) {
        UmsMember member = getUmsMemberByUid(uid);
        if(member==null){
            log.error("user not found for uid: {}", uid);
            return false;
        }
        OmsOrder omsOrder = getOmsOrderById(orderId);
        if(omsOrder==null){
            log.error("order not found for orderId: {}", orderId);
            return false;
        }
        //发送短消息
        boolean ret1;
        NotifyMsgDTO smsNotifyMsgDTO = NotifyMsgDTO.builder()
                .uid(member.getId())
                .price(omsOrder.getPrice())
                .businessType(orderBusinessType.getCode())
                .languageType(LocaleEnum.EN_US.getCode())
                .platformType(PlatformEnum.UHHA.getCode())
                .sendType(SendTypeEnum.SMS_TEXT.getCode())
                .orderNo(omsOrder.getOrderCode())
                .phone(member.getMobile())
                .build();
        ret1 = sendBaseSms(smsNotifyMsgDTO);

        //发送邮件
        boolean ret2;
        if(member.getEmail()==null){
            log.error("user doesn't config email. customerId= {}", uid);
            ret2 = false;
        }else{
            NotifyMsgDTO emailNotifyMsgDTO = NotifyMsgDTO.builder()
                    .uid(member.getId())
                    .email(member.getEmail())
                    .price(omsOrder.getPrice())
                    .businessType(orderBusinessType.getCode())
                    .languageType(LocaleEnum.EN_US.getCode())
                    .platformType(PlatformEnum.UHHA.getCode())
                    .sendType(SendTypeEnum.EMAIL.getCode())
                    .orderNo(omsOrder.getOrderCode())
                    .build();
            ret2 = mailSendBase(emailNotifyMsgDTO);
        }

        //发送站内信
        boolean ret3;
        NotifyMsgDTO slNotifyMsgDTO = NotifyMsgDTO.builder()
                .uid(member.getId())
                .price(omsOrder.getPrice())
                .businessType(orderBusinessType.getCode())
                .languageType(LocaleEnum.EN_US.getCode())
                .platformType(PlatformEnum.UHHA.getCode())
                .sendType(SendTypeEnum.STATION_LETTER.getCode())
                .orderNo(omsOrder.getOrderCode())
                .build();
        ret3 = sendStationLetter(slNotifyMsgDTO);
        return ret1 & ret2 & ret3;
    }

    private boolean sendOrderNotifies(Long orderId, String waybillCode, String logisticCompanyName, BusinessTypeEnum orderBusinessType) {
        OmsOrder omsOrder = getOmsOrderById(orderId);
        if(omsOrder==null){
            log.error("order not found for orderId: {}", orderId);
            return false;
        }
        Long uid = omsOrder.getCustomerId();

        UmsMember member = getUmsMemberByUid(uid);
        if(member==null){
            log.error("user not found for uid: {}", uid);
            return false;
        }

        //发送短消息
        boolean ret1;
        NotifyMsgDTO smsNotifyMsgDTO = NotifyMsgDTO.builder()
                .uid(member.getId())
                .price(omsOrder.getPrice())
                .businessType(orderBusinessType.getCode())
                .languageType(LocaleEnum.EN_US.getCode())
                .platformType(PlatformEnum.UHHA.getCode())
                .sendType(SendTypeEnum.SMS_TEXT.getCode())
                .orderNo(omsOrder.getOrderCode())
                .phone(member.getMobile())
                .waybillCode(waybillCode)
                .logisticCompanyName(logisticCompanyName)
                .build();
        ret1 = sendBaseSms(smsNotifyMsgDTO);

        //发送邮件
        boolean ret2;
        if(member.getEmail()==null){
            log.error("user doesn't config email. customerId= {}", uid);
            ret2 = false;
        }else{
            NotifyMsgDTO emailNotifyMsgDTO = NotifyMsgDTO.builder()
                    .uid(member.getId())
                    .email(member.getEmail())
                    .price(omsOrder.getPrice())
                    .businessType(orderBusinessType.getCode())
                    .languageType(LocaleEnum.EN_US.getCode())
                    .platformType(PlatformEnum.UHHA.getCode())
                    .sendType(SendTypeEnum.EMAIL.getCode())
                    .orderNo(omsOrder.getOrderCode())
                    .phone(member.getMobile())
                    .waybillCode(waybillCode)
                    .logisticCompanyName(logisticCompanyName)
                    .build();
            ret2 = mailSendBase(emailNotifyMsgDTO);
        }

        //发送站内信
        boolean ret3;
        NotifyMsgDTO slNotifyMsgDTO = NotifyMsgDTO.builder()
                .uid(member.getId())
                .price(omsOrder.getPrice())
                .businessType(orderBusinessType.getCode())
                .languageType(LocaleEnum.EN_US.getCode())
                .platformType(PlatformEnum.UHHA.getCode())
                .sendType(SendTypeEnum.STATION_LETTER.getCode())
                .orderNo(omsOrder.getOrderCode())
                .phone(member.getMobile())
                .waybillCode(waybillCode)
                .logisticCompanyName(logisticCompanyName)
                .build();
        ret3 = sendStationLetter(slNotifyMsgDTO);
        return ret1 & ret2 & ret3;
    }

    private boolean sendOrderNotifies(List<String> orderCodes, Long uid, BusinessTypeEnum orderBusinessType) {
        UmsMember member = getUmsMemberByUid(uid);
        if(member==null){
            log.error("user not found for uid: {}", uid);
            return false;
        }
        String orderNos = String.join(",", orderCodes);

        //发送短消息
        boolean ret1;
        NotifyMsgDTO smsNotifyMsgDTO = NotifyMsgDTO.builder()
                .uid(member.getId())
                .businessType(orderBusinessType.getCode())
                .languageType(LocaleEnum.EN_US.getCode())
                .platformType(PlatformEnum.UHHA.getCode())
                .sendType(SendTypeEnum.SMS_TEXT.getCode())
                .orderNo(orderNos)
                .phone(member.getMobile())
                .build();
        ret1 = sendBaseSms(smsNotifyMsgDTO);

        //发送邮件
        boolean ret2;
        if(member.getEmail()==null){
            log.error("user doesn't config email. customerId= {}", uid);
            ret2 = false;
        }else {
            NotifyMsgDTO emailNotifyMsgDTO = NotifyMsgDTO.builder()
                    .uid(member.getId())
                    .email(member.getEmail())
                    .businessType(orderBusinessType.getCode())
                    .languageType(LocaleEnum.EN_US.getCode())
                    .platformType(PlatformEnum.UHHA.getCode())
                    .sendType(SendTypeEnum.EMAIL.getCode())
                    .orderNo(orderNos)
                    .build();
            ret2 = mailSendBase(emailNotifyMsgDTO);
        }

        //发送站内信
        boolean ret3;
        NotifyMsgDTO slNotifyMsgDTO = NotifyMsgDTO.builder()
                .uid(member.getId())
                .businessType(orderBusinessType.getCode())
                .languageType(LocaleEnum.EN_US.getCode())
                .platformType(PlatformEnum.UHHA.getCode())
                .sendType(SendTypeEnum.STATION_LETTER.getCode())
                .orderNo(orderNos)
                .build();
        ret3 = sendStationLetter(slNotifyMsgDTO);
        return ret1 & ret2 & ret3;
    }

    private boolean sendRealnameAdminNotifies(UmsMember member, String reasons, BusinessTypeEnum orderBusinessType) {
        Long uid = member.getId();

        //发送短消息
        boolean ret1;
        NotifyMsgDTO smsNotifyMsgDTO = NotifyMsgDTO.builder()
                .uid(uid)
                .businessType(orderBusinessType.getCode())
                .languageType(LocaleEnum.EN_US.getCode())
                .platformType(PlatformEnum.UHHA.getCode())
                .sendType(SendTypeEnum.SMS_TEXT.getCode())
                .reason(reasons)
                .phone(member.getMobile())
                .build();
        ret1 = sendBaseSms(smsNotifyMsgDTO);

        //发送邮件
        boolean ret2;
        if(member.getEmail()==null){
            log.error("user doesn't config email. customerId= {}", uid);
            ret2 = false;
        }else{
            NotifyMsgDTO emailNotifyMsgDTO = NotifyMsgDTO.builder()
                    .uid(member.getId())
                    .email(member.getEmail())
                    .businessType(orderBusinessType.getCode())
                    .languageType(LocaleEnum.EN_US.getCode())
                    .platformType(PlatformEnum.UHHA.getCode())
                    .reason(reasons)
                    .sendType(SendTypeEnum.EMAIL.getCode())
                    .build();
            ret2 = mailSendBase(emailNotifyMsgDTO);
        }

        //发送站内信
        boolean ret3;
        NotifyMsgDTO slNotifyMsgDTO = NotifyMsgDTO.builder()
                .uid(member.getId())
                .businessType(orderBusinessType.getCode())
                .languageType(LocaleEnum.EN_US.getCode())
                .platformType(PlatformEnum.UHHA.getCode())
                .reason(reasons)
                .sendType(SendTypeEnum.STATION_LETTER.getCode())
                .build();
        ret3 = sendStationLetter(slNotifyMsgDTO);
        return ret2 & ret3;
    }

    private boolean sendVideoAdminNotifies(ShortVideo shortVideo, String reasons, BusinessTypeEnum orderBusinessType) {
        Long uid = shortVideo.getUid();
        UmsMember member = getUmsMemberByUid(uid);
        if(member==null){
            log.error("user not found for uid: {}", uid);
            return false;
        }

        //发送邮件
        boolean ret2;
        if(member.getEmail()==null){
            log.error("user doesn't config email. customerId= {}", uid);
            ret2 = false;
        }else{
            NotifyMsgDTO emailNotifyMsgDTO = NotifyMsgDTO.builder()
                    .uid(member.getId())
                    .email(member.getEmail())
                    .businessType(orderBusinessType.getCode())
                    .languageType(LocaleEnum.EN_US.getCode())
                    .platformType(PlatformEnum.UHHA.getCode())
                    .videoTitle(shortVideo.getContent())
                    .reason(reasons)
                    .sendType(SendTypeEnum.EMAIL.getCode())
                    .build();
            ret2 = mailSendBase(emailNotifyMsgDTO);
        }

        //发送站内信
        boolean ret3;
        NotifyMsgDTO slNotifyMsgDTO = NotifyMsgDTO.builder()
                .uid(member.getId())
                .businessType(orderBusinessType.getCode())
                .languageType(LocaleEnum.EN_US.getCode())
                .platformType(PlatformEnum.UHHA.getCode())
                .videoTitle(shortVideo.getContent())
                .reason(reasons)
                .sendType(SendTypeEnum.STATION_LETTER.getCode())
                .build();
        ret3 = sendStationLetter(slNotifyMsgDTO);
        return ret2 & ret3;
    }

}

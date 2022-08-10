package io.uhha.member.service;


import io.uhha.common.utils.bean.WechatSetting;

/**
 * 微信小程序access_token服务接口
 *
 * @author mj created on 2020/4/28
 */
public interface WxAppletAccessTokenService {

    /**
     * 获取微信小程序accesstoken
     *
     * @param wechatSetting 微信设置实体
     * @return 微信小程序accesstoken
     */
    String getWxAppletAccessToken(WechatSetting wechatSetting);

}

package io.uhha.member.service.impl;


import io.uhha.common.core.redis.RedisCache;
import io.uhha.common.utils.WeChatAppletUtils;
import io.uhha.common.utils.bean.WechatSetting;
import io.uhha.member.service.WxAppletAccessTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * 微信小程序access_token服务接口实现类
 *
 * @author mj created on 2020/4/28
 */
@Service
@Slf4j
public class WxAppletAccessTokenServiceImpl implements WxAppletAccessTokenService {

    /**
     * 注入redis服务接口
     */
    @Autowired
    private RedisCache redisService;


    /**
     * 获取微信小程序accesstoken
     *
     * @param wechatSetting 微信设置实体
     * @return 微信小程序accesstoken
     */
    @Override
    public String getWxAppletAccessToken(WechatSetting wechatSetting) {
        log.debug("getWxAppletAccessToken and wechatSetting :{}", wechatSetting);
        String wxAppletAccessToken = redisService.getCacheObject(String.format("%s_%s", "wxAppletAccessToken_", wechatSetting.getAppId()));
        log.debug("getWxAppletAccessToken and wxAppletAccessToken :{}", wxAppletAccessToken);
        if (StringUtils.isEmpty(wxAppletAccessToken)) {
            wxAppletAccessToken = WeChatAppletUtils.getAccessToken(wechatSetting);
            redisService.setCacheObject(String.format("%s_%s", "wxAppletAccessToken_", wechatSetting.getAppId()), wxAppletAccessToken, 60, TimeUnit.MINUTES);
            log.info("getWxAppletAccessToken form HttpPost");
        } else {
            log.info("getWxAppletAccessToken form redis");
        }
        return wxAppletAccessToken;

    }

}

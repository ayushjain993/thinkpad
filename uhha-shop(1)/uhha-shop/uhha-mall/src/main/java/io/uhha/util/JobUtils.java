package io.uhha.util;

import com.alibaba.fastjson.JSON;
import io.uhha.coin.common.Enum.validate.SendTypeEnum;
import io.uhha.coin.common.framework.redis.RedisCryptoHelper;
import io.uhha.coin.system.domain.SystemCoinType;
import io.uhha.coin.system.domain.SystemTradeType;
import io.uhha.common.core.redis.RedisCache;
import io.uhha.common.core.redis.RedisConstant;
import io.uhha.common.core.redis.RedisObject;
import io.uhha.setting.domain.LsEmailSetting;
import io.uhha.setting.domain.LsSmsSetting;
import io.uhha.validate.bean.ValidateTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 工具类
 *
 * @author jany
 */
@Component
public class JobUtils {

    @Autowired
    private RedisCryptoHelper redisCryptoHelper;
    @Autowired
    private RedisCache redisCache;

    public List<SystemTradeType> getTradeTypeList() {
        // 获取交易列表
        List<SystemTradeType> tradeTypes = redisCryptoHelper.getTradeTypeList();
        return tradeTypes;
    }

    public SystemTradeType getTradeType(int tradeId) {
        SystemTradeType tradeType = redisCryptoHelper.getTradeType(tradeId);
        return tradeType;
    }

    public List<SystemCoinType> getCoinTypeList() {
        List<SystemCoinType> coinTypes = redisCryptoHelper.getCoinTypeCoinList();
        return coinTypes;

    }

    public SystemCoinType getCoinType(int coinId) {
        SystemCoinType coinType = redisCryptoHelper.getCoinType(coinId);
        return coinType;
    }

    public String getSystemArgs(String key) {
        return redisCryptoHelper.getSystemArgs(key);
    }

    public BigDecimal getLastPrice(Integer tradeId) {
        return redisCryptoHelper.getLastPrice(tradeId);
    }

    public String getRedisData(String key) {
        return redisCache.getCacheObject(key);
    }


    public void setRedisData(String key, String value) {
        redisCache.setCacheObject(key, value);
    }

    public void setRedisData(String key, String value, int expireSeconds) {
        redisCache.setCacheObject(key, value, expireSeconds, TimeUnit.SECONDS);
    }

    public Map<Integer, Integer> getCoinIdToTradeId() {
        return  redisCryptoHelper.getCoinIdToTradeId();
    }


    public LsSmsSetting getActiveSMSetting(){
        String json = redisCache.getCacheObject(
                RedisConstant.ACTIVE_SMS_SETTING);
        if(!StringUtils.isEmpty(json)){
            RedisObject ro = JSON.parseObject(json, RedisObject.class);
            return JSON.parseObject(ro.getExtObject().toString(), LsSmsSetting.class);
        }else{
            return null;
        }
    }

    public LsEmailSetting getActiveEmailSetting(){
        String json = redisCache.getCacheObject(RedisConstant.ACTIVE_EMAIL_SETTING);
        if(!StringUtils.isEmpty(json)){
            RedisObject ro = JSON.parseObject(json, RedisObject.class);
            return JSON.parseObject(ro.getExtObject().toString(), LsEmailSetting.class);
        }else{
            return null;
        }

    }

    public ValidateTemplate getValidateTemplate(Integer platformType, Integer sendType, Integer businessType,
                                                Integer language, Integer systemVersion /*新加系统使用版本参数*/){
        String json;
        //类型为邮件时加上系统使用版本
        if (sendType.equals(SendTypeEnum.EMAIL.getCode())){
            json = redisCache.getCacheObject(RedisConstant.VALIDATE_TEMPLATE + platformType + "_" + sendType + "_" + businessType + "_" + language + "_" + systemVersion /*新加系统使用版本*/);
        }else {
            json = redisCache.getCacheObject(RedisConstant.VALIDATE_TEMPLATE + platformType + "_" + sendType + "_" + businessType + "_" + language);
        }
        if(!StringUtils.isEmpty(json)){
            RedisObject ro = JSON.parseObject(json, RedisObject.class);
            return JSON.parseObject(ro.getExtObject().toString(), ValidateTemplate.class);
        }else{
            return null;
        }
    }
}

package io.uhha.system.service.impl;

import com.alibaba.fastjson.JSON;
import io.uhha.coin.common.Enum.validate.SendTypeEnum;
import io.uhha.common.core.redis.RedisCache;
import io.uhha.common.core.redis.RedisConstant;
import io.uhha.common.core.redis.RedisObject;
import io.uhha.order.domain.OmsLogisticsCompany;
import io.uhha.order.service.IOmsLogisticsCompanyService;
import io.uhha.setting.domain.LsEmailSetting;
import io.uhha.setting.domain.LsSmsSetting;
import io.uhha.setting.service.ILsEmailSettingService;
import io.uhha.setting.service.ILsSmsSettingService;
import io.uhha.system.domain.FCountry;
import io.uhha.system.service.IFCountryService;
import io.uhha.system.service.IMallRedisInitService;
import io.uhha.validate.bean.ValidateTemplate;
import io.uhha.validate.mapper.ValidateTemplateMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MallRedisInitServiceImpl implements IMallRedisInitService {
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IFCountryService countryService;

    @Autowired
    private IOmsLogisticsCompanyService logisticCompanyService;

    @Autowired
    private ValidateTemplateMapper validateTemplateMapper;
    @Autowired
    private ILsEmailSettingService emailSettingService;
    @Autowired
    private ILsSmsSettingService smsSettingService;

    @Override
    public void redisInit(){
        // 国家
        log.info("--------------> 国家开始初始化");
        initCountries();
        log.info("--------------> 物流公司列表初始化");
        initLogisticCompany();
        log.info("--------------> Email配置开始初始化");
        initEmailSettings();
        log.info("--------------> SMS配置开始初始化");
        initSmsSettings();
        log.info("--------------> 消息模板开始初始化");
        initValidateTemplate();
    }


    /**
     * 初始化国家列表
     */
    @Override
    public void initCountries(){
        List<FCountry> countryList = countryService.selectAllCountries();
        RedisObject allcountries = new RedisObject();
        allcountries.setExtObject(countryList);
        setNoExpire(RedisConstant.COUNTRIES, allcountries);
    }

    @Override
    public void initLogisticCompany() {
        List<OmsLogisticsCompany> lsLogisticCompanies = logisticCompanyService.list();
        RedisObject allLogisticCompanies = new RedisObject();
        allLogisticCompanies.setExtObject(lsLogisticCompanies);
        setNoExpire(RedisConstant.LOGISTIC_COMPANIES, allLogisticCompanies);
    }

    /**
     * Redis设置
     *
     * @param key key
     * @param obj obj
     */
    public void setNoExpire(String key, RedisObject obj) {
        redisCache.setCacheObject(key, JSON.toJSONString(obj));
    }

    /**
     * Redis删除（重置之前，批量删除）
     *
     * @param key key
     */
    public void deleteNoExpire(String key) {
        redisCache.removeByPattern(key);
    }


    public void initEmailSettings() {
        LsEmailSetting lsEmailSetting = emailSettingService.queryActiveEmailSetting();
        if(lsEmailSetting==null){
            log.error("Email Setting not found in database, please ensure at least one is enabled!");
            return;
        }
        RedisObject ro = new RedisObject();
        ro.setExtObject(lsEmailSetting);
        setNoExpire(RedisConstant.ACTIVE_EMAIL_SETTING, ro);
    }

    public void initSmsSettings() {
        LsSmsSetting smsSetting = smsSettingService.queryActiveSmsSetting();
        if(smsSetting==null){
            log.error("SMS Setting not found in database, please ensure at least one is enabled!");
            return;
        }
        RedisObject ro = new RedisObject();
        ro.setExtObject(smsSetting);
        setNoExpire(RedisConstant.ACTIVE_SMS_SETTING, ro);
    }

    public void initValidateTemplate() {
        List<ValidateTemplate> templateList = validateTemplateMapper.selectAll();
        for (ValidateTemplate template : templateList) {
            //businessType修改为
            String[] BusionessTypes = template.getBusinessType().split("#");
            for (int i = 0; i < BusionessTypes.length; i++) {
                RedisObject ro = new RedisObject();
                ro.setExtObject(template);
                //只有邮件时添加系统使用版本
                if (template.getSendType() == SendTypeEnum.EMAIL.getCode()){
                    setNoExpire(RedisConstant.VALIDATE_TEMPLATE + template.getPlatform()
                            + "_" + template.getSendType()
                            + "_" + BusionessTypes[i]
                            + "_" + template.getLanguage(), ro);
                }else {
                    setNoExpire(RedisConstant.VALIDATE_TEMPLATE + template.getPlatform()
                            + "_" + template.getSendType()
                            + "_" + BusionessTypes[i]
                            + "_" + template.getLanguage(), ro);
                }
            }
        }
    }

}

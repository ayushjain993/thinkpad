package io.uhha.common.notify;

import com.alibaba.fastjson.JSON;
import io.uhha.coin.common.Enum.msg.MsgInfromTypeEnum;
import io.uhha.coin.common.Enum.validate.BusinessTypeEnum;
import io.uhha.coin.common.Enum.validate.LocaleEnum;
import io.uhha.coin.common.Enum.validate.PlatformEnum;
import io.uhha.coin.common.Enum.validate.SendTypeEnum;
import io.uhha.coin.common.dto.msg.EmailInform;
import io.uhha.coin.common.dto.msg.MsgInform;
import io.uhha.coin.common.framework.redis.RedisCryptoHelper;
import io.uhha.coin.common.util.GUIDUtils;
import io.uhha.coin.common.util.ValidataeConstant;
import io.uhha.common.core.domain.entity.SysUser;
import io.uhha.common.core.redis.RedisConstant;
import io.uhha.common.core.redis.RedisObject;
import io.uhha.common.exception.BCException;
import io.uhha.common.utils.Utils;
import io.uhha.member.domain.UmsMember;
import io.uhha.validate.dto.NotifyMsgDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 短信邮件公共接口
 *
 * @author ZKF
 * PS: 新调用在spring中增加以下配置
 * <bean id="validateHelper" class="io.uhha.common.ValidateHelper">
 * <property name="redisHelper" ref="redisHelper" />
 * <property name="validateProducer" ref="validateProducer" />
 * </bean>
 * <p>
 * 如果redisHelper和validateProducer不存在需要再增加他们的配置
 */
@Component
public class ValidateNotifyHelper extends BaseNotifyHelper {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(ValidateNotifyHelper.class);

    @Autowired
    private RedisCryptoHelper redisCryptoHelper;

    /**
     * 发送验证码短信
     */
    public boolean smsValidateCode(Long uid, String areaCode, String phone, Integer sendType,
                                   Integer platform, Integer businessType, Integer languageType, Double price) throws BCException {
        String token = RedisConstant.VALIDATE_KEY + phone + "_" + platform + "_" + businessType;

        // 检查缓存
        RedisObject obj = redisCryptoHelper.getRedisObject(token);
        if (obj != null) {
            long lastActive = obj.getLastActiveDateTime();
            long now = System.currentTimeMillis() / 1000;
            if ((now - lastActive) < ValidataeConstant.MSG_SEND_TIME) {
                return false;
            }
        }

        // 国际短信处理
        if (StringUtils.isEmpty(areaCode)) {
            logger.error("----> mq send failed : areaCode is null");
            throw new BCException("区号为空!");
        }
        if (!areaCode.equals("86")) {
            if (phone.charAt(0) == '0') {
                phone = phone.substring(1, phone.length());
            }
            phone = areaCode + phone;
            sendType = SendTypeEnum.SMS_INTERNATIONAL.getCode();
            languageType = LocaleEnum.EN_US.getCode();
        } else {
            sendType = SendTypeEnum.SMS_TEXT.getCode();
            languageType = LocaleEnum.ZH_CN.getCode();
        }

        NotifyMsgDTO send = new NotifyMsgDTO();
        send.setUid(uid);
        send.setPhone(phone);
        send.setPlatformType(platform);
        send.setBusinessType(businessType);
        send.setSendType(sendType);
        send.setLanguageType(languageType);
        send.setPrice(price==null? BigDecimal.ZERO:new BigDecimal(price));

        //fixme 测试默认使用123456
//		String code = Utils.randomInteger(6);
        String code = "123456";
        send.setCode(code);

        // 投递Redis
        redisCryptoHelper.setRedisData(token, code, ValidataeConstant.MSG_OUT_TIME);

        return sendBaseSms(send);
    }

    /**
     * 发送风控短信
     */
    public boolean smsRiskManage(String username, String phone, Integer platform, Integer businessType,
                                 String type, BigDecimal amount, String coin) throws BCException {

        NotifyMsgDTO send = new NotifyMsgDTO();
        send.setUsername(username);
        send.setPhone(phone);
        send.setPlatformType(platform);
        send.setBusinessType(businessType);
        send.setSendType(SendTypeEnum.SMS_TEXT.getCode());
        send.setLanguageType(LocaleEnum.ZH_CN.getCode());
        send.setType(type);
        send.setAmount(amount);
        send.setCoin(coin);

        return sendBaseSms(send);
    }

    /**
     * 敏感信息短信
     */
    public boolean smsSensitiveInfo(String areaCode, String phone, Integer languageType,
                                    Integer platform, Integer businessType) throws BCException {
        Integer sendType = SendTypeEnum.SMS_TEXT.getCode();
        // 国际短信处理
        if (StringUtils.isEmpty(areaCode)) {
            logger.error("----> mq send failed : areaCode is null");
            throw new BCException("区号为空!");
        }
        if (!areaCode.equals("86")) {
            if (phone.charAt(0) == '0') {
                phone = phone.substring(1, phone.length());
            }
            phone = areaCode + phone;
            sendType = SendTypeEnum.SMS_INTERNATIONAL.getCode();
            languageType = LocaleEnum.EN_US.getCode();
        }
        NotifyMsgDTO send = new NotifyMsgDTO();
        send.setPhone(phone);
        send.setPlatformType(platform);
        send.setBusinessType(businessType);
        send.setSendType(sendType);
        send.setLanguageType(languageType);

        return sendBaseSms(send);
    }


    /**
     * 发送用户充值短信
     */
    public boolean smsUserRecharge(String areaCode, String phone, Integer platform, Integer businessType,
                                   BigDecimal amount) throws BCException {
        Integer sendType = SendTypeEnum.SMS_TEXT.getCode();
        Integer languageType = LocaleEnum.ZH_CN.getCode();

        // 国际短信处理
        if (StringUtils.isEmpty(areaCode)) {
            logger.error("----> mq send failed : areaCode is null");
            throw new BCException("区号为空!");
        }
        if (!areaCode.equals("86")) {
            if (phone.charAt(0) == '0') {
                phone = phone.substring(1, phone.length());
            }
            phone = areaCode + phone;
            sendType = SendTypeEnum.SMS_INTERNATIONAL.getCode();
            languageType = LocaleEnum.EN_US.getCode();
        }

        NotifyMsgDTO send = new NotifyMsgDTO();
        send.setPhone(phone);
        send.setPlatformType(platform);
        send.setBusinessType(businessType);
        send.setSendType(sendType);
        send.setLanguageType(languageType);
        send.setAmount(amount);

        return sendBaseSms(send);
    }

    /**
     * 新版校验验证码
     *
     * @return
     */
    public Boolean validateSmsCode(String areaCode, String phone,
                                   Integer platform, Integer businessType, String code) {

        String token = RedisConstant.VALIDATE_KEY + phone + "_" + platform + "_" + businessType;

        String json = redisCryptoHelper.getRedisData(token);
        if (StringUtils.isEmpty(json)) {
            return false;
        }
        RedisObject redisObject = JSON.parseObject(json, RedisObject.class);
        String validateCode = (String) redisObject.getExtObject();
        if (StringUtils.isEmpty(validateCode)) {
            return false;
        }

        if (validateCode.equals(code)) {
            redisCryptoHelper.deleteRedisData(token);
            return true;
        }
        return false;
    }

    /**
     * 发送邮件验证码验证
     *
     * @param email        邮件地址
     * @param platform     平台枚举 {@link PlatformEnum}
     * @param locale       语言枚举 {@link LocaleEnum}
     * @param businessType 业务枚举 {@link BusinessTypeEnum}
     * @param code         验证码
     * @return true 未通过，false 已通过
     */
    public Boolean mailCodeValidate(String email, PlatformEnum platform, LocaleEnum locale, BusinessTypeEnum businessType, String code, Integer systemVersion/*新加系统使用版本参数*/) {
        String token = RedisConstant.VALIDATE_KEY + businessType.getCode() + "_" + platform.getCode() + "_" + locale.getCode() + "_" + email + "_" + systemVersion/*新加系统使用版本参数*/;
        String validateCode = redisCryptoHelper.getRedisData(token);
        if (StringUtils.isEmpty(validateCode)) {
            return true;
        }
        if (validateCode.equals(code)) {
            redisCryptoHelper.deleteRedisData(token);
            return false;
        }
        return true;
    }

    /**
     * 发送邮件过期验证
     *
     * @param email        邮件地址
     * @param platform     平台枚举 {@link PlatformEnum}
     * @param locale       语言枚举 {@link LocaleEnum}
     * @param businessType 业务枚举 {@link BusinessTypeEnum}
     * @return true 未过期，false 已过期
     */
    public Boolean mailOverdueValidate(String email, PlatformEnum platform, LocaleEnum locale, BusinessTypeEnum businessType, Integer systemVersion/*新加系统使用版本参数*/) {
        String token = RedisConstant.VALIDATE_KEY + businessType.getCode() + "_" + platform.getCode() + "_" + locale.getCode() + "_" + email + "_" + systemVersion/*新加系统使用版本参数*/;
        String data = redisCryptoHelper.getRedisData(token);
        return !StringUtils.isEmpty(data);
    }

    /**
     * 发送内容邮件
     *
     * @param email        邮件地址
     * @param platform     平台枚举 {@link PlatformEnum}
     * @param locale       语言枚举 {@link LocaleEnum}
     * @param businessType 业务枚举 {@link BusinessTypeEnum}
     * @param ip           当前IP
     * @return true 成功，false 失败
     */
    public Boolean mailSendContent(String email, PlatformEnum platform, LocaleEnum locale, BusinessTypeEnum businessType, String ip, UmsMember user, Integer systemVersion/*新加系统使用版本参数*/) {
        // 判断是否已发送
        String token = RedisConstant.VALIDATE_KEY + businessType.getCode() + "_" + platform.getCode() + "_" + locale.getCode() + "_" + email + "_" + systemVersion/*新加系统使用版本参数*/;
        RedisObject json = redisCryptoHelper.getRedisObject(token);
        if (json != null) {
            return false;
        }
        String uuid = GUIDUtils.getGUIDString();
        // 数据组装
        NotifyMsgDTO sendDTO = new NotifyMsgDTO();
        sendDTO.setBusinessType(businessType.getCode());
        sendDTO.setEmail(email);
        sendDTO.setPlatformType(platform.getCode());
        sendDTO.setSendType(SendTypeEnum.EMAIL.getCode());
        sendDTO.setLanguageType(locale.getCode());
        sendDTO.setUuid(uuid);
        sendDTO.setUid(user.getId());
        sendDTO.setUsername(user.getUsername());
        sendDTO.setIp(ip);
        //新加系统使用版本参数
        sendDTO.setSystemVersion(systemVersion);
        // 投递redis
        redisCryptoHelper.setRedisData(token, uuid, ValidataeConstant.EMAIL_OUT_TIME);
        // 发送
        return mailSendBase(sendDTO);
    }

    /**
     * 发送内容邮件
     *
     * @param email        邮件地址
     * @param platform     平台枚举 {@link PlatformEnum}
     * @param locale       语言枚举 {@link LocaleEnum}
     * @param businessType 业务枚举 {@link BusinessTypeEnum}
     * @param ip           当前IP
     * @return true 成功，false 失败
     */
    public Boolean mailSendContent(String email, PlatformEnum platform, LocaleEnum locale, BusinessTypeEnum businessType, String ip, SysUser user, Integer systemVersion/*新加系统使用版本参数*/) {
        // 判断是否已发送
        String token = RedisConstant.VALIDATE_KEY + businessType.getCode() + "_" + platform.getCode() + "_" + locale.getCode() + "_" + email + "_" + systemVersion/*新加系统使用版本参数*/;
        RedisObject json = redisCryptoHelper.getRedisObject(token);
        if (json != null) {
            return false;
        }
        String uuid = GUIDUtils.getGUIDString();
        // 数据组装
        NotifyMsgDTO sendDTO = new NotifyMsgDTO();
        sendDTO.setBusinessType(businessType.getCode());
        sendDTO.setEmail(email);
        sendDTO.setPlatformType(platform.getCode());
        sendDTO.setSendType(SendTypeEnum.EMAIL.getCode());
        sendDTO.setLanguageType(locale.getCode());
        sendDTO.setUuid(uuid);
        sendDTO.setUid(user.getUserId());
        sendDTO.setUsername(user.getUserName());
        sendDTO.setIp(ip);
        //新加系统使用版本参数
        sendDTO.setSystemVersion(systemVersion);
        // 投递redis
        redisCryptoHelper.setRedisData(token, uuid, ValidataeConstant.EMAIL_OUT_TIME);
        // 发送
        return mailSendBase(sendDTO);
    }

    /**
     * 发送验证码邮件
     *
     * @param email        邮件地址
     * @param platform     平台枚举 {@link PlatformEnum}
     * @param languageType 语言枚举 {@link LocaleEnum}
     * @param ip           当前IP
     * @return true 成功，false 失败
     */
    public Boolean mailSendCode(String email, PlatformEnum platform, LocaleEnum languageType, String ip, Integer systemVersion/*新加系统使用版本参数*/) {

        // 判断是否已发送
        String token = RedisConstant.VALIDATE_KEY + BusinessTypeEnum.EMAIL_REGISTER_CODE.getCode() + "_"
                + platform.getCode() + "_" + languageType.getCode() + "_" + email + "_" + systemVersion/*新加系统使用版本参数*/;
        RedisObject json = redisCryptoHelper.getRedisObject(token);
        if (json != null) {
            return false;
        }
        // 数据组装
        String code = Utils.randomInteger(6);
        String uuid = GUIDUtils.getGUIDString();

        NotifyMsgDTO sendDTO = new NotifyMsgDTO();
        sendDTO.setBusinessType(BusinessTypeEnum.EMAIL_REGISTER_CODE.getCode());
        sendDTO.setCode(code);
        sendDTO.setEmail(email);
        sendDTO.setPlatformType(platform.getCode());
        sendDTO.setSendType(SendTypeEnum.EMAIL.getCode());
        sendDTO.setLanguageType(languageType.getCode());
        sendDTO.setUuid(uuid);
        sendDTO.setIp(ip);
        //添加系统使用版本参数
        sendDTO.setSystemVersion(systemVersion);
        // 投递redis
        redisCryptoHelper.setRedisData(token, code, ValidataeConstant.EMAIL_CODE_OUT_TIME);
        // 发送
        return mailSendBase(sendDTO);
    }

    /**
     * 发送验证码邮件-新版
     *
     * @param email        邮件地址
     * @param platform     平台枚举 {@link PlatformEnum}
     * @param languageType 语言枚举 {@link LocaleEnum}
     * @param ip           当前IP
     * @return true 成功，false 失败
     */
    public Boolean mailSendCode(String email, PlatformEnum platform, LocaleEnum languageType, BusinessTypeEnum businessTypeEnum, String ip, Integer systemVersion/*新加系统使用版本参数*/) {

        // 判断是否已发送
        String token = RedisConstant.VALIDATE_KEY + businessTypeEnum.getCode() + "_"
                + platform.getCode() + "_" + languageType.getCode() + "_" + email + "_" + systemVersion/*新加系统使用版本参数*/;
        RedisObject json = redisCryptoHelper.getRedisObject(token);
        if (json != null) {
            return false;
        }
        // 数据组装
        String code = Utils.randomInteger(6);
        String uuid = GUIDUtils.getGUIDString();

        NotifyMsgDTO sendDTO = new NotifyMsgDTO();
        sendDTO.setBusinessType(businessTypeEnum.getCode());
        sendDTO.setCode(code);
        sendDTO.setEmail(email);
        sendDTO.setPlatformType(platform.getCode());
        sendDTO.setSendType(SendTypeEnum.EMAIL.getCode());
        sendDTO.setLanguageType(languageType.getCode());
        sendDTO.setUuid(uuid);
        sendDTO.setIp(ip);
        //添加系统使用版本参数
        sendDTO.setSystemVersion(systemVersion);
        // 投递redis
        redisCryptoHelper.setRedisData(token, code, ValidataeConstant.EMAIL_CODE_OUT_TIME);
        // 发送
        return mailSendBase(sendDTO);
    }



    /**
     * 发送
     *
     * @param msg
     * @return
     * @throws Exception
     */
    public boolean sendPayMsg(MsgInform msg) throws Exception {
        // 发送短信
        NotifyMsgDTO send = new NotifyMsgDTO();
        send.setPhone(msg.getMobile());
        send.setPlatformType(PlatformEnum.UHHA.getCode());
        if (MsgInfromTypeEnum.PAYMENT.getCode().equals(msg.getMsgType())) {
            send.setBusinessType(BusinessTypeEnum.SMS_PAYMENT.getCode());
        } else {
            send.setBusinessType(BusinessTypeEnum.SMS_RECEIVABLE.getCode());
        }
        send.setSendType(SendTypeEnum.SMS_TEXT.getCode());
        send.setLanguageType(LocaleEnum.ZH_CN.getCode());
        send.setCode(msg.getActCode());
        return sendBaseSms(send);
    }

    /**
     * 发送Email
     *
     * @param msg
     * @return
     * @throws Exception
     */
    public boolean sendPayEmail(EmailInform msg) throws Exception {
        // 发送Email
        NotifyMsgDTO sendDTO = new NotifyMsgDTO();
        if (MsgInfromTypeEnum.PAYMENT.getCode().equals(msg.getMsgType())) {
            sendDTO.setBusinessType(BusinessTypeEnum.EMAIL_PAYMENT.getCode());
        } else {
            sendDTO.setBusinessType(BusinessTypeEnum.EMAIL_RECEIVABLE.getCode());
        }
        sendDTO.setCode(msg.getActCode());
        sendDTO.setEmail(msg.getEmail());
        sendDTO.setPlatformType(PlatformEnum.UHHA.getCode());
        sendDTO.setSendType(SendTypeEnum.EMAIL.getCode());
        sendDTO.setLanguageType(LocaleEnum.ZH_CN.getCode());
        String uuid = GUIDUtils.getGUIDString();
        sendDTO.setUuid(uuid);
        sendDTO.setIp(msg.getRemark());
        return mailSendBase(sendDTO);
    }

}

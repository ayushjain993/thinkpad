package io.uhha.validate.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import io.uhha.coin.common.Enum.validate.*;
import io.uhha.coin.common.framework.redis.RedisCryptoHelper;
import io.uhha.coin.common.match.MathUtils;
import io.uhha.coin.common.util.DateUtils;
import io.uhha.common.enums.StationLetterTypeEnum;
import io.uhha.setting.domain.LsEmailSetting;
import io.uhha.setting.domain.LsSmsSetting;
import io.uhha.setting.domain.LsStationLetter;
import io.uhha.setting.service.ILsStationLetterService;
import io.uhha.util.JobUtils;
import io.uhha.util.send.Sender;
import io.uhha.validate.bean.ValidateEmailDO;
import io.uhha.validate.bean.ValidateSmsDO;
import io.uhha.validate.bean.ValidateTemplate;
import io.uhha.validate.dto.NotifyMsgDTO;
import io.uhha.validate.mapper.ValidateEmailMapper;
import io.uhha.validate.mapper.ValidateSmsMapper;
import io.uhha.validate.service.INotifyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

/**
 * Created by ZKF on 2017/5/4.
 */
@Service("validateService")
@Slf4j
public class NotifyServiceImpl implements INotifyService {


    @Autowired
    private ValidateSmsMapper smsMapper;
    @Autowired
    private ValidateEmailMapper emailMapper;
    @Autowired
    private JobUtils jobUtils;

    @Autowired
    private RedisCryptoHelper redisCryptoHelper;

    @Resource(name = "wjNormalSmsSender")
    private Sender smsSender;

    @Autowired
    private ILsStationLetterService stationLetterService;

    /**
     * 发送消息
     *
     * @param vs 消息传输对象
     */
    @Override
    public Boolean updateSend(NotifyMsgDTO vs) {

        // 查询平台信息
        if (SendTypeEnum.EMAIL.getCode().equals(vs.getSendType())) {
            return sendEmail(vs);
        } else if (SendTypeEnum.SMS_INTERNATIONAL.getCode().equals(vs.getSendType()) || SendTypeEnum.SMS_TEXT.getCode().equals(vs.getSendType())) {
            return sendSMS(vs);
        } else if(SendTypeEnum.STATION_LETTER.getCode().equals(vs.getSendType())){
            return sendStationLetter(vs);
        }else {
            log.error("not supported sendType: {}", vs.getSendType());
            return false;
        }
    }

    private boolean sendEmail(NotifyMsgDTO vs) {
        LsEmailSetting emailSetting = jobUtils.getActiveEmailSetting();
        if (emailSetting == null) {
            log.error("invalid emailSetting in system!!! contact your administrator");
            return false;
        }

        // 查询模板信息
        ValidateTemplate template = jobUtils.getValidateTemplate(vs.getPlatformType(), vs.getSendType(),
                vs.getBusinessType(), vs.getLanguageType(), vs.getSystemVersion()/*新加系统使用版本参数*/);
        if (template == null) {
            log.error("模板信息未找到！ info : {}" + vs.toString());
            return false;
        }

        // 拼接内容
        String content = template.getTemplate();
        if (StringUtils.isEmpty(content)) {
            log.error("模板内容未找到！");
            return false;
        }

        if (!StringUtils.isEmpty(template.getParams())) {
            content = replaceContent(content, template.getParams(), vs);
        }
        ValidateEmailDO email = new ValidateEmailDO();

        email.setUid(vs.getUid());
        email.setEmail(vs.getEmail());
        email.setPlatform(vs.getPlatformType());
        email.setTemplateId(template.getId());
        email.setTitle("UHHA reminder: " + BusinessTypeEnum.getEnValueByCode(vs.getBusinessType()));
        email.setCode(vs.getCode());
        email.setUuid(vs.getUuid());
        email.setStatus(SendStatusEnum.SEND_SUCCESS.getCode());
        email.setGmtCreate(new Date());
        email.setGmtSend(new Date());
        email.setVersion(0);
        email.setContent(content);

        if("1".equalsIgnoreCase(redisCryptoHelper.getSystemArgs("isLiveEnabled"))){
            if (!sendAliEmail(emailSetting, email)) {
                email.setStatus(SendStatusEnum.SEND_FAILURE.getCode());
            }
        }else{
            log.error("isLiveEnabled set to 0, no sending to sever");
        }

        return emailMapper.insert(email) > 0;
    }

    private boolean sendSMS(NotifyMsgDTO vs) {
        LsSmsSetting smsSetting = jobUtils.getActiveSMSetting();
        if (smsSetting == null) {
            log.error("invalid smsSetting in system!!!  contact your administrator");
            return false;
        }

        // 查询模板信息
        ValidateTemplate template = jobUtils.getValidateTemplate(vs.getPlatformType(), vs.getSendType(),
                vs.getBusinessType(), vs.getLanguageType(), vs.getSystemVersion()/*新加系统使用版本参数*/);
        if (template == null) {
            log.error("模板信息未找到！ info : {}" + vs.toString());
            return false;
        }

        // 拼接内容
        String content = template.getTemplate();
        if (StringUtils.isEmpty(content)) {
            log.error("模板内容未找到！");
            return false;
        }

        if (!StringUtils.isEmpty(template.getParams())) {
            content = replaceContent(content, template.getParams(), vs);
        }
        ValidateSmsDO sms = new ValidateSmsDO();

        sms.setPhone(vs.getPhone());
        sms.setUid(vs.getUid());
        sms.setContent(content);
        sms.setSendType(vs.getSendType());
        sms.setPlatform(vs.getPlatformType());
        sms.setTemplateId(template.getId());
        sms.setGmtCreate(new Date());
        sms.setGmtSend(new Date());
        sms.setVersion(0);
        sms.setStatus(SendStatusEnum.SEND_SUCCESS.getCode());

        boolean send;

        //是否开启线上？
        if("1".equalsIgnoreCase(redisCryptoHelper.getSystemArgs("isLiveEnabled"))){
            if (vs.getSendType().equals(SendTypeEnum.SMS_INTERNATIONAL.getCode())) {
                send = send253InternationalSms(smsSetting, sms);
            } else {
                send = smsSender.send(smsSetting, sms);
            }
            if (!send) {
                sms.setStatus(SendStatusEnum.SEND_FAILURE.getCode());
            }
        }else{
            log.error("isLiveEnabled set to 0, no sending to sever");
        }

        return smsMapper.insert(sms) > 0;
    }


    private String replaceContent(String content, String params, NotifyMsgDTO vs) {
        if (params.contains("#")) {
            String[] paramArray = params.split("#");
            for (String aParamArray : paramArray) {
                if (StringUtils.isEmpty(aParamArray)) {
                    continue;
                }
                Integer paramType = Integer.valueOf(aParamArray);
                if (paramType.equals(ParameterTypeEnum.CODE.getCode())) {
                    content = content.replace("#code#", vs.getCode());
                    continue;
                }
                if (paramType.equals(ParameterTypeEnum.DATE.getCode())) {
                    content = content.replace("#date#", DateUtils.format(new Date(),
                            DateUtils.YYYY_MM_DD_HH_MM_SS));
                    continue;
                }
                if (paramType.equals(ParameterTypeEnum.UUID.getCode())) {
                    content = content.replace("#uuid#", vs.getUuid());
                    continue;
                }
                if (paramType.equals(ParameterTypeEnum.language.getCode())) {
                    content = content.replace("#language#", LocaleEnum.getNameByCode(vs.getLanguageType()));
                    continue;
                }
                if (paramType.equals(ParameterTypeEnum.USER.getCode())) {
                    content = content.replace("#user#", vs.getUsername());
                    continue;
                }
                if (paramType.equals(ParameterTypeEnum.TYPE.getCode())) {
                    content = content.replace("#type#", vs.getType());
                    continue;
                }
                if (paramType.equals(ParameterTypeEnum.PRICE.getCode())) {
                    content = content.replace("#price#", MathUtils.decimalFormat(vs.getPrice()));
                    continue;
                }
                if (paramType.equals(ParameterTypeEnum.USERPRICE.getCode())) {
                    content = content.replace("#userPrice#", MathUtils.decimalFormat(vs.getUserPrice()));
                    continue;
                }
                if (paramType.equals(ParameterTypeEnum.AMOUNT.getCode())) {
                    content = content.replace("#amount#", MathUtils.decimalFormat(vs.getAmount()));
                    continue;
                }
                if (paramType.equals(ParameterTypeEnum.COIN.getCode())) {
                    content = content.replaceAll("#coin#", vs.getCoin());
                    continue;
                }
                if (paramType.equals(ParameterTypeEnum.UID.getCode())) {
                    content = content.replaceAll("#uid#", vs.getUid().toString());
                    continue;
                }
                if (paramType.equals(ParameterTypeEnum.IPADDRESS.getCode())) {
                    content = content.replaceAll("#ipAddress#", vs.getIp());
                    continue;
                }
                if (paramType.equals(ParameterTypeEnum.ORDERNO.getCode())) {
                    content = content.replaceAll("#orderNo#", vs.getOrderNo());
                    continue;
                }
                if (paramType.equals(ParameterTypeEnum.LOGISTIC_COMPANY_NAME.getCode())) {
                    content = content.replaceAll("#logisticCompanyName#", vs.getLogisticCompanyName());
                    continue;
                }
                if (paramType.equals(ParameterTypeEnum.WAY_BILL_CODE.getCode())) {
                    content = content.replaceAll("#wayBillCode#", vs.getWaybillCode());
                    continue;
                }
                if (paramType.equals(ParameterTypeEnum.VIDEO_TITLE.getCode())) {
                    content = content.replaceAll("#videoTitle#", vs.getVideoTitle());
                    continue;
                }
                if (paramType.equals(ParameterTypeEnum.ADMIN_APPROVAL_REASON.getCode())) {
                    content = content.replaceAll("#reason#", vs.getReason());
                    continue;
                }
                if (paramType.equals(ParameterTypeEnum.COMMENT.getCode())) {
                    content = content.replaceAll("#comment#", vs.getComment());
                    continue;
                }
            }
            return content;
        } else {
            Integer paramType = Integer.valueOf(params);
            if (paramType.equals(ParameterTypeEnum.CODE.getCode())) {
                content = content.replace("#code#", vs.getCode());
                return content;
            }
            if (paramType.equals(ParameterTypeEnum.DATE.getCode())) {
                content = content.replace("#date#", DateUtils.format(new Date(),
                        DateUtils.YYYY_MM_DD_HH_MM_SS));
                return content;
            }
            if (paramType.equals(ParameterTypeEnum.UUID.getCode())) {
                content = content.replace("#uuid#", vs.getUuid());
                return content;
            }
            if (paramType.equals(ParameterTypeEnum.language.getCode())) {
                content = content.replace("#language#", LocaleEnum.getNameByCode(vs.getLanguageType()));
                return content;
            }
            if (paramType.equals(ParameterTypeEnum.USER.getCode())) {
                content = content.replace("#user#", vs.getUsername());
                return content;
            }
            if (paramType.equals(ParameterTypeEnum.TYPE.getCode())) {
                content = content.replace("#type#", vs.getType());
                return content;
            }
            if (paramType.equals(ParameterTypeEnum.PRICE.getCode())) {
                content = content.replace("#price#", MathUtils.decimalFormat(vs.getPrice()));
                return content;
            }
            if (paramType.equals(ParameterTypeEnum.USERPRICE.getCode())) {
                content = content.replace("#userPrice#", MathUtils.decimalFormat(vs.getUserPrice()));
                return content;
            }
            if (paramType.equals(ParameterTypeEnum.AMOUNT.getCode())) {
                content = content.replace("#amount#", MathUtils.decimalFormat(vs.getAmount()));
                return content;
            }
            if (paramType.equals(ParameterTypeEnum.COIN.getCode())) {
                content = content.replaceAll("#coin#", vs.getCoin());
                return content;
            }
            if (paramType.equals(ParameterTypeEnum.UID.getCode())) {
                content = content.replaceAll("#uid#", vs.getUid().toString());
                return content;
            }
            if (paramType.equals(ParameterTypeEnum.IPADDRESS.getCode())) {
                content = content.replaceAll("#ipAddress#", vs.getIp());
                return content;
            }
            if (paramType.equals(ParameterTypeEnum.ORDERNO.getCode())) {
                content = content.replaceAll("#orderNo#", vs.getOrderNo());
                return content;
            }
            if (paramType.equals(ParameterTypeEnum.LOGISTIC_COMPANY_NAME.getCode())) {
                content = content.replaceAll("#logisticCompanyName#", vs.getLogisticCompanyName());
                return content;
            }
            if (paramType.equals(ParameterTypeEnum.WAY_BILL_CODE.getCode())) {
                content = content.replaceAll("#wayBillCode#", vs.getWaybillCode());
                return content;
            }
            if (paramType.equals(ParameterTypeEnum.VIDEO_TITLE.getCode())) {
                content = content.replaceAll("#videoTitle#", vs.getVideoTitle());
                return content;
            }
            if (paramType.equals(ParameterTypeEnum.ADMIN_APPROVAL_REASON.getCode())) {
                content = content.replaceAll("#reason#", vs.getReason());
                return content;
            }
            if (paramType.equals(ParameterTypeEnum.COMMENT.getCode())) {
                content = content.replaceAll("#comment#", vs.getComment());
                return content;
            }

            return content;
        }
    }

    /**
     * 创蓝短信接口
     *
     * @param smsSetting 账号信息
     * @param vs 短信信息
     */
    private static boolean send253NormalSms(LsSmsSetting smsSetting, ValidateSmsDO vs) {
        try {
            // 创建StringBuffer对象用来操作字符串
            StringBuilder sb = new StringBuilder();
            sb.append(smsSetting.getUrl());
            sb.append("?");
            // APIKEY
            sb.append("account=");
            sb.append(smsSetting.getAppKey());
            // 用户名
            sb.append("&pswd=");
            sb.append(smsSetting.getAppSecret());
            // 向StringBuffer追加手机号码
            sb.append("&mobile=");
            sb.append(vs.getPhone());
            // 向StringBuffer追加消息内容转URL标准码
            sb.append("&msg=");
            sb.append(URLEncoder.encode(vs.getContent(), "UTF-8"));
            // 是否状态报告
            sb.append("&needstatus=" + 1);
            // 创建url对象
            URL url = new URL(sb.toString());
            // 打开url连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置url请求方式 ‘get’ 或者 ‘post’
            connection.setRequestMethod("POST");
            // 发送
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            // 返回发送结果
            String inputline = in.readLine();
            // 输出结果
            System.out.println(inputline);
            connection.disconnect();
        } catch (Exception e) {
            log.error("send253NormalSms failed");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 创蓝国际短信接口
     *
     * @param smsSetting 账号信息
     * @param vs 短信信息
     */
    private boolean send253InternationalSms(LsSmsSetting smsSetting, ValidateSmsDO vs) {
        try {
            // 创建StringBuffer对象用来操作字符串
            StringBuilder sb = new StringBuilder();
            sb.append(smsSetting.getUrl());
            sb.append("?");
            // APIKEY
            sb.append("un=");
            sb.append(smsSetting.getAppKey());
            // 用户名
            sb.append("&pw=");
            sb.append(smsSetting.getAppSecret());
            // 向StringBuffer追加手机号码
            sb.append("&da=");
            sb.append(vs.getPhone());
            // 向StringBuffer追加消息内容转URL标准码
            sb.append("&sm=");
            sb.append(URLEncoder.encode(vs.getContent(), "UTF-8"));
            // 是否状态报告
            sb.append("&dc=15&rd=1&rf=2&tf=3");
            // 创建url对象
            URL url = new URL(sb.toString());
            // 打开url连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置url请求方式 ‘get’ 或者 ‘post’
            connection.setRequestMethod("POST");
            // 发送
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            // 返回发送结果
            String inputline = in.readLine();
            // 输出结果
            System.out.println(inputline);
            connection.disconnect();
        } catch (Exception e) {
            log.error("send253InternationalSms failed");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 发送阿里邮件
     *
     * @param emailSetting 账号信息
     * @param ve 短信信息
     */
    private boolean sendAliEmail(LsEmailSetting emailSetting, ValidateEmailDO ve) {
        boolean flag = true;
        IClientProfile profile = DefaultProfile.getProfile("ap-southeast-1", emailSetting.getAccessKey(), emailSetting.getSecretKey());
        try {
            DefaultProfile.addEndpoint("dm.ap-southeast-1.aliyuncs.com", "ap-southeast-1", "Dm", "dm.ap-southeast-1.aliyuncs.com");
        } catch (ClientException e) {
            e.printStackTrace();
        }
        IAcsClient client = new DefaultAcsClient(profile);
        SingleSendMailRequest request = new SingleSendMailRequest();
        try {
            request.setVersion("2017-06-22");
            request.setAccountName(emailSetting.getUrl());
            request.setAddressType(1);
            request.setReplyToAddress(true);
            request.setToAddress(ve.getEmail());
            request.setSubject(ve.getTitle());
            request.setHtmlBody(ve.getContent());
            client.getAcsResponse(request);
        } catch (ServerException e) {
            flag = false;
            e.printStackTrace();
        } catch (ClientException e) {
            log.error("sendAliEmail failed");
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    private boolean sendStationLetter(NotifyMsgDTO vs) {
        // 查询模板信息
        ValidateTemplate template = jobUtils.getValidateTemplate(vs.getPlatformType(), vs.getSendType(),
                vs.getBusinessType(), vs.getLanguageType(), vs.getSystemVersion()/*新加系统使用版本参数*/);
        if (template == null) {
            log.error("模板信息未找到！ info : {}" + vs.toString());
            return false;
        }

        // 拼接内容
        String content = template.getTemplate();
        if (StringUtils.isEmpty(content)) {
            log.error("模板内容未找到！");
            return false;
        }

        if (!StringUtils.isEmpty(template.getParams())) {
            content = replaceContent(content, template.getParams(), vs);
        }
        LsStationLetter stationLetter = LsStationLetter.builder()
                .customerId(vs.getUid())
                .type(StationLetterTypeEnum.ORDER_RELATED.getCode())
                .title(BusinessTypeEnum.getEnValueByCode(vs.getBusinessType()))
                .content(content)
                .isRead("0")
                .delFlag(0)
                .build();
        return stationLetterService.insertLsStationLetter(stationLetter)> 0;

    }


}

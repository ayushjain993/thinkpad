package io.uhha.diagno.service.impl;

import io.uhha.coin.common.Enum.validate.BusinessTypeEnum;
import io.uhha.coin.common.Enum.validate.LocaleEnum;
import io.uhha.coin.common.Enum.validate.PlatformEnum;
import io.uhha.coin.common.coin.ETHUtils;
import io.uhha.common.exception.BCException;
import io.uhha.coin.common.framework.redis.RedisCryptoHelper;
import io.uhha.coin.system.domain.SystemCoinType;
import io.uhha.common.redis.RedisMallHelper;
import io.uhha.setting.domain.LsEmailSetting;
import io.uhha.setting.domain.LsSmsSetting;
import io.uhha.common.notify.ValidateNotifyHelper;
import io.uhha.common.utils.StringUtils;
import io.uhha.diagno.service.SysDiagnosticService;
import io.uhha.diagno.vo.DiagnoResponse;
import io.uhha.member.domain.UmsMember;
import io.uhha.member.service.IUmsMemberService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 系统自检服务（未实现）
 *
 * @deprecated
 */
@Service
@Slf4j
public class SysDiagnosticServiceImpl implements SysDiagnosticService {
    @Autowired
    private RedisCryptoHelper redisCryptoHelper;

    @Autowired
    private RedisMallHelper redisMallHelper;

    @Autowired
    private ValidateNotifyHelper validateNotifyHelper;

    @Autowired
    private IUmsMemberService customerService;

    @Override
    public List<DiagnoResponse> diagnosticAll(Long uid, String email, String phone) throws BCException {

        List<DiagnoResponse> responses = new ArrayList<>();

        log.info("diagnostic crypto start.");

        ///////////////////////////////////////////////////////
        //检查未禁用币种列表
        List<SystemCoinType> coins = redisCryptoHelper.getCoinTypeList();
        if (CollectionUtils.isEmpty(coins)) {
            DiagnoResponse res = DiagnoResponse.builder()
                    .item("检查Redis中的币种列表")
                    .status("failed")
                    .data("SystemCoinType List is empty in Redis.")
                    .build();
            responses.add(res);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(coins.size() + " SystemCoinType types in Redis.\r\n");
            coins.stream().forEach(x -> {
                sb.append(x.toString() + "\r\n");
            });

            DiagnoResponse res = DiagnoResponse.builder()
                    .item("检查Redis中的币种列表")
                    .status("success")
                    .data(sb.toString())
                    .build();
            responses.add(res);
        }

        log.info("diagno ETH node start");
        ///////////////////////////////////////////////////////
        ETHUtils ethUtils = new ETHUtils();
        String version = null;
        try {
            version = ethUtils.getVersion();
        } catch (Exception e) {
            e.printStackTrace();
            DiagnoResponse res = DiagnoResponse.builder()
                    .item("检查ETH节点")
                    .status("failed")
                    .data(e.toString())
                    .build();
            responses.add(res);
        }

        if (!StringUtils.isEmpty(version)) {
            DiagnoResponse res = DiagnoResponse.builder()
                    .item("检查ETH节点")
                    .status("success")
                    .data("diagnosticPayment failed. response=[" + version + "]")
                    .build();
            responses.add(res);
        }
        log.info("diagno ETH node end");


        ///////////////////////////////////////////////////////
        //系统参数列表
        Map<String, Object> systemArgs = redisCryptoHelper.getSystemArgsList();
        if (MapUtils.isEmpty(systemArgs)) {
            DiagnoResponse res = DiagnoResponse.builder()
                    .item("系统参数列表")
                    .status("failed")
                    .data("systemArgs List is empty in Redis.")
                    .build();
            responses.add(res);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(systemArgs.size() + " systemArgs types in Redis.\r\n");
            systemArgs.entrySet().forEach(x -> {
                sb.append(x.toString() + "\r\n");
            });

            DiagnoResponse res = DiagnoResponse.builder()
                    .item("系统参数列表")
                    .status("success")
                    .data(sb.toString())
                    .build();
            responses.add(res);
        }
        log.info("diagnostic crypto end.");


        ///////////////////////////////////////////////////////
        // SMS setting
        log.info("diagnosticSmsSetting start.");
        LsSmsSetting smsSetting = redisMallHelper.getActiveSmsSetting();
        if (smsSetting!=null) {
            DiagnoResponse res = DiagnoResponse.builder()
                    .item("检查短消息设置")
                    .status("success")
                    .data(smsSetting.toString())
                    .build();
            responses.add(res);
        } else {
            DiagnoResponse res = DiagnoResponse.builder()
                    .item("检查短消息设置")
                    .status("failed")
                    .data("diagnosticSmsSetting failed.")
                    .build();
            responses.add(res);
        }
        log.info("diagnosticSmsSetting finished.");


        ///////////////////////////////////////////////////////
        // Email setting
        log.info("diagnosticEmailSetting start.");
        LsEmailSetting emailSetting = redisMallHelper.getActiveEmailSetting();
        if (emailSetting!=null) {
            DiagnoResponse res = DiagnoResponse.builder()
                    .item("检查邮件设置")
                    .status("success")
                    .data(emailSetting.toString())
                    .build();
            responses.add(res);
        } else {
            DiagnoResponse res = DiagnoResponse.builder()
                    .item("检查邮件设置")
                    .status("failed")
                    .data("diagnosticEmailSetting failed.")
                    .build();
            responses.add(res);
        }
        log.info("diagnosticEmailSetting finished.");

        ///////////////////////////////////////////////////////
        // SMS
        log.info("diagnosticSms start.");
        if (diagnosticSms(uid, phone)) {
            DiagnoResponse res = DiagnoResponse.builder()
                    .item("检查短消息接口")
                    .status("success")
                    .data("diagnosticSms pass.")
                    .build();
            responses.add(res);
        } else {
            DiagnoResponse res = DiagnoResponse.builder()
                    .item("检查短消息接口")
                    .status("failed")
                    .data("diagnosticSms failed.")
                    .build();
            responses.add(res);
        }
        log.info("diagnosticSms finished.");

        ///////////////////////////////////////////////////////
        // EMAIL
        log.info("diagnosticEmail start.");
        if (diagnosticEmail(email)) {
            DiagnoResponse res = DiagnoResponse.builder()
                    .item("检查Email接口")
                    .status("success")
                    .data("diagnosticEmail pass.")
                    .build();
            responses.add(res);
        } else {
            DiagnoResponse res = DiagnoResponse.builder()
                    .item("检查Email接口")
                    .status("failed")
                    .data("diagnosticEmail failed.")
                    .build();
            responses.add(res);
        }
        log.info("diagnosticEmail finished.");

        ///////////////////////////////////////////////////////
        //Payment
        log.info("diagnosticPayment start.");
        DiagnoResponse res = null;
        if (diagnosticPayment()) {
            res = DiagnoResponse.builder()
                    .item("检查支付接口")
                    .status("success")
                    .data("diagnosticPayment pass.")
                    .build();
        } else {
            res = DiagnoResponse.builder()
                    .item("检查支付接口")
                    .status("failed")
                    .data("diagnosticPayment failed.")
                    .build();
        }
        responses.add(res);

        log.info("diagnosticPayment finished.");

        return responses;
    }

    @Override
    public String diagnosticProgress() {
        return null;
    }


    @Override
    public DiagnoResponse diagnosticETHNode() {
        log.info("diagno ETH node start");
        ETHUtils ethUtils = new ETHUtils();
        DiagnoResponse res = null;
        String version = null;
        try {
            version = ethUtils.getVersion();
        } catch (IOException e) {
            e.printStackTrace();
            res = DiagnoResponse.builder()
                    .item("检查ETH节点")
                    .status("failed")
                    .data(e.toString())
                    .build();
            return res;
        }
        if (!StringUtils.isEmpty(version)) {
            res = DiagnoResponse.builder()
                    .item("检查ETH节点")
                    .status("success")
                    .data(version)
                    .build();
            log.info("diagno ETH node end");
        }
        return res;
    }

    @Override
    public String diagnosticCrypto() {

        log.info("diagnosticCrypto start.");

        //检查未禁用币种列表
        List<SystemCoinType> coins = redisCryptoHelper.getCoinTypeList();
        if (CollectionUtils.isEmpty(coins)) {
            log.error("SystemCoinType List is empty in Redis.");
            return "SystemCoinType List is empty in Redis";
        }

        //系统参数列表
        Map<String, Object> systemArgs = redisCryptoHelper.getSystemArgsList();
        if (MapUtils.isEmpty(systemArgs)) {
            log.error("systemArgs List is empty in Redis.");
            return "systemArgs List is empty in Redis";
        }

        log.info("diagnosticCrypto finished.");
        return "diagnosticCrypto finished.";
    }

    @Override
    public Boolean diagnosticSms(Long uid, String phone) throws BCException {
        UmsMember user = customerService.queryCustomerWithNoPasswordById(uid);
        if(null==user){
            log.warn("user not found. Invalid user[{}]", uid);
            return false;
        }
        return validateNotifyHelper.smsValidateCode(user.getId(), user.getAreacode(), user.getMobile(), 1,
                PlatformEnum.UHHA.getCode(), BusinessTypeEnum.SMS_APP_REGISTER.getCode(), LocaleEnum.EN_US.getCode(),0.0);
    }

    @Override
    public Boolean diagnosticEmail(String email) {
        return true;
//        return validateHelper.mailSendCode(email, PlatformEnum.BC, LocaleEnum.EN_US, BusinessTypeEnum.SMS_NEW_IP_LOGIN, "localhost", 0);
    }

    @Override
    public Boolean diagnosticPayment() {
        return true;
    }
}

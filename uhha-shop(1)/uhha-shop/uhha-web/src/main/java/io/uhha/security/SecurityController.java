package io.uhha.security;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.uhha.appletsutil.AppletsLoginUtils;
import io.uhha.coin.common.Enum.LogUserActionEnum;
import io.uhha.coin.common.Enum.ScoreTypeEnum;
import io.uhha.coin.common.Enum.validate.BusinessTypeEnum;
import io.uhha.coin.common.Enum.validate.LocaleEnum;
import io.uhha.coin.common.Enum.validate.PlatformEnum;
import io.uhha.coin.common.framework.mq.ScoreHelper;
import io.uhha.coin.common.framework.redis.RedisCryptoHelper;
import io.uhha.coin.common.result.Result;
import io.uhha.common.constant.Constant;
import io.uhha.coin.common.util.LanguageUtil;
import io.uhha.common.utils.Utils;
import io.uhha.coin.user.service.UserCapitalService;
import io.uhha.validate.service.IValidityCheckService;
import io.uhha.common.notify.ValidateNotifyHelper;
import io.uhha.common.annotation.UnAuth;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.utils.MessageUtils;
import io.uhha.common.utils.ip.IpUtils;
import io.uhha.member.domain.UmsMember;
import io.uhha.member.service.IUmsMemberService;
import io.uhha.member.vo.ValidateCodeParams;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/security")
@Api(tags = "安全接口")
@Slf4j
public class SecurityController{

    @Autowired
    private IUmsMemberService userService;
    @Autowired
    private ValidateNotifyHelper validateNotifyHelper;
    @Autowired
    private UserCapitalService userCapitalService;
    @Autowired
    private RedisCryptoHelper redisCryptoHelper;
    @Autowired
    private ScoreHelper scoreHelper;
    @Autowired
    private IValidityCheckService validityCheckService;

    /**
     * 注入会员接口
     */
    @Autowired
    private IUmsMemberService customerService;

    @ApiOperation(value = "发送验证短信", notes = "SendValidateCode", response = AjaxResult.class)
    @RequestMapping(value = "/sendValidateCode", method = RequestMethod.POST)
    public AjaxResult sendValidateCode(
            HttpServletRequest request,
            @RequestBody ValidateCodeParams params) {
        try {
            UmsMember user = customerService.queryCustomerWithNoPasswordById(AppletsLoginUtils.getInstance().getCustomerId(request));

            if (StringUtils.isEmpty(user.getMobile()) || StringUtils.isEmpty(user.getAreacode())) {
                return AjaxResult.error(MessageUtils.message("app.com.err.auth.3"));
            }

            boolean i = validateNotifyHelper.smsValidateCode(user.getId(), user.getAreacode(), user.getMobile(), params.getMsgType(),
                    PlatformEnum.UHHA.getCode(), smsTypeToBusinessType(params.getType()), LocaleEnum.ZH_CN.getCode(),params.getPrice());
            if (i) {
                return AjaxResult.success(MessageUtils.message("app.com.err.pwd.12"));
            }
            return AjaxResult.error(MessageUtils.message("app.com.validate.err.108"));
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(MessageUtils.message("app.com.err.net.0"));
        }
    }


    @UnAuth
    @ApiOperation(value = "发送验证短信(无需登录)", notes = "SendValidateCodeNoLogin", response = AjaxResult.class)
    @RequestMapping(value = "/sendValidateCodeNoLogin", method = RequestMethod.POST)
    public AjaxResult SendValidateCodeNoLogin(@RequestBody ValidateCodeParams params) {
        try {
            String mobile = params.getMobile();
            String areacode = params.getAreacode();
            Integer type = params.getType();
            Integer msgtype = params.getMsgType();

            boolean i = validateNotifyHelper.smsValidateCode(null, areacode, mobile, msgtype,
                    PlatformEnum.UHHA.getCode(), smsTypeToBusinessType(type), LocaleEnum.ZH_CN.getCode(),0.0);
            if (i) {
                return AjaxResult.success(MessageUtils.message("app.com.suc.1"));
            }
            return AjaxResult.error(MessageUtils.message("app.com.validate.err.108"));
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(MessageUtils.message("app.com.err.net.0"));
        }
    }

    /**
     * 注册邮箱验证接口
     * @param email 电子邮件
     * @return
     */
    @UnAuth
    @ApiOperation(value = "发送验证码邮件", notes = "SendValidateEmail", response = AjaxResult.class)
    @RequestMapping(value = "/sendValidateEmail", method = RequestMethod.POST)
    public AjaxResult sendValidateEmail(HttpServletRequest request,
            @RequestParam(value = "email", required = true) String email) {

        boolean result = false;
        try {
            UmsMember newuser = new UmsMember();
            newuser.setEmail(email);
            List<UmsMember> umsMembers = null;
            try {
                umsMembers = this.userService.selectUmsMemberList(newuser);
            } catch (Exception e) {
                log.error("sendValidateEmail err {}", e.toString());
            }
            if (null!=umsMembers && umsMembers.size() > 0) {
                return AjaxResult.error(MessageUtils.message("app.com.err.reg.2"));
            }
            boolean emailvalidate = this.validateNotifyHelper.mailOverdueValidate(email, PlatformEnum.UHHA, LocaleEnum.ZH_CN
                    , BusinessTypeEnum.EMAIL_REGISTER_CODE,0);
            if (emailvalidate) {
                AjaxResult.error("3分钟内只能发送一次");
            }
            try {
                result = validateNotifyHelper.mailSendCode(email, PlatformEnum.UHHA, LocaleEnum.ZH_CN, Utils.getIpAddr(request),0);
            } catch (Exception e) {
                log.error("sendValidateEmail err {}", e.toString());
            }
            if (result) {
                return AjaxResult.success(MessageUtils.message("app.com.suc.1"));
            } else {
                return AjaxResult.error(MessageUtils.message("app.com.err.pwd.1"));
            }

        } catch (Exception e) {
            log.error("sendValidateEmail err {}", e.toString());
            return AjaxResult.error(MessageUtils.message("app.com.err.net.0"));
        }
    }

    /**
     * 发送绑定邮件
     * @param lan 语言
     * @param email 电子邮箱
     * @return
     */
    @UnAuth
    @ApiOperation(value = "发送绑定邮件", notes = "SendBindEmail", response = AjaxResult.class)
    @RequestMapping(value = "/sendBindEmail", method = RequestMethod.POST)
    public AjaxResult SendBindEmail(
            HttpServletRequest request,
            @RequestParam(value = "lan", required = true) String lan,
            @RequestParam(value = "email", required = true) String email) {
        try {
            LocaleEnum localeLan = LanguageUtil.getLan(lan);
            UmsMember user = new UmsMember();
            if (!email.equals("0")){
                user.setEmail(email);
                user.setUsername(email);
            }
            List<UmsMember> dUsers = userService.selectUmsMemberList(user);
            if(dUsers!=null && dUsers.size()>0){
                return AjaxResult.error(MessageUtils.message("app.com.err.reg.2"));
            }
            user.setEmail(email);
            user = userService.selectUmsMemberById(user.getId());
            boolean result = validateNotifyHelper.mailSendContent(email, PlatformEnum.UHHA, localeLan
                    , BusinessTypeEnum.EMAIL_VALIDATE_BING, Utils.getIpAddr(request), user,2);
            if (result) {
                this.userService.updateCustomer(user);
                return AjaxResult.success(MessageUtils.message("app.com.suc.1"));
            } else {
                return AjaxResult.error("5分钟内只能发送一次");
            }
        } catch (Exception e) {
            log.error("SendBindEmail err {}", e.toString());
            return AjaxResult.error(MessageUtils.message("app.com.err.net.0"));
        }
    }

    /**
     * 邮件找回密码发送邮件
     * @param email
     * @param lan
     * @return
     */
    @UnAuth
    @ApiOperation(value = "发送找回邮件", notes = "SendBackEmail", response = AjaxResult.class)
    @RequestMapping(value = "/sendBackEmail", method = RequestMethod.POST)
    public AjaxResult SendBackEmail(
            HttpServletRequest request,
            @RequestParam(value = "email", required = true) String email,
            @RequestParam(value="lan" , required = true) String lan
    ) {
        try {
            LocaleEnum localeLan = LanguageUtil.getLan(lan);
            UmsMember fuser = new UmsMember();
            fuser.setEmail(email);
            List<UmsMember> fusers = this.userService.selectUmsMemberList(fuser);
            // 没找到用户
            if (fusers ==null ||fusers.size() <= 0) {
                return AjaxResult.error(MessageUtils.message("app.com.err.usr.0"));
            }
            fuser = fusers.get(0);

            if (!email.matches(Constant.EmailReg)) {
                return AjaxResult.error(MessageUtils.message("app.com.err.reg.4"));
            }
            if (fuser.isEmailVerified()) {
                boolean emailvalidate = this.validateNotifyHelper.mailOverdueValidate(email, PlatformEnum.UHHA, localeLan
                        , BusinessTypeEnum.APP_EMAIL_FIND_PASSWORD,0);
                if (emailvalidate) {
                    return AjaxResult.error(MessageUtils.message("app.com.err.pwd.10"));
                }
                boolean result = false;
                try {
                    result = validateNotifyHelper.mailSendCode(email, PlatformEnum.UHHA, localeLan
                            , BusinessTypeEnum.APP_EMAIL_FIND_PASSWORD, Utils.getIpAddr(request),0);
                } catch (Exception e) {
                    e.printStackTrace();
                    result = false;
                }
                if (result) {
                    return AjaxResult.success(MessageUtils.message("app.com.err.pwd.1"));
                } else {
                    return AjaxResult.error(MessageUtils.message("app.com.err.pwd.10"));
                }
            } else {
                return AjaxResult.error(MessageUtils.message("app.com.err.pwd.2"));
            }
        } catch (Exception e) {
            log.error("SendBackEmail err {}", e.toString());
            return AjaxResult.error(MessageUtils.message("app.com.err.net.0"));
        }
    }

    /**
     * 邮箱找回密码
     * @param gcode
     * @param ecode
     * @param newpassword
     * @param repassword
     * @param email
     * @return
     * @throws Exception
     */
    @UnAuth
    @ApiOperation(value = "邮箱找回密码", notes = "findbackPasswordByEmail", response = AjaxResult.class)
    @RequestMapping(value = "/findbackPasswordByEmail", method = RequestMethod.POST)
    public AjaxResult findbackPasswordByEmail(
            HttpServletRequest request,
            @RequestParam(value = "gcode", required = false) String gcode,
            @RequestParam(value = "ecode", required = false) String ecode,
            @RequestParam(value = "newpassword", required = true) String newpassword,
            @RequestParam(value = "repassword", required = true) String repassword,
            @RequestParam(value = "email", required = true) String email,
            @RequestParam(value = "lan",required = true) String lan
    ) throws Exception {
        try {
            //IP
            String ip = IpUtils.getIpAddr(request);
            //LoginUser
            UmsMember fuser = new UmsMember();
            fuser.setEmail(email);
            List<UmsMember> fusers = userService.selectUmsMemberList(fuser);

            // 没找到用户
            if (fusers==null || fusers.size() <= 0) {
                return AjaxResult.error(MessageUtils.message("app.com.err.usr.0"));
            }
            fuser = fusers.get(0);

            if ((StringUtils.isEmpty(gcode) && fuser.isGoogleVerified())
                    || (StringUtils.isEmpty(ecode) && fuser.isMobileVerified())) {
                return AjaxResult.error(MessageUtils.message("app.com.err.reg.7"));
            }

            if (!newpassword.equals(repassword)) {
                return AjaxResult.error(MessageUtils.message("app.com.err.pwd.6"));
            }

            if (!newpassword.matches(Constant.passwordReg)) {
                return AjaxResult.error(MessageUtils.message("app.com.err.pwd.0"));
            }

            //登录密码不能与交易密码相同
            if (!StringUtils.isEmpty(fuser.getPaypassword()) && fuser.getPaypassword().equals(Utils.MD5(newpassword))) {
                return AjaxResult.error(MessageUtils.message("app.com.err.usr.19"));
            }
            Boolean codeValidate = validateNotifyHelper.mailCodeValidate(email, PlatformEnum.UHHA, LanguageUtil.getLan(lan), BusinessTypeEnum.APP_EMAIL_FIND_PASSWORD, ecode,0);
            if(codeValidate){
                return AjaxResult.error(MessageUtils.message("app.com.err.reg.5"));
            }
            if(fuser.isGoogleVerified()){
                Result googleCodeCheck = validityCheckService.getGoogleCodeCheck(fuser.getGoogleAuthenticator(), gcode, ip);
                if (!googleCodeCheck.getSuccess()) {
                    return AjaxResult.error(googleCodeCheck.getMsg());
                }
            }
            fuser = userService.selectUmsMemberById(fuser.getId());

            fuser.setPassword(Utils.MD5(newpassword));
            fuser.setIp(ip);
            int i = userService.updateCustomer(fuser);
            if (i==1) {
                return AjaxResult.success(MessageUtils.message("app.com.suc.5"));
            }
            return AjaxResult.error(MessageUtils.message("app.com.err.1"));
        } catch (Exception e) {
            log.error("findbackPassword err {}", e.toString());
            return AjaxResult.error(MessageUtils.message("app.com.err.net.0"));
        }
    }


    @UnAuth
    @ApiOperation(value = "绑定手机", notes = "bindTelePhone", response = AjaxResult.class)
    @RequestMapping(value = "/bindTelePhone", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult validatePhone(
            HttpServletRequest request,
            @RequestParam(value = "areaCode", required = true) String areaCode,
            @RequestParam(value = "phone", required = true) String phone,
            @RequestParam(value = "oldcode", required = false) String oldcode,
            @RequestParam(value = "newcode", required = true) String newcode,
            @RequestParam(value = "googleCode", required = false, defaultValue = "0") String totpCode) {
        try {
            UmsMember fuser = customerService.queryCustomerWithNoPasswordById(AppletsLoginUtils.getInstance().getCustomerId(request));

            if ("86".equals(areaCode) && !phone.matches(Constant.PhoneReg)) {// 手機格式不對
                return AjaxResult.error(MessageUtils.message("app.com.err.reg.3"));
            }
            areaCode = areaCode.replace("+", "");
            String ip = Utils.getIpAddr(request);

            //短信验证和google验证
            Result validityResult = validityCheckService.getChangeCheck(fuser, oldcode,
                    BusinessTypeEnum.SMS_UNBIND_MOBILE.getCode(), totpCode, ip, PlatformEnum.UHHA.getCode());
            if (!validityResult.getSuccess()) {
                return AjaxResult.error(MessageUtils.message("app.com.validate.err." + validityResult.getCode(), validityResult.getData()));
            }
            //新手机号码判断
            validityResult = validityCheckService.getPhoneCodeCheck(areaCode, phone, newcode,
                    BusinessTypeEnum.SMS_BING_MOBILE.getCode(), ip, PlatformEnum.UHHA.getCode());
            if (!validityResult.getSuccess()) {
                return AjaxResult.error(MessageUtils.message("app.com.validate.err." + validityResult.getCode(), validityResult.getData()));
            }

            // 判斷手機是否被綁定了
            UmsMember newUser = new UmsMember();
            newUser.setMobile(phone);
            List<UmsMember> fusers = userService.selectUmsMemberList(newUser);
            if (fusers!=null && fusers.size() > 0) {// 手機號碼已經被綁定了
                return AjaxResult.error(MessageUtils.message("app.com.err.reg.1"));
            }
            fuser.setAreacode(areaCode);
            fuser.setMobile(phone);
            fuser.setIsMobileVerification("1");
            fuser.setModifyTime(Utils.getTimestamp());
            fuser.setIp(ip);
            if (!fuser.getUsername().matches(Constant.EmailReg)) {
                fuser.setUsername(phone);
                fuser.setNickname(phone);
            }
            LogUserActionEnum action = LogUserActionEnum.BIND_PHONE;
            ScoreTypeEnum scoreType = ScoreTypeEnum.PHONE;
            if (fuser.isMobileVerified()) {
                action = LogUserActionEnum.MODIFY_PHONE;
                scoreType = null;
            }
            userService.updateCustomer(fuser);
//            if (userService.updateCustomer(fuser)) {
                // 更新redis中的用户信息
                //TODO要求重新登录
//                updateUserInfo(fuser);
//            }
            // 成功
            return AjaxResult.success(MessageUtils.message("app.com.suc.0"));
        } catch (Exception e) {
            log.error("validatePhone err {}", e.toString());
            return AjaxResult.error(MessageUtils.message("app.com.err.net.0"));
        }
    }


    /**
     * 短信映射
     *
     * @param smsType
     * @return
     */
    // TODO
    private Integer smsTypeToBusinessType(Integer smsType) {
        switch (smsType) {
            case 1:
                return BusinessTypeEnum.SMS_APPLY_API.getCode();
            case 2:
                return BusinessTypeEnum.SMS_BING_MOBILE.getCode();
            case 3:
                return BusinessTypeEnum.SMS_UNBIND_MOBILE.getCode();
            case 4:
                return BusinessTypeEnum.SMS_CNY_WITHDRAW.getCode();
            case 5:
                return BusinessTypeEnum.SMS_COIN_WITHDRAW.getCode();
            case 6:
                return BusinessTypeEnum.SMS_MODIFY_LOGIN_PASSWORD.getCode();
            case 7:
                return BusinessTypeEnum.SMS_MODIFY_TRADE_PASSWORD.getCode();
            case 8:
                return BusinessTypeEnum.SMS_COIN_WITHDRAW_ACCOUNT.getCode();
            case 9:
                return BusinessTypeEnum.SMS_FIND_PHONE_PASSWORD.getCode();
            case 10:
                return BusinessTypeEnum.SMS_CNY_ACCOUNT_WITHDRAW.getCode();
            case 11:
                return BusinessTypeEnum.SMS_WEB_REGISTER.getCode();
            case 12:
                return BusinessTypeEnum.SMS_APP_REGISTER.getCode();
            case 18:
                return BusinessTypeEnum.SMS_PUSHASSET.getCode();
            case 19:
                return BusinessTypeEnum.SMS_FINANCES.getCode();
            case 20:
                return BusinessTypeEnum.SMS_TRANSFER.getCode();
            case 21:
                return BusinessTypeEnum.SMS_LOGIN.getCode();
            case 22:
                return BusinessTypeEnum.SMS_STORE_REGISTER.getCode();
            default:
                return smsType;
        }
    }
}

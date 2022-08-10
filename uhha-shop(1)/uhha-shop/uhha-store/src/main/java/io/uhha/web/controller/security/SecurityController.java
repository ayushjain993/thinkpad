package io.uhha.web.controller.security;

import io.swagger.annotations.*;
import io.uhha.coin.common.Enum.LogUserActionEnum;
import io.uhha.coin.common.Enum.ScoreTypeEnum;
import io.uhha.coin.common.Enum.validate.BusinessTypeEnum;
import io.uhha.coin.common.Enum.validate.LocaleEnum;
import io.uhha.coin.common.Enum.validate.PlatformEnum;
import io.uhha.coin.common.result.Result;
import io.uhha.coin.common.util.LanguageUtil;
import io.uhha.common.annotation.UnAuth;
import io.uhha.common.constant.Constant;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.domain.entity.SysUser;
import io.uhha.common.notify.ValidateNotifyHelper;
import io.uhha.common.utils.MessageUtils;
import io.uhha.common.utils.Utils;
import io.uhha.store.service.IStoreSecurityService;
import io.uhha.system.domain.vo.UpdateStorePwdBean;
import io.uhha.system.service.ISysStoreUserService;
import io.uhha.validate.service.IValidityCheckService;
import io.uhha.web.utils.AdminLoginUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/security")
@Api(tags = "安全接口")
@Slf4j
public class SecurityController {

    @Autowired
    private ValidateNotifyHelper validateNotifyHelper;
    @Autowired
    private IValidityCheckService validityCheckService;

    @Autowired
    private ISysStoreUserService storeUserService;

    @Autowired
    private IStoreSecurityService storeSecurityService;


    @ApiOperation(value = "发送验证短信", notes = "SendValidateCode", response = AjaxResult.class)
    @RequestMapping(value = "/sendValidateCode", method = RequestMethod.POST)
    public AjaxResult sendValidateCode(
            HttpServletRequest request,
            @RequestParam(value = "type", required = true) int type,
            @ApiParam(value = "1：国内短信；2：语音短信")
            @RequestParam(value = "msgtype", required = false, defaultValue = "1") int msgtype) {
        try {
            SysUser user = storeUserService.selectUserById(AdminLoginUtils.getInstance().getManagerId());

            if (StringUtils.isEmpty(user.getPhonenumber()) || StringUtils.isEmpty(user.getAreacode())) {
                return AjaxResult.error(MessageUtils.message("app.com.err.auth.3"));
            }

            boolean i = validateNotifyHelper.smsValidateCode(user.getUserId(), user.getAreacode(), user.getPhonenumber(), msgtype,
                    PlatformEnum.UHHA.getCode(), smsTypeToBusinessType(type), LocaleEnum.EN_US.getCode(),0.0);
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
    public AjaxResult SendValidateCodeNoLogin(
            @RequestParam(value = "mobile", required = true) String mobile,
            @RequestParam(value = "type", required = true) int type,
            @RequestParam(value = "areacode", required = false, defaultValue = "86") String areacode,
            @ApiParam(value = "1：国内短信；2：语音短信")
            @RequestParam(value = "msgtype", required = false, defaultValue = "1") int msgtype) {
        try {

            boolean i = validateNotifyHelper.smsValidateCode(null, areacode, mobile, msgtype,
                    PlatformEnum.SMS.getCode(), smsTypeToBusinessType(type), LocaleEnum.EN_US.getCode(),0.0);
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
     * @param email
     * @return
     */
    @UnAuth
    @ApiOperation(value = "发送验证码邮件", notes = "SendValidateEmail", response = AjaxResult.class)
    @RequestMapping(value = "/sendValidateEmail", method = RequestMethod.POST)
    public AjaxResult sendValidateEmail(HttpServletRequest request,
            @RequestParam(value = "email", required = true) String email) {

        boolean result = false;
        try {
            SysUser user = storeUserService.selectUserByEmail(email);
            if (null==user) {
                return AjaxResult.error(MessageUtils.message("app.com.err.reg.2"));
            }
            boolean emailvalidate = this.validateNotifyHelper.mailOverdueValidate(email, PlatformEnum.SMS, LocaleEnum.ZH_CN
                    , BusinessTypeEnum.EMAIL_REGISTER_CODE,0);
            if (emailvalidate) {
                AjaxResult.error("3分钟内只能发送一次");
            }
            try {
                result = validateNotifyHelper.mailSendCode(email, PlatformEnum.SMS, LocaleEnum.ZH_CN, Utils.getIpAddr(request),0);
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
     * @param lan
     * @param email
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
            SysUser user = storeUserService.selectUserByEmail(email);
            if(user==null){
                return AjaxResult.error(MessageUtils.message("app.com.err.reg.2"));
            }

            boolean result = validateNotifyHelper.mailSendContent(email, PlatformEnum.SMS, localeLan
                    , BusinessTypeEnum.EMAIL_VALIDATE_BING, Utils.getIpAddr(request), user,2);
            if (result) {
                this.storeUserService.updateUser(user);
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
            SysUser user = storeUserService.selectUserByEmail(email);
            // 没找到用户
            if (null == user) {
                return AjaxResult.error(MessageUtils.message("app.com.err.usr.0"));
            }

            if (!email.matches(Constant.EmailReg)) {
                return AjaxResult.error(MessageUtils.message("app.com.err.reg.4"));
            }
            if (user.isEmailVerified()) {
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
//            String ip = IpUtils.getIpAddr(request);
            //LoginUser
            SysUser user = storeUserService.selectUserByEmail(email);

            // 没找到用户
            if (null == user) {
                return AjaxResult.error(MessageUtils.message("app.com.err.usr.0"));
            }

//            if ((StringUtils.isEmpty(gcode) && user.isGoogleVerified())
//                    || (StringUtils.isEmpty(ecode) && user.isMobileVerified())) {
//                return AjaxResult.error(MessageUtils.message("app.com.err.reg.7"));
//            }

            if (!newpassword.equals(repassword)) {
                return AjaxResult.error(MessageUtils.message("app.com.err.pwd.6"));
            }

            if (!newpassword.matches(Constant.passwordReg)) {
                return AjaxResult.error(MessageUtils.message("app.com.err.pwd.0"));
            }

            Boolean codeValidate = validateNotifyHelper.mailCodeValidate(email, PlatformEnum.UHHA, LanguageUtil.getLan(lan), BusinessTypeEnum.APP_EMAIL_FIND_PASSWORD, ecode,0);
            if(codeValidate){
                return AjaxResult.error(MessageUtils.message("app.com.err.reg.5"));
            }
//            if(user.isGoogleVerified()){
//                Result googleCodeCheck = validityCheckService.getGoogleCodeCheck(user.getGoogleAuthenticator(), gcode, ip);
//                if (!googleCodeCheck.getSuccess()) {
//                    return AjaxResult.error(googleCodeCheck.getMsg());
//                }
//            }
            user = storeUserService.selectUserById(user.getUserId());

            user.setPassword(Utils.MD5(newpassword));
            int i = storeUserService.updateUser(user);
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
            Long adminId = AdminLoginUtils.getInstance().getManagerId();

            SysUser fuser = storeUserService.selectUserById(adminId);

            if ("86".equals(areaCode) && !phone.matches(Constant.PhoneReg)) {// 手機格式不對
                return AjaxResult.error(MessageUtils.message("app.com.err.reg.3"));
            }
            areaCode = areaCode.replace("+", "");
            String ip = Utils.getIpAddr(request);

            //短信验证和google验证
            Result validityResult = validityCheckService.getChangeCheck(fuser, oldcode,
                    BusinessTypeEnum.SMS_UNBIND_MOBILE.getCode(), totpCode, ip, PlatformEnum.SMS.getCode());
            if (!validityResult.getSuccess()) {
                return AjaxResult.error(MessageUtils.message("app.com.validate.err." + validityResult.getCode(), validityResult.getData()));
            }
            //新手机号码判断
            validityResult = validityCheckService.getPhoneCodeCheck(areaCode, phone, newcode,
                    BusinessTypeEnum.SMS_BING_MOBILE.getCode(), ip, PlatformEnum.SMS.getCode());
            if (!validityResult.getSuccess()) {
                return AjaxResult.error(MessageUtils.message("app.com.validate.err." + validityResult.getCode(), validityResult.getData()));
            }

            // 判斷手機是否被綁定了
            SysUser fuser1 = storeUserService.selectUserByMobile(phone);
            if (null== fuser1) {
                return AjaxResult.error(MessageUtils.message("app.com.err.reg.1"));
            }
            fuser1.setAreacode(areaCode);
            fuser1.setPhonenumber(phone);
            fuser1.setIsMobileVerification("1");
            fuser1.setUpdateTime(Utils.getTimestamp());
            if (!fuser1.getUserName().matches(Constant.EmailReg)) {
                fuser1.setUserName(phone);
                fuser1.setNickName(phone);
            }
            LogUserActionEnum action = LogUserActionEnum.BIND_PHONE;
            ScoreTypeEnum scoreType = ScoreTypeEnum.PHONE;
            if (fuser1.isMobileVerified()) {
                action = LogUserActionEnum.MODIFY_PHONE;
                scoreType = null;
            }
            storeUserService.updateUser(fuser);
            // 成功
            return AjaxResult.success(MessageUtils.message("app.com.suc.0"));
        } catch (Exception e) {
            log.error("validatePhone err {}", e.toString());
            return AjaxResult.error(MessageUtils.message("app.com.err.net.0"));
        }
    }


    /**
     * 找回用户密码
     *
     * @param updatePwdBean 修改密码实体
     * @return -1 参数错误  -2 验证码错误 -3 用户不匹配 0 失败 1 成功
     */
    @RequestMapping("/recover")
    @ResponseBody
    @ApiOperation(value = "修改用户密码", notes = "修改用户密码（需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "mobile", value = "手机号"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "code", value = "验证码"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "password", value = "密码"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "-1 参数错误  -2 验证码错误 -3 用户不匹配 0 失败 1 成功", response = Integer.class)
    })
    public AjaxResult recoverPassword(@RequestBody UpdateStorePwdBean updatePwdBean) {
        //验证验证码
        if(!validateNotifyHelper.validateSmsCode(updatePwdBean.getAreacode(),
                updatePwdBean.getMobile(),
                PlatformEnum.SMS.getCode(),
                BusinessTypeEnum.SMS_STORE_FIND_PHONE_PASSWORD.getCode(),
                updatePwdBean.getCode()))
        {
            log.info("login fail due to validateCode fail....");
            return AjaxResult.error();
        }

        //修改密码
        return AjaxResult.success(storeSecurityService.recoverPassword(updatePwdBean));
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
            case 23:
                return BusinessTypeEnum.SMS_STORE_FIND_PHONE_PASSWORD.getCode();
            default:
                return smsType;
        }
    }
}

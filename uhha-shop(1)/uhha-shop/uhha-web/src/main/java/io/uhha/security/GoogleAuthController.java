package io.uhha.security;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.uhha.appletsutil.AppletsLoginUtils;
import io.uhha.coin.common.google.GoogleAuth;
import io.uhha.coin.common.result.Result;
import io.uhha.coin.common.result.ResultErrorCode;
import io.uhha.validate.service.IValidityCheckService;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.enums.UmsMemberChangeEventEnum;
import io.uhha.common.utils.MessageUtils;
import io.uhha.common.utils.StringUtils;
import io.uhha.common.utils.Utils;
import io.uhha.member.domain.UmsMember;
import io.uhha.member.service.IUmsMemberChangeHistoryService;
import io.uhha.member.service.IUmsMemberService;
import io.uhha.vo.GoogleBindVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Google认证器控制器
 */
@RestController
@Api(tags = "Google认证器控制器")
@Slf4j
public class GoogleAuthController {

    @Autowired
    private IUmsMemberService umsMemberService;

    @Autowired
    private IValidityCheckService validityCheckService;

    @Autowired
    private IUmsMemberChangeHistoryService umsMemberChangeHistoryService;

    /**
     * 添加谷歌设备
     */
    @ResponseBody
    @RequestMapping(value = "/customer/bind_google_device", method = RequestMethod.GET)
    public AjaxResult googleAuth(HttpServletRequest request) {
        Long userId = AppletsLoginUtils.getInstance().getCustomerId(request);

        UmsMember umsMember = null;
        JSONObject jsonObject = new JSONObject();
        try {
            umsMember = umsMemberService.selectUmsMemberById(userId);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        if (null == umsMember) {
            log.error("user not found.");
            return AjaxResult.error(MessageUtils.message("com.public.error.10001"));
        }

        if (umsMember.isGoogleVerified()) {
            log.error("userId {} was already bind google authenticator.", userId);
            return AjaxResult.error(MessageUtils.message("com.public.error.10001"));
        }
        Map<String, String> map = GoogleAuth.genSecret(umsMember.getUsername());
        String totpKey = map.get("secret");
        String qecode = map.get("url");
        jsonObject.put("qecode", qecode);
        jsonObject.put("totpKey", totpKey);

        return AjaxResult.success(jsonObject);
    }


    /**
     * 添加设备认证
     *
     * @param googleBindVo    vo
     * @return
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @ResponseBody
    @RequestMapping(value = "/customer/google_auth", method = RequestMethod.POST)
    public AjaxResult validateAuthenticator(HttpServletRequest request, @RequestBody GoogleBindVo googleBindVo) {
        Long userId = AppletsLoginUtils.getInstance().getCustomerId(request);

        String totpKey = googleBindVo.getTotpkey();
        String qecode = googleBindVo.getQecode();
        String code = googleBindVo.getCode();

        UmsMember umsMember = null;
        try {
            umsMember = umsMemberService.selectUmsMemberById(userId);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        if (null == umsMember) {
            log.error("user not found.");
            return AjaxResult.error(MessageUtils.message("com.public.error.10001"));
        }

        boolean b_status = ("1".equalsIgnoreCase(umsMember.getIsGoogleVerification()))
                || !StringUtils.isEmpty(umsMember.getGoogleAuthenticator());
        if (b_status) {
            // 非法提交
            return AjaxResult.error(MessageUtils.message("com.public.error.10001"));
        }
        //验证谷歌
        String ip = Utils.getIpAddr(request);
        Result checkResult = validityCheckService.getGoogleCodeCheck(totpKey, code, ip);
        if (!checkResult.getSuccess()) {
            if (checkResult.getCode() == ResultErrorCode.GOOGLE_LIMIT_BEYOND_ERROR.getCode()) {
                return AjaxResult.error(MessageUtils.message("com.user.error.10025"));
            }
            if (checkResult.getCode() == ResultErrorCode.GOOGLE_LIMIT_COUNT_ERROR.getCode()) {
                return AjaxResult.error(MessageUtils.message("com.user.error.10024", checkResult.getData()));
            }
        }
        umsMember.setGoogleAuthenticator(totpKey);
        umsMember.setGoogleUrl(qecode);
        umsMember.setIsGoogleVerification("1");
        umsMember.setModifyTime(Utils.getTimestamp());
        umsMember.setIp(ip);
        try {
            if (umsMemberService.updateCustomer(umsMember) != 1) {
                log.error(MessageUtils.message("com.public.error.10000"));
                return AjaxResult.error(MessageUtils.message("com.public.error.10000"));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        //记录用户事件
        umsMemberChangeHistoryService.logMemberEvent(UmsMemberChangeEventEnum.BIND_GOOGLE_AUTH, umsMember.getMobile(),"user");
        return AjaxResult.success(MessageUtils.message("com.user.succ.10003"));
    }

    /**
     * 查看谷歌密匙
     */
    @ResponseBody
    @RequestMapping(value = "/customer/get_google_key", method = RequestMethod.GET)
    public AjaxResult getGoogleTotpKey(HttpServletRequest request,
                                       @RequestParam String totpCode) {
        JSONObject jsonObject = new JSONObject();
        Long userId = AppletsLoginUtils.getInstance().getCustomerId(request);

        UmsMember umsMember = null;
        try {
            umsMember = umsMemberService.selectUmsMemberById(userId);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        if (null == umsMember) {
            log.error("user not found.");
            return AjaxResult.error(MessageUtils.message("com.public.error.10001"));
        }
        //验证谷歌
        String ip = Utils.getIpAddr(request);
        Result checkResult = validityCheckService.getGoogleCodeCheck(umsMember.getGoogleAuthenticator(), totpCode, ip);
        if (!checkResult.getSuccess()) {
            if (checkResult.getCode() == ResultErrorCode.GOOGLE_LIMIT_BEYOND_ERROR.getCode()) {
                return AjaxResult.error(MessageUtils.message("com.user.error.10025"));
            }
            if (checkResult.getCode() == ResultErrorCode.GOOGLE_LIMIT_COUNT_ERROR.getCode()) {
                return AjaxResult.error(MessageUtils.message("com.user.error.10024", checkResult.getData()));
            }
        }
        jsonObject.put("qecode", umsMember.getGoogleUrl());
        jsonObject.put("code", 0);
        jsonObject.put("totpKey", umsMember.getGoogleAuthenticator());
        return AjaxResult.success(jsonObject);
    }


}

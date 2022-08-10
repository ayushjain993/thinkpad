package io.uhha.member;

import io.uhha.appletsutil.AppletsLoginUtils;
import io.uhha.common.annotation.UnAuth;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.redis.RedisCache;
import io.uhha.common.enums.UmsMemberChangeEventEnum;
import io.uhha.member.service.IUmsMemberChangeHistoryService;
import io.uhha.member.service.UpdatePwdService;
import io.uhha.member.vo.UpdatePwdBean;
import io.uhha.util.CommonConstant;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

/**
 * 修改用户密码控制器
 */
@Controller
@RequestMapping("/updatepwd")
@Api(tags = "修改用户密码接口")
public class UpdatePwdController {

    /**
     * 注入修改密码服务接口
     */
    @Autowired
    private UpdatePwdService updatePwdService;

    /**
     * 注入redis服务
     */
    @Autowired
    private RedisCache redisService;

    @Autowired
    private IUmsMemberChangeHistoryService umsMemberChangeHistoryService;

    /**
     * 找回用户密码
     *
     * @param updatePwdBean 修改密码实体
     * @return -1 参数错误  -2 验证码错误 -3 用户不匹配 0 失败 1 成功
     */
    @UnAuth
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
    public AjaxResult recoverPassword(@RequestBody UpdatePwdBean updatePwdBean) {

        return AjaxResult.success(updatePwdService.recoverPassword(updatePwdBean));
    }

    /**
     * 修改用户密码
     *
     * @param updatePwdBean 修改密码实体
     * @return -1 参数错误  -2 验证码错误 -3 用户不匹配 0 失败 1 成功
     */
    @RequestMapping("/update")
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
    public AjaxResult updatePassword(HttpServletRequest request, @ApiIgnore @RequestBody UpdatePwdBean updatePwdBean) {
        updatePwdBean.setCustomerId(AppletsLoginUtils.getInstance().getCustomerId(request));
        //记录本次用户事件
        umsMemberChangeHistoryService.logMemberEvent(UmsMemberChangeEventEnum.LOGIN_PASSWORD_CHANGED, updatePwdBean.getMobile(),"user");
        return AjaxResult.success(updatePwdService.updatePassword(updatePwdBean));
    }
}

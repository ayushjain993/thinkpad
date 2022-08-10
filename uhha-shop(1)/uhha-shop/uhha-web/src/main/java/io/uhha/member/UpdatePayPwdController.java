package io.uhha.member;

import io.uhha.appletsutil.AppletsLoginUtils;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.redis.RedisCache;
import io.uhha.common.enums.UmsMemberChangeEventEnum;
import io.uhha.member.service.IUmsMemberChangeHistoryService;
import io.uhha.member.service.IUmsPreDepositRecordService;
import io.uhha.member.vo.UpdatePayPwdBean;
import io.uhha.util.CommonConstant;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

/**
 * 修改用户支付密码控制器
 */
@Controller
@RequestMapping("/updatepaypwd")
@Api(tags = "修改用户支付密码接口")
public class UpdatePayPwdController {

    /**
     * 注入修改密码服务接口
     */
    @Autowired
    private IUmsPreDepositRecordService predepositRecordService;

    /**
     * 注入redis服务
     */
    @Autowired
    private RedisCache redisService;

    @Autowired
    private IUmsMemberChangeHistoryService umsMemberChangeHistoryService;


    /**
     * 修改用户支付密码
     *
     * @param updatePayPwdBean 修改支付密码实体
     * @return -1 参数错误  -2 验证码错误 -3 用户不匹配 0 失败 1 成功
     */
    @RequestMapping("/update")
    @ResponseBody
    @ApiOperation(value = "修改用户支付密码", notes = "修改用户支付密码（需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "mobile", value = "手机号"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "code", value = "验证码"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "password", value = "密码"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "-1 参数错误  -2 验证码错误 -3 用户不匹配 0 失败 1 成功", response = Integer.class)
    })
    public AjaxResult updatePassword(HttpServletRequest request, @ApiIgnore UpdatePayPwdBean updatePayPwdBean) {
        updatePayPwdBean.setCustomerId(AppletsLoginUtils.getInstance().getCustomerId(request));
        //记录本次用户事件
        umsMemberChangeHistoryService.logMemberEvent(UmsMemberChangeEventEnum.PAY_PASSWORD_CHANGED, updatePayPwdBean.getMobile(),"user");
        return AjaxResult.success(predepositRecordService.updatePayPassword(updatePayPwdBean));
    }

}

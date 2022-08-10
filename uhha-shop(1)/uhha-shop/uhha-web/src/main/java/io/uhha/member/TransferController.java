package io.uhha.member;

import io.swagger.annotations.*;
import io.uhha.appletsutil.AppletsLoginUtils;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.member.domain.UmsWithdraw;
import io.uhha.member.service.IUmsTransferService;
import io.uhha.member.vo.UmsTransferVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(tags = "会员转账接口")
public class TransferController {

    @Autowired
    private IUmsTransferService transferService;

    /**
     * 从分销余额转账到可用余额
     *
     * @param transferVo 转账记录实体
     * @return 1成功 0失败 -1用户不存在 -2用户没有设置支付密码 -3支付密码不正确 -4参数不全 -5佣金不足
     */
    @PostMapping("/transfer")
    @ResponseBody
    @ApiOperation(value = "从分销余额转账到可用余额", notes = "从分销余额转账到可用余额（需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "money", value = "金额"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "password", value = "支付密码"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "1成功 0失败 -1用户不存在 -2用户没有设置支付密码 -3支付密码不正确 -4参数不全 -5佣金不足", response = Integer.class)
    })
    public AjaxResult addTransferRecord(@RequestBody @Validated UmsTransferVo transferVo, HttpServletRequest request) {
        Long userId = AppletsLoginUtils.getInstance().getCustomerId(request);
        return AjaxResult.success(transferService.umsTransfer(transferVo, userId));
    }
}

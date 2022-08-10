package io.uhha.member;

import io.uhha.appletsutil.AppletsLoginUtils;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.member.domain.UmsWithdraw;
import io.uhha.member.service.IUmsWithdrawService;
import io.uhha.member.vo.UmsWithdrawVo;
import io.uhha.util.PageHelper;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

/**
 * 会员提现控制器
 *
 * @author mj created on 2020/6/22
 */
@RestController
@Api(tags = "会员提现接口")
public class WithdrawController {

    /**
     * 注入提现记录服务
     */
    @Autowired
    private IUmsWithdrawService withdrawRecordService;


    /**
     * 增加会员提现记录
     *
     * @param withdrawRecord 提现记录实体
     * @param password       支付密码
     * @return 1成功 0失败 -1用户不存在 -2用户没有设置支付密码 -3支付密码不正确 -4参数不全 -5佣金不足
     */
    @RequestMapping("/addwithdrawrecord1")
    @ResponseBody
    @ApiOperation(value = "增加会员提现记录", notes = "增加会员提现记录（需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "money", value = "金额"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "account", value = "支付宝账号"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "name", value = "姓名"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "password", value = "支付密码"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "1成功 0失败 -1用户不存在 -2用户没有设置支付密码 -3支付密码不正确 -4参数不全 -5佣金不足", response = Integer.class)
    })
    @Deprecated
    public AjaxResult addWithdrawRecord1(@ApiIgnore UmsWithdraw withdrawRecord, String password, HttpServletRequest request) {
        return AjaxResult.success(withdrawRecordService.addWithdrawRecord(withdrawRecord.buildCustomerId(AppletsLoginUtils.getInstance().getCustomerId(request)), password));
    }

    /**
     * 增加会员提现记录
     *
     * @param withdrawVo 提现记录实体
     * @return 1成功 0失败 -1用户不存在 -2用户没有设置支付密码 -3支付密码不正确 -4参数不全 -5佣金不足
     */
    @RequestMapping("/addwithdrawrecord")
    @ResponseBody
    @ApiOperation(value = "增加会员提现记录", notes = "增加会员提现记录（需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "money", value = "金额"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "account", value = "支付宝账号"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "name", value = "姓名"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "password", value = "支付密码"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "1成功 0失败 -1用户不存在 -2用户没有设置支付密码 -3支付密码不正确 -4参数不全 -5佣金不足", response = Integer.class)
    })
    public AjaxResult addWithdrawRecord(@RequestBody UmsWithdrawVo withdrawVo, HttpServletRequest request) {
        return AjaxResult.success(withdrawRecordService.addWithdrawRecord(withdrawVo, AppletsLoginUtils.getInstance().getCustomerId(request)));
    }

    /**
     * 分页查询会员提现记录
     *
     * @param pageHelper 分页帮助类
     * @return 提现记录
     */
    @RequestMapping("/querywithdrawrecords")
    @ResponseBody
    @ApiOperation(value = "分页查询会员提现记录", notes = "分页查询会员提现记录（需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "pageNum", value = "当前页"),
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "pageSize", value = "每页显示的记录数"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "提现记录", response = UmsWithdraw.class)
    })
    public AjaxResult queryWithdrawRecords(@ApiIgnore PageHelper<UmsWithdraw> pageHelper, HttpServletRequest request,String status) {
        return AjaxResult.success(withdrawRecordService.queryWithdrawRecords(pageHelper, UmsWithdraw.QueryCriteria.buildForSite(AppletsLoginUtils.getInstance().getCustomerId(request),status)));
    }


}

package io.uhha.member;

import io.uhha.appletsutil.AppletsLoginUtils;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.integral.domain.CustomerPoint;
import io.uhha.integral.domain.PointSetting;
import io.uhha.integral.service.CustomerPointService;
import io.uhha.integral.service.PointSettingService;
import io.uhha.member.vo.UmsRedeemVo;
import io.uhha.setting.domain.LsStationLetter;
import io.uhha.util.PageHelper;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by mj on 18/6/23.
 * 会员积分控制器
 */
@RestController
@Api(tags = "会员积分接口")
public class PointController {

    /**
     * 注入会员积分服务
     */
    @Autowired
    private CustomerPointService customerPointService;

    @Autowired
    private PointSettingService pointSettingService;

    /**
     * 分页查询用户积分
     *
     * @param pageHelper 分页帮助类
     * @return 返回用户积分
     */
    @RequestMapping(value = "/points")
    @ResponseBody
    @ApiOperation(value = "分页查询用户积分", notes = "分页查询用户积分（需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "pageNum", value = "当前页"),
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "pageSize", value = "每页显示的记录数"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回用户积分", response = CustomerPoint.class)
    })
    public AjaxResult queryPoints(HttpServletRequest request, @ApiIgnore PageHelper<CustomerPoint> pageHelper,String type) {
        return AjaxResult.success(customerPointService.queryCustomerPoints(pageHelper, AppletsLoginUtils.getInstance().getCustomerId(request), null,type));
    }

    /**
     * 查询用户总积分
     *
     * @return 返回用户总积分
     */
    @RequestMapping(value = "/points/total")
    @ResponseBody
    @ApiOperation(value = "查询用户总积分", notes = "查询用户总积分（需要认证）", httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回用户总积分", response = Integer.class)
    })
    public AjaxResult queryAllPoints(HttpServletRequest request) {
        return AjaxResult.success(customerPointService.queryCustomerPointCount(AppletsLoginUtils.getInstance().getCustomerId(request)));
    }
    /**
     * 分页查询用户积分
     *
     * @return 返回用户积分
     */
    @RequestMapping(value = "/pointTotal")
    @ResponseBody
    @ApiOperation(value = "分页查询用户积分", notes = "分页查询用户积分（需要认证）", httpMethod = "POST")
    public AjaxResult pointTotal(HttpServletRequest request) {
        return AjaxResult.success(customerPointService.queryCustomerPointGroupByType(AppletsLoginUtils.getInstance().getCustomerId(request)));
    }

    /**
     * 查询积分配置
     *
     * @return 返回积分配置
     */
    @RequestMapping("/pointSetting")
    @ApiOperation(value = "查询积分配置", notes = "查询积分配置（需要认证）", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回积分配置", response = PointSetting.class)
    })
    public AjaxResult querySetting() {
        return AjaxResult.success(pointSettingService.queryPointSetting());
    }

    /**
     * 积分兑换Token接口
     *
     * @return 返回是否成功
     */
    @RequestMapping(value = "/redeemToken")
    @ResponseBody
    @ApiOperation(value = "积分兑换Token接口", notes = "积分兑换Token接口（需要认证）", httpMethod = "POST")
    public AjaxResult redeemToken(HttpServletRequest request, @RequestBody UmsRedeemVo redeemVo) {
        long uid = AppletsLoginUtils.getInstance().getCustomerId(request);
        return AjaxResult.success(customerPointService.redeemToken(uid,redeemVo));
    }
}

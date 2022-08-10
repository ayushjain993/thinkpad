package io.uhha.index;


import io.uhha.appletsutil.AppletsLoginUtils;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.member.domain.ComplaintOrder;
import io.uhha.member.domain.Complaints;
import io.uhha.member.service.ComplaintOrderService;
import io.uhha.member.service.ComplaintsService;
import io.uhha.util.PageHelper;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

/**
 * @author gxs
 * @date 2020-03-02 14:50
 * <p>
 * 投诉接口
 */
@RestController
@Api(tags = "投诉接口")
public class ComplaintsController {

    /**
     * 注入平台投诉服务接口
     */
    @Autowired
    private ComplaintsService complaintsService;

    /**
     * 注入订单投诉服务
     */
    @Autowired
    private ComplaintOrderService complaintOrderService;


    /**
     * 新增订单投诉
     *
     * @param complaintOrder 订单投诉实体
     * @return 1成功 否则失败
     */
    @PostMapping("/complaint/order")
    @ApiOperation(value = "新增订单投诉", notes = "新增订单投诉(需要认证)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "1成功 否则失败", response = Integer.class)
    })
    public AjaxResult addComplaintOrder(@RequestBody ComplaintOrder complaintOrder, HttpServletRequest request) {
        return AjaxResult.success(complaintOrderService.addComplaintOrder(complaintOrder.buildCustomerId(AppletsLoginUtils.getInstance().getCustomerId(request))));
    }


    /**
     * 分页查找订单投诉
     *
     * @param pageHelper 分页帮助类
     * @return 订单投诉列表
     */
    @GetMapping("/complaint/order")
    @ApiOperation(value = "分页查找订单投诉", notes = "分页查找订单投诉(需要认证)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageNum", value = "当前页"),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageSize", value = "每页显示的记录数"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "订单投诉列表", response = ComplaintOrder.class)
    })
    public AjaxResult queryComplaintOrder(@ApiIgnore PageHelper<ComplaintOrder> pageHelper, HttpServletRequest request) {
        return AjaxResult.success(complaintOrderService.queryComplaintOrder(pageHelper, ComplaintOrder.QueryCriteria.buildForPc(AppletsLoginUtils.getInstance().getCustomerId(request))));
    }


    /**
     * 新增平台投诉
     *
     * @param complaints 平台投诉实体
     * @return 成功1 失败0
     */
    @PostMapping("/complaint")
    @ApiOperation(value = "新增平台投诉", notes = "新增平台投诉(需要认证)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "1成功 否则失败", response = Integer.class)
    })
    public AjaxResult addComplaints(@RequestBody Complaints complaints, HttpServletRequest request) {
        complaints.setCustomerId(AppletsLoginUtils.getInstance().getCustomerId(request));
        return AjaxResult.success(complaintsService.addComplaints(complaints));
    }


    /**
     * 分页查询平台投诉
     *
     * @param pageHelper 分页帮助类
     * @return 平台投诉列表
     */
    @GetMapping("complaint")
    @ApiOperation(value = "分页查询平台投诉", notes = "分页查询平台投诉(需要认证)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageNum", value = "当前页"),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageSize", value = "每页显示的记录数"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "平台投诉列表", response = Complaints.class)
    })
    public AjaxResult queryComplaints(@ApiIgnore PageHelper<Complaints> pageHelper, HttpServletRequest request) {
        return AjaxResult.success(complaintsService.queryComplaints(pageHelper, AppletsLoginUtils.getInstance().getCustomerId(request), null));
    }

    @GetMapping("complaintDetail")
    @ApiOperation(value = "分页查询平台投诉", notes = "分页查询平台投诉(需要认证)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "id", value = "投诉id"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "平台投诉列表", response = Complaints.class)
    })
    public AjaxResult complaintDetail(Long id) {
        return AjaxResult.success(complaintsService.queryComplaintsById(id));
    }
}

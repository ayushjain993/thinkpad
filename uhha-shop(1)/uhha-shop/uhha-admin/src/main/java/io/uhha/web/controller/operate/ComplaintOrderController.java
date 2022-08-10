package io.uhha.web.controller.operate;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.member.domain.ComplaintOrder;
import io.uhha.member.domain.Complaints;
import io.uhha.member.service.ComplaintOrderService;
import io.uhha.member.service.ComplaintsService;
import io.uhha.util.PageHelper;
import io.uhha.web.utils.AdminLoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

/**
 * @author gxs
 * @date 2020-03-02 14:50
 * <p>
 * 投诉订单接口
 */
@RestController
@RequestMapping("/complaintorder")
@Api(tags = "投诉接口")
public class ComplaintOrderController {

    /**
     * 注入订单投诉服务
     */
    @Autowired
    private ComplaintOrderService complaintOrderService;


    /**
     * 分页查询平台订单投诉
     *
     * @param pageHelper 分页帮助类
     * @return 平台投诉订单列表
     */
    @GetMapping
    @ApiOperation(value = "分页查询平台订单投诉", notes = "分页查询平台订单投诉")
    @PreAuthorize("@ss.hasPermi('operate:complaintOrder:list')")
    public AjaxResult queryComplaintOrder(@ApiIgnore PageHelper<ComplaintOrder> pageHelper, ComplaintOrder.QueryCriteria queryCriteria) {
        return AjaxResult.success(complaintOrderService.queryComplaintOrder(pageHelper, queryCriteria));
    }

    /**
     * 查询平台订单投诉
     *
     * @return 平台投诉订单列表
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "分页查询平台订单投诉", notes = "分页查询平台订单投诉")
    @PreAuthorize("@ss.hasPermi('operate:complaintOrder:list')")
    public AjaxResult queryComplaintOrder(@PathVariable("id")Long id) {
        return AjaxResult.success(complaintOrderService.queryComplaintOrderById(id));
    }

    @PutMapping
    @ApiOperation(value = "回复平台订单投诉", notes = "回复平台订单投诉")
    @PreAuthorize("@ss.hasPermi('operate:complaintOrder:edit')")
    public AjaxResult complaintDetailReply(Long id, String feedback, HttpServletRequest request) {
        ComplaintOrder complaints = complaintOrderService.queryComplaintOrderById(id);
        if(complaints == null){
            return AjaxResult.error("not found");
        }
        complaints.setComplaintsReply(feedback);
        complaints.setOperator(AdminLoginUtils.getInstance().getManagerName());
        return AjaxResult.success(complaintOrderService.replyComplaintOrder(complaints));
    }

}

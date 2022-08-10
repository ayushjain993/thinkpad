package io.uhha.web.controller.operate;


import io.swagger.annotations.*;
import io.uhha.common.core.domain.AjaxResult;
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
 * 投诉接口
 */
@RestController
@RequestMapping("/complaint")
@Api(tags = "投诉接口")
public class ComplaintsController {

    /**
     * 注入平台投诉服务接口
     */
    @Autowired
    private ComplaintsService complaintsService;


    /**
     * 分页查询平台投诉
     *
     * @param pageHelper 分页帮助类
     * @return 平台投诉列表
     */
    @GetMapping
    @ApiOperation(value = "分页查询平台投诉", notes = "分页查询平台投诉")
    @PreAuthorize("@ss.hasPermi('operate:complaint:list')")
    public AjaxResult queryComplaints(@ApiIgnore PageHelper<Complaints> pageHelper, String status) {
        return AjaxResult.success(complaintsService.queryComplaints(pageHelper, status));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查询平台详情投诉", notes = "查询平台详情投诉")
    @PreAuthorize("@ss.hasPermi('operate:complaint:query')")
    public AjaxResult complaintDetail(@PathVariable("id") Long id) {
        return AjaxResult.success(complaintsService.queryComplaintsById(id));
    }

    @PutMapping
    @ApiOperation(value = "回复平台投诉", notes = "回复平台投诉")
    @PreAuthorize("@ss.hasPermi('operate:complaint:edit')")
    public AjaxResult complaintDetailReply(Long id, String feedback, HttpServletRequest request) {
        Complaints complaints = complaintsService.queryComplaintsById(id);
        if(null== complaints){
            return AjaxResult.error("not found.");
        }
        complaints.setComplaintsReplay(feedback);
        complaints.setOperator(AdminLoginUtils.getInstance().getManagerName());
        return AjaxResult.success(complaintsService.updateComplaintsReplay(complaints));
    }

}

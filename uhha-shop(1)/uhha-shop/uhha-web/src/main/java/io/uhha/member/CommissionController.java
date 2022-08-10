package io.uhha.member;

import io.uhha.appletsutil.AppletsLoginUtils;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.order.domain.OmsCommissionRecords;
import io.uhha.order.service.IOmsCommissionRecordsService;
import io.uhha.util.PageHelper;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

/**
 * 会员佣金控制器
 *
 * @author mj created on 2020/6/22
 */
@RestController
@Api(tags = "会员佣金接口")
public class CommissionController {

    /**
     * 注入提现记录服务
     */
    @Autowired
    private IOmsCommissionRecordsService commissionRecordService;


    /**
     * 分页查询会员佣金记录
     *
     * @param pageHelper 分页帮助类
     * @return 佣金记录
     */
    @RequestMapping("/querycommissionrecords")
    @ResponseBody
    @ApiOperation(value = "分页查询会员佣金记录", notes = "分页查询会员佣金记录（需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "pageNum", value = "当前页"),
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "pageSize", value = "每页显示的记录数"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "佣金记录", response = OmsCommissionRecords.class)
    })
    public AjaxResult queryCommissionRecords(@ApiIgnore PageHelper<OmsCommissionRecords> pageHelper, HttpServletRequest request,String type) {
        return AjaxResult.success(commissionRecordService.queryCommissionRecords(pageHelper, OmsCommissionRecords.QueryCriteria.buildForSite(AppletsLoginUtils.getInstance().getCustomerId(request),type)));
    }

}

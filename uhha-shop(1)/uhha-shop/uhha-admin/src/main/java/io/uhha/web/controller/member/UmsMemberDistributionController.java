package io.uhha.web.controller.member;

import io.swagger.annotations.Api;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.member.domain.UmsMember;
import io.uhha.member.service.IUmsMemberService;
import io.uhha.order.domain.OmsOrder;
import io.uhha.order.service.IOmsOrderService;
import io.uhha.order.vo.QueryOrderCriteria;
import io.uhha.util.CommonConstant;
import io.uhha.util.PageHelper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户分销控制器
 * @author peter
 */
@Api(tags = "用户分销控制器")
@RestController
public class UmsMemberDistributionController {
    @Autowired
    private IUmsMemberService umsMemberService;

    @Autowired
    private IOmsOrderService orderService;

    /**
     * 查询分销用户
     *
     * @return 用户列表
     */
    @GetMapping("/spreadcustomer")
    @ApiOperation(value = "查询分销用户", notes = "查询分销用户（需要认证）")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询分销用户", response = UmsMember.class)
    })
    public PageHelper<UmsMember> spreadcustomer(PageHelper<UmsMember> pageHelper) {
        return umsMemberService.querySpreadCustomerList(pageHelper);
    }

    /**
     * 查询分销用户
     *
     * @return 用户列表
     */
    @GetMapping("/subdistributioncustomer/{id}")
    @ApiOperation(value = "查询分销用户", notes = "查询分销用户（需要认证）")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询分销用户", response = UmsMember.class)
    })
    public AjaxResult spreadcustomer(@PathVariable("id") Long id) {
        return AjaxResult.success(umsMemberService.querySpreadCustomerByCustomerId(id));
    }

    /**
     * 分页查询会员分销订单信息
     *
     * @return 分销订单列表
     */
    @GetMapping("/spreadorder")
    @ApiOperation(value = "分页查询会员分销订单信息", notes = "查询分销用户（需要认证）")
    @ApiResponses({
            @ApiResponse(code = 200, message = "分页查询会员分销订单信息", response = OmsOrder.class)
    })
    public PageHelper<OmsOrder> spreadorder(PageHelper<OmsOrder> pageHelper) {
        QueryOrderCriteria queryCriteria = new QueryOrderCriteria();
        queryCriteria.setStoreId(CommonConstant.QUERY_WITH_NO_STORE);
        queryCriteria.setCustomerId(CommonConstant.QUERY_WITH_NO_CUSTOMER);
        return orderService.querySpreadOrders(pageHelper, queryCriteria);
    }
}

package io.uhha.order;


import io.uhha.appletsutil.AppletsLoginUtils;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.order.domain.OmsBackOrder;
import io.uhha.order.domain.OmsOrder;
import io.uhha.order.service.BackOrderServiceApi;
import io.uhha.order.service.IOmsBackOrderService;
import io.uhha.order.vo.*;
import io.uhha.util.CommonConstant;
import io.uhha.util.PageHelper;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

/**
 * 退单控制器
 */
@RestController
@Api(tags = "退单接口")
public class BackOrderController {


    /**
     * 注入退单混合api
     */
    @Autowired
    private BackOrderServiceApi backOrderServiceApi;

    /**
     * 注入退单服务接口
     */
    @Autowired
    private IOmsBackOrderService backOrderService;


    /**
     * 申请退款
     *
     * @param orderId 订单id
     * @param reason  退款原因
     * @param desc    描述
     * @return -1 订单状态错误  成功>0  失败= 0
     */
    @RequestMapping(value = "applyrefund", method = RequestMethod.POST)
    @ApiOperation(value = "申请退款", notes = "申请退款（需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "Long", name = "orderId", value = "订单id"),
            @ApiImplicitParam(paramType = "form", dataType = "String", name = "reason", value = "退款原因"),
            @ApiImplicitParam(paramType = "form", dataType = "String", name = "description", value = "描述"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "-1 订单状态错误  成功>0  失败= 0", response = Integer.class)
    })
    public AjaxResult applyRefund(HttpServletRequest request,
                                  @RequestBody ApplyBackOrderParams applyBackOrderParams) {
        applyBackOrderParams.setCustomerId(AppletsLoginUtils.getInstance().getCustomerId(request));
        return AjaxResult.success(backOrderServiceApi.applyRefundOrder(applyBackOrderParams));
    }


    /**
     * 查询订单详情(退货用)
     *
     * @param orderId 订单id
     * @return 返回订单详情
     */
    @RequestMapping(value = "queryorderforreturn", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "查询订单详情(退货用)", notes = "查询订单详情(退货用)（需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "long", name = "orderId", value = "订单id"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回订单详情", response = OmsOrder.class)
    })
    public AjaxResult queryOrderForReturn(HttpServletRequest request, Long orderId) {
        return AjaxResult.success(backOrderServiceApi.queryOrderForReturn(AppletsLoginUtils.getInstance().getCustomerId(request), orderId));
    }


    /**
     * 申请退货
     *
     * @param applyReturnParams 申请退货请求
     * @return -1  0 失败  成功1
     */
    @RequestMapping(value = "applyreturn", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "申请退货", notes = "申请退货（需要认证）", httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(code = 200, message = "-1  0 失败  成功1", response = Integer.class)
    })
    public AjaxResult applyReturnOrder(HttpServletRequest request, @RequestBody ApplyReturnParams applyReturnParams) {
        applyReturnParams.setCustomerId(AppletsLoginUtils.getInstance().getCustomerId(request));
        return AjaxResult.success(backOrderServiceApi.applyReturnOrder(applyReturnParams));
    }


    /**
     * 分页查询用户退单信息
     *
     * @param pageHelper    分页帮助类
     * @param queryCriteria 查询条件
     * @return 返回用户退单信息
     */
    @RequestMapping(value = "backorderlist", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "分页查询用户退单信息", notes = "分页查询用户退单信息（需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "pageNum", value = "当前页"),
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "pageSize", value = "每页显示的记录数"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回用户退单信息", response = OmsBackOrder.class)
    })
    public AjaxResult queryCustomerBackOrders(HttpServletRequest request, @ApiIgnore PageHelper<OmsBackOrder> pageHelper, @ApiIgnore QueryOrderCriteria queryCriteria) {
        queryCriteria.setCustomerId(AppletsLoginUtils.getInstance().getCustomerId(request));
        return AjaxResult.success(backOrderService.queryBackOrderForSite(pageHelper, queryCriteria));
    }

    /**
     * 填写物流信息
     *
     * @param id               退单id
     * @param logisCompanyName 物流公司名称
     * @param waybillCode      物流单号
     * @return 成功=1 订单号含有中文-2 其他失败
     */
    @RequestMapping(value = "fillthelogistics", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "填写物流信息", notes = "填写物流信息（需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "long", name = "id", value = "退单id"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "logisCompanyName", value = "物流公司名称"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "waybillCode", value = "物流单号"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功=1 订单号含有中文-2 其他失败", response = Integer.class)
    })
    public AjaxResult fillTheLogistics(HttpServletRequest request, @Validated @RequestBody BackOrderLogisticParam param) {
        return AjaxResult.success(backOrderService.fillTheLogistics(AppletsLoginUtils.getInstance().getCustomerId(request),
                param.getId(),
                param.getLogisCompanyName(),
                param.getWaybillCode()));
    }


    /**
     * 查询退单详情
     *
     * @param id 退单id
     * @return 返回退单详情
     */
    @RequestMapping(value = "querybackdetail", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "查询退单详情", notes = "查询退单详情（需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "long", name = "id", value = "退单id"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回退单详情", response = OmsBackOrder.class)
    })
    public AjaxResult queryBackDetail(HttpServletRequest request, Long id) {
        return AjaxResult.success(backOrderService.queryBackOrderById(id, CommonConstant.QUERY_WITH_NO_STORE, AppletsLoginUtils.getInstance().getCustomerId(request), BackOrderItem.LOG, BackOrderItem.SKUS));
    }
}

package io.uhha.order;


import io.swagger.annotations.*;
import io.uhha.appletsutil.AppletsLoginUtils;
import io.uhha.common.annotation.RateLimiter;
import io.uhha.common.annotation.UnAuth;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.enums.LimitType;
import io.uhha.common.exception.ServiceException;
import io.uhha.common.utils.StringUtils;
import io.uhha.integral.service.CustomerPointService;
import io.uhha.marketing.service.CouponService;
import io.uhha.member.domain.UmsMember;
import io.uhha.member.service.IUmsMemberService;
import io.uhha.member.service.IUmsPreDepositRecordService;
import io.uhha.member.service.IUmsWithdrawService;
import io.uhha.order.domain.OmsOrder;
import io.uhha.order.service.*;
import io.uhha.order.vo.*;
import io.uhha.util.CommonConstant;
import io.uhha.util.PageHelper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单控制器
 */
@RestController
@Api(tags = "订单接口")
public class OrderController {

    /**
     * 自动注入预存款service
     */
    @Autowired
    private IUmsPreDepositRecordService predepositRecordService;

    /**
     * 注入订单服务接口
     */
    @Autowired
    private OrderServiceApi orderServiceApi;

    /**
     * 注入订单服务接口
     */
    @Autowired
    private IOmsOrderService orderService;

    /**
     * 退单服务接口
     */
    @Autowired
    private IOmsBackOrderService backOrderService;
    /**
     * 注入购物车服务接口
     */
    @Autowired
    private IOmsShoppingCartService shoppingCartService;

    /**
     * 注入提现记录服务
     */
    @Autowired
    private IOmsCommissionRecordsService commissionRecordService;
    /**
     * 注入提现记录服务
     */
    @Autowired
    private IUmsWithdrawService withdrawRecordService;

    @Autowired
    private  CouponService couponService;

    @Autowired
    private IUmsMemberService umsMemberService;

    /**
     * 注入会员积分服务
     */
    @Autowired
    private CustomerPointService customerPointService;

    /**
     * 提交订单
     *
     * @param submitOrderParams 提交订单参数
     * @return 返回订单提交响应实体
     */
    @RequestMapping(value = "/submitorder")
    @ResponseBody
    @ApiOperation(value = "提交订单", notes = "提交订单（需要认证）", httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回订单提交响应实体", response = SubmitOrderResponse.class)
    })
    public AjaxResult submitOrder(@RequestBody SubmitOrderParams submitOrderParams, HttpServletRequest request) {
        submitOrderParams.setCustomerId(AppletsLoginUtils.getInstance().getCustomerId(request));
        submitOrderParams.setFromApp();
        // 订单返回实体
        SubmitOrderResponse submitOrderResponse;
        try {
            submitOrderResponse = orderServiceApi.submitOrder(submitOrderParams);
        } catch (ServiceException e) {
            return AjaxResult.error(e.getErrorCode());
        }

        return AjaxResult.success(submitOrderResponse);
    }

    /**
     * 查询用户订单信息
     *
     * @param pageHelper    分页帮助类
     * @param queryCriteria 查询条件
     * @return 返回用户订单信息
     */
    @RequestMapping("querycustomerorders")
    @ResponseBody
    @ApiOperation(value = "查询用户订单信息", notes = "查询用户订单信息（需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "pageNum", value = "当前页"),
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "pageSize", value = "每页显示的记录数"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "status", value = "订单状态"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回用户订单信息", response = OmsOrder.class)
    })
    public AjaxResult queryCustomerOrders(HttpServletRequest request, @ApiIgnore PageHelper<OmsOrder> pageHelper, @ApiIgnore QueryOrderCriteria queryCriteria) {
        queryCriteria.setCustomerId(AppletsLoginUtils.getInstance().getCustomerId(request));
        return AjaxResult.success(orderService.queryOrdersForSite(pageHelper, queryCriteria, OrderItem.SKUS, OrderItem.CANREFUND, OrderItem.CANRETRUN));
    }


    /**
     * 取消订单
     *
     * @param cancelOrderParams cancelOrderParams
     * @return 订单取消原因\n1:现在不想买\n2:商品价格较贵\n3:价格波动\n4:商品缺货\n5:重复下单\n6:收货人信息有误\n7:发票信息有误/发票未开\n8:送货时间过长\n9:其他原因\n0:系统取消
     */
    @PostMapping(value = "/cancelorder")
    @ApiOperation(value = "取消订单", notes = "取消订单（需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "long", name = "orderId", value = "订单id"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "reason", value = "取消原因"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "订单取消原因\\n1:现在不想买\\n2:商品价格较贵\\n3:价格波动\\n4:商品缺货\\n5:重复下单\\n6:收货人信息有误\\n7:发票信息有误/发票未开\\n8:送货时间过长\\n9:其他原因\\n0:系统取消", response = Integer.class)
    })
    public AjaxResult cancelOrder(HttpServletRequest request, @RequestBody CancelOrderParams cancelOrderParams) {
        return AjaxResult.success(orderServiceApi.cancleOrder(CancelOrderParams.buildCustomerSource(AppletsLoginUtils.getInstance().getCustomerId(request), cancelOrderParams.getOrderId(), cancelOrderParams.getReason())));
    }

    /**
     * 删除订单
     *
     * @param orderId 订单id
     * @return 返回结果
     */
    @DeleteMapping(value = "/deleteorder")
    @ApiOperation(value = "删除订单", notes = "删除订单（需要认证）", httpMethod = "DELETE")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "long", name = "orderId", value = "订单id"),
    })
    public AjaxResult deleteOrder(HttpServletRequest request, @RequestParam("orderId") Long orderId) {
        return AjaxResult.success(orderServiceApi.deleteOrder(DeleteOrderParams.buildCustomerSource(AppletsLoginUtils.getInstance().getCustomerId(request), orderId)));
    }


    /**
     * 确认收货订单
     *
     * @param orderId 订单id
     * @return 成功返回>0 失败返回0
     */
    @RequestMapping(value = "/receiptorder")
    @ResponseBody
    @ApiOperation(value = "确认收货订单", notes = "确认收货订单（需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "long", name = "orderId", value = "订单id"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功返回>0 失败返回0", response = Integer.class)
    })
    public AjaxResult confirmReceipt(HttpServletRequest request, @RequestParam("orderId")Long orderId) {
        return AjaxResult.success(orderServiceApi.confirmReceipt(orderId, AppletsLoginUtils.getInstance().getCustomerId(request)));
    }


    /**
     * 订单统计
     *
     * @return 返回订单消息总数返回实体
     */
    @RequestMapping(value = "/order/count")
    @ResponseBody
    @ApiOperation(value = "订单统计", notes = "订单统计（需要认证）", httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回订单消息总数返回实体", response = OrderMessageCount.class)
    })
    public AjaxResult queryOrderMessageCount(HttpServletRequest request) {
        Long customerId = AppletsLoginUtils.getInstance().getCustomerId(request);
        Map<String, Object> map = new HashMap();
        map.put("type","0");
        map.put("customerId",customerId);
        String commissonMoney = commissionRecordService.queryCommissionMoney(map); // 累计收入佣金
        map.put("status","3");
        String withdrawMoney = withdrawRecordService.queryWithdrawMoney(map);// 累计提现记录

        BigDecimal frozenMoney = predepositRecordService.queryCutomerAllFrozen(customerId); // 冻结提现额
        String strFrozenMoney = frozenMoney==null? "0.0":frozenMoney.toString();
        int pointsTotal = customerPointService.queryCustomerPointCount(customerId); //积分总额

        UmsMember member = umsMemberService.selectUmsMemberById(customerId);
        String frozenCommission = "";
        if(member!=null){
            BigDecimal frozen = member.getCommissionFrozen();
            frozenCommission = frozen ==null? "": frozen.toString();
        }

        return AjaxResult.success(
                OrderMessageCount.build()
                        .addToCommissonMoney(commissonMoney)
                        .addToFrozenCommissonMoney(frozenCommission)
                        .addToWithdrawMoney(withdrawMoney)
                        .addToFrozenBalance(strFrozenMoney)
                        .addToPointsTotal(Integer.toString(pointsTotal))
                        .addToBlance(predepositRecordService.queryCutomerAllPredeposit(AppletsLoginUtils.getInstance().getCustomerId(request)))
                        .addToCartNum(shoppingCartService.queryShoppingCartCount(AppletsLoginUtils.getInstance().getCustomerId(request)).getAllNum())
                        .addToCouponNum(couponService.queryCustomerCouponCount(AppletsLoginUtils.getInstance().getCustomerId(request)))
                        .addToPayCount(orderService.toPayOrderCount(AppletsLoginUtils.getInstance().getCustomerId(request)))
                        .addToDeliverCount(orderService.toDeliverOrderCount(AppletsLoginUtils.getInstance().getCustomerId(request)))
                        .addToReceiptCount(orderService.toReceiptOrderCount(AppletsLoginUtils.getInstance().getCustomerId(request)))
                        .addToEvaluateCount(orderService.toEvaluateOrderCount(AppletsLoginUtils.getInstance().getCustomerId(request)))
                        .addBackOrderCount(backOrderService.queryInProcessBackOrder(AppletsLoginUtils.getInstance().getCustomerId(request))));
    }

    /**
     * 查看物流
     *
     * @param orderId 订单id
     * @return H5查询物流的url
     */
    @RequestMapping("/getchecklogisticsurl")
    @ResponseBody
    @ApiOperation(value = "查看物流", notes = "查看物流（需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "long", name = "orderId", value = "订单id"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "H5查询物流的url", response = String.class)
    })
    public AjaxResult getCheckLogisticsUrl(HttpServletRequest request, Long orderId) {
        return AjaxResult.success(orderService.getCheckLogisticsUrl(orderId, AppletsLoginUtils.getInstance().getCustomerId(request), ""));
    }

    /**
     * 查看物流
     *
     * @param orderId 订单id
     * @return 物流数据
     */
    @GetMapping("/shipppingdetails")
    @ResponseBody
    @ApiOperation(value = "查看物流", notes = "查看物流（需要认证）", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "orderId", name = "orderId", value = "订单id"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "物流数据", response = String.class)
    })
    public AjaxResult getShippingDetails(HttpServletRequest request, @RequestParam("orderId") Long orderId) throws Exception {
        return AjaxResult.success(orderService.getShippingDetails(orderId, AppletsLoginUtils.getInstance().getCustomerId(request)));
    }

    /**
     * 查询订单详情
     *
     * @param orderId 订单id
     * @return 返回订单详情
     */
    @RequestMapping(value = "/orderdetail")
    @ResponseBody
    @ApiOperation(value = "查询订单详情", notes = "查询订单详情（需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "long", name = "orderId", value = "订单id"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回订单详情", response = OmsOrder.class)
    })
    public AjaxResult queryOrderDetail(HttpServletRequest request, Long orderId) {
        return AjaxResult.success(orderServiceApi.queryOrderDetailById(orderId, AppletsLoginUtils.getInstance().getCustomerId(request), CommonConstant.QUERY_WITH_NO_STORE, OrderItem.CANREFUND, OrderItem.CANRETRUN, OrderItem.SKUS, OrderItem.ATTR, OrderItem.STORE_INFO, OrderItem.BACK_PROGRESS));
    }

    /**
     * 查询订单集合
     *
     * @param orderCode 订单code
     * @return 返回订单集合
     */
    @ResponseBody
    @GetMapping(value = "/ordersByCode")
    @ApiOperation(value = "查询订单集合", notes = "查询订单集合（需要认证）")
    public AjaxResult queryOrdersByOrderCode(String orderCode, HttpServletRequest request) {
        return AjaxResult.success(orderService.queryOrderByOrderCode(orderCode, AppletsLoginUtils.getInstance().getCustomerId(request)));
    }

    /**
     * 匿名查询订单集合
     *
     * @param orderCode 订单code
     * @return 返回订单集合
     */
    @UnAuth
    @ResponseBody
    @GetMapping(value = "/ordersByCodeAnonymous")
    @RateLimiter(key = "orderByCode", time = 1, count = 10)
    @ApiOperation(value = "查询订单集合", notes = "查询订单集合（需要认证）")
    public AjaxResult queryOrdersByOrderCodeAnonymous(String orderCode, HttpServletRequest request) {
        return AjaxResult.success(orderService.queryOrderByOrderCode(orderCode));
    }

    /**
     * 查询拼团订单
     *
     * @param pageHelper    分页帮助类
     * @param queryCriteria 查询参数实体
     * @return 拼团订单集合
     */
    @ApiOperation(value = "查询拼团订单列表", notes = "查询拼团订单列表（不需要认证）", httpMethod = "POST")
    @RequestMapping(value = "/spudetail/grouporders")
    @ResponseBody
    @UnAuth
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "marketingId", value = "促销id"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "skuId", value = "单品id"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "拼团订单集合", response = GroupOrder.class)
    })
    public AjaxResult queryGroupOrdersForSpuDetail(@ApiIgnore PageHelper<GroupOrder> pageHelper, @ApiIgnore GroupOrder.QueryCriteria queryCriteria) {
        return AjaxResult.success(orderService.queryGroupOrders(queryCriteria.buildForSpuDetail(), pageHelper, true, false));
    }

    /**
     * 校验是否已在团中
     *
     * @param groupId 拼团id
     * @return 1:不在 -1在 -2缺少参数
     */
    @ApiOperation(value = "判断会员是否在指定的团中", notes = "判断会员是否在指定的团中（需要认证）", httpMethod = "POST")
    @RequestMapping(value = "/checkisingroup")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "String", name = "groupId", value = "拼团id"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "1:不在 -1在 -2缺少参数", response = Integer.class)
    })
    public AjaxResult checkIsInGroup(HttpServletRequest request, String groupId) {
        return AjaxResult.success(StringUtils.isEmpty(groupId) ? -2 : ObjectUtils.isEmpty(orderService.queryByGroupIdAndCustomerId(groupId, AppletsLoginUtils.getInstance().getCustomerId(request))) ? 1 : -1);
    }

}

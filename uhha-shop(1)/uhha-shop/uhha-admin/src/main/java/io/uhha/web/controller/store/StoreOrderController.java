package io.uhha.web.controller.store;

import io.swagger.annotations.*;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.order.domain.OmsBackOrder;
import io.uhha.order.domain.OmsOrder;
import io.uhha.order.service.*;
import io.uhha.order.vo.CancelOrderParams;
import io.uhha.order.vo.ConfirmOrderParams;
import io.uhha.order.vo.OrderItem;
import io.uhha.order.vo.QueryOrderCriteria;
import io.uhha.store.domain.TStoreOrder;
import io.uhha.store.service.ITStoreOrderAttrService;
import io.uhha.store.service.ITStoreOrderService;
import io.uhha.util.CommonConstant;
import io.uhha.util.PageHelper;
import io.uhha.web.utils.AdminLoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "管理后台门店订单相关接口")
@RestController
public class StoreOrderController extends BaseController {
    @Autowired
    private IOmsOrderService omsOrderService;

    @Autowired
    private OrderServiceApi orderServiceApi;

    @Autowired
    private IOmsOrderSkuService omsOrderSkuService;

    @Autowired
    private IOmsBackOrderService omsBackOrderService;


    @Autowired
    private IOmsBackOrderLogService omsBackOrderLogService;

    /**
     * 查询门店订单列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreOrder:list')")
    @GetMapping("/storeorders")
    public PageHelper<OmsOrder> storeorders(PageHelper<OmsOrder> pageHelper, QueryOrderCriteria queryCriteria)
    {
        return omsOrderService.queryStoreOrders(pageHelper, queryCriteria);
    }

    /**
     * 查询门店订单详情
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreOrder:query')")
    @GetMapping("/store/orderdetail/{orderId}")
    public AjaxResult storeorderDetail(@PathVariable("orderId") Long orderId)
    {
        OmsOrder order = omsOrderService.selectOmsOrderById(orderId);
        OmsOrder omsOrder = omsOrderService.queryOrderDetailById(orderId, order.getCustomerId(), order.getStoreId(),
                OrderItem.LOG, OrderItem.ATTR, OrderItem.SKUS, OrderItem.EXPRESSURL, OrderItem.STORE_INFO, OrderItem.CUSTOMER);

        return AjaxResult.success(omsOrder);
    }

    /**
     * 取消订单
     *
     * @param id 订单id
     * @return 1 成功 -1 订单不存在 -2:用户不匹配 0 失败
     */
    @DeleteMapping("/order/{id}")
    @ApiOperation(value = "取消订单", notes = "取消订单（需要认证）")
    // @PreAuthorize("hasAuthority('order/cancelorder')")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "id", value = "订单id"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "1 成功 -1 订单不存在 -2:用户不匹配 0 失败", response = Integer.class)
    })
    public int cancelOrder(@PathVariable long id) {
        return orderServiceApi.cancleOrder(CancelOrderParams.buildManagerSource(CommonConstant.ADMIN_STOREID, id, AdminLoginUtils.getInstance().getManagerName()));
    }


    /**
     * 确认付款
     *
     * @param id 订单id
     * @return 成功返回1 失败返回0
     */
    @PutMapping("/confirmorder/{id}")
    @ApiOperation(value = "确认付款", notes = "确认付款（需要认证）")
    // @PreAuthorize("hasAuthority('order/confirmorder')")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "id", value = "订单id"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "reason", value = "原因"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功返回1 失败返回0", response = Integer.class)
    })
    public int confirmOrder(@PathVariable long id, String reason) {
        return orderServiceApi.confirmOrderPayed(ConfirmOrderParams.buildManagerSource(id, CommonConstant.ADMIN_STOREID, reason, AdminLoginUtils.getInstance().getManagerName()));
    }

    /**
     * 查询门店退货订单列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreOrder:list')")
    @GetMapping("/storebackorders")
    public TableDataInfo storebackorders(OmsBackOrder omsOrder)
    {
        startPage();
        List<OmsBackOrder> list = omsBackOrderService.selectOmsBackOrderList(omsOrder);
        return getDataTable(list);
    }

    /**
     * 查询门店退货订单详情
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreOrder:query')")
    @GetMapping("/store/backorder/{orderId}")
    public AjaxResult storebackorderDetail(@PathVariable("orderId") Long orderId)
    {
        OmsBackOrder omsOrder = omsBackOrderService.selectOmsBackOrderById(orderId);
        omsOrder.setOrderSkus(omsOrderSkuService.queryByOrderId(orderId));
        omsOrder.setBackOrderLogs(omsBackOrderLogService.queryByBackOrderId(orderId));
        return AjaxResult.success(omsOrder);
    }

}

package io.uhha.web.controller.order;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.order.domain.OmsBackOrder;
import io.uhha.order.service.BackOrderServiceApi;
import io.uhha.order.service.IOmsBackOrderLogService;
import io.uhha.order.service.IOmsBackOrderService;
import io.uhha.order.service.IOmsOrderSkuService;
import io.uhha.util.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 退单退款Controller
 *
 * @author mj
 * @date 2020-07-24
 */
@Api(tags = "退单退款Controller")
@RestController
@RequestMapping
public class OmsBackOrderController extends BaseController {
    @Autowired
    private IOmsBackOrderService omsBackOrderService;

    @Autowired
    private IOmsOrderSkuService omsOrderSkuService;


    @Autowired
    private IOmsBackOrderLogService omsBackOrderLogService;

    @Autowired
    private BackOrderServiceApi backOrderServiceApi;

    /**
     * 查询退单退款列表
     */
    @PreAuthorize("@ss.hasPermi('order:OmsBackOrder:list')")
    @GetMapping("/order/OmsBackOrder/list")
    public TableDataInfo list(OmsBackOrder omsBackOrder) {
        startPage();
        List<OmsBackOrder> list = omsBackOrderService.selectOmsBackOrderList(omsBackOrder);
        return getDataTable(list);
    }

    /**
     * 导出退单退款列表
     */
    @PreAuthorize("@ss.hasPermi('order:OmsBackOrder:export')")
    @Log(title = "退单退款", businessType = BusinessType.EXPORT)
    @GetMapping("/order/OmsBackOrder/export")
    public AjaxResult export(OmsBackOrder omsBackOrder) {
        List<OmsBackOrder> list = omsBackOrderService.selectOmsBackOrderList(omsBackOrder);
        ExcelUtil<OmsBackOrder> util = new ExcelUtil<OmsBackOrder>(OmsBackOrder.class);
        return util.exportExcel(list, "OmsBackOrder");
    }

    /**
     * 获取退单退款详细信息
     */
    @PreAuthorize("@ss.hasPermi('order:OmsBackOrder:query')")
    @GetMapping(value = "/order/OmsBackOrder/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(omsBackOrderService.selectOmsBackOrderById(id));
    }

    /**
     * 新增退单退款
     */
    @PreAuthorize("@ss.hasPermi('order:OmsBackOrder:add')")
    @Log(title = "退单退款", businessType = BusinessType.INSERT)
    @PostMapping("/order/OmsBackOrder")
    public AjaxResult add(@RequestBody OmsBackOrder omsBackOrder) {
        return toAjax(omsBackOrderService.insertOmsBackOrder(omsBackOrder));
    }

    /**
     * 修改退单退款
     */
    @PreAuthorize("@ss.hasPermi('order:OmsBackOrder:edit')")
    @Log(title = "退单退款", businessType = BusinessType.UPDATE)
    @PutMapping("/order/OmsBackOrder")
    public AjaxResult edit(@RequestBody OmsBackOrder omsBackOrder) {
        return toAjax(omsBackOrderService.updateOmsBackOrder(omsBackOrder));
    }

    /**
     * 删除退单退款
     */
    @PreAuthorize("@ss.hasPermi('order:OmsBackOrder:remove')")
    @Log(title = "退单退款", businessType = BusinessType.DELETE)
    @DeleteMapping("/order/OmsBackOrder/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(omsBackOrderService.deleteOmsBackOrderByIds(ids));
    }

    /**
     * 查询门店退货订单详情
     */
    @PreAuthorize("@ss.hasPermi('order:OmsBackOrder:query')")
    @GetMapping("/return/backorderdetail/{orderId}")
    public AjaxResult returnBackorderDetail(@PathVariable("orderId") Long orderId)
    {
        OmsBackOrder omsOrder = omsBackOrderService.selectOmsBackOrderById(orderId);
        omsOrder.setOrderSkus(omsOrderSkuService.queryByOrderId(orderId));
        omsOrder.setBackOrderLogs(omsBackOrderLogService.queryByBackOrderId(orderId));
        return AjaxResult.success(omsOrder);
    }

    /**
     * 查询门店退货订单详情
     */
    @PreAuthorize("@ss.hasPermi('order:OmsBackOrder:query')")
    @GetMapping("/refund/backorderdetail/{orderId}")
    public AjaxResult refundBackorderDetail(@PathVariable("orderId") Long orderId)
    {
        OmsBackOrder omsOrder = omsBackOrderService.selectOmsBackOrderById(orderId);
        omsOrder.setOrderSkus(omsOrderSkuService.queryByOrderId(omsOrder.getOrderId()));
        omsOrder.setBackOrderLogs(omsBackOrderLogService.queryByBackOrderId(orderId));
        return AjaxResult.success(omsOrder);
    }

    /**
     * 同意退货订单退款
     */
    @PreAuthorize("@ss.hasPermi('order:OmsBackOrder:edit')")
    @PutMapping("/refund/agree")
    public AjaxResult agreeToRefund(@RequestParam("backOrderId") Long backOrderId,
                                    @RequestParam("message") String message)
    {
        return AjaxResult.success( backOrderServiceApi.agreeToRefund(backOrderId, CommonConstant.ADMIN_STOREID, message));
    }

    /**
     * 拒绝退货订单退款
     */
    @PreAuthorize("@ss.hasPermi('order:OmsBackOrder:edit')")
    @PutMapping("/refund/refuse")
    public AjaxResult refuseToRefund(@RequestParam("backOrderId") Long backOrderId,
                                    @RequestParam("message") String message)
    {
        return AjaxResult.success( omsBackOrderService.refuseRefund(backOrderId, CommonConstant.ADMIN_STOREID, message));
    }

    /**
     * 同意退货订单退货
     */
    @PreAuthorize("@ss.hasPermi('order:OmsBackOrder:edit')")
    @PutMapping("/return/agree")
    public AjaxResult agreeToReturn(@RequestParam("backOrderId") Long backOrderId,
                                    @RequestParam("message") String message)
    {
        return AjaxResult.success( omsBackOrderService.agreeToReturn(backOrderId, CommonConstant.ADMIN_STOREID, message));
    }

    /**
     * 拒绝退货订单退货
     */
    @PreAuthorize("@ss.hasPermi('order:OmsBackOrder:edit')")
    @PutMapping("/return/refuse")
    public AjaxResult refuseToReturn(@RequestParam("backOrderId") Long backOrderId,
                                     @RequestParam("message") String message)
    {
        return AjaxResult.success( omsBackOrderService.refuseReturn(backOrderId, CommonConstant.ADMIN_STOREID, message));
    }

    /**
     * 拒绝退货订单收货
     */
    @PreAuthorize("@ss.hasPermi('order:OmsBackOrder:edit')")
    @PutMapping("/return/refuseToReceive")
    public AjaxResult refuseToReceive(@RequestParam("backOrderId") Long backOrderId,
                                     @RequestParam("message") String message)
    {
        return AjaxResult.success( omsBackOrderService.refuseToReceive(backOrderId, CommonConstant.ADMIN_STOREID, message));
    }

    /**
     * 确认收货
     */
    @PreAuthorize("@ss.hasPermi('order:OmsBackOrder:edit')")
    @PutMapping("/return/confirmReturn")
    public AjaxResult confirmReturn(@RequestParam("backOrderId") Long backOrderId,
                                    @RequestParam("message") String message,
                                    @RequestParam("money") BigDecimal money)
    {
        return AjaxResult.success( omsBackOrderService.confirmReturn(backOrderId, CommonConstant.ADMIN_STOREID, message, money));
    }


}

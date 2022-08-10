package io.uhha.web.controller.order;

import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.order.domain.OmsBackOrder;
import io.uhha.order.service.IOmsBackOrderService;
import io.uhha.web.utils.AdminLoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 退单退款Controller
 *
 * @author mj
 * @date 2020-07-24
 */
@RestController
@RequestMapping("/order/OmsBackOrder")
public class OmsBackOrderController extends BaseController {
    @Autowired
    private IOmsBackOrderService omsBackOrderService;

    /**
     * 查询退单退款列表
     */
    @PreAuthorize("@ss.hasPermi('order:OmsBackOrder:list')")
    @GetMapping("/list")
    public TableDataInfo list(OmsBackOrder omsBackOrder) {
        omsBackOrder.setStoreId(AdminLoginUtils.getInstance().getStoreId());
        startPage();
        List<OmsBackOrder> list = omsBackOrderService.selectOmsBackOrderList(omsBackOrder);
        return getDataTable(list);
    }

    /**
     * 导出退单退款列表
     */
    @PreAuthorize("@ss.hasPermi('order:OmsBackOrder:export')")
    @Log(title = "退单退款", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(OmsBackOrder omsBackOrder) {
        omsBackOrder.setStoreId(AdminLoginUtils.getInstance().getStoreId());
        List<OmsBackOrder> list = omsBackOrderService.selectOmsBackOrderList(omsBackOrder);
        ExcelUtil<OmsBackOrder> util = new ExcelUtil<OmsBackOrder>(OmsBackOrder.class);
        return util.exportExcel(list, "OmsBackOrder");
    }

    /**
     * 获取退单退款详细信息
     */
    @PreAuthorize("@ss.hasPermi('order:OmsBackOrder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(omsBackOrderService.selectOmsBackOrderById(id));
    }


    /**
     * 删除退单退款
     */
    @PreAuthorize("@ss.hasPermi('order:OmsBackOrder:remove')")
    @Log(title = "退单退款", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(omsBackOrderService.deleteOmsBackOrderByIds(ids));
    }
}

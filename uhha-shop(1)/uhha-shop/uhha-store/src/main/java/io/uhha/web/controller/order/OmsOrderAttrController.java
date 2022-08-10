package io.uhha.web.controller.order;

import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.order.domain.OmsOrderAttr;
import io.uhha.order.service.IOmsOrderAttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单属性Controller
 *
 * @author mj
 * @date 2020-07-24
 */
@RestController
@RequestMapping("/order/OmsOrderAttr")
public class OmsOrderAttrController extends BaseController {
    @Autowired
    private IOmsOrderAttrService omsOrderAttrService;

    /**
     * 查询订单属性列表
     */
    @PreAuthorize("@ss.hasPermi('order:OmsOrderAttr:list')")
    @GetMapping("/list")
    public TableDataInfo list(OmsOrderAttr omsOrderAttr) {
        startPage();
        List<OmsOrderAttr> list = omsOrderAttrService.selectOmsOrderAttrList(omsOrderAttr);
        return getDataTable(list);
    }

    /**
     * 导出订单属性列表
     */
    @PreAuthorize("@ss.hasPermi('order:OmsOrderAttr:export')")
    @Log(title = "订单属性", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(OmsOrderAttr omsOrderAttr) {
        List<OmsOrderAttr> list = omsOrderAttrService.selectOmsOrderAttrList(omsOrderAttr);
        ExcelUtil<OmsOrderAttr> util = new ExcelUtil<OmsOrderAttr>(OmsOrderAttr.class);
        return util.exportExcel(list, "OmsOrderAttr");
    }

    /**
     * 获取订单属性详细信息
     */
    @PreAuthorize("@ss.hasPermi('order:OmsOrderAttr:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(omsOrderAttrService.selectOmsOrderAttrById(id));
    }

    /**
     * 新增订单属性
     */
    @PreAuthorize("@ss.hasPermi('order:OmsOrderAttr:add')")
    @Log(title = "订单属性", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OmsOrderAttr omsOrderAttr) {
        return toAjax(omsOrderAttrService.insertOmsOrderAttr(omsOrderAttr));
    }

    /**
     * 修改订单属性
     */
    @PreAuthorize("@ss.hasPermi('order:OmsOrderAttr:edit')")
    @Log(title = "订单属性", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OmsOrderAttr omsOrderAttr) {
        return toAjax(omsOrderAttrService.updateOmsOrderAttr(omsOrderAttr));
    }

    /**
     * 删除订单属性
     */
    @PreAuthorize("@ss.hasPermi('order:OmsOrderAttr:remove')")
    @Log(title = "订单属性", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(omsOrderAttrService.deleteOmsOrderAttrByIds(ids));
    }
}

package io.uhha.web.controller.store;

import java.util.List;

import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.enums.BusinessType;
import io.uhha.store.domain.TStoreOrder;
import io.uhha.store.service.ITStoreOrderService;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.common.core.page.TableDataInfo;

/**
 * 门店订单Controller
 * 
 * @author ruoyi
 * @date 2021-05-23
 */
@Api(tags = "门店订单Controller")
@RestController
@RequestMapping("/store/TStoreOrder")
public class TStoreOrderController extends BaseController
{
    @Autowired
    private ITStoreOrderService tStoreOrderService;

    /**
     * 查询门店订单列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreOrder:list')")
    @GetMapping("/list")
    public TableDataInfo list(TStoreOrder tStoreOrder)
    {
        startPage();
        List<TStoreOrder> list = tStoreOrderService.selectTStoreOrderList(tStoreOrder);
        return getDataTable(list);
    }

    /**
     * 导出门店订单列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreOrder:export')")
    @Log(title = "门店订单", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TStoreOrder tStoreOrder)
    {
        List<TStoreOrder> list = tStoreOrderService.selectTStoreOrderList(tStoreOrder);
        ExcelUtil<TStoreOrder> util = new ExcelUtil<TStoreOrder>(TStoreOrder.class);
        return util.exportExcel(list, "门店订单数据");
    }

    /**
     * 获取门店订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreOrder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tStoreOrderService.selectTStoreOrderById(id));
    }

    /**
     * 新增门店订单
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreOrder:add')")
    @Log(title = "门店订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TStoreOrder tStoreOrder)
    {
        return toAjax(tStoreOrderService.insertTStoreOrder(tStoreOrder));
    }

    /**
     * 修改门店订单
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreOrder:edit')")
    @Log(title = "门店订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TStoreOrder tStoreOrder)
    {
        return toAjax(tStoreOrderService.updateTStoreOrder(tStoreOrder));
    }

    /**
     * 删除门店订单
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreOrder:remove')")
    @Log(title = "门店订单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tStoreOrderService.deleteTStoreOrderByIds(ids));
    }
}

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
import io.uhha.store.domain.TStoreOrderSku;
import io.uhha.store.service.ITStoreOrderSkuService;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.common.core.page.TableDataInfo;

/**
 * 门店订单单品信息Controller
 * 
 * @author ruoyi
 * @date 2021-05-23
 */
@Api(tags = "门店订单单品信息Controller")
@RestController
@RequestMapping("/store/TStoreOrderSku")
public class TStoreOrderSkuController extends BaseController
{
    @Autowired
    private ITStoreOrderSkuService tStoreOrderSkuService;

    /**
     * 查询门店订单单品信息列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreOrderSku:list')")
    @GetMapping("/list")
    public TableDataInfo list(TStoreOrderSku tStoreOrderSku)
    {
        startPage();
        List<TStoreOrderSku> list = tStoreOrderSkuService.selectTStoreOrderSkuList(tStoreOrderSku);
        return getDataTable(list);
    }

    /**
     * 导出门店订单单品信息列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreOrderSku:export')")
    @Log(title = "门店订单单品信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TStoreOrderSku tStoreOrderSku)
    {
        List<TStoreOrderSku> list = tStoreOrderSkuService.selectTStoreOrderSkuList(tStoreOrderSku);
        ExcelUtil<TStoreOrderSku> util = new ExcelUtil<TStoreOrderSku>(TStoreOrderSku.class);
        return util.exportExcel(list, "门店订单单品信息数据");
    }

    /**
     * 获取门店订单单品信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreOrderSku:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tStoreOrderSkuService.selectTStoreOrderSkuById(id));
    }

    /**
     * 新增门店订单单品信息
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreOrderSku:add')")
    @Log(title = "门店订单单品信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TStoreOrderSku tStoreOrderSku)
    {
        return toAjax(tStoreOrderSkuService.insertTStoreOrderSku(tStoreOrderSku));
    }

    /**
     * 修改门店订单单品信息
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreOrderSku:edit')")
    @Log(title = "门店订单单品信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TStoreOrderSku tStoreOrderSku)
    {
        return toAjax(tStoreOrderSkuService.updateTStoreOrderSku(tStoreOrderSku));
    }

    /**
     * 删除门店订单单品信息
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreOrderSku:remove')")
    @Log(title = "门店订单单品信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tStoreOrderSkuService.deleteTStoreOrderSkuByIds(ids));
    }
}

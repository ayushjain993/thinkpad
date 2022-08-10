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
import io.uhha.store.domain.TStoreSku;
import io.uhha.store.service.ITStoreSkuService;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.common.core.page.TableDataInfo;

/**
 * 门店单品Controller
 * 
 * @author ruoyi
 * @date 2021-05-23
 */
@Api(tags = "门店单品Controller")
@RestController
@RequestMapping("/store/TStoreSku")
public class TStoreSkuController extends BaseController
{
    @Autowired
    private ITStoreSkuService tStoreSkuService;

    /**
     * 查询门店单品列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreSku:list')")
    @GetMapping("/list")
    public TableDataInfo list(TStoreSku tStoreSku)
    {
        startPage();
        List<TStoreSku> list = tStoreSkuService.selectTStoreSkuList(tStoreSku);
        return getDataTable(list);
    }

    /**
     * 导出门店单品列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreSku:export')")
    @Log(title = "门店单品", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TStoreSku tStoreSku)
    {
        List<TStoreSku> list = tStoreSkuService.selectTStoreSkuList(tStoreSku);
        ExcelUtil<TStoreSku> util = new ExcelUtil<TStoreSku>(TStoreSku.class);
        return util.exportExcel(list, "门店单品数据");
    }

    /**
     * 获取门店单品详细信息
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreSku:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tStoreSkuService.selectTStoreSkuById(id));
    }

    /**
     * 新增门店单品
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreSku:add')")
    @Log(title = "门店单品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TStoreSku tStoreSku)
    {
        return toAjax(tStoreSkuService.insertTStoreSku(tStoreSku));
    }

    /**
     * 修改门店单品
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreSku:edit')")
    @Log(title = "门店单品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TStoreSku tStoreSku)
    {
        return toAjax(tStoreSkuService.updateTStoreSku(tStoreSku));
    }

    /**
     * 删除门店单品
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreSku:remove')")
    @Log(title = "门店单品", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tStoreSkuService.deleteTStoreSkuByIds(ids));
    }
}

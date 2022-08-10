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
import io.uhha.store.domain.TStoreCategory;
import io.uhha.store.service.ITStoreCategoryService;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.common.core.page.TableDataInfo;

/**
 * 店铺分类Controller
 * 
 * @author ruoyi
 * @date 2021-05-23
 */
@Api(tags = "店铺分类Controller")
@RestController
@RequestMapping("/store/TStoreCategory")
public class TStoreCategoryController extends BaseController
{
    @Autowired
    private ITStoreCategoryService tStoreCategoryService;

    /**
     * 查询店铺分类列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreCategory:list')")
    @GetMapping("/list")
    public TableDataInfo list(TStoreCategory tStoreCategory)
    {
        startPage();
        List<TStoreCategory> list = tStoreCategoryService.selectTStoreCategoryList(tStoreCategory);
        return getDataTable(list);
    }

    /**
     * 导出店铺分类列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreCategory:export')")
    @Log(title = "店铺分类", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TStoreCategory tStoreCategory)
    {
        List<TStoreCategory> list = tStoreCategoryService.selectTStoreCategoryList(tStoreCategory);
        ExcelUtil<TStoreCategory> util = new ExcelUtil<TStoreCategory>(TStoreCategory.class);
        return util.exportExcel(list, "店铺分类数据");
    }

    /**
     * 获取店铺分类详细信息
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreCategory:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tStoreCategoryService.selectTStoreCategoryById(id));
    }

    /**
     * 新增店铺分类
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreCategory:add')")
    @Log(title = "店铺分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TStoreCategory tStoreCategory)
    {
        return toAjax(tStoreCategoryService.insertTStoreCategory(tStoreCategory));
    }

    /**
     * 修改店铺分类
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreCategory:edit')")
    @Log(title = "店铺分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TStoreCategory tStoreCategory)
    {
        return toAjax(tStoreCategoryService.updateTStoreCategory(tStoreCategory));
    }

    /**
     * 删除店铺分类
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreCategory:remove')")
    @Log(title = "店铺分类", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tStoreCategoryService.deleteTStoreCategoryByIds(ids));
    }
}

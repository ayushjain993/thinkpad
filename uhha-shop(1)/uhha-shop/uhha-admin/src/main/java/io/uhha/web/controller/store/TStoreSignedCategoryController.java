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
import io.uhha.store.domain.TStoreSignedCategory;
import io.uhha.store.service.ITStoreSignedCategoryService;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.common.core.page.TableDataInfo;

/**
 * 店铺的签约分类Controller
 * 
 * @author ruoyi
 * @date 2021-05-23
 */
@Api(tags = "店铺的签约分类Controller")
@RestController
@RequestMapping("/store/TStoreSignedCategory")
public class TStoreSignedCategoryController extends BaseController
{
    @Autowired
    private ITStoreSignedCategoryService tStoreSignedCategoryService;

    /**
     * 查询店铺的签约分类列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreSignedCategory:list')")
    @GetMapping("/list")
    public TableDataInfo list(TStoreSignedCategory tStoreSignedCategory)
    {
        startPage();
        List<TStoreSignedCategory> list = tStoreSignedCategoryService.selectTStoreSignedCategoryList(tStoreSignedCategory);
        return getDataTable(list);
    }

    /**
     * 导出店铺的签约分类列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreSignedCategory:export')")
    @Log(title = "店铺的签约分类", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TStoreSignedCategory tStoreSignedCategory)
    {
        List<TStoreSignedCategory> list = tStoreSignedCategoryService.selectTStoreSignedCategoryList(tStoreSignedCategory);
        ExcelUtil<TStoreSignedCategory> util = new ExcelUtil<TStoreSignedCategory>(TStoreSignedCategory.class);
        return util.exportExcel(list, "店铺的签约分类数据");
    }

    /**
     * 获取店铺的签约分类详细信息
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreSignedCategory:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tStoreSignedCategoryService.selectTStoreSignedCategoryById(id));
    }

    /**
     * 新增店铺的签约分类
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreSignedCategory:add')")
    @Log(title = "店铺的签约分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TStoreSignedCategory tStoreSignedCategory)
    {
        return toAjax(tStoreSignedCategoryService.insertTStoreSignedCategory(tStoreSignedCategory));
    }

    /**
     * 修改店铺的签约分类
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreSignedCategory:edit')")
    @Log(title = "店铺的签约分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TStoreSignedCategory tStoreSignedCategory)
    {
        return toAjax(tStoreSignedCategoryService.updateTStoreSignedCategory(tStoreSignedCategory));
    }

    /**
     * 删除店铺的签约分类
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreSignedCategory:remove')")
    @Log(title = "店铺的签约分类", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tStoreSignedCategoryService.deleteTStoreSignedCategoryByIds(ids));
    }
}

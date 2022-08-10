package io.uhha.web.controller.store;

import java.util.List;

import io.swagger.annotations.Api;
import io.uhha.goods.domain.PmsBrandApply;
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
import io.uhha.store.domain.TStoreCustomizeBrand;
import io.uhha.store.service.ITStoreCustomizeBrandService;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.common.core.page.TableDataInfo;

/**
 * 店铺自定义品牌列Controller
 * 
 * @author ruoyi
 * @date 2021-05-23
 */
@Api(tags = "店铺自定义品牌列Controller")
@RestController
@RequestMapping("/store/TStoreCustomizeBrand")
public class TStoreCustomizeBrandController extends BaseController
{
    @Autowired
    private ITStoreCustomizeBrandService tStoreCustomizeBrandService;

    /**
     * 查询店铺自定义品牌列列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreCustomizeBrand:list')")
    @GetMapping("/list")
    public TableDataInfo list(TStoreCustomizeBrand tStoreCustomizeBrand)
    {
        startPage();
        List<TStoreCustomizeBrand> list = tStoreCustomizeBrandService.selectTStoreCustomizeBrandList(tStoreCustomizeBrand);
        return getDataTable(list);
    }

    /**
     * 导出店铺自定义品牌列列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreCustomizeBrand:export')")
    @Log(title = "店铺自定义品牌列", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TStoreCustomizeBrand tStoreCustomizeBrand)
    {
        List<TStoreCustomizeBrand> list = tStoreCustomizeBrandService.selectTStoreCustomizeBrandList(tStoreCustomizeBrand);
        ExcelUtil<TStoreCustomizeBrand> util = new ExcelUtil<TStoreCustomizeBrand>(TStoreCustomizeBrand.class);
        return util.exportExcel(list, "店铺自定义品牌列数据");
    }

    /**
     * 获取店铺自定义品牌列详细信息
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreCustomizeBrand:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tStoreCustomizeBrandService.selectTStoreCustomizeBrandById(id));
    }

    /**
     * 新增店铺自定义品牌列
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreCustomizeBrand:add')")
    @Log(title = "店铺自定义品牌列", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TStoreCustomizeBrand tStoreCustomizeBrand)
    {
        return toAjax(tStoreCustomizeBrandService.insertTStoreCustomizeBrand(tStoreCustomizeBrand));
    }

    /**
     * 修改店铺自定义品牌列
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreCustomizeBrand:edit')")
    @Log(title = "店铺自定义品牌列", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TStoreCustomizeBrand tStoreCustomizeBrand)
    {
        return toAjax(tStoreCustomizeBrandService.updateTStoreCustomizeBrand(tStoreCustomizeBrand));
    }

    /**
     * 删除店铺自定义品牌列
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreCustomizeBrand:remove')")
    @Log(title = "店铺自定义品牌列", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tStoreCustomizeBrandService.deleteTStoreCustomizeBrandByIds(ids));
    }


    /**
     * 通过自定义品牌审批
     */
    @PreAuthorize("@ss.hasPermi('goods:TStoreCustomizeBrand:edit')")
    @Log(title = "自定义品牌", businessType = BusinessType.UPDATE)
    @PutMapping("/pass/{brandId}")
    public AjaxResult pass(@PathVariable("brandId") Long brandId) {
        return AjaxResult.success(tStoreCustomizeBrandService.passBrandAudit(brandId));
    }

    /**
     * 拒绝自定义品牌审批
     */
    @PreAuthorize("@ss.hasPermi('goods:TStoreCustomizeBrand:edit')")
    @Log(title = "自定义品牌", businessType = BusinessType.UPDATE)
    @PutMapping("/refuse")
    public AjaxResult refuse(@RequestBody TStoreCustomizeBrand pmsBrandApply) {
        return AjaxResult.success(tStoreCustomizeBrandService.refuseBrandAudit(pmsBrandApply));
    }

    /**
     * 批量通过自定义品牌审批
     */
    @PreAuthorize("@ss.hasPermi('goods:TStoreCustomizeBrand:edit')")
    @Log(title = "自定义品牌", businessType = BusinessType.UPDATE)
    @PutMapping("/batchpass")
    public AjaxResult batchpass(Long[] ids) {
        return AjaxResult.success(tStoreCustomizeBrandService.batchPassBrandAudit(ids));
    }

    /**
     * 批量拒绝自定义品牌审批
     */
    @PreAuthorize("@ss.hasPermi('goods:TStoreCustomizeBrand:edit')")
    @Log(title = "自定义品牌", businessType = BusinessType.UPDATE)
    @PutMapping("/batchrefuse")
    public AjaxResult batchrefuse(Long[] ids, String reason) {
        return AjaxResult.success(tStoreCustomizeBrandService.batchRefuseBrandAudit(ids, reason));
    }
}

package io.uhha.web.controller.store;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.AuditStatusEnum;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.store.domain.TStoreCustomizeBrand;
import io.uhha.store.service.ITStoreCustomizeBrandService;
import io.uhha.util.CommonConstant;
import io.uhha.web.utils.AdminLoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/approved")
    public TableDataInfo myapprovedList(TStoreCustomizeBrand tStoreCustomizeBrand)
    {
        tStoreCustomizeBrand.setStoreId(AdminLoginUtils.getInstance().getStoreId());
        tStoreCustomizeBrand.setDelFlag(0);
        tStoreCustomizeBrand.setStatus(AuditStatusEnum.PASS.getCode());
        startPage();
        List<TStoreCustomizeBrand> list = tStoreCustomizeBrandService.selectTStoreCustomizeBrandList(tStoreCustomizeBrand);
        return getDataTable(list);
    }

    /**
     * 查询官方店铺自定义品牌列列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreCustomizeBrand:list')")
    @GetMapping("/admin")
    public TableDataInfo adminList(TStoreCustomizeBrand tStoreCustomizeBrand)
    {
        tStoreCustomizeBrand.setStoreId(CommonConstant.ADMIN_STOREID);
        tStoreCustomizeBrand.setDelFlag(0);
        tStoreCustomizeBrand.setStatus(AuditStatusEnum.PASS.getCode());
        startPage();
        List<TStoreCustomizeBrand> list = tStoreCustomizeBrandService.selectTStoreCustomizeBrandList(tStoreCustomizeBrand);
        return getDataTable(list);
    }

    /**
     * 查询店铺自定义品牌列列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreCustomizeBrand:list')")
    @GetMapping("/list")
    public TableDataInfo list(TStoreCustomizeBrand tStoreCustomizeBrand)
    {
        tStoreCustomizeBrand.setStoreId(AdminLoginUtils.getInstance().getStoreId());
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
        tStoreCustomizeBrand.setStoreId(AdminLoginUtils.getInstance().getStoreId());
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
}

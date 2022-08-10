package io.uhha.web.controller.goods;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.goods.domain.PmsBrand;
import io.uhha.goods.service.IPmsBrandService;
import io.uhha.util.CommonConstant;
import io.uhha.web.utils.AdminLoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 品牌Controller
 *
 * @author mj
 * @date 2020-07-24
 */
@Api(tags = "品牌Controller")
@RestController
@RequestMapping("/goods/brand")
public class PmsBrandController extends BaseController {
    @Autowired
    private IPmsBrandService pmsBrandService;

    /**
     * 查询品牌列表
     */
    @PreAuthorize("@ss.hasPermi('goods:brand:list')")
    @GetMapping("/list")
    public TableDataInfo list(PmsBrand pmsBrand) {
        startPage();
        List<PmsBrand> list = pmsBrandService.selectPmsBrandList(pmsBrand);
        return getDataTable(list);
    }

    /**
     * 导出品牌列表
     */
    @PreAuthorize("@ss.hasPermi('goods:brand:export')")
    @Log(title = "品牌", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(PmsBrand pmsBrand) {
        List<PmsBrand> list = pmsBrandService.selectPmsBrandList(pmsBrand);
        ExcelUtil<PmsBrand> util = new ExcelUtil<PmsBrand>(PmsBrand.class);
        return util.exportExcel(list, "brand");
    }

    /**
     * 获取品牌详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:brand:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(pmsBrandService.selectPmsBrandById(id));
    }

    /**
     * 新增品牌
     */
    @PreAuthorize("@ss.hasPermi('goods:brand:add')")
    @Log(title = "品牌", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PmsBrand pmsBrand) {
        return toAjax(pmsBrandService.insertPmsBrand(pmsBrand.setDefaultValuesForAdd(AdminLoginUtils.getInstance().getManagerName(), CommonConstant.ADMIN_STOREID)));
    }

    /**
     * 修改品牌
     */
    @PreAuthorize("@ss.hasPermi('goods:brand:edit')")
    @Log(title = "品牌", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PmsBrand pmsBrand) {
        return toAjax(pmsBrandService.updatePmsBrand(pmsBrand.setDefaultValuesForAdd(AdminLoginUtils.getInstance().getManagerName(), CommonConstant.ADMIN_STOREID)));
    }

    /**
     * 删除品牌
     */
    @PreAuthorize("@ss.hasPermi('goods:brand:remove')")
    @Log(title = "品牌", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(pmsBrandService.batchDeleteBrands(Arrays.stream(ids).map(id -> PmsBrand.buildDeleteBrand(id, AdminLoginUtils.getInstance().getManagerName(), CommonConstant.ADMIN_STOREID)).collect(Collectors.toList())));
    }
}

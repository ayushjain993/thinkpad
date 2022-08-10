package io.uhha.web.controller.goods;

import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.goods.domain.PmsCategorySpec;
import io.uhha.goods.service.IPmsCategorySpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类和规格的关联Controller
 *
 * @author mj
 * @date 2020-07-24
 */
@RestController
@RequestMapping("/goods/categorySpec")
public class PmsCategorySpecController extends BaseController {
    @Autowired
    private IPmsCategorySpecService pmsCategorySpecService;

    /**
     * 查询分类和规格的关联列表
     */
    @PreAuthorize("@ss.hasPermi('goods:spec:list')")
    @GetMapping("/list")
    public TableDataInfo list(PmsCategorySpec pmsCategorySpec) {
        startPage();
        List<PmsCategorySpec> list = pmsCategorySpecService.selectPmsCategorySpecList(pmsCategorySpec);
        return getDataTable(list);
    }

    /**
     * 导出分类和规格的关联列表
     */
    @PreAuthorize("@ss.hasPermi('goods:spec:export')")
    @Log(title = "分类和规格的关联", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(PmsCategorySpec pmsCategorySpec) {
        List<PmsCategorySpec> list = pmsCategorySpecService.selectPmsCategorySpecList(pmsCategorySpec);
        ExcelUtil<PmsCategorySpec> util = new ExcelUtil<PmsCategorySpec>(PmsCategorySpec.class);
        return util.exportExcel(list, "spec");
    }

    /**
     * 获取分类和规格的关联详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:spec:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(pmsCategorySpecService.selectPmsCategorySpecById(id));
    }

    /**
     * 新增分类和规格的关联
     */
    @PreAuthorize("@ss.hasPermi('goods:spec:add')")
    @Log(title = "分类和规格的关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PmsCategorySpec pmsCategorySpec) {
        return toAjax(pmsCategorySpecService.insertPmsCategorySpec(pmsCategorySpec));
    }

    /**
     * 修改分类和规格的关联
     */
    @PreAuthorize("@ss.hasPermi('goods:spec:edit')")
    @Log(title = "分类和规格的关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PmsCategorySpec pmsCategorySpec) {
        return toAjax(pmsCategorySpecService.updatePmsCategorySpec(pmsCategorySpec));
    }

    /**
     * 删除分类和规格的关联
     */
    @PreAuthorize("@ss.hasPermi('goods:spec:remove')")
    @Log(title = "分类和规格的关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(pmsCategorySpecService.deletePmsCategorySpecByIds(ids));
    }
}

package io.uhha.web.controller.goods;

import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.goods.domain.PmsSpecValue;
import io.uhha.goods.service.IPmsSpecValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 规格值Controller
 *
 * @author mj
 * @date 2020-07-24
 */
@RestController
@RequestMapping("/goods/specValue")
public class PmsSpecValueController extends BaseController {
    @Autowired
    private IPmsSpecValueService pmsSpecValueService;

    /**
     * 查询规格值列表
     */
    @PreAuthorize("@ss.hasPermi('goods:value:list')")
    @GetMapping("/list")
    public TableDataInfo list(PmsSpecValue pmsSpecValue) {
        startPage();
        List<PmsSpecValue> list = pmsSpecValueService.selectPmsSpecValueList(pmsSpecValue);
        return getDataTable(list);
    }

    /**
     * 导出规格值列表
     */
    @PreAuthorize("@ss.hasPermi('goods:value:export')")
    @Log(title = "规格值", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(PmsSpecValue pmsSpecValue) {
        List<PmsSpecValue> list = pmsSpecValueService.selectPmsSpecValueList(pmsSpecValue);
        ExcelUtil<PmsSpecValue> util = new ExcelUtil<PmsSpecValue>(PmsSpecValue.class);
        return util.exportExcel(list, "value");
    }

    /**
     * 获取规格值详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:value:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(pmsSpecValueService.selectPmsSpecValueById(id));
    }

    /**
     * 新增规格值
     */
    @PreAuthorize("@ss.hasPermi('goods:value:add')")
    @Log(title = "规格值", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PmsSpecValue pmsSpecValue) {
        return toAjax(pmsSpecValueService.insertPmsSpecValue(pmsSpecValue));
    }

    /**
     * 修改规格值
     */
    @PreAuthorize("@ss.hasPermi('goods:value:edit')")
    @Log(title = "规格值", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PmsSpecValue pmsSpecValue) {
        return toAjax(pmsSpecValueService.updatePmsSpecValue(pmsSpecValue));
    }

    /**
     * 删除规格值
     */
    @PreAuthorize("@ss.hasPermi('goods:value:remove')")
    @Log(title = "规格值", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(pmsSpecValueService.deletePmsSpecValueByIds(ids));
    }
}

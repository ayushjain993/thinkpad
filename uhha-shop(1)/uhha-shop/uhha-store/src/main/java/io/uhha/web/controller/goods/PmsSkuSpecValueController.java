package io.uhha.web.controller.goods;

import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.goods.domain.PmsSkuSpecValue;
import io.uhha.goods.service.IPmsSkuSpecValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 单品的规格值Controller
 *
 * @author mj
 * @date 2020-07-24
 */
@RestController
@RequestMapping("/goods/skuSpecValue")
public class PmsSkuSpecValueController extends BaseController {
    @Autowired
    private IPmsSkuSpecValueService pmsSkuSpecValueService;

    /**
     * 查询单品的规格值列表
     */
    @PreAuthorize("@ss.hasPermi('goods:value:list')")
    @GetMapping("/list")
    public TableDataInfo list(PmsSkuSpecValue pmsSkuSpecValue) {
        startPage();
        List<PmsSkuSpecValue> list = pmsSkuSpecValueService.selectPmsSkuSpecValueList(pmsSkuSpecValue);
        return getDataTable(list);
    }

    /**
     * 导出单品的规格值列表
     */
    @PreAuthorize("@ss.hasPermi('goods:value:export')")
    @Log(title = "单品的规格值", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(PmsSkuSpecValue pmsSkuSpecValue) {
        List<PmsSkuSpecValue> list = pmsSkuSpecValueService.selectPmsSkuSpecValueList(pmsSkuSpecValue);
        ExcelUtil<PmsSkuSpecValue> util = new ExcelUtil<PmsSkuSpecValue>(PmsSkuSpecValue.class);
        return util.exportExcel(list, "value");
    }

    /**
     * 获取单品的规格值详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:value:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(pmsSkuSpecValueService.selectPmsSkuSpecValueById(id));
    }

    /**
     * 新增单品的规格值
     */
    @PreAuthorize("@ss.hasPermi('goods:value:add')")
    @Log(title = "单品的规格值", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PmsSkuSpecValue pmsSkuSpecValue) {
        return toAjax(pmsSkuSpecValueService.insertPmsSkuSpecValue(pmsSkuSpecValue));
    }

    /**
     * 修改单品的规格值
     */
    @PreAuthorize("@ss.hasPermi('goods:value:edit')")
    @Log(title = "单品的规格值", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PmsSkuSpecValue pmsSkuSpecValue) {
        return toAjax(pmsSkuSpecValueService.updatePmsSkuSpecValue(pmsSkuSpecValue));
    }

    /**
     * 删除单品的规格值
     */
    @PreAuthorize("@ss.hasPermi('goods:value:remove')")
    @Log(title = "单品的规格值", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(pmsSkuSpecValueService.deletePmsSkuSpecValueByIds(ids));
    }
}

package io.uhha.web.controller.goods;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.goods.domain.PmsAttributeValue;
import io.uhha.goods.service.IPmsAttributeValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 属性值Controller
 *
 * @author mj
 * @date 2020-07-24
 */
@Api(tags = "商品属性值Controller")
@RestController
@RequestMapping("/goods/attriValue")
public class PmsAttributeValueController extends BaseController {
    @Autowired
    private IPmsAttributeValueService pmsAttributeValueService;

    /**
     * 查询属性值列表
     */
    @PreAuthorize("@ss.hasPermi('goods:value:list')")
    @GetMapping("/list")
    public TableDataInfo list(PmsAttributeValue pmsAttributeValue) {
        startPage();
        List<PmsAttributeValue> list = pmsAttributeValueService.selectPmsAttributeValueList(pmsAttributeValue);
        return getDataTable(list);
    }

    /**
     * 导出属性值列表
     */
    @PreAuthorize("@ss.hasPermi('goods:value:export')")
    @Log(title = "属性值", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(PmsAttributeValue pmsAttributeValue) {
        List<PmsAttributeValue> list = pmsAttributeValueService.selectPmsAttributeValueList(pmsAttributeValue);
        ExcelUtil<PmsAttributeValue> util = new ExcelUtil<PmsAttributeValue>(PmsAttributeValue.class);
        return util.exportExcel(list, "value");
    }

    /**
     * 获取属性值详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:value:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(pmsAttributeValueService.selectPmsAttributeValueById(id));
    }

    /**
     * 新增属性值
     */
    @PreAuthorize("@ss.hasPermi('goods:value:add')")
    @Log(title = "属性值", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PmsAttributeValue pmsAttributeValue) {
        return toAjax(pmsAttributeValueService.insertPmsAttributeValue(pmsAttributeValue));
    }

    /**
     * 修改属性值
     */
    @PreAuthorize("@ss.hasPermi('goods:value:edit')")
    @Log(title = "属性值", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PmsAttributeValue pmsAttributeValue) {
        return toAjax(pmsAttributeValueService.updatePmsAttributeValue(pmsAttributeValue));
    }

    /**
     * 删除属性值
     */
    @PreAuthorize("@ss.hasPermi('goods:value:remove')")
    @Log(title = "属性值", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(pmsAttributeValueService.deletePmsAttributeValueByIds(ids));
    }
}

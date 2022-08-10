package io.uhha.web.controller.goods;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.goods.domain.PmsAttribute;
import io.uhha.goods.service.IPmsAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品属性Controller
 *
 * @author mj
 * @date 2020-07-24
 */
@Api(tags = "商品属性Controller")
@RestController
@RequestMapping("/goods/attribute")
public class PmsAttributeController extends BaseController {
    @Autowired
    private IPmsAttributeService pmsAttributeService;

    /**
     * 查询商品属性列表
     */
    @PreAuthorize("@ss.hasPermi('goods:attribute:list')")
    @GetMapping("/list")
    public TableDataInfo list(PmsAttribute pmsAttribute) {
        startPage();
        List<PmsAttribute> list = pmsAttributeService.selectPmsAttributeList(pmsAttribute);
        return getDataTable(list);
    }

    /**
     * 导出商品属性列表
     */
    @PreAuthorize("@ss.hasPermi('goods:attribute:export')")
    @Log(title = "商品属性", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(PmsAttribute pmsAttribute) {
        List<PmsAttribute> list = pmsAttributeService.selectPmsAttributeList(pmsAttribute);
        ExcelUtil<PmsAttribute> util = new ExcelUtil<PmsAttribute>(PmsAttribute.class);
        return util.exportExcel(list, "attribute");
    }

    /**
     * 获取商品属性详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:attribute:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(pmsAttributeService.selectPmsAttributeById(id));
    }

    /**
     * 新增商品属性
     */
    @PreAuthorize("@ss.hasPermi('goods:attribute:add')")
    @Log(title = "商品属性", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PmsAttribute pmsAttribute) {
        return toAjax(pmsAttributeService.insertPmsAttribute(pmsAttribute));
    }

    /**
     * 修改商品属性
     */
    @PreAuthorize("@ss.hasPermi('goods:attribute:edit')")
    @Log(title = "商品属性", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PmsAttribute pmsAttribute) {
        return toAjax(pmsAttributeService.updatePmsAttribute(pmsAttribute));
    }

    /**
     * 删除商品属性
     */
    @PreAuthorize("@ss.hasPermi('goods:attribute:remove')")
    @Log(title = "商品属性", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(pmsAttributeService.deletePmsAttributeByIds(ids));
    }
}

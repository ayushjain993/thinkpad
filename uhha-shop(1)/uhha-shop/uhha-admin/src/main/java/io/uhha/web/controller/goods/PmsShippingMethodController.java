package io.uhha.web.controller.goods;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.goods.domain.PmsShippingMethod;
import io.uhha.goods.service.IPmsShippingMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 运费方式Controller
 *
 * @author mj
 * @date 2020-07-24
 */
@Api(tags = "运费方式Controller")
@RestController
@RequestMapping("/goods/method")
public class PmsShippingMethodController extends BaseController {
    @Autowired
    private IPmsShippingMethodService pmsShippingMethodService;

    /**
     * 查询运费方式列表
     */
    @PreAuthorize("@ss.hasPermi('goods:method:list')")
    @GetMapping("/list")
    public TableDataInfo list(PmsShippingMethod pmsShippingMethod) {
        startPage();
        List<PmsShippingMethod> list = pmsShippingMethodService.selectPmsShippingMethodList(pmsShippingMethod);
        return getDataTable(list);
    }

    /**
     * 导出运费方式列表
     */
    @PreAuthorize("@ss.hasPermi('goods:method:export')")
    @Log(title = "运费方式", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(PmsShippingMethod pmsShippingMethod) {
        List<PmsShippingMethod> list = pmsShippingMethodService.selectPmsShippingMethodList(pmsShippingMethod);
        ExcelUtil<PmsShippingMethod> util = new ExcelUtil<PmsShippingMethod>(PmsShippingMethod.class);
        return util.exportExcel(list, "method");
    }

    /**
     * 获取运费方式详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:method:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(pmsShippingMethodService.selectPmsShippingMethodById(id));
    }

    /**
     * 新增运费方式
     */
    @PreAuthorize("@ss.hasPermi('goods:method:add')")
    @Log(title = "运费方式", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PmsShippingMethod pmsShippingMethod) {
        return toAjax(pmsShippingMethodService.insertPmsShippingMethod(pmsShippingMethod));
    }

    /**
     * 修改运费方式
     */
    @PreAuthorize("@ss.hasPermi('goods:method:edit')")
    @Log(title = "运费方式", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PmsShippingMethod pmsShippingMethod) {
        return toAjax(pmsShippingMethodService.updatePmsShippingMethod(pmsShippingMethod));
    }

    /**
     * 删除运费方式
     */
    @PreAuthorize("@ss.hasPermi('goods:method:remove')")
    @Log(title = "运费方式", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(pmsShippingMethodService.deletePmsShippingMethodByIds(ids));
    }
}

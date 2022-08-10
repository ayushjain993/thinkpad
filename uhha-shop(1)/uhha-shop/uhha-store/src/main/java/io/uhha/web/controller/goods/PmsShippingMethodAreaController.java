package io.uhha.web.controller.goods;

import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.goods.domain.PmsShippingMethodArea;
import io.uhha.goods.service.IPmsShippingMethodAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 运费方式关联的区域Controller
 *
 * @author mj
 * @date 2020-07-24
 */
@RestController
@RequestMapping("/goods/area")
public class PmsShippingMethodAreaController extends BaseController {
    @Autowired
    private IPmsShippingMethodAreaService pmsShippingMethodAreaService;

    /**
     * 查询运费方式关联的区域列表
     */
    @PreAuthorize("@ss.hasPermi('goods:area:list')")
    @GetMapping("/list")
    public TableDataInfo list(PmsShippingMethodArea pmsShippingMethodArea) {
        startPage();
        List<PmsShippingMethodArea> list = pmsShippingMethodAreaService.selectPmsShippingMethodAreaList(pmsShippingMethodArea);
        return getDataTable(list);
    }

    /**
     * 导出运费方式关联的区域列表
     */
    @PreAuthorize("@ss.hasPermi('goods:area:export')")
    @Log(title = "运费方式关联的区域", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(PmsShippingMethodArea pmsShippingMethodArea) {
        List<PmsShippingMethodArea> list = pmsShippingMethodAreaService.selectPmsShippingMethodAreaList(pmsShippingMethodArea);
        ExcelUtil<PmsShippingMethodArea> util = new ExcelUtil<PmsShippingMethodArea>(PmsShippingMethodArea.class);
        return util.exportExcel(list, "area");
    }

    /**
     * 获取运费方式关联的区域详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:area:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(pmsShippingMethodAreaService.selectPmsShippingMethodAreaById(id));
    }

    /**
     * 新增运费方式关联的区域
     */
    @PreAuthorize("@ss.hasPermi('goods:area:add')")
    @Log(title = "运费方式关联的区域", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PmsShippingMethodArea pmsShippingMethodArea) {
        return toAjax(pmsShippingMethodAreaService.insertPmsShippingMethodArea(pmsShippingMethodArea));
    }

    /**
     * 修改运费方式关联的区域
     */
    @PreAuthorize("@ss.hasPermi('goods:area:edit')")
    @Log(title = "运费方式关联的区域", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PmsShippingMethodArea pmsShippingMethodArea) {
        return toAjax(pmsShippingMethodAreaService.updatePmsShippingMethodArea(pmsShippingMethodArea));
    }

    /**
     * 删除运费方式关联的区域
     */
    @PreAuthorize("@ss.hasPermi('goods:area:remove')")
    @Log(title = "运费方式关联的区域", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(pmsShippingMethodAreaService.deletePmsShippingMethodAreaByIds(ids));
    }
}

package io.uhha.web.controller.marketing;

import io.swagger.annotations.Api;
import io.uhha.util.PageHelper;
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
import io.uhha.integral.domain.PointSku;
import io.uhha.integral.service.PointSkuService;
import io.uhha.common.utils.poi.ExcelUtil;

/**
 * 积分商品Controller
 *
 * @author uhha
 * @date 2021-11-09
 */
@Api(tags = "积分商品Controller")
@RestController
@RequestMapping("/marketing/pointSku")
public class PointSkuController extends BaseController {
    @Autowired
    private PointSkuService pointSkuService;

    /**
     * 查询积分商品列表
     */
    @PreAuthorize("@ss.hasPermi('marketing:pointSku:list')")
    @GetMapping
    public PageHelper<PointSku> list(PageHelper<PointSku> pageHelper, Long cateId, String name, Boolean isFilterStatus) {
        return pointSkuService.queryPointSkus(pageHelper, cateId, name, isFilterStatus);
    }

    /**
     * 导出积分商品列表
     */
    @PreAuthorize("@ss.hasPermi('marketing:pointSku:export')")
    @Log(title = "积分商品", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(PageHelper<PointSku> pageHelper, Long cateId, String name, Boolean isFilterStatus) {
        pageHelper = pointSkuService.queryPointSkus(pageHelper, cateId, name, isFilterStatus);
        ExcelUtil<PointSku> util = new ExcelUtil<PointSku>(PointSku.class);
        return util.exportExcel(pageHelper.getList(), "积分商品数据");
    }

    /**
     * 获取积分商品详细信息
     */
    @PreAuthorize("@ss.hasPermi('marketing:pointSku:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(pointSkuService.queryPointSkuById(id));
    }

    /**
     * 新增积分商品
     */
    @PreAuthorize("@ss.hasPermi('marketing:pointSku:add')")
    @Log(title = "积分商品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PointSku pointSku) {
        return toAjax(pointSkuService.addPointSku(pointSku));
    }

    /**
     * 修改积分商品
     */
    @PreAuthorize("@ss.hasPermi('marketing:pointSku:edit')")
    @Log(title = "积分商品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PointSku pointSku) {
        return toAjax(pointSkuService.updatePointSku(pointSku));
    }

    /**
     * 删除积分商品
     */
    @PreAuthorize("@ss.hasPermi('marketing:pointSku:remove')")
    @Log(title = "积分商品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        for (int i = 0; i < ids.length; i++) {
            pointSkuService.deletePointSku(ids[i]);
        }
        return AjaxResult.success();
    }
}
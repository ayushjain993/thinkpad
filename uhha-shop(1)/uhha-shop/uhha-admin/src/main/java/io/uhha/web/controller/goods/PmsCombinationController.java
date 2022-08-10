package io.uhha.web.controller.goods;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.goods.domain.PmsCombination;
import io.uhha.goods.service.IPmsCombinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品组合Controller
 *
 * @author mj
 * @date 2020-07-24
 */
@Api(tags = "商品组合Controller")
@RestController
@RequestMapping("/goods/combination")
public class PmsCombinationController extends BaseController {
    @Autowired
    private IPmsCombinationService pmsCombinationService;

    /**
     * 查询商品组合列表
     */
    @PreAuthorize("@ss.hasPermi('goods:combination:list')")
    @GetMapping("/list")
    public TableDataInfo list(PmsCombination pmsCombination) {
        startPage();
        List<PmsCombination> list = pmsCombinationService.selectPmsCombinationList(pmsCombination);
        return getDataTable(list);
    }

    /**
     * 导出商品组合列表
     */
    @PreAuthorize("@ss.hasPermi('goods:combination:export')")
    @Log(title = "商品组合", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(PmsCombination pmsCombination) {
        List<PmsCombination> list = pmsCombinationService.selectPmsCombinationList(pmsCombination);
        ExcelUtil<PmsCombination> util = new ExcelUtil<PmsCombination>(PmsCombination.class);
        return util.exportExcel(list, "combination");
    }

    /**
     * 获取商品组合详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:combination:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(pmsCombinationService.selectPmsCombinationById(id));
    }

    /**
     * 新增商品组合
     */
    @PreAuthorize("@ss.hasPermi('goods:combination:add')")
    @Log(title = "商品组合", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PmsCombination pmsCombination) {
        return toAjax(pmsCombinationService.insertPmsCombination(pmsCombination));
    }

    /**
     * 修改商品组合
     */
    @PreAuthorize("@ss.hasPermi('goods:combination:edit')")
    @Log(title = "商品组合", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PmsCombination pmsCombination) {
        return toAjax(pmsCombinationService.updatePmsCombination(pmsCombination));
    }

    /**
     * 删除商品组合
     */
    @PreAuthorize("@ss.hasPermi('goods:combination:remove')")
    @Log(title = "商品组合", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(pmsCombinationService.deletePmsCombinationByIds(ids));
    }
}

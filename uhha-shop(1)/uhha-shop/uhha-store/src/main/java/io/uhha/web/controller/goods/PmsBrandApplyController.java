package io.uhha.web.controller.goods;

import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.goods.domain.PmsBrandApply;
import io.uhha.goods.service.IPmsBrandApplyService;
import io.uhha.web.utils.AdminLoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 品牌申请Controller
 *
 * @author mj
 * @date 2020-07-24
 */
@RestController
@RequestMapping("/goods/apply")
public class PmsBrandApplyController extends BaseController {
    @Autowired
    private IPmsBrandApplyService pmsBrandApplyService;

    /**
     * 查询品牌申请列表
     */
    @PreAuthorize("@ss.hasPermi('goods:apply:list')")
    @GetMapping("/list")
    public TableDataInfo list(PmsBrandApply pmsBrandApply) {
        startPage();
        List<PmsBrandApply> list = pmsBrandApplyService.selectPmsBrandApplyList(pmsBrandApply);
        return getDataTable(list);
    }

    /**
     * 导出品牌申请列表
     */
    @PreAuthorize("@ss.hasPermi('goods:apply:export')")
    @Log(title = "品牌申请", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(PmsBrandApply pmsBrandApply) {
        List<PmsBrandApply> list = pmsBrandApplyService.selectPmsBrandApplyList(pmsBrandApply);
        ExcelUtil<PmsBrandApply> util = new ExcelUtil<PmsBrandApply>(PmsBrandApply.class);
        return util.exportExcel(list, "apply");
    }

    /**
     * 获取品牌申请详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:apply:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(pmsBrandApplyService.selectPmsBrandApplyById(id));
    }

    /**
     * 新增品牌申请
     */
    @PreAuthorize("@ss.hasPermi('goods:apply:add')")
    @Log(title = "品牌申请", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PmsBrandApply pmsBrandApply) {
        Long storeId = AdminLoginUtils.getInstance().getStoreId();
        pmsBrandApply.setStoreId(storeId);
        return toAjax(pmsBrandApplyService.insertPmsBrandApply(pmsBrandApply));
    }

    /**
     * 修改品牌申请
     */
    @PreAuthorize("@ss.hasPermi('goods:apply:edit')")
    @Log(title = "品牌申请", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PmsBrandApply pmsBrandApply) {
        return toAjax(pmsBrandApplyService.updatePmsBrandApply(pmsBrandApply));
    }

    /**
     * 删除品牌申请
     */
    @PreAuthorize("@ss.hasPermi('goods:apply:remove')")
    @Log(title = "品牌申请", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(pmsBrandApplyService.deletePmsBrandApplyByIds(ids));
    }
}

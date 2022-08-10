package io.uhha.web.controller.goods;

import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.uhha.cms.bean.ArticleList;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.goods.domain.PmsBrand;
import io.uhha.goods.domain.PmsBrandApply;
import io.uhha.goods.service.IPmsBrandApplyService;
import io.uhha.util.BaseResponse;
import io.uhha.util.PageHelper;
import io.uhha.web.utils.AdminLoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 品牌申请Controller
 *
 * @author mj
 * @date 2020-07-24
 */
@Api(tags = "品牌申请Controller")
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

    /**
     * 查询品牌申请列表
     */
    @PreAuthorize("@ss.hasPermi('goods:apply:list')")
    @GetMapping("/byStatus/{status}")
    public TableDataInfo listByStatus(@PathVariable("status") String status) {
        startPage();
        List<PmsBrandApply> list = pmsBrandApplyService.selectPmsBrandApplyByStatus(status);
        return getDataTable(list);
    }

    /**
     * 查询品牌申请列表
     */
    @PreAuthorize("@ss.hasPermi('goods:apply:list')")
    @GetMapping("/toBeAudit")
    public BaseResponse queryBrandToBeAudit(PageHelper<PmsBrandApply> pageHelper, String name, String storeName){
        return BaseResponse.build(pmsBrandApplyService.queryBrandToBeAudit(pageHelper, name, storeName));
    }

    /**
     * 通过品牌审批
     */
    @PreAuthorize("@ss.hasPermi('goods:brand:edit')")
    @Log(title = "品牌", businessType = BusinessType.UPDATE)
    @PutMapping("/pass/{brandId}")
    public AjaxResult pass(@PathVariable("brandId") Long brandId) {
        return AjaxResult.success(pmsBrandApplyService.passBrandAudit(brandId));
    }

    /**
     * 拒绝品牌审批
     */
    @PreAuthorize("@ss.hasPermi('goods:brand:edit')")
    @Log(title = "品牌", businessType = BusinessType.UPDATE)
    @PutMapping("/refuse")
    public AjaxResult refuse(@RequestBody PmsBrandApply pmsBrandApply) {
        return AjaxResult.success(pmsBrandApplyService.refuseBrandAudit(pmsBrandApply));
    }

    /**
     * 批量通过品牌审批
     */
    @PreAuthorize("@ss.hasPermi('goods:brand:edit')")
    @Log(title = "品牌", businessType = BusinessType.UPDATE)
    @PutMapping("/batchpass")
    public AjaxResult batchpass(Long[] ids) {
        return AjaxResult.success(pmsBrandApplyService.batchPassBrandAudit(ids));
    }

    /**
     * 批量拒绝品牌审批
     */
    @PreAuthorize("@ss.hasPermi('goods:brand:edit')")
    @Log(title = "品牌", businessType = BusinessType.UPDATE)
    @PutMapping("/batchrefuse")
    public AjaxResult batchrefuse(Long[] ids, String reason) {
        return AjaxResult.success(pmsBrandApplyService.batchRefuseBrandAudit(ids, reason));
    }
}

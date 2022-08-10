package io.uhha.web.controller.goods;

import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.goods.domain.PmsSkuMarketing;
import io.uhha.goods.service.IPmsSkuMarketingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 单品和营销的关联Controller
 *
 * @author mj
 * @date 2020-07-24
 */
@RestController
@RequestMapping("/goods/marketing")
public class PmsSkuMarketingController extends BaseController {
    @Autowired
    private IPmsSkuMarketingService pmsSkuMarketingService;

    /**
     * 查询单品和营销的关联列表
     */
    @PreAuthorize("@ss.hasPermi('goods:marketing:list')")
    @GetMapping("/list")
    public TableDataInfo list(PmsSkuMarketing pmsSkuMarketing) {
        startPage();
        List<PmsSkuMarketing> list = pmsSkuMarketingService.selectPmsSkuMarketingList(pmsSkuMarketing);
        return getDataTable(list);
    }

    /**
     * 导出单品和营销的关联列表
     */
    @PreAuthorize("@ss.hasPermi('goods:marketing:export')")
    @Log(title = "单品和营销的关联", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(PmsSkuMarketing pmsSkuMarketing) {
        List<PmsSkuMarketing> list = pmsSkuMarketingService.selectPmsSkuMarketingList(pmsSkuMarketing);
        ExcelUtil<PmsSkuMarketing> util = new ExcelUtil<PmsSkuMarketing>(PmsSkuMarketing.class);
        return util.exportExcel(list, "marketing");
    }

    /**
     * 获取单品和营销的关联详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:marketing:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(pmsSkuMarketingService.selectPmsSkuMarketingById(id));
    }

    /**
     * 新增单品和营销的关联
     */
    @PreAuthorize("@ss.hasPermi('goods:marketing:add')")
    @Log(title = "单品和营销的关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PmsSkuMarketing pmsSkuMarketing) {
        return toAjax(pmsSkuMarketingService.insertPmsSkuMarketing(pmsSkuMarketing));
    }

    /**
     * 修改单品和营销的关联
     */
    @PreAuthorize("@ss.hasPermi('goods:marketing:edit')")
    @Log(title = "单品和营销的关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PmsSkuMarketing pmsSkuMarketing) {
        return toAjax(pmsSkuMarketingService.updatePmsSkuMarketing(pmsSkuMarketing));
    }

    /**
     * 删除单品和营销的关联
     */
    @PreAuthorize("@ss.hasPermi('goods:marketing:remove')")
    @Log(title = "单品和营销的关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(pmsSkuMarketingService.deletePmsSkuMarketingByIds(ids));
    }
}

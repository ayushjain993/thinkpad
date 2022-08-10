package io.uhha.web.controller.goods;

import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.goods.domain.PmsAttention;
import io.uhha.goods.service.IPmsAttentionService;
import io.uhha.web.utils.AdminLoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品关注Controller
 *
 * @author mj
 * @date 2020-07-24
 */
@RestController
@RequestMapping("/goods/attention")
public class PmsAttentionController extends BaseController {
    @Autowired
    private IPmsAttentionService pmsAttentionService;

    /**
     * 查询商品关注列表
     */
    @PreAuthorize("@ss.hasPermi('goods:attention:list')")
    @GetMapping("/list")
    public TableDataInfo list(PmsAttention pmsAttention) {
        pmsAttention.setStoreId(AdminLoginUtils.getInstance().getStoreId());
        startPage();
        List<PmsAttention> list = pmsAttentionService.selectPmsAttentionList(pmsAttention);
        return getDataTable(list);
    }

    /**
     * 导出商品关注列表
     */
    @PreAuthorize("@ss.hasPermi('goods:attention:export')")
    @Log(title = "商品关注", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(PmsAttention pmsAttention) {
        pmsAttention.setStoreId(AdminLoginUtils.getInstance().getStoreId());
        List<PmsAttention> list = pmsAttentionService.selectPmsAttentionList(pmsAttention);
        ExcelUtil<PmsAttention> util = new ExcelUtil<PmsAttention>(PmsAttention.class);
        return util.exportExcel(list, "attention");
    }

    /**
     * 获取商品关注详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:attention:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(pmsAttentionService.selectPmsAttentionById(id));
    }

    /**
     * 新增商品关注
     */
    @PreAuthorize("@ss.hasPermi('goods:attention:add')")
    @Log(title = "商品关注", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PmsAttention pmsAttention) {
        return toAjax(pmsAttentionService.insertPmsAttention(pmsAttention));
    }

    /**
     * 修改商品关注
     */
    @PreAuthorize("@ss.hasPermi('goods:attention:edit')")
    @Log(title = "商品关注", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PmsAttention pmsAttention) {
        return toAjax(pmsAttentionService.updatePmsAttention(pmsAttention));
    }

    /**
     * 删除商品关注
     */
    @PreAuthorize("@ss.hasPermi('goods:attention:remove')")
    @Log(title = "商品关注", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(pmsAttentionService.deletePmsAttentionByIds(ids));
    }
}

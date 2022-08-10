package io.uhha.web.controller.goods;

import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.goods.domain.PmsGoodsServiceSupport;
import io.uhha.goods.service.IPmsGoodsServiceSupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品和服务支持的关联Controller
 *
 * @author mj
 * @date 2020-07-24
 */
@RestController
@RequestMapping("/goods/goodsSupport")
public class PmsGoodsServiceSupportController extends BaseController {
    @Autowired
    private IPmsGoodsServiceSupportService pmsGoodsServiceSupportService;

    /**
     * 查询商品和服务支持的关联列表
     */
    @PreAuthorize("@ss.hasPermi('goods:support:list')")
    @GetMapping("/list")
    public TableDataInfo list(PmsGoodsServiceSupport pmsGoodsServiceSupport) {
        startPage();
        List<PmsGoodsServiceSupport> list = pmsGoodsServiceSupportService.selectPmsGoodsServiceSupportList(pmsGoodsServiceSupport);
        return getDataTable(list);
    }

    /**
     * 导出商品和服务支持的关联列表
     */
    @PreAuthorize("@ss.hasPermi('goods:support:export')")
    @Log(title = "商品和服务支持的关联", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(PmsGoodsServiceSupport pmsGoodsServiceSupport) {
        List<PmsGoodsServiceSupport> list = pmsGoodsServiceSupportService.selectPmsGoodsServiceSupportList(pmsGoodsServiceSupport);
        ExcelUtil<PmsGoodsServiceSupport> util = new ExcelUtil<PmsGoodsServiceSupport>(PmsGoodsServiceSupport.class);
        return util.exportExcel(list, "support");
    }

    /**
     * 获取商品和服务支持的关联详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:support:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(pmsGoodsServiceSupportService.selectPmsGoodsServiceSupportById(id));
    }

    /**
     * 新增商品和服务支持的关联
     */
    @PreAuthorize("@ss.hasPermi('goods:support:add')")
    @Log(title = "商品和服务支持的关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PmsGoodsServiceSupport pmsGoodsServiceSupport) {
        return toAjax(pmsGoodsServiceSupportService.insertPmsGoodsServiceSupport(pmsGoodsServiceSupport));
    }

    /**
     * 修改商品和服务支持的关联
     */
    @PreAuthorize("@ss.hasPermi('goods:support:edit')")
    @Log(title = "商品和服务支持的关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PmsGoodsServiceSupport pmsGoodsServiceSupport) {
        return toAjax(pmsGoodsServiceSupportService.updatePmsGoodsServiceSupport(pmsGoodsServiceSupport));
    }

    /**
     * 删除商品和服务支持的关联
     */
    @PreAuthorize("@ss.hasPermi('goods:support:remove')")
    @Log(title = "商品和服务支持的关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(pmsGoodsServiceSupportService.deletePmsGoodsServiceSupportByIds(ids));
    }
}

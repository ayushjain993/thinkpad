package io.uhha.web.controller.goods;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.goods.domain.PmsSkuMemberPrice;
import io.uhha.goods.service.IPmsSkuMemberPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 单品的会员价Controller
 *
 * @author mj
 * @date 2020-07-24
 */
@Api(tags = "单品的会员价Controller")
@RestController
@RequestMapping("/goods/price")
public class PmsSkuMemberPriceController extends BaseController {
    @Autowired
    private IPmsSkuMemberPriceService pmsSkuMemberPriceService;

    /**
     * 查询单品的会员价列表
     */
    @PreAuthorize("@ss.hasPermi('goods:price:list')")
    @GetMapping("/list")
    public TableDataInfo list(PmsSkuMemberPrice pmsSkuMemberPrice) {
        startPage();
        List<PmsSkuMemberPrice> list = pmsSkuMemberPriceService.selectPmsSkuMemberPriceList(pmsSkuMemberPrice);
        return getDataTable(list);
    }

    /**
     * 导出单品的会员价列表
     */
    @PreAuthorize("@ss.hasPermi('goods:price:export')")
    @Log(title = "单品的会员价", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(PmsSkuMemberPrice pmsSkuMemberPrice) {
        List<PmsSkuMemberPrice> list = pmsSkuMemberPriceService.selectPmsSkuMemberPriceList(pmsSkuMemberPrice);
        ExcelUtil<PmsSkuMemberPrice> util = new ExcelUtil<PmsSkuMemberPrice>(PmsSkuMemberPrice.class);
        return util.exportExcel(list, "price");
    }

    /**
     * 获取单品的会员价详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:price:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(pmsSkuMemberPriceService.selectPmsSkuMemberPriceById(id));
    }

    /**
     * 新增单品的会员价
     */
    @PreAuthorize("@ss.hasPermi('goods:price:add')")
    @Log(title = "单品的会员价", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PmsSkuMemberPrice pmsSkuMemberPrice) {
        return toAjax(pmsSkuMemberPriceService.insertPmsSkuMemberPrice(pmsSkuMemberPrice));
    }

    /**
     * 修改单品的会员价
     */
    @PreAuthorize("@ss.hasPermi('goods:price:edit')")
    @Log(title = "单品的会员价", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PmsSkuMemberPrice pmsSkuMemberPrice) {
        return toAjax(pmsSkuMemberPriceService.updatePmsSkuMemberPrice(pmsSkuMemberPrice));
    }

    /**
     * 删除单品的会员价
     */
    @PreAuthorize("@ss.hasPermi('goods:price:remove')")
    @Log(title = "单品的会员价", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(pmsSkuMemberPriceService.deletePmsSkuMemberPriceByIds(ids));
    }
}

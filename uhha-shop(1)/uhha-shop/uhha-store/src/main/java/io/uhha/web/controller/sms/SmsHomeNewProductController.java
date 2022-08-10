package io.uhha.web.controller.sms;

import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.enums.StatusEnum;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.goods.service.IPmsGoodsService;
import io.uhha.sms.domain.SmsHomeNewProduct;
import io.uhha.sms.service.ISmsHomeNewProductService;
import io.uhha.web.utils.AdminLoginUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 新鲜好物Controller
 *
 * @author ruoyi
 * @date 2020-08-06
 */
@RestController
@RequestMapping("/sms/SmsHomeNewProduct")
public class SmsHomeNewProductController extends BaseController {
    @Autowired
    private ISmsHomeNewProductService smsHomeNewProductService;
    @Autowired
    private IPmsGoodsService goodsService;
    /**
     * 查询新鲜好物列表
     */
    @PreAuthorize("@ss.hasPermi('sms:SmsHomeNewProduct:list')")
    @GetMapping("/list")
    public TableDataInfo list(SmsHomeNewProduct smsHomeNewProduct) {
        startPage();
        smsHomeNewProduct.setStoreId(AdminLoginUtils.getInstance().getStoreId());
        List<SmsHomeNewProduct> list = smsHomeNewProductService.selectSmsHomeNewProductList(smsHomeNewProduct);
        return getDataTable(list);
    }

    /**
     * 导出新鲜好物列表
     */
    @PreAuthorize("@ss.hasPermi('sms:SmsHomeNewProduct:export')")
    @Log(title = "新鲜好物", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SmsHomeNewProduct smsHomeNewProduct) {
        smsHomeNewProduct.setStoreId(AdminLoginUtils.getInstance().getStoreId());
        List<SmsHomeNewProduct> list = smsHomeNewProductService.selectSmsHomeNewProductList(smsHomeNewProduct);
        ExcelUtil<SmsHomeNewProduct> util = new ExcelUtil<SmsHomeNewProduct>(SmsHomeNewProduct.class);
        return util.exportExcel(list, "SmsHomeNewProduct");
    }

    /**
     * 获取新鲜好物详细信息
     */
    @PreAuthorize("@ss.hasPermi('sms:SmsHomeNewProduct:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(smsHomeNewProductService.selectSmsHomeNewProductById(id));
    }

    /**
     * 新增新鲜好物
     */
    @PreAuthorize("@ss.hasPermi('sms:SmsHomeNewProduct:add')")
    @Log(title = "新鲜好物", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult create(@RequestBody List<SmsHomeNewProduct> homeNewProducts) {
        int added = 0;

        for (SmsHomeNewProduct smsNewProduct : homeNewProducts) {
            smsNewProduct.setRecommendStatus(StatusEnum.YesNoType.YES.code());
            smsNewProduct.setSort(0);
            smsNewProduct.setStoreId(AdminLoginUtils.getInstance().getStoreId());

            SmsHomeNewProduct query = SmsHomeNewProduct.builder()
                    .recommendStatus(StatusEnum.YesNoType.YES.code())
                    .storeId(AdminLoginUtils.getInstance().getStoreId())
                    .productId(smsNewProduct.getProductId())
                    .build();

            List<SmsHomeNewProduct> list = smsHomeNewProductService.selectSmsHomeNewProductList(query);
            if (CollectionUtils.isEmpty(list)){
                added = added +smsHomeNewProductService.insertSmsHomeNewProduct(smsNewProduct);
            }

        }
        return toAjax(added);
    }

    /**
     * 修改新鲜好物
     */
    @PreAuthorize("@ss.hasPermi('sms:SmsHomeNewProduct:edit')")
    @Log(title = "新鲜好物", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SmsHomeNewProduct smsHomeNewProduct) {
        smsHomeNewProduct.setStoreId(AdminLoginUtils.getInstance().getStoreId());
        return toAjax(smsHomeNewProductService.updateSmsHomeNewProduct(smsHomeNewProduct));
    }

    /**
     * 删除新鲜好物
     */
    @PreAuthorize("@ss.hasPermi('sms:SmsHomeNewProduct:remove')")
    @Log(title = "新鲜好物", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(smsHomeNewProductService.deleteSmsHomeNewProductByIds(ids, AdminLoginUtils.getInstance().getStoreId()));
    }
    @ApiOperation("批量修改推荐状态")
    @RequestMapping(value = "/update/recommendStatus", method = RequestMethod.GET)
    @ResponseBody
    public Object updateRecommendStatus(@RequestParam("ids") Long[] ids, @RequestParam("recommendStatus") Integer recommendStatus) {
        int count = smsHomeNewProductService.updateRecommendStatus(ids, recommendStatus,AdminLoginUtils.getInstance().getStoreId());
        return toAjax(count);
    }

    @ApiOperation("修改推荐排序")
    @RequestMapping(value = "/update/sort/{id}/{sort}", method = RequestMethod.POST)
    @ResponseBody
    public Object updateSort(@PathVariable Long id, @PathVariable Integer sort) {
        int count = smsHomeNewProductService.updateSort(id, sort,AdminLoginUtils.getInstance().getStoreId());
        return toAjax(count);
    }
}

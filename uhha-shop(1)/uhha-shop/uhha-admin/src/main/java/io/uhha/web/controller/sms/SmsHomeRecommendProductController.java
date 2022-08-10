package io.uhha.web.controller.sms;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.enums.StatusEnum;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.goods.service.IPmsGoodsService;
import io.uhha.sms.domain.SmsHomeRecommendProduct;
import io.uhha.sms.service.ISmsHomeRecommendProductService;
import io.uhha.util.CommonConstant;
import io.uhha.web.utils.AdminLoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 人气推荐商品Controller
 *
 * @author ruoyi
 * @date 2020-08-06
 */
@Api(tags = "人气推荐商品Controller")
@RestController
@RequestMapping("/sms/SmsHomeRecommendProduct")
public class SmsHomeRecommendProductController extends BaseController {
    @Autowired
    private ISmsHomeRecommendProductService smsHomeRecommendProductService;

    /**
     * 查询人气推荐商品列表
     */
    @PreAuthorize("@ss.hasPermi('sms:SmsHomeRecommendProduct:list')")
    @GetMapping("/list")
    public TableDataInfo list(SmsHomeRecommendProduct recommendProduct) {
        startPage();
        recommendProduct.setStoreId(CommonConstant.ADMIN_STOREID);
        List<SmsHomeRecommendProduct> list = smsHomeRecommendProductService.selectSmsHomeRecommendProductList(recommendProduct);
        return getDataTable(list);
    }

    /**
     * 导出人气推荐商品列表
     */
    @PreAuthorize("@ss.hasPermi('sms:SmsHomeRecommendProduct:export')")
    @Log(title = "人气推荐商品", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SmsHomeRecommendProduct smsHomeRecommendProduct) {
        smsHomeRecommendProduct.setStoreId(CommonConstant.ADMIN_STOREID);
        List<SmsHomeRecommendProduct> list = smsHomeRecommendProductService.selectSmsHomeRecommendProductList(smsHomeRecommendProduct);
        ExcelUtil<SmsHomeRecommendProduct> util = new ExcelUtil<SmsHomeRecommendProduct>(SmsHomeRecommendProduct.class);
        return util.exportExcel(list, "SmsHomeRecommendProduct");
    }

    /**
     * 获取人气推荐商品详细信息
     */
    @PreAuthorize("@ss.hasPermi('sms:SmsHomeRecommendProduct:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(smsHomeRecommendProductService.selectSmsHomeRecommendProductById(id));
    }

    /**
     * 新增人气推荐商品
     */
    @PreAuthorize("@ss.hasPermi('sms:SmsHomeRecommendProduct:add')")
    @Log(title = "人气推荐商品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult create(@RequestBody List<SmsHomeRecommendProduct> homeRecommendProducts) {
        int added = 0;
        for (SmsHomeRecommendProduct recommendProduct : homeRecommendProducts) {
            recommendProduct.setRecommendStatus(StatusEnum.YesNoType.YES.code());
            recommendProduct.setSort(0);
            recommendProduct.setStoreId(CommonConstant.ADMIN_STOREID);

            SmsHomeRecommendProduct query = SmsHomeRecommendProduct.builder()
                    .productId(recommendProduct.getProductId())
                    .storeId(CommonConstant.ADMIN_STOREID)
                    .recommendStatus(StatusEnum.YesNoType.YES.code())
                    .build();
            List<SmsHomeRecommendProduct> list = smsHomeRecommendProductService.selectSmsHomeRecommendProductList(query);
            if (CollectionUtils.isEmpty(list)){
                added = added+smsHomeRecommendProductService.insertSmsHomeRecommendProduct(recommendProduct);
            }
        }
        return toAjax(added);
    }

    /**
     * 修改人气推荐商品
     */
    @PreAuthorize("@ss.hasPermi('sms:SmsHomeRecommendProduct:edit')")
    @Log(title = "人气推荐商品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SmsHomeRecommendProduct smsHomeRecommendProduct) {
        return toAjax(smsHomeRecommendProductService.updateSmsHomeRecommendProduct(smsHomeRecommendProduct));
    }

    /**
     * 删除人气推荐商品
     */
    @PreAuthorize("@ss.hasPermi('sms:SmsHomeRecommendProduct:remove')")
    @Log(title = "人气推荐商品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(smsHomeRecommendProductService.deleteSmsHomeRecommendProductByIds(ids, CommonConstant.ADMIN_STOREID));
    }
    @ApiOperation("修改推荐排序")
    @RequestMapping(value = "/update/sort/{id}/{sort}", method = RequestMethod.POST)
    @ResponseBody
    public Object updateSort(@PathVariable Long id, @PathVariable Integer sort) {
        int count = smsHomeRecommendProductService.updateSort(id, sort, CommonConstant.ADMIN_STOREID);
        return toAjax(count);
    }

    @ApiOperation("批量修改推荐状态")
    @RequestMapping(value = "/update/recommendStatus", method = RequestMethod.POST)
    @ResponseBody
    public Object updateRecommendStatus(@RequestParam("ids") Long[] ids, @RequestParam("recommendStatus") Integer recommendStatus) {
        int count = smsHomeRecommendProductService.updateRecommendStatus(ids, recommendStatus, CommonConstant.ADMIN_STOREID);
        return toAjax(count);
    }
}

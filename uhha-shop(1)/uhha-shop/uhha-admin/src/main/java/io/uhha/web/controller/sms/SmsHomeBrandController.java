package io.uhha.web.controller.sms;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.enums.StatusEnum;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.sms.domain.SmsHomeBrand;
import io.uhha.sms.service.ISmsHomeBrandService;
import io.uhha.util.CommonConstant;
import io.swagger.annotations.ApiOperation;
import io.uhha.web.utils.AdminLoginUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 首页推荐品牌Controller
 * 使用redis缓存
 *
 * @author ruoyi
 * @date 2020-08-06
 */
@Api(tags = "首页推荐品牌Controller")
@RestController
@RequestMapping("/sms/SmsHomeBrand")
public class SmsHomeBrandController extends BaseController {
    @Autowired
    private ISmsHomeBrandService smsHomeBrandService;

    /**
     * 查询首页推荐品牌列表
     */
    @PreAuthorize("@ss.hasPermi('sms:SmsHomeBrand:list')")
    @GetMapping("/list")
    public TableDataInfo list(SmsHomeBrand smsHomeBrand) {
        smsHomeBrand.setStoreId(CommonConstant.ADMIN_STOREID);
        startPage();
        List<SmsHomeBrand> list = smsHomeBrandService.selectSmsHomeBrandList(smsHomeBrand);
        return getDataTable(list);
    }

    /**
     * 导出首页推荐品牌列表
     */
    @PreAuthorize("@ss.hasPermi('sms:SmsHomeBrand:export')")
    @Log(title = "首页推荐品牌", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SmsHomeBrand smsHomeBrand) {
        smsHomeBrand.setStoreId(CommonConstant.ADMIN_STOREID);
        List<SmsHomeBrand> list = smsHomeBrandService.selectSmsHomeBrandList(smsHomeBrand);
        ExcelUtil<SmsHomeBrand> util = new ExcelUtil<SmsHomeBrand>(SmsHomeBrand.class);
        return util.exportExcel(list, "SmsHomeBrand");
    }

    /**
     * 获取首页推荐品牌详细信息
     */
    @PreAuthorize("@ss.hasPermi('sms:SmsHomeBrand:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(smsHomeBrandService.selectSmsHomeBrandById(id));
    }

    @PreAuthorize("@ss.hasPermi('sms:SmsHomeBrand:add')")
    @Log(title = "首页推荐品牌", businessType = BusinessType.INSERT)
    @PostMapping
    public Object create(@RequestBody List<SmsHomeBrand> homeBrandList) {
        int added = 0;

        for(SmsHomeBrand smsHomeBrand: homeBrandList){
            smsHomeBrand.setStoreId(CommonConstant.ADMIN_STOREID);
            smsHomeBrand.setRecommendStatus(StatusEnum.YesNoType.YES.code());
            smsHomeBrand.setSort(0);

            SmsHomeBrand query = SmsHomeBrand.builder()
                    .brandId(smsHomeBrand.getBrandId())
                    .storeId(smsHomeBrand.getStoreId())
                    .recommendStatus(StatusEnum.YesNoType.YES.code())
                    .build();
            List<SmsHomeBrand> result = smsHomeBrandService.selectSmsHomeBrandList(query);
            if(CollectionUtils.isEmpty(result)){
                added = added + smsHomeBrandService.insertSmsHomeBrand(smsHomeBrand);
            }
        }
        return  toAjax(added);
    }



    /**
     * 修改首页推荐品牌
     */
    @PreAuthorize("@ss.hasPermi('sms:SmsHomeBrand:edit')")
    @Log(title = "首页推荐品牌", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SmsHomeBrand smsHomeBrand) {
        smsHomeBrand.setStoreId(CommonConstant.ADMIN_STOREID);
        return toAjax(smsHomeBrandService.updateSmsHomeBrand(smsHomeBrand));
    }

    /**
     * 删除首页推荐品牌
     */
    @PreAuthorize("@ss.hasPermi('sms:SmsHomeBrand:remove')")
    @Log(title = "首页推荐品牌", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(smsHomeBrandService.deleteSmsHomeBrandByIds(ids));
    }


    @ApiOperation("修改品牌排序")
    @RequestMapping(value = "/update/sort/{id}/{sort}", method = RequestMethod.POST)
    @ResponseBody
    public Object updateSort(@PathVariable Long id, @PathVariable Integer sort) {
        int count = smsHomeBrandService.updateSort(id, sort);
        return toAjax(count);

    }


    @ApiOperation("批量修改推荐状态")
    @RequestMapping(value = "/update/recommendStatus", method = RequestMethod.GET)
    @ResponseBody
    public Object updateRecommendStatus(@RequestParam("ids") List<Long> ids, @RequestParam Integer recommendStatus) {
        int count = smsHomeBrandService.updateRecommendStatus(ids, recommendStatus);
        return toAjax(count);
    }
}

package io.uhha.web.controller.sms;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.sms.domain.SmsHomeAdvertise;
import io.uhha.sms.service.ISmsHomeAdvertiseService;
import io.uhha.util.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 首页轮播广告Controller
 *
 * @author ruoyi
 * @date 2020-08-06
 */
@Api(tags = "首页轮播广告Controller")
@RestController
@RequestMapping("/sms/SmsHomeAdvertise")
public class SmsHomeAdvertiseController extends BaseController {
    @Autowired
    private ISmsHomeAdvertiseService smsHomeAdvertiseService;

    /**
     * 查询首页轮播广告列表
     */
    @PreAuthorize("@ss.hasPermi('sms:SmsHomeAdvertise:list')")
    @GetMapping("/list")
    public TableDataInfo list(SmsHomeAdvertise smsHomeAdvertise) {
        smsHomeAdvertise.setStoreId(CommonConstant.ADMIN_STOREID);
        startPage();
        List<SmsHomeAdvertise> list = smsHomeAdvertiseService.selectSmsHomeAdvertiseList(smsHomeAdvertise);
        return getDataTable(list);
    }

    /**
     * 导出首页轮播广告列表
     */
    @PreAuthorize("@ss.hasPermi('sms:SmsHomeAdvertise:export')")
    @Log(title = "首页轮播广告", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SmsHomeAdvertise smsHomeAdvertise) {
        smsHomeAdvertise.setStoreId(CommonConstant.ADMIN_STOREID);
        List<SmsHomeAdvertise> list = smsHomeAdvertiseService.selectSmsHomeAdvertiseList(smsHomeAdvertise);
        ExcelUtil<SmsHomeAdvertise> util = new ExcelUtil<SmsHomeAdvertise>(SmsHomeAdvertise.class);
        return util.exportExcel(list, "SmsHomeAdvertise");
    }

    /**
     * 获取首页轮播广告详细信息
     */
    @PreAuthorize("@ss.hasPermi('sms:SmsHomeAdvertise:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(smsHomeAdvertiseService.selectSmsHomeAdvertiseById(id));
    }

    /**
     * 新增首页轮播广告
     */
    @PreAuthorize("@ss.hasPermi('sms:SmsHomeAdvertise:add')")
    @Log(title = "首页轮播广告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SmsHomeAdvertise smsHomeAdvertise) {
        smsHomeAdvertise.setStoreId(CommonConstant.ADMIN_STOREID);
        return toAjax(smsHomeAdvertiseService.insertSmsHomeAdvertise(smsHomeAdvertise));
    }

    /**
     * 修改首页轮播广告
     */
    @PreAuthorize("@ss.hasPermi('sms:SmsHomeAdvertise:edit')")
    @Log(title = "首页轮播广告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SmsHomeAdvertise smsHomeAdvertise) {
        smsHomeAdvertise.setStoreId(CommonConstant.ADMIN_STOREID);
        return toAjax(smsHomeAdvertiseService.updateSmsHomeAdvertise(smsHomeAdvertise));
    }

    /**
     * 删除首页轮播广告
     */
    @PreAuthorize("@ss.hasPermi('sms:SmsHomeAdvertise:remove')")
    @Log(title = "首页轮播广告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(smsHomeAdvertiseService.deleteSmsHomeAdvertiseByIds(ids));
    }
}

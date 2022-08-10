package io.uhha.web.controller.setting;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.setting.bean.OssSetCommon;
import io.uhha.setting.bean.SkuSourceCommon;
import io.uhha.setting.domain.LsOssSetting;
import io.uhha.setting.domain.LsSkuSourceSetting;
import io.uhha.setting.service.ILsOssSettingService;
import io.uhha.setting.service.ILsSkuSourceSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Sku来源设置Controller
 *
 * @author uhha
 * @date 2021-07-28
 */
@Api(tags = "Sku来源设置Controller")
@RestController
@RequestMapping("/setting/lsSkuSourceSetting")
public class LsSkuSourceSettingController extends BaseController {
    @Autowired
    private ILsSkuSourceSettingService lsSkuSourceSettingService;

    /**
     * 查询Sku第三方来源列表
     */
    @PreAuthorize("@ss.hasPermi('setting:skuSourceSetting:list')")
    @GetMapping("/list")
    public TableDataInfo list(LsSkuSourceSetting skuSourceSetting) {
        startPage();
        List<LsSkuSourceSetting> list = lsSkuSourceSettingService.selectLsSkuSourceSettingList(skuSourceSetting);
        return getDataTable(list);
    }

    /**
     * 查询Sku第三方来源
     *
     * @return Sku第三方来源信息
     */
    @GetMapping("/skuSourceSet")
    @ApiOperation(value = "查询Sku第三方来源", notes = "查询Sku第三方来源（需要认证）")
    @PreAuthorize("@ss.hasPermi('setting:skuSourceSetting:list')")
    public SkuSourceCommon querySkuSourceSet() {
        return lsSkuSourceSettingService.querySkuSourceSet();
    }

    /**
     * 修改Sku第三方来源
     *
     * @param skuSourceCommon 实体类参数
     * @param codeType     Sku第三方来源类型 1 Oss宝 2 微信 3 银联
     * @return 成功>=1 否则失败 -1 Sku第三方来源类型不存在
     */
    @PostMapping("/skuSourceSet/{codeType}")
    @ApiOperation(value = "修改Sku第三方来源", notes = "修改Sku第三方来源（需要认证）")
    @PreAuthorize("@ss.hasPermi('setting:skuSourceSetting:add')")
    public int editOssSet(@RequestBody SkuSourceCommon skuSourceCommon, @PathVariable String codeType) {
        return lsSkuSourceSettingService.editSkuSourceSet(skuSourceCommon, codeType);
    }

    /**
     * 导出Sku第三方来源列表
     */
    @PreAuthorize("@ss.hasPermi('setting:skuSourceSetting:export')")
    @Log(title = "Sku第三方来源", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(LsSkuSourceSetting lsOssSetting) {
        List<LsSkuSourceSetting> list = lsSkuSourceSettingService.selectLsSkuSourceSettingList(lsOssSetting);
        ExcelUtil<LsSkuSourceSetting> util = new ExcelUtil<LsSkuSourceSetting>(LsSkuSourceSetting.class);
        return util.exportExcel(list, "LsOssSetting");
    }

    /**
     * 获取Sku第三方来源详细信息
     */
    @PreAuthorize("@ss.hasPermi('setting:skuSourceSetting:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(lsSkuSourceSettingService.selectLsSkuSourceSettingById(id));
    }

    /**
     * 新增Sku第三方来源
     */
    @PreAuthorize("@ss.hasPermi('setting:skuSourceSetting:add')")
    @Log(title = "Sku第三方来源", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LsSkuSourceSetting lsOssSetting) {
        return toAjax(lsSkuSourceSettingService.insertLsSkuSourceSetting(lsOssSetting));
    }

    /**
     * 修改Sku第三方来源
     */
    @PreAuthorize("@ss.hasPermi('setting:skuSourceSetting:edit')")
    @Log(title = "Sku第三方来源", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LsSkuSourceSetting lsOssSetting) {
        return toAjax(lsSkuSourceSettingService.updateLsSkuSourceSetting(lsOssSetting));
    }

    /**
     * 删除Sku第三方来源
     */
    @PreAuthorize("@ss.hasPermi('setting:skuSourceSetting:remove')")
    @Log(title = "Sku第三方来源", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(lsSkuSourceSettingService.deleteLsSkuSourceSettingByIds(ids));
    }
}

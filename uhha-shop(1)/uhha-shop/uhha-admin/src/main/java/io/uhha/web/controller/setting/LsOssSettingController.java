package io.uhha.web.controller.setting;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.setting.bean.OssSetCommon;
import io.uhha.setting.bean.PaySetCommon;
import io.uhha.setting.domain.LsOssSetting;
import io.uhha.setting.service.ILsOssSettingService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Oss设置Controller
 *
 * @author mj
 * @date 2020-07-28
 */
@Api(tags = "Oss设置Controller")
@RestController
@RequestMapping("/setting/LsOssSetting")
public class LsOssSettingController extends BaseController {
    @Autowired
    private ILsOssSettingService lsOssSettingService;

    /**
     * 查询Oss设置列表
     */
    @PreAuthorize("@ss.hasPermi('setting:LsOssSetting:list')")
    @GetMapping("/list")
    public TableDataInfo list(LsOssSetting lsOssSetting) {
        startPage();
        List<LsOssSetting> list = lsOssSettingService.selectLsOssSettingList(lsOssSetting);
        return getDataTable(list);
    }

    /**
     * 查询Oss设置
     *
     * @return Oss设置信息
     */
    @GetMapping("/ossSet")
    @ApiOperation(value = "查询Oss设置", notes = "查询Oss设置（需要认证）")
    @PreAuthorize("@ss.hasPermi('setting:LsOssSetting:list')")
    public OssSetCommon queryOssSet() {
        return lsOssSettingService.queryOssSet();
    }

    /**
     * 修改Oss设置
     *
     * @param ossSetCommon 实体类参数
     * @param codeType     Oss设置类型 1 Oss宝 2 微信 3 银联
     * @return 成功>=1 否则失败 -1 Oss设置类型不存在
     */
    @PostMapping("/ossSet/{codeType}")
    @ApiOperation(value = "修改Oss设置", notes = "修改Oss设置（需要认证）")
    @PreAuthorize("@ss.hasPermi('setting:LsOssSetting:add')")
    public int editOssSet(@RequestBody OssSetCommon ossSetCommon, @PathVariable String codeType) {
        return lsOssSettingService.editOssSet(ossSetCommon, codeType);
    }

    /**
     * 导出Oss设置列表
     */
    @PreAuthorize("@ss.hasPermi('setting:LsOssSetting:export')")
    @Log(title = "Oss设置", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(LsOssSetting lsOssSetting) {
        List<LsOssSetting> list = lsOssSettingService.selectLsOssSettingList(lsOssSetting);
        ExcelUtil<LsOssSetting> util = new ExcelUtil<LsOssSetting>(LsOssSetting.class);
        return util.exportExcel(list, "LsOssSetting");
    }

    /**
     * 获取Oss设置详细信息
     */
    @PreAuthorize("@ss.hasPermi('setting:LsOssSetting:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(lsOssSettingService.selectLsOssSettingById(id));
    }

    /**
     * 新增Oss设置
     */
    @PreAuthorize("@ss.hasPermi('setting:LsOssSetting:add')")
    @Log(title = "Oss设置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LsOssSetting lsOssSetting) {
        return toAjax(lsOssSettingService.insertLsOssSetting(lsOssSetting));
    }

    /**
     * 修改Oss设置
     */
    @PreAuthorize("@ss.hasPermi('setting:LsOssSetting:edit')")
    @Log(title = "Oss设置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LsOssSetting lsOssSetting) {
        return toAjax(lsOssSettingService.updateLsOssSetting(lsOssSetting));
    }

    /**
     * 删除Oss设置
     */
    @PreAuthorize("@ss.hasPermi('setting:LsOssSetting:remove')")
    @Log(title = "Oss设置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(lsOssSettingService.deleteLsOssSettingByIds(ids));
    }
}

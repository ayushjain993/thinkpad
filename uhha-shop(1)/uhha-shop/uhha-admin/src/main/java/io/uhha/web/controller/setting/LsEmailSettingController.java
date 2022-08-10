package io.uhha.web.controller.setting;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.setting.domain.LsEmailSetting;
import io.uhha.setting.service.ILsEmailSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 邮箱设置Controller
 *
 * @author mj
 * @date 2020-07-28
 */
@Api(tags = "邮箱设置Controller")
@RestController
@RequestMapping("/setting/LsEmailSetting")
public class LsEmailSettingController extends BaseController {
    @Autowired
    private ILsEmailSettingService lsEmailSettingService;

    /**
     * 查询邮箱设置列表
     */
    @PreAuthorize("@ss.hasPermi('setting:LsEmailSetting:list')")
    @GetMapping("/list")
    public TableDataInfo list(LsEmailSetting lsEmailSetting) {
        startPage();
        List<LsEmailSetting> list = lsEmailSettingService.selectLsEmailSettingList(lsEmailSetting);
        return getDataTable(list);
    }

    /**
     * 导出邮箱设置列表
     */
    @PreAuthorize("@ss.hasPermi('setting:LsEmailSetting:export')")
    @Log(title = "邮箱设置", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(LsEmailSetting lsEmailSetting) {
        List<LsEmailSetting> list = lsEmailSettingService.selectLsEmailSettingList(lsEmailSetting);
        ExcelUtil<LsEmailSetting> util = new ExcelUtil<LsEmailSetting>(LsEmailSetting.class);
        return util.exportExcel(list, "LsEmailSetting");
    }

    /**
     * 获取邮箱设置详细信息
     */
    @PreAuthorize("@ss.hasPermi('setting:LsEmailSetting:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(lsEmailSettingService.selectLsEmailSettingById(id));
    }

    /**
     * 新增邮箱设置
     */
    @PreAuthorize("@ss.hasPermi('setting:LsEmailSetting:add')")
    @Log(title = "邮箱设置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LsEmailSetting lsEmailSetting) {
        return toAjax(lsEmailSettingService.insertLsEmailSetting(lsEmailSetting));
    }

    /**
     * 修改邮箱设置
     */
    @PreAuthorize("@ss.hasPermi('setting:LsEmailSetting:edit')")
    @Log(title = "邮箱设置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LsEmailSetting lsEmailSetting) {
        return toAjax(lsEmailSettingService.updateLsEmailSetting(lsEmailSetting));
    }

}

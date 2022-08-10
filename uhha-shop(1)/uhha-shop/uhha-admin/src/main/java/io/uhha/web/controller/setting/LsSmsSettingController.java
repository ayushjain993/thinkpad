package io.uhha.web.controller.setting;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.setting.domain.LsSmsSetting;
import io.uhha.setting.service.ILsSmsSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 短信接口设置Controller
 *
 * @author mj
 * @date 2020-07-28
 */
@Api(tags = "短信接口设置Controller")
@RestController
@RequestMapping("/setting/LsSmsSetting")
public class LsSmsSettingController extends BaseController {
    @Autowired
    private ILsSmsSettingService lsSmsSettingService;

    /**
     * 查询短信接口设置列表
     */
    @PreAuthorize("@ss.hasPermi('setting:LsSmsSetting:list')")
    @GetMapping("/list")
    public TableDataInfo list(LsSmsSetting lsSmsSetting) {
        startPage();
        List<LsSmsSetting> list = lsSmsSettingService.selectLsSmsSettingList(lsSmsSetting);
        return getDataTable(list);
    }

    /**
     * 导出短信接口设置列表
     */
    @PreAuthorize("@ss.hasPermi('setting:LsSmsSetting:export')")
    @Log(title = "短信接口设置", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(LsSmsSetting lsSmsSetting) {
        List<LsSmsSetting> list = lsSmsSettingService.selectLsSmsSettingList(lsSmsSetting);
        ExcelUtil<LsSmsSetting> util = new ExcelUtil<LsSmsSetting>(LsSmsSetting.class);
        return util.exportExcel(list, "LsSmsSetting");
    }

    /**
     * 获取短信接口设置详细信息
     */
    @PreAuthorize("@ss.hasPermi('setting:LsSmsSetting:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(lsSmsSettingService.selectLsSmsSettingById(id));
    }

    /**
     * 新增短信接口设置
     */
    @PreAuthorize("@ss.hasPermi('setting:LsSmsSetting:add')")
    @Log(title = "短信接口设置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LsSmsSetting lsSmsSetting) {
        return toAjax(lsSmsSettingService.insertLsSmsSetting(lsSmsSetting));
    }

    /**
     * 修改短信接口设置
     */
    @PreAuthorize("@ss.hasPermi('setting:LsSmsSetting:edit')")
    @Log(title = "短信接口设置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LsSmsSetting lsSmsSetting) {
        return toAjax(lsSmsSettingService.updateLsSmsSetting(lsSmsSetting));
    }

}

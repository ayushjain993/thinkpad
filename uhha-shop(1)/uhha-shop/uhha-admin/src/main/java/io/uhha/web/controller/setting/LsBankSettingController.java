package io.uhha.web.controller.setting;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.enums.BusinessType;
import io.uhha.setting.domain.LsBankSetting;
import io.uhha.setting.service.ILsBankSettingService;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.common.core.page.TableDataInfo;

/**
 * 银行配置信息Controller
 * 
 * @author uhha
 * @date 2022-03-12
 */
@RestController
@RequestMapping("/setting/LsBankSetting")
public class LsBankSettingController extends BaseController
{
    @Autowired
    private ILsBankSettingService lsBankSettingService;

    /**
     * 查询银行配置信息列表
     */
    @PreAuthorize("@ss.hasPermi('setting:LsBankSetting:list')")
    @GetMapping("/list")
    public TableDataInfo list(LsBankSetting lsBankSetting)
    {
        startPage();
        List<LsBankSetting> list = lsBankSettingService.selectLsBankSettingList(lsBankSetting);
        return getDataTable(list);
    }

    /**
     * 导出银行配置信息列表
     */
    @PreAuthorize("@ss.hasPermi('setting:LsBankSetting:export')")
    @Log(title = "银行配置信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(LsBankSetting lsBankSetting)
    {
        List<LsBankSetting> list = lsBankSettingService.selectLsBankSettingList(lsBankSetting);
        ExcelUtil<LsBankSetting> util = new ExcelUtil<LsBankSetting>(LsBankSetting.class);
        return util.exportExcel(list, "银行配置信息数据");
    }

    /**
     * 获取银行配置信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('setting:LsBankSetting:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(lsBankSettingService.selectLsBankSettingById(id));
    }

    /**
     * 新增银行配置信息
     */
    @PreAuthorize("@ss.hasPermi('setting:LsBankSetting:add')")
    @Log(title = "银行配置信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LsBankSetting lsBankSetting)
    {
        return toAjax(lsBankSettingService.insertLsBankSetting(lsBankSetting));
    }

    /**
     * 修改银行配置信息
     */
    @PreAuthorize("@ss.hasPermi('setting:LsBankSetting:edit')")
    @Log(title = "银行配置信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LsBankSetting lsBankSetting)
    {
        return toAjax(lsBankSettingService.updateLsBankSetting(lsBankSetting));
    }

    /**
     * 删除银行配置信息
     */
    @PreAuthorize("@ss.hasPermi('setting:LsBankSetting:remove')")
    @Log(title = "银行配置信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(lsBankSettingService.deleteLsBankSettingByIds(ids));
    }
}

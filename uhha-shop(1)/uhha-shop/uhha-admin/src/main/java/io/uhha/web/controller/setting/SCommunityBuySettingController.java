package io.uhha.web.controller.setting;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.setting.domain.SCommunityBuySetting;
import io.uhha.setting.service.ISCommunityBuySettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 社区团购设置Controller
 *
 * @author mj
 * @date 2020-07-28
 */
@Api(tags = "社区团购设置Controller")
@RestController
@RequestMapping("/setting/SCommunityBuySetting")
public class SCommunityBuySettingController extends BaseController {
    @Autowired
    private ISCommunityBuySettingService sCommunityBuySettingService;

    /**
     * 查询社区团购设置列表
     */
    @PreAuthorize("@ss.hasPermi('setting:SCommunityBuySetting:list')")
    @GetMapping("/list")
    public TableDataInfo list(SCommunityBuySetting sCommunityBuySetting) {
        startPage();
        List<SCommunityBuySetting> list = sCommunityBuySettingService.selectSCommunityBuySettingList(sCommunityBuySetting);
        return getDataTable(list);
    }

    /**
     * 导出社区团购设置列表
     */
    @PreAuthorize("@ss.hasPermi('setting:SCommunityBuySetting:export')")
    @Log(title = "社区团购设置", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SCommunityBuySetting sCommunityBuySetting) {
        List<SCommunityBuySetting> list = sCommunityBuySettingService.selectSCommunityBuySettingList(sCommunityBuySetting);
        ExcelUtil<SCommunityBuySetting> util = new ExcelUtil<SCommunityBuySetting>(SCommunityBuySetting.class);
        return util.exportExcel(list, "SCommunityBuySetting");
    }

    /**
     * 获取社区团购设置详细信息
     */
    @PreAuthorize("@ss.hasPermi('setting:SCommunityBuySetting:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(sCommunityBuySettingService.selectSCommunityBuySettingById(id));
    }

    /**
     * 新增社区团购设置
     */
    @PreAuthorize("@ss.hasPermi('setting:SCommunityBuySetting:add')")
    @Log(title = "社区团购设置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SCommunityBuySetting sCommunityBuySetting) {
        return toAjax(sCommunityBuySettingService.insertSCommunityBuySetting(sCommunityBuySetting));
    }

    /**
     * 修改社区团购设置
     */
    @PreAuthorize("@ss.hasPermi('setting:SCommunityBuySetting:edit')")
    @Log(title = "社区团购设置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SCommunityBuySetting sCommunityBuySetting) {
        return toAjax(sCommunityBuySettingService.updateSCommunityBuySetting(sCommunityBuySetting));
    }

    /**
     * 删除社区团购设置
     */
    @PreAuthorize("@ss.hasPermi('setting:SCommunityBuySetting:remove')")
    @Log(title = "社区团购设置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sCommunityBuySettingService.deleteSCommunityBuySettingByIds(ids));
    }
}

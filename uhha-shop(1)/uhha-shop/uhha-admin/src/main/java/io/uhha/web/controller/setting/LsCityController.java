package io.uhha.web.controller.setting;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.setting.domain.LsCity;
import io.uhha.setting.service.ILsCityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 区域市Controller
 *
 * @author mj
 * @date 2020-07-29
 */
@Api(tags = "区域市Controller")
@RestController
@RequestMapping("/setting/LsCity")
public class LsCityController extends BaseController {
    @Autowired
    private ILsCityService lsCityService;

    /**
     * 查询区域市列表
     */
    @PreAuthorize("@ss.hasPermi('setting:LsCity:list')")
    @GetMapping("/list")
    public TableDataInfo list(LsCity lsCity) {
        startPage();
        List<LsCity> list = lsCityService.selectLsCityList(lsCity);
        return getDataTable(list);
    }

    /**
     * 导出区域市列表
     */
    @PreAuthorize("@ss.hasPermi('setting:LsCity:export')")
    @Log(title = "区域市", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(LsCity lsCity) {
        List<LsCity> list = lsCityService.selectLsCityList(lsCity);
        ExcelUtil<LsCity> util = new ExcelUtil<LsCity>(LsCity.class);
        return util.exportExcel(list, "LsCity");
    }

    /**
     * 获取区域市详细信息
     */
    @PreAuthorize("@ss.hasPermi('setting:LsCity:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(lsCityService.selectLsCityById(id));
    }

    /**
     * 新增区域市
     */
    @PreAuthorize("@ss.hasPermi('setting:LsCity:add')")
    @Log(title = "区域市", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LsCity lsCity) {
        return toAjax(lsCityService.insertLsCity(lsCity));
    }

    /**
     * 修改区域市
     */
    @PreAuthorize("@ss.hasPermi('setting:LsCity:edit')")
    @Log(title = "区域市", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LsCity lsCity) {
        return toAjax(lsCityService.updateLsCity(lsCity));
    }

    /**
     * 删除区域市
     */
    @PreAuthorize("@ss.hasPermi('setting:LsCity:remove')")
    @Log(title = "区域市", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(lsCityService.deleteLsCityByIds(ids));
    }
}

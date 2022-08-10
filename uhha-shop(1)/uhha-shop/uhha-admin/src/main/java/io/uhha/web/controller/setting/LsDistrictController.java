package io.uhha.web.controller.setting;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.setting.domain.LsDistrict;
import io.uhha.setting.service.ILsDistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 区域区Controller
 *
 * @author mj
 * @date 2020-07-29
 */
@Api(tags = "区域区Controller")
@RestController
@RequestMapping("/setting/LsDistrict")
public class LsDistrictController extends BaseController {
    @Autowired
    private ILsDistrictService lsDistrictService;

    /**
     * 查询区域区列表
     */
    @PreAuthorize("@ss.hasPermi('setting:LsDistrict:list')")
    @GetMapping("/list")
    public TableDataInfo list(LsDistrict lsDistrict) {
        startPage();
        List<LsDistrict> list = lsDistrictService.selectLsDistrictList(lsDistrict);
        return getDataTable(list);
    }

    /**
     * 导出区域区列表
     */
    @PreAuthorize("@ss.hasPermi('setting:LsDistrict:export')")
    @Log(title = "区域区", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(LsDistrict lsDistrict) {
        List<LsDistrict> list = lsDistrictService.selectLsDistrictList(lsDistrict);
        ExcelUtil<LsDistrict> util = new ExcelUtil<LsDistrict>(LsDistrict.class);
        return util.exportExcel(list, "LsDistrict");
    }

    /**
     * 获取区域区详细信息
     */
    @PreAuthorize("@ss.hasPermi('setting:LsDistrict:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(lsDistrictService.selectLsDistrictById(id));
    }

    /**
     * 新增区域区
     */
    @PreAuthorize("@ss.hasPermi('setting:LsDistrict:add')")
    @Log(title = "区域区", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LsDistrict lsDistrict) {
        return toAjax(lsDistrictService.insertLsDistrict(lsDistrict));
    }

    /**
     * 修改区域区
     */
    @PreAuthorize("@ss.hasPermi('setting:LsDistrict:edit')")
    @Log(title = "区域区", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LsDistrict lsDistrict) {
        return toAjax(lsDistrictService.updateLsDistrict(lsDistrict));
    }

    /**
     * 删除区域区
     */
    @PreAuthorize("@ss.hasPermi('setting:LsDistrict:remove')")
    @Log(title = "区域区", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(lsDistrictService.deleteLsDistrictByIds(ids));
    }
}

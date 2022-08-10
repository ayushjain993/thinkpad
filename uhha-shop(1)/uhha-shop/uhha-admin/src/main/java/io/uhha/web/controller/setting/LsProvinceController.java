package io.uhha.web.controller.setting;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.setting.domain.LsProvince;
import io.uhha.setting.service.ILsProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 区域省Controller
 *
 * @author mj
 * @date 2020-07-29
 */
@Api(tags = "区域省Controller")
@RestController
@RequestMapping("/setting/LsProvince")
public class LsProvinceController extends BaseController {
    @Autowired
    private ILsProvinceService lsProvinceService;

    /**
     * 查询区域省列表
     */
    @PreAuthorize("@ss.hasPermi('setting:LsProvince:list')")
    @GetMapping("/list")
    public TableDataInfo list(LsProvince lsProvince) {
        startPage();
        List<LsProvince> list = lsProvinceService.selectLsProvinceList(lsProvince);
        return getDataTable(list);
    }

    /**
     * 导出区域省列表
     */
    @PreAuthorize("@ss.hasPermi('setting:LsProvince:export')")
    @Log(title = "区域省", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(LsProvince lsProvince) {
        List<LsProvince> list = lsProvinceService.selectLsProvinceList(lsProvince);
        ExcelUtil<LsProvince> util = new ExcelUtil<LsProvince>(LsProvince.class);
        return util.exportExcel(list, "LsProvince");
    }

    /**
     * 获取区域省详细信息
     */
    @PreAuthorize("@ss.hasPermi('setting:LsProvince:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(lsProvinceService.selectLsProvinceById(id));
    }

    /**
     * 新增区域省
     */
    @PreAuthorize("@ss.hasPermi('setting:LsProvince:add')")
    @Log(title = "区域省", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LsProvince lsProvince) {
        return toAjax(lsProvinceService.insertLsProvince(lsProvince));
    }

    /**
     * 修改区域省
     */
    @PreAuthorize("@ss.hasPermi('setting:LsProvince:edit')")
    @Log(title = "区域省", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LsProvince lsProvince) {
        return toAjax(lsProvinceService.updateLsProvince(lsProvince));
    }

    /**
     * 删除区域省
     */
    @PreAuthorize("@ss.hasPermi('setting:LsProvince:remove')")
    @Log(title = "区域省", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(lsProvinceService.deleteLsProvinceByIds(ids));
    }
}

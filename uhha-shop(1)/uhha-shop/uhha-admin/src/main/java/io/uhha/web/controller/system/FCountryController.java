package io.uhha.web.controller.system;

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
import io.uhha.system.domain.FCountry;
import io.uhha.system.service.IFCountryService;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.common.core.page.TableDataInfo;

/**
 * 国家Controller
 *
 * @author peter
 * @date 2022-01-09
 */
@RestController
@RequestMapping("/system/fcountry")
public class FCountryController extends BaseController
{
    @Autowired
    private IFCountryService fCountryService;

    /**
     * 获取国家详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:fcountry:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(fCountryService.selectFCountryById(id));
    }

    /**
     * 查询国家列表
     */
    @PreAuthorize("@ss.hasPermi('system:fcountry:list')")
    @GetMapping("/list")
    public TableDataInfo list(FCountry fCountry)
    {
        startPage();
        List<FCountry> list = fCountryService.selectFCountryList(fCountry);
        return getDataTable(list);
    }

    /**
     * 导出国家列表
     */
    @PreAuthorize("@ss.hasPermi('system:fcountry:export')")
    @Log(title = "国家", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(FCountry fCountry)
    {
        List<FCountry> list = fCountryService.selectFCountryList(fCountry);
        ExcelUtil<FCountry> util = new ExcelUtil<FCountry>(FCountry.class);
        return util.exportExcel(list, "fcountry数据");
    }

    /**
     * 新增国家
     */
    @PreAuthorize("@ss.hasPermi('system:fcountry:add')")
    @Log(title = "国家", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FCountry fCountry)
    {
        return toAjax(fCountryService.insertFCountry(fCountry));
    }

    /**
     * 修改国家
     */
    @PreAuthorize("@ss.hasPermi('system:fcountry:edit')")
    @Log(title = "国家", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FCountry fCountry)
    {
        return toAjax(fCountryService.updateFCountry(fCountry));
    }

    /**
     * 删除国家
     */
    @PreAuthorize("@ss.hasPermi('system:fcountry:remove')")
    @Log(title = "国家", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(fCountryService.deleteFCountryByIds(ids));
    }
}
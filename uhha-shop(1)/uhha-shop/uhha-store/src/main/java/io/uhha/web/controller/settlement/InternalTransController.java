package io.uhha.web.controller.settlement;

import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.settlement.domain.InternalTrans;
import io.uhha.settlement.service.IInternalTransService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 转移记录Controller
 *
 * @author uhha
 * @date 2021-12-31
 */
@RestController
@RequestMapping("/settlement/trans")
public class InternalTransController extends BaseController
{
    @Autowired
    private IInternalTransService internalTransService;

    /**
     * 查询转移记录列表
     */
    @PreAuthorize("@ss.hasPermi('settlement:trans:list')")
    @GetMapping("/list")
    public TableDataInfo list(InternalTrans internalTrans)
    {
        startPage();
        List<InternalTrans> list = internalTransService.selectInternalTransList(internalTrans);
        return getDataTable(list);
    }

    /**
     * 导出转移记录列表
     */
    @PreAuthorize("@ss.hasPermi('settlement:trans:export')")
    @Log(title = "转移记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(InternalTrans internalTrans)
    {
        List<InternalTrans> list = internalTransService.selectInternalTransList(internalTrans);
        ExcelUtil<InternalTrans> util = new ExcelUtil<InternalTrans>(InternalTrans.class);
        return util.exportExcel(list, "转移记录数据");
    }

    /**
     * 获取转移记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('settlement:trans:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(internalTransService.selectInternalTransById(id));
    }

    /**
     * 新增转移记录
     */
    @PreAuthorize("@ss.hasPermi('settlement:trans:add')")
    @Log(title = "转移记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InternalTrans internalTrans)
    {
        return toAjax(internalTransService.insertInternalTrans(internalTrans));
    }

    /**
     * 修改转移记录
     */
    @PreAuthorize("@ss.hasPermi('settlement:trans:edit')")
    @Log(title = "转移记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InternalTrans internalTrans)
    {
        return toAjax(internalTransService.updateInternalTrans(internalTrans));
    }

    /**
     * 删除转移记录
     */
    @PreAuthorize("@ss.hasPermi('settlement:trans:remove')")
    @Log(title = "转移记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(internalTransService.deleteInternalTransByIds(ids));
    }
}

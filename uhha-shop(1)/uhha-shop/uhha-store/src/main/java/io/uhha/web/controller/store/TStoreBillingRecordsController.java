package io.uhha.web.controller.store;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.store.domain.TStoreBillingRecords;
import io.uhha.store.service.ITStoreBillingRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 门店账单收入支出Controller
 * 
 * @author test
 * @date 2021-05-23
 */
@Api(tags = "门店账单收入支出Controller")
@RestController
@RequestMapping("/store/TStoreBillingRecords")
public class TStoreBillingRecordsController extends BaseController
{
    @Autowired
    private ITStoreBillingRecordsService tStoreBillingRecordsService;

    /**
     * 查询门店账单收入支出列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreBillingRecords:list')")
    @GetMapping("/list")
    public TableDataInfo list(TStoreBillingRecords tStoreBillingRecords)
    {
        startPage();
        List<TStoreBillingRecords> list = tStoreBillingRecordsService.selectTStoreBillingRecordsList(tStoreBillingRecords);
        return getDataTable(list);
    }

    /**
     * 导出门店账单收入支出列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreBillingRecords:export')")
    @Log(title = "门店账单收入支出", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TStoreBillingRecords tStoreBillingRecords)
    {
        List<TStoreBillingRecords> list = tStoreBillingRecordsService.selectTStoreBillingRecordsList(tStoreBillingRecords);
        ExcelUtil<TStoreBillingRecords> util = new ExcelUtil<TStoreBillingRecords>(TStoreBillingRecords.class);
        return util.exportExcel(list, "门店账单收入支出数据");
    }

    /**
     * 获取门店账单收入支出详细信息
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreBillingRecords:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tStoreBillingRecordsService.selectTStoreBillingRecordsById(id));
    }

    /**
     * 新增门店账单收入支出
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreBillingRecords:add')")
    @Log(title = "门店账单收入支出", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TStoreBillingRecords tStoreBillingRecords)
    {
        return toAjax(tStoreBillingRecordsService.insertTStoreBillingRecords(tStoreBillingRecords));
    }

    /**
     * 修改门店账单收入支出
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreBillingRecords:edit')")
    @Log(title = "门店账单收入支出", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TStoreBillingRecords tStoreBillingRecords)
    {
        return toAjax(tStoreBillingRecordsService.updateTStoreBillingRecords(tStoreBillingRecords));
    }

    /**
     * 删除门店账单收入支出
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreBillingRecords:remove')")
    @Log(title = "门店账单收入支出", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tStoreBillingRecordsService.deleteTStoreBillingRecordsByIds(ids));
    }
}

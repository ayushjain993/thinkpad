package io.uhha.web.controller.settlement;

import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.settlement.domain.SettlementRecord;
import io.uhha.settlement.service.ISettlementRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 结算记录Controller
 *
 * @author uhha
 * @date 2021-12-31
 */
@RestController
@RequestMapping("/settlement/settlementRecord")
public class SettlementRecordController extends BaseController
{
    @Autowired
    private ISettlementRecordService settlementRecordService;

    /**
     * 查询结算记录列表
     */
    @PreAuthorize("@ss.hasPermi('settlement:settlementRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(SettlementRecord settlementRecord)
    {
        startPage();
        List<SettlementRecord> list = settlementRecordService.selectSettlementRecordList(settlementRecord);
        return getDataTable(list);
    }

    /**
     * 导出结算记录列表
     */
    @PreAuthorize("@ss.hasPermi('settlement:settlementRecord:export')")
    @Log(title = "结算记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SettlementRecord settlementRecord)
    {
        List<SettlementRecord> list = settlementRecordService.selectSettlementRecordList(settlementRecord);
        ExcelUtil<SettlementRecord> util = new ExcelUtil<SettlementRecord>(SettlementRecord.class);
        return util.exportExcel(list, "结算记录数据");
    }

    /**
     * 获取结算记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('settlement:settlementRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(settlementRecordService.selectSettlementRecordById(id));
    }

    /**
     * 新增结算记录
     */
    @PreAuthorize("@ss.hasPermi('settlement:settlementRecord:add')")
    @Log(title = "结算记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SettlementRecord settlementRecord)
    {
        return toAjax(settlementRecordService.insertSettlementRecord(settlementRecord));
    }

    /**
     * 修改结算记录
     */
    @PreAuthorize("@ss.hasPermi('settlement:settlementRecord:edit')")
    @Log(title = "结算记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SettlementRecord settlementRecord)
    {
        return toAjax(settlementRecordService.updateSettlementRecord(settlementRecord));
    }

    /**
     * 删除结算记录
     */
    @PreAuthorize("@ss.hasPermi('settlement:settlementRecord:remove')")
    @Log(title = "结算记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(settlementRecordService.deleteSettlementRecordByIds(ids));
    }
}

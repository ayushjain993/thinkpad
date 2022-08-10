package io.uhha.web.controller.order;

import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.order.domain.OmsCommissionRecords;
import io.uhha.order.service.IOmsCommissionRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 佣金记录Controller
 *
 * @author mj
 * @date 2020-07-24
 */
@RestController
@RequestMapping("/order/OmsCommissionRecords")
public class OmsCommissionRecordsController extends BaseController {
    @Autowired
    private IOmsCommissionRecordsService omsCommissionRecordsService;

    /**
     * 查询佣金记录列表
     */
    @PreAuthorize("@ss.hasPermi('order:OmsCommissionRecords:list')")
    @GetMapping("/list")
    public TableDataInfo list(OmsCommissionRecords omsCommissionRecords) {
        startPage();
        List<OmsCommissionRecords> list = omsCommissionRecordsService.selectOmsCommissionRecordsList(omsCommissionRecords);
        return getDataTable(list);
    }

    /**
     * 导出佣金记录列表
     */
    @PreAuthorize("@ss.hasPermi('order:OmsCommissionRecords:export')")
    @Log(title = "佣金记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(OmsCommissionRecords omsCommissionRecords) {
        List<OmsCommissionRecords> list = omsCommissionRecordsService.selectOmsCommissionRecordsList(omsCommissionRecords);
        ExcelUtil<OmsCommissionRecords> util = new ExcelUtil<OmsCommissionRecords>(OmsCommissionRecords.class);
        return util.exportExcel(list, "OmsCommissionRecords");
    }

    /**
     * 获取佣金记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('order:OmsCommissionRecords:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(omsCommissionRecordsService.selectOmsCommissionRecordsById(id));
    }

    /**
     * 新增佣金记录
     */
    @PreAuthorize("@ss.hasPermi('order:OmsCommissionRecords:add')")
    @Log(title = "佣金记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OmsCommissionRecords omsCommissionRecords) {
        return toAjax(omsCommissionRecordsService.insertOmsCommissionRecords(omsCommissionRecords));
    }

    /**
     * 修改佣金记录
     */
    @PreAuthorize("@ss.hasPermi('order:OmsCommissionRecords:edit')")
    @Log(title = "佣金记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OmsCommissionRecords omsCommissionRecords) {
        return toAjax(omsCommissionRecordsService.updateOmsCommissionRecords(omsCommissionRecords));
    }

    /**
     * 删除佣金记录
     */
    @PreAuthorize("@ss.hasPermi('order:OmsCommissionRecords:remove')")
    @Log(title = "佣金记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(omsCommissionRecordsService.deleteOmsCommissionRecordsByIds(ids));
    }
}

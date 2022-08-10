package io.uhha.web.controller.order;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.order.domain.OmsBackOrderLog;
import io.uhha.order.service.IOmsBackOrderLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 退款退货操作日志Controller
 *
 * @author mj
 * @date 2020-07-24
 */
@Api(tags = "退款退货操作日志Controller")
@RestController
@RequestMapping("/order/OmsBackOrderLog")
public class OmsBackOrderLogController extends BaseController {
    @Autowired
    private IOmsBackOrderLogService omsBackOrderLogService;

    /**
     * 查询退款退货操作日志列表
     */
    @PreAuthorize("@ss.hasPermi('order:OmsBackOrderLog:list')")
    @GetMapping("/list")
    public TableDataInfo list(OmsBackOrderLog omsBackOrderLog) {
        startPage();
        List<OmsBackOrderLog> list = omsBackOrderLogService.selectOmsBackOrderLogList(omsBackOrderLog);
        return getDataTable(list);
    }

    /**
     * 导出退款退货操作日志列表
     */
    @PreAuthorize("@ss.hasPermi('order:OmsBackOrderLog:export')")
    @Log(title = "退款退货操作日志", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(OmsBackOrderLog omsBackOrderLog) {
        List<OmsBackOrderLog> list = omsBackOrderLogService.selectOmsBackOrderLogList(omsBackOrderLog);
        ExcelUtil<OmsBackOrderLog> util = new ExcelUtil<OmsBackOrderLog>(OmsBackOrderLog.class);
        return util.exportExcel(list, "OmsBackOrderLog");
    }

    /**
     * 获取退款退货操作日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('order:OmsBackOrderLog:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(omsBackOrderLogService.selectOmsBackOrderLogById(id));
    }

    /**
     * 新增退款退货操作日志
     */
    @PreAuthorize("@ss.hasPermi('order:OmsBackOrderLog:add')")
    @Log(title = "退款退货操作日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OmsBackOrderLog omsBackOrderLog) {
        return toAjax(omsBackOrderLogService.insertOmsBackOrderLog(omsBackOrderLog));
    }

    /**
     * 修改退款退货操作日志
     */
    @PreAuthorize("@ss.hasPermi('order:OmsBackOrderLog:edit')")
    @Log(title = "退款退货操作日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OmsBackOrderLog omsBackOrderLog) {
        return toAjax(omsBackOrderLogService.updateOmsBackOrderLog(omsBackOrderLog));
    }

    /**
     * 删除退款退货操作日志
     */
    @PreAuthorize("@ss.hasPermi('order:OmsBackOrderLog:remove')")
    @Log(title = "退款退货操作日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(omsBackOrderLogService.deleteOmsBackOrderLogByIds(ids));
    }
}

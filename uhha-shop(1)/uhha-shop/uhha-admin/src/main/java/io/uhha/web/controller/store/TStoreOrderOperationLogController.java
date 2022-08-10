package io.uhha.web.controller.store;

import java.util.List;

import io.swagger.annotations.Api;
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
import io.uhha.store.domain.TStoreOrderOperationLog;
import io.uhha.store.service.ITStoreOrderOperationLogService;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.common.core.page.TableDataInfo;

/**
 * 门店订单操作日志Controller
 * 
 * @author ruoyi
 * @date 2021-05-23
 */
@Api(tags = "门店订单操作日志Controller")
@RestController
@RequestMapping("/store/TStoreOrderOperationLog")
public class TStoreOrderOperationLogController extends BaseController
{
    @Autowired
    private ITStoreOrderOperationLogService tStoreOrderOperationLogService;

    /**
     * 查询门店订单操作日志列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreOrderOperationLog:list')")
    @GetMapping("/list")
    public TableDataInfo list(TStoreOrderOperationLog tStoreOrderOperationLog)
    {
        startPage();
        List<TStoreOrderOperationLog> list = tStoreOrderOperationLogService.selectTStoreOrderOperationLogList(tStoreOrderOperationLog);
        return getDataTable(list);
    }

    /**
     * 导出门店订单操作日志列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreOrderOperationLog:export')")
    @Log(title = "门店订单操作日志", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TStoreOrderOperationLog tStoreOrderOperationLog)
    {
        List<TStoreOrderOperationLog> list = tStoreOrderOperationLogService.selectTStoreOrderOperationLogList(tStoreOrderOperationLog);
        ExcelUtil<TStoreOrderOperationLog> util = new ExcelUtil<TStoreOrderOperationLog>(TStoreOrderOperationLog.class);
        return util.exportExcel(list, "门店订单操作日志数据");
    }

    /**
     * 获取门店订单操作日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreOrderOperationLog:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tStoreOrderOperationLogService.selectTStoreOrderOperationLogById(id));
    }

    /**
     * 新增门店订单操作日志
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreOrderOperationLog:add')")
    @Log(title = "门店订单操作日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TStoreOrderOperationLog tStoreOrderOperationLog)
    {
        return toAjax(tStoreOrderOperationLogService.insertTStoreOrderOperationLog(tStoreOrderOperationLog));
    }

    /**
     * 修改门店订单操作日志
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreOrderOperationLog:edit')")
    @Log(title = "门店订单操作日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TStoreOrderOperationLog tStoreOrderOperationLog)
    {
        return toAjax(tStoreOrderOperationLogService.updateTStoreOrderOperationLog(tStoreOrderOperationLog));
    }

    /**
     * 删除门店订单操作日志
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreOrderOperationLog:remove')")
    @Log(title = "门店订单操作日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tStoreOrderOperationLogService.deleteTStoreOrderOperationLogByIds(ids));
    }
}

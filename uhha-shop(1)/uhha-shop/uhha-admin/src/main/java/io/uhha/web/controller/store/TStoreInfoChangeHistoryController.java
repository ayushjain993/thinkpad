package io.uhha.web.controller.store;

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
import io.uhha.store.domain.TStoreInfoChangeHistory;
import io.uhha.store.service.ITStoreInfoChangeHistoryService;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.common.core.page.TableDataInfo;

/**
 * 店铺信息更改日志Controller
 * 
 * @author uhha
 * @date 2022-01-26
 */
@RestController
@RequestMapping("/store/storeHistory")
public class TStoreInfoChangeHistoryController extends BaseController
{
    @Autowired
    private ITStoreInfoChangeHistoryService tStoreInfoChangeHistoryService;

//    /**
//     * 查询店铺信息更改日志列表
//     */
//    @PreAuthorize("@ss.hasPermi('store:storeHistory:list')")
//    @GetMapping("/list")
//    public TableDataInfo list(TStoreInfoChangeHistory tStoreInfoChangeHistory)
//    {
//        startPage();
//        List<TStoreInfoChangeHistory> list = tStoreInfoChangeHistoryService.selectTStoreInfoChangeHistoryList(tStoreInfoChangeHistory);
//        return getDataTable(list);
//    }
//
//    /**
//     * 导出店铺信息更改日志列表
//     */
//    @PreAuthorize("@ss.hasPermi('store:storeHistory:export')")
//    @Log(title = "店铺信息更改日志", businessType = BusinessType.EXPORT)
//    @GetMapping("/export")
//    public AjaxResult export(TStoreInfoChangeHistory tStoreInfoChangeHistory)
//    {
//        List<TStoreInfoChangeHistory> list = tStoreInfoChangeHistoryService.selectTStoreInfoChangeHistoryList(tStoreInfoChangeHistory);
//        ExcelUtil<TStoreInfoChangeHistory> util = new ExcelUtil<TStoreInfoChangeHistory>(TStoreInfoChangeHistory.class);
//        return util.exportExcel(list, "店铺信息更改日志数据");
//    }

    /**
     * 获取店铺信息更改日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('store:storeHistory:query')")
    @GetMapping(value = "/byStoreId/{storeId}")
    public AjaxResult getHistoryByStoreId(@PathVariable("storeId") Long storeId)
    {
        return AjaxResult.success(tStoreInfoChangeHistoryService.selectTStoreInfoChangeHistoryByStoreId(storeId));
    }

//    /**
//     * 新增店铺信息更改日志
//     */
//    @PreAuthorize("@ss.hasPermi('store:storeHistory:add')")
//    @Log(title = "店铺信息更改日志", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody TStoreInfoChangeHistory tStoreInfoChangeHistory)
//    {
//        return toAjax(tStoreInfoChangeHistoryService.insertTStoreInfoChangeHistory(tStoreInfoChangeHistory));
//    }
//
//    /**
//     * 修改店铺信息更改日志
//     */
//    @PreAuthorize("@ss.hasPermi('store:storeHistory:edit')")
//    @Log(title = "店铺信息更改日志", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody TStoreInfoChangeHistory tStoreInfoChangeHistory)
//    {
//        return toAjax(tStoreInfoChangeHistoryService.updateTStoreInfoChangeHistory(tStoreInfoChangeHistory));
//    }
//
//    /**
//     * 删除店铺信息更改日志
//     */
//    @PreAuthorize("@ss.hasPermi('store:storeHistory:remove')")
//    @Log(title = "店铺信息更改日志", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(tStoreInfoChangeHistoryService.deleteTStoreInfoChangeHistoryByIds(ids));
//    }
}

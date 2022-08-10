package io.uhha.web.controller.order;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.order.domain.OmsBillingRecords;
import io.uhha.order.service.IOmsBillingRecordsService;
import io.uhha.order.service.IOmsOrderService;
import io.uhha.util.CommonConstant;
import io.uhha.task.SettlementTask;
import io.uhha.web.utils.AdminLoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 账单记录Controller
 *
 * @author mj
 * @date 2020-07-24
 */
@Api(tags = "账单记录Controller")
@RestController
@RequestMapping("/order/OmsBillingRecords")
public class OmsBillingRecordsController extends BaseController {
    @Autowired
    private IOmsBillingRecordsService omsBillingRecordsService;

    @Autowired
    private IOmsOrderService omsOrderService;

    @Autowired
    private SettlementTask settlementTask;

    /**
     * 查询账单记录列表
     */
    @PreAuthorize("@ss.hasPermi('order:OmsBillingRecords:list')")
    @GetMapping("/list")
    public TableDataInfo list(OmsBillingRecords omsBillingRecords) {
        startPage();
        List<OmsBillingRecords> list = omsBillingRecordsService.selectOmsBillingRecordsList(omsBillingRecords);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('order:OmsBillingRecords:list')")
    @GetMapping("/settlementRecord")
    public TableDataInfo settlementRecord(OmsBillingRecords omsBillingRecords) {
        omsBillingRecords.setStoreId(CommonConstant.ADMIN_STOREID);
        startPage();
        List<OmsBillingRecords> list = omsBillingRecordsService.selectOmsBillingRecordsList(omsBillingRecords);
        omsOrderService.buildOrderDetail(list);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('order:OmsBillingRecords:list')")
    @GetMapping("/settlementRecordStatics")
    public AjaxResult settlementRecordStatics(OmsBillingRecords omsBillingRecords) {
        omsBillingRecords.setStoreId(CommonConstant.ADMIN_STOREID);
        List<OmsBillingRecords> list = omsBillingRecordsService.selectOmsBillingRecordsList(omsBillingRecords);
        return AjaxResult.success(omsOrderService.buildSettlementRecordStatics(list, omsBillingRecords.getStoreId()));
    }

    /**
     * 导出账单记录列表
     */
    @PreAuthorize("@ss.hasPermi('order:OmsBillingRecords:export')")
    @Log(title = "账单记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(OmsBillingRecords omsBillingRecords) {
        List<OmsBillingRecords> list = omsBillingRecordsService.selectOmsBillingRecordsList(omsBillingRecords);
        ExcelUtil<OmsBillingRecords> util = new ExcelUtil<OmsBillingRecords>(OmsBillingRecords.class);
        return util.exportExcel(list, "OmsBillingRecords");
    }

    @PreAuthorize("@ss.hasPermi('order:OmsBillingRecords:edit')")
    @Log(title = "结算", businessType = BusinessType.UPDATE)
    @PostMapping("/settlement")
    public AjaxResult export(@RequestBody Map<String, Object> dateRange) {
        Long storeId = CommonConstant.ADMIN_STOREID;
        String operator = AdminLoginUtils.getInstance().getManagerName();
        return AjaxResult.success(settlementTask.settlementJob(storeId,operator, dateRange));
    }

    /**
     * 获取账单记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('order:OmsBillingRecords:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(omsBillingRecordsService.selectOmsBillingRecordsById(id));
    }

    /**
     * 新增账单记录
     */
    @PreAuthorize("@ss.hasPermi('order:OmsBillingRecords:add')")
    @Log(title = "账单记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OmsBillingRecords omsBillingRecords) {
        return toAjax(omsBillingRecordsService.insertOmsBillingRecords(omsBillingRecords));
    }

    /**
     * 修改账单记录
     */
    @PreAuthorize("@ss.hasPermi('order:OmsBillingRecords:edit')")
    @Log(title = "账单记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OmsBillingRecords omsBillingRecords) {
        return toAjax(omsBillingRecordsService.updateOmsBillingRecords(omsBillingRecords));
    }

    /**
     * 删除账单记录
     */
    @PreAuthorize("@ss.hasPermi('order:OmsBillingRecords:remove')")
    @Log(title = "账单记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(omsBillingRecordsService.deleteOmsBillingRecordsByIds(ids));
    }
}

package io.uhha.web.controller.settlement;

import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.settlement.domain.SettlementWithdrawal;
import io.uhha.settlement.model.SettlementWithdrawalRequest;
import io.uhha.settlement.model.WithdrawalPayRequest;
import io.uhha.settlement.model.WithdrawalRequest;
import io.uhha.settlement.service.ISettlementWithdrawalService;
import io.uhha.util.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 提现Controller
 *
 * @author uhha
 * @date 2022-01-04
 */
@RestController
@RequestMapping("/settlement/settlementWithdrawal")
public class SettlementWithdrawalController extends BaseController {
    @Autowired
    private ISettlementWithdrawalService settlementWithdrawalService;

    /**
     * 查询提现列表
     */
    @PreAuthorize("@ss.hasPermi('settlement:settlementWithdrawal:list')")
    @GetMapping("/list")
    public TableDataInfo list(SettlementWithdrawal settlementWithdrawal) {
        startPage();
        List<SettlementWithdrawal> list = settlementWithdrawalService.selectSettlementWithdrawalList(settlementWithdrawal);
        return getDataTable(list);
    }

    /**
     * 导出提现列表
     */
    @PreAuthorize("@ss.hasPermi('settlement:settlementWithdrawal:export')")
    @Log(title = "提现", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SettlementWithdrawal settlementWithdrawal) {
        List<SettlementWithdrawal> list = settlementWithdrawalService.selectSettlementWithdrawalList(settlementWithdrawal);
        ExcelUtil<SettlementWithdrawal> util = new ExcelUtil<SettlementWithdrawal>(SettlementWithdrawal.class);
        return util.exportExcel(list, "提现数据");
    }

    /**
     * 获取提现详细信息
     */
    @PreAuthorize("@ss.hasPermi('settlement:settlementWithdrawal:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(settlementWithdrawalService.selectSettlementWithdrawalById(id));
    }

    /**
     * 新增提现
     */
    @PreAuthorize("@ss.hasPermi('settlement:settlementWithdrawal:add')")
    @Log(title = "提现", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SettlementWithdrawalRequest settlementWithdrawal) {
        settlementWithdrawal.setCreateBy(getUsername());
        settlementWithdrawal.setStoreId(CommonConstant.ADMIN_STOREID);
        settlementWithdrawal.setStoreName(CommonConstant.ADMIN_STORENAME);
        return toAjax(settlementWithdrawalService.insertSettlementWithdrawal(settlementWithdrawal));
    }

    /**
     * 修改提现
     */
    @PreAuthorize("@ss.hasPermi('settlement:settlementWithdrawal:edit')")
    @Log(title = "提现", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SettlementWithdrawal settlementWithdrawal) {
        return toAjax(settlementWithdrawalService.applyStatusUpdate(settlementWithdrawal));
    }

    /**
     * 审核
     */
    @PreAuthorize("@ss.hasPermi('settlement:settlementWithdrawal:edit')")
    @Log(title = "审核", businessType = BusinessType.UPDATE)
    @PutMapping("/audit")
    public AjaxResult auditWithdrawal(@RequestBody WithdrawalRequest withdrawalRequest) {
        return toAjax(settlementWithdrawalService.auditWithdrawal(withdrawalRequest));
    }

    /**
     * 付款
     */
    @PreAuthorize("@ss.hasPermi('settlement:settlementWithdrawal:edit')")
    @Log(title = "付款", businessType = BusinessType.UPDATE)
    @PutMapping("/pay")
    public AjaxResult withdrawalPay(@RequestBody WithdrawalPayRequest withdrawalPayRequest) {
        return toAjax(settlementWithdrawalService.withdrawalPay(withdrawalPayRequest));
    }

    @PreAuthorize("@ss.hasPermi('settlement:settlementWithdrawal:remove')")
    @Log(title = "结算记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(settlementWithdrawalService.deleteSettlementWithdrawalByIds(ids));
    }

}

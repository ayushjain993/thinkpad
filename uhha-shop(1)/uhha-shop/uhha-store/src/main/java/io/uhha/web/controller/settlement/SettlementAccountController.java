package io.uhha.web.controller.settlement;

import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.AccountTypeEnum;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.settlement.domain.SettlementAccount;
import io.uhha.settlement.service.ISettlementAccountService;
import io.uhha.util.CommonConstant;
import io.uhha.web.utils.AdminLoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 结算账户Controller
 *
 * @author uhha
 * @date 2021-12-31
 */
@RestController
@RequestMapping("/settlement/settlementAccount")
@Validated
public class SettlementAccountController extends BaseController {
    @Autowired
    private ISettlementAccountService settlementAccountService;

    /**
     * 查询结算账户列表
     */
    @PreAuthorize("@ss.hasPermi('settlement:settlementAccount:list')")
    @GetMapping("/list")
    public TableDataInfo list(SettlementAccount settlementAccount) {
        Long storeId = AdminLoginUtils.getInstance().getStoreId();
        startPage();

        if(CommonConstant.ADMIN_STOREID.equals(storeId)){
            settlementAccount.setAccountType(AccountTypeEnum.SYSTEM.name());
        }else{
            settlementAccount.setAccountType(AccountTypeEnum.STORE.name());
        }
        settlementAccount.setAssociateId(storeId);

        List<SettlementAccount> list = settlementAccountService.selectSettlementAccountList(settlementAccount);
        return getDataTable(list);
    }

    /**
     * 导出结算账户列表
     */
    @PreAuthorize("@ss.hasPermi('settlement:settlementAccount:export')")
    @Log(title = "结算账户", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SettlementAccount settlementAccount) {
        settlementAccount.setAccountType(AccountTypeEnum.STORE.name());
        settlementAccount.setAssociateId(AdminLoginUtils.getInstance().getStoreId());
        List<SettlementAccount> list = settlementAccountService.selectSettlementAccountList(settlementAccount);
        ExcelUtil<SettlementAccount> util = new ExcelUtil<SettlementAccount>(SettlementAccount.class);
        return util.exportExcel(list, "结算账户数据");
    }

    /**
     * 获取结算账户详细信息
     */
    @PreAuthorize("@ss.hasPermi('settlement:settlementAccount:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(settlementAccountService.selectSettlementAccountById(id));
    }

    /**
     * 获取账户
     */
    @PreAuthorize("@ss.hasPermi('settlement:settlementAccount:query')")
    @GetMapping(value = "/accountUser")
    public AjaxResult getInfo(@NotNull(message = "账户类型不能为空") AccountTypeEnum accountType) {
        return AjaxResult.success(settlementAccountService.selectAccountUserByAccountType(accountType));
    }

}

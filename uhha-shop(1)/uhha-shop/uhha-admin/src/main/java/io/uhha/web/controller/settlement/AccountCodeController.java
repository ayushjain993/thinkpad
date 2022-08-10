package io.uhha.web.controller.settlement;

import java.util.List;

import io.uhha.common.enums.AccountTypeEnum;
import io.uhha.settlement.AccountCodeGenReqVo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
import io.uhha.settlement.domain.AccountCode;
import io.uhha.settlement.service.IAccountCodeService;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.common.core.page.TableDataInfo;

/**
 * 账户号码池Controller
 * 
 * @author uhha
 * @date 2022-02-23
 */
@RestController
@RequestMapping("/settlement/code")
public class AccountCodeController extends BaseController
{
    @Autowired
    private IAccountCodeService accountCodeService;

    /**
     * 查询账户号码池列表
     */
    @PreAuthorize("@ss.hasPermi('settlement:code:list')")
    @GetMapping("/list")
    public TableDataInfo list(AccountCode accountCode)
    {
        startPage();
        List<AccountCode> list = accountCodeService.selectAccountCodeList(accountCode);
        return getDataTable(list);
    }

    /**
     * 导出账户号码池列表
     */
    @PreAuthorize("@ss.hasPermi('settlement:code:export')")
    @Log(title = "账户号码池", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(AccountCode accountCode)
    {
        List<AccountCode> list = accountCodeService.selectAccountCodeList(accountCode);
        ExcelUtil<AccountCode> util = new ExcelUtil<AccountCode>(AccountCode.class);
        return util.exportExcel(list, "账户号码池数据");
    }

    /**
     * 生成账户号码池
     */
    @PreAuthorize("@ss.hasPermi('settlement:code:add')")
    @Log(title = "账户号码池", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult generate(@RequestBody @Validated AccountCodeGenReqVo reqVo)
    {
        return toAjax(accountCodeService.generateAccountCode(reqVo.getAccountType(),reqVo.getCurrency(),reqVo.getAmount()));
    }


}

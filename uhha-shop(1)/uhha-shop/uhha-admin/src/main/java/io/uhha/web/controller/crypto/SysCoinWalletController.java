package io.uhha.web.controller.crypto;

import java.util.List;

import io.swagger.annotations.Api;
import io.uhha.coin.system.domain.FSysCoinOperation;
import io.uhha.coin.system.service.IFSysCoinOperationService;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.domain.model.LoginUser;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.web.utils.AdminLoginUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.uhha.coin.system.domain.FSysCoinWallet;
import io.uhha.coin.system.service.IFSysCoinWalletService;


/**
 * 系统钱包Controller
 *
 * @author uhha
 * @date 2021-10-04
 */
@Api(tags = "系统钱包Controller")
@RestController
@RequestMapping("/crypto/sysWallet")
public class SysCoinWalletController extends BaseController
{
    @Autowired
    private IFSysCoinWalletService fSysCoinWalletService;

    @Autowired
    private IFSysCoinOperationService fSysCoinOperationService;

    /**
     * 查询系统钱包列表
     */
    @PreAuthorize("@ss.hasPermi('crypto:sysWallet:list')")
    @GetMapping("/list")
    public TableDataInfo list(FSysCoinWallet fSysCoinWallet)
    {
        startPage();
        List<FSysCoinWallet> list = fSysCoinWalletService.selectFSysCoinWalletList(fSysCoinWallet);
        return getDataTable(list);
    }

    /**
     * 导出系统钱包列表
     */
    @PreAuthorize("@ss.hasPermi('crypto:sysWallet:export')")
    @Log(title = "系统钱包", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(FSysCoinWallet fSysCoinWallet)
    {
        List<FSysCoinWallet> list = fSysCoinWalletService.selectFSysCoinWalletList(fSysCoinWallet);
        ExcelUtil<FSysCoinWallet> util = new ExcelUtil<FSysCoinWallet>(FSysCoinWallet.class);
        return util.exportExcel(list, "系统钱包数据");
    }

    /**
     * 根据coindId获取系统钱包详细信息
     */
    @PreAuthorize("@ss.hasPermi('crypto:sysWallet:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return AjaxResult.success(fSysCoinWalletService.getSysCoinWalletByCoinId(id));
    }
    /**
     * 修改系统钱包
     */
    @PreAuthorize("@ss.hasPermi('crypto:sysWallet:edit')")
    @Log(title = "系统钱包", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FSysCoinWallet fSysCoinWallet)
    {
        return toAjax(fSysCoinWalletService.updateFSysCoinWallet(fSysCoinWallet));
    }

    // -----------------------------------------------------------------------------------------


    /**
     * 查询系统钱包操作日志列表
     */
    @PreAuthorize("@ss.hasPermi('crypto:sysWallet:list')")
    @GetMapping("/op/coin/{coinid}")
    public TableDataInfo listByCoinId(@PathVariable("coinid") Integer coinid)
    {
        startPage();
        List<FSysCoinOperation> list = fSysCoinOperationService.selectFSysCoinOperationListByCoinId(coinid);
        return getDataTable(list);
    }

    /**
     * 新增系统钱包操作日志
     */
    @PreAuthorize("@ss.hasPermi('crypto:sysWallet:query')")
    @GetMapping("/op/{fid}")
    public AjaxResult getById(@PathVariable("fid") Long fid)
    {
        return AjaxResult.success(fSysCoinOperationService.selectFSysCoinOperationByFid(fid));
    }

    /**
     * 新增系统钱包操作日志
     */
    @PreAuthorize("@ss.hasPermi('crypto:sysWallet:add')")
    @Log(title = "系统钱包操作日志", businessType = BusinessType.INSERT)
    @PostMapping("/op")
    public AjaxResult add(@RequestBody FSysCoinOperation fSysCoinOperation)
    {
        LoginUser admin = AdminLoginUtils.getInstance().getManager();
        fSysCoinOperation.setFadminid(admin.getUserId());
        return toAjax(fSysCoinOperationService.insertFSysCoinOperation(fSysCoinOperation));
    }

    /**
     * 修改系统钱包操作日志
     */
    @PreAuthorize("@ss.hasPermi('crypto:sysWallet:edit')")
    @Log(title = "系统钱包操作日志", businessType = BusinessType.UPDATE)
    @PutMapping("/op")
    public AjaxResult edit(@RequestBody FSysCoinOperation fSysCoinOperation)
    {
        LoginUser admin = AdminLoginUtils.getInstance().getManager();
        fSysCoinOperation.setFadminid(admin.getUserId());
        return toAjax(fSysCoinOperationService.updateFSysCoinOperation(fSysCoinOperation));
    }


}
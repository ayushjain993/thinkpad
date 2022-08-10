package io.uhha.web.controller.store;

import java.util.List;

import io.swagger.annotations.Api;
import io.uhha.coin.common.Enum.LogAdminActionEnum;
import io.uhha.coin.common.util.MQSend;
import io.uhha.common.utils.SecurityUtils;
import io.uhha.web.utils.AdminLoginUtils;
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
import io.uhha.store.domain.TStoreInfo;
import io.uhha.store.service.ITStoreInfoService;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.common.core.page.TableDataInfo;

/**
 * 店铺信息Controller
 * 
 * @author ruoyi
 * @date 2021-05-23
 */
@Api(tags = "店铺信息Controller")
@RestController
@RequestMapping("/store/TStoreInfo")
public class TStoreInfoController extends BaseController
{
    @Autowired
    private ITStoreInfoService tStoreInfoService;

    @Autowired
    private MQSend mqSend;

    /**
     * 查询店铺信息列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(TStoreInfo tStoreInfo)
    {
        startPage();
        List<TStoreInfo> list = tStoreInfoService.selectTStoreInfoList(tStoreInfo);
        return getDataTable(list);
    }

    /**
     * 导出店铺信息列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreInfo:export')")
    @Log(title = "店铺信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TStoreInfo tStoreInfo)
    {
        List<TStoreInfo> list = tStoreInfoService.selectTStoreInfoList(tStoreInfo);
        ExcelUtil<TStoreInfo> util = new ExcelUtil<TStoreInfo>(TStoreInfo.class);
        return util.exportExcel(list, "店铺信息数据");
    }

    /**
     * 获取店铺信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreInfo:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tStoreInfoService.selectTStoreInfoById(id));
    }

    /**
     * 新增店铺信息
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreInfo:add')")
    @Log(title = "店铺信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TStoreInfo tStoreInfo)
    {
        tStoreInfo.setCreateBy(SecurityUtils.getUsername());
        return toAjax(tStoreInfoService.insertTStoreInfo(tStoreInfo));
    }

    /**
     * 修改店铺信息
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreInfo:edit')")
    @Log(title = "店铺信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TStoreInfo tStoreInfo)
    {
        tStoreInfo.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(tStoreInfoService.updateTStoreInfo(tStoreInfo));
    }

    /**
     * 删除店铺信息
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreInfo:remove')")
    @Log(title = "店铺信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tStoreInfoService.deleteTStoreInfoByIds(ids));
    }

    /**
     * 店铺开设申请批准
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreInfo:edit')")
    @Log(title = "店铺信息", businessType = BusinessType.UPDATE)
    @PutMapping("/auditseller")
    public AjaxResult approve(@RequestBody TStoreInfo tStoreInfo)
    {
        mqSend.SendAdminAction(AdminLoginUtils.getInstance().getManagerId(), LogAdminActionEnum.STORE_APPROVED, "");
        tStoreInfo.setUpdateBy(SecurityUtils.getUsername());
        return AjaxResult.success(tStoreInfoService.passStoreAudit(tStoreInfo));
    }

    /**
     * 店铺开设申请拒绝
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreInfo:edit')")
    @Log(title = "店铺信息", businessType = BusinessType.UPDATE)
    @PutMapping("/auditseller/refuse")
    public AjaxResult refuse(@RequestBody TStoreInfo tStoreInfo)
    {
        mqSend.SendAdminAction(AdminLoginUtils.getInstance().getManagerId(), LogAdminActionEnum.STORE_REFUSED, "");
        tStoreInfo.setUpdateBy(SecurityUtils.getUsername());
        return AjaxResult.success(tStoreInfoService.refuseStoreAudit(tStoreInfo));
    }

    /**
     * 关闭店铺
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreInfo:edit')")
    @Log(title = "店铺信息", businessType = BusinessType.UPDATE)
    @PutMapping("/close")
    public AjaxResult close(@RequestBody TStoreInfo tStoreInfo)
    {
        //TODO
        return AjaxResult.success(1);
    }

    /**
     * 店铺启用
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreInfo:edit')")
    @Log(title = "店铺启用", businessType = BusinessType.UPDATE)
    @PutMapping("/freeze")
    public AjaxResult freeze(@RequestBody TStoreInfo tStoreInfo)
    {
        mqSend.SendAdminAction(AdminLoginUtils.getInstance().getManagerId(), LogAdminActionEnum.STORE_FROZEN, "");
        tStoreInfo.setUpdateBy(SecurityUtils.getUsername());
        return AjaxResult.success(tStoreInfoService.freeze(tStoreInfo));
    }

    /**
     * 店铺禁用
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreInfo:edit')")
    @Log(title = "店铺禁用", businessType = BusinessType.UPDATE)
    @PutMapping("/unfreeze")
    public AjaxResult unfreeze(@RequestBody TStoreInfo tStoreInfo)
    {
        mqSend.SendAdminAction(AdminLoginUtils.getInstance().getManagerId(), LogAdminActionEnum.STORE_UNFROZEN, "");
        tStoreInfo.setUpdateBy(SecurityUtils.getUsername());
        return AjaxResult.success(tStoreInfoService.unfreeze(tStoreInfo));
    }
}

package io.uhha.web.controller.store;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.SecurityUtils;
import io.uhha.store.domain.TStoreInfo;
import io.uhha.store.service.ITStoreInfoService;
import io.uhha.web.utils.AdminLoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 获取店铺信息详细信息
     */
    @GetMapping
    public AjaxResult getInfo()
    {
        Long storeId = AdminLoginUtils.getInstance().getStoreId();
        return AjaxResult.success(tStoreInfoService.selectTStoreInfoById(storeId));
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

}

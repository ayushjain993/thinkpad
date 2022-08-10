package io.uhha.web.controller.system;

import io.uhha.coin.common.framework.redis.RedisCryptoHelper;
import io.uhha.common.core.redis.RedisConstant;
import io.uhha.common.core.redis.RedisObject;
import io.uhha.coin.system.domain.FSystemArgs;
import io.uhha.coin.system.service.IFSystemArgsService;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 业务参数Controller
 *
 * @author uhha
 * @date 2021-09-27
 */
@RestController
@RequestMapping("/system/sysArgs")
public class SysArgsController extends BaseController
{
    @Autowired
    private IFSystemArgsService fSystemArgsService;

    @Autowired
    private RedisCryptoHelper redisCryptoHelper;

    /**
     * 查询业务参数列表
     */
    @PreAuthorize("@ss.hasPermi('system:sysArgs:list')")
    @GetMapping("/list")
    public TableDataInfo list(FSystemArgs fSystemArgs)
    {
        startPage();
        List<FSystemArgs> list = fSystemArgsService.selectFSystemArgsList(fSystemArgs);
        return getDataTable(list);
    }

    /**
     * 导出业务参数列表
     */
    @PreAuthorize("@ss.hasPermi('system:sysArgs:export')")
    @Log(title = "业务参数", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(FSystemArgs fSystemArgs)
    {
        List<FSystemArgs> list = fSystemArgsService.selectFSystemArgsList(fSystemArgs);
        ExcelUtil<FSystemArgs> util = new ExcelUtil<FSystemArgs>(FSystemArgs.class);
        return util.exportExcel(list, "业务参数数据");
    }

    /**
     * 获取业务参数详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:sysArgs:query')")
    @GetMapping(value = "/{fid}")
    public AjaxResult getInfo(@PathVariable("fid") Integer fid)
    {
        return AjaxResult.success(fSystemArgsService.selectFSystemArgsByFid(fid));
    }

    /**
     * 新增业务参数
     */
    @PreAuthorize("@ss.hasPermi('system:sysArgs:add')")
    @Log(title = "业务参数", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FSystemArgs fSystemArgs)
    {
        int ret = fSystemArgsService.insertFSystemArgs(fSystemArgs);
        refreshRedis(fSystemArgs);
        return toAjax(ret);
    }

    /**
     * 修改业务参数
     */
    @PreAuthorize("@ss.hasPermi('system:sysArgs:edit')")
    @Log(title = "业务参数", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FSystemArgs fSystemArgs)
    {
        int ret = fSystemArgsService.updateFSystemArgs(fSystemArgs);
        refreshRedis(fSystemArgs);
        return toAjax(ret);
    }

    private void refreshRedis(FSystemArgs fSystemArgs){
        redisCryptoHelper.delete(RedisConstant.ARGS_KET + fSystemArgs.getFkey());
        RedisObject args = new RedisObject();
        args.setExtObject(fSystemArgs.getFvalue());
        redisCryptoHelper.setNoExpire(RedisConstant.ARGS_KET + fSystemArgs.getFkey(), args);
    }
}

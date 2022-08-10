package io.uhha.web.controller.setting;


import io.swagger.annotations.ApiOperation;
import io.uhha.common.core.controller.BaseController;
import io.uhha.setting.bean.PlatformFeeSet;
import io.uhha.setting.service.ILsPlatformFeeSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 平台费用设置Controller
 *
 * @author peter
 * @date 2022-01-26
 */
@RestController
@RequestMapping("/setting/platFeeSetting")
public class LsPlatformFeeSettingController extends BaseController
{
    @Autowired
    private ILsPlatformFeeSettingService lsPlatformFeeSettingService;

    /**
     * 查询平台费用设置
     *
     * @return 支付设置信息
     */
    @GetMapping
    @ApiOperation(value = "查询平台费用设置", notes = "查询平台费用设置（需要认证）")
    @PreAuthorize("@ss.hasPermi('setting:platFeeSetting:list')")
    public PlatformFeeSet queryPlatformFeeSet() {
        return lsPlatformFeeSettingService.queryPlatformFeeSet();
    }

    /**
     * 修改平台费用设置
     *
     * @param platformFeeSet 实体类参数
     * @return 成功>=1 否则失败 -1
     */
    @PostMapping
    @ApiOperation(value = "修改平台费用设置", notes = "修改平台费用设置（需要认证）")
    @PreAuthorize("@ss.hasPermi('setting:LsPaySetting:add')")
    public int editPlatformFeeSet(@RequestBody PlatformFeeSet platformFeeSet) {
        return lsPlatformFeeSettingService.editPlatformFeeSet(platformFeeSet);
    }


}
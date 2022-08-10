package io.uhha.web.controller.member;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.enums.BusinessType;
import io.uhha.integral.domain.PointSetting;
import io.uhha.integral.service.PointSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "会员积分配置控制器")
@RestController
@RequestMapping("/member/PointSetting")
public class PointSettingController extends BaseController {

    @Autowired
    private PointSettingService pointSettingService;

    @PreAuthorize("@ss.hasPermi('member:PointSeting:edit')")
    @Log(title = "积分配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PointSetting pointSetting) {
        return toAjax(pointSettingService.updatePointSetting(pointSetting));
    }

    @PreAuthorize("@ss.hasPermi('member:PointSeting:query')")
    @GetMapping
    public AjaxResult query() {
        return AjaxResult.success(pointSettingService.queryPointSetting());
    }

}

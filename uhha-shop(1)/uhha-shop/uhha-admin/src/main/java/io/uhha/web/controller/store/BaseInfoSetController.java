package io.uhha.web.controller.store;

import io.uhha.common.annotation.UnAuth;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.enums.OnOffEnum;
import io.uhha.setting.bean.BaseInfoSet;
import io.uhha.setting.service.BaseInfoSetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * 系统设置控制器
 */
@RestController
@Api(tags = "系统设置接口")
@Slf4j
public class BaseInfoSetController {

    /**
     * 注入系统设置服务
     */
    @Autowired
    private BaseInfoSetService baseInfoSetService;

    /**
     * 查询基本信息和高级信息设置
     *
     * @return 基本信息和高级信息设置实体类
     */
    @RequestMapping("/querybaseinfoset")
    @ResponseBody
    @ApiOperation(value = "查询基本信息和高级信息设置", notes = "查询基本信息和高级信息设置（不需要认证）", httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(code = 200, message = "基本信息和高级信息设置实体类", response = BaseInfoSet.class)
    })
    public AjaxResult queryBaseInfoSet() {
        return AjaxResult.success(baseInfoSetService.queryBaseInfoSet());
    }

    /**
     * 查询商铺是否需要审核内容
     *
     * @return 查询商铺是否需要审核内容
     */
    @GetMapping("/goodsauditset")
    @ResponseBody
    @ApiOperation(value = "查询商铺是否需要审核内容", notes = "查询商铺是否需要审核内容（需要认证）", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询商铺是否需要审核内容", response = String.class)
    })
    public AjaxResult goodsauditset() {
        return AjaxResult.success(baseInfoSetService.queryBaseInfoSet().getStoreSpuAudit());
    }

    /**
     * 商铺提交的SPU是否需要审核 （开 0， 关 1）
     *
     * @return 商铺提交的SPU是否需要审核
     */
    @PostMapping("/goodsauditset")
    @ApiOperation(value = "商铺提交的SPU是否需要审核", notes = "商铺提交的SPU是否需要审核（需要认证）", httpMethod = "POST")
    public AjaxResult goodsauditset(@RequestParam("audit") String audit) {
        if(!OnOffEnum.ON.getCode().equalsIgnoreCase(audit)&&!OnOffEnum.OFF.getCode().equalsIgnoreCase(audit)){
            log.error("invalid param audit. [{}]", audit);
            return AjaxResult.error("invalid param audit.");
        }
        return AjaxResult.success(baseInfoSetService.setAuditSwitch(audit));
    }


}

package io.uhha.util;

import io.uhha.common.annotation.UnAuth;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.setting.bean.BaseInfoSet;
import io.uhha.setting.service.BaseInfoSetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 系统设置控制器
 */
@Controller
@Api(tags = "系统设置接口")
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
    @UnAuth
    @ApiOperation(value = "查询基本信息和高级信息设置", notes = "查询基本信息和高级信息设置（不需要认证）", httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(code = 200, message = "基本信息和高级信息设置实体类", response = BaseInfoSet.class)
    })
    public AjaxResult queryBaseInfoSet() {
        return AjaxResult.success(baseInfoSetService.queryBaseInfoSet());
    }
}

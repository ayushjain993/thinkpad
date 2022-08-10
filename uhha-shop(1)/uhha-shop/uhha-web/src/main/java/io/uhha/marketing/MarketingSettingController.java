package io.uhha.marketing;


import io.uhha.common.annotation.UnAuth;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.marketing.domain.MarketingSetting;
import io.uhha.marketing.service.MarketingSettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 营销设置控制器
 */
@RestController
@Api(tags = "营销设置接口")
public class MarketingSettingController {

    /**
     * 注入营销设置服务
     */
    @Autowired
    private MarketingSettingService marketingSettingService;

    /**
     * 查询营销设置
     *
     * @return 营销设置实体
     */
    @UnAuth
    @RequestMapping(value = "/querymarketingsetting", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "查询营销设置", notes = "查询营销设置（不需要认证）", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "营销设置实体", response = MarketingSetting.class)
    })
    public AjaxResult queryMarketingSetting() {
        return AjaxResult.success(marketingSettingService.queryMarketingSetting());
    }
}

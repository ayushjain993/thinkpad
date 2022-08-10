package io.uhha.support;

import io.swagger.annotations.*;
import io.uhha.common.annotation.UnAuth;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.order.domain.OmsOrder;
import io.uhha.store.domain.TStoreInfo;
import io.uhha.store.service.ITStoreInfoService;
import io.uhha.util.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by bob on 2022/8/23.
 * 客户支持控制器
 */
@Api(tags = "店铺信息接口")
@RestController
@RequestMapping("/support")
public class CustomerSupportController {
    @Autowired
    private ITStoreInfoService storeInfoService;

    @Autowired
    private ICustomerSupportService customerSupportService;

    /**
     * 查询平台支持信息
     *
     * @return 返回平台运营的IM的UserCode
     */
    @RequestMapping(value = "/platform")
    @UnAuth
    @ResponseBody
    @ApiOperation(value = "查询平台支持信息", notes = "查询平台支持信息（不需要认证）", httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回平台运营的IM的UserCode", response = OmsOrder.class)
    })
    public AjaxResult getPlatformSupport(HttpServletRequest request) {
        String userCode = customerSupportService.getSupportByStoreId(CommonConstant.ADMIN_STOREID);
        if(userCode==null){
            return AjaxResult.error("Platform userCode was not found");
        }
        return AjaxResult.success("",userCode);
    }

    /**
     * 查询平台支持信息
     *
     * @return 返回平台运营的IM的UserCode
     */
    @RequestMapping(value = "/store")
    @ResponseBody
    @ApiOperation(value = "查询平台支持信息", notes = "查询平台支持信息（需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "long", name = "storeId", value = "店铺id"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回平台运营的IM的UserCode", response = OmsOrder.class)
    })
    public AjaxResult getSupportByOrderId( Long storeId) {
        String userCode = customerSupportService.getSupportByStoreId(storeId);
        if(userCode==null){
            return AjaxResult.error("Support userCode:{} was not found", storeId);
        }
        return AjaxResult.success("",userCode);
    }
}

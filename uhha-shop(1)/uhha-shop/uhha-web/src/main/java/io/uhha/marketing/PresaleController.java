package io.uhha.marketing;


import io.uhha.common.annotation.UnAuth;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.marketing.domain.MarketingCate;
import io.uhha.marketing.domain.MarketingPic;
import io.uhha.marketing.domain.PreSaleShow;
import io.uhha.marketing.service.MarketingCateService;
import io.uhha.marketing.service.MarketingPicService;
import io.uhha.marketing.service.PreSaleShowService;
import io.uhha.util.CommonConstant;
import io.uhha.util.PageHelper;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by mj on 18/7/11
 * 预售控制器
 */
@RestController
@Api(tags = "预售接口")
public class PresaleController {

    /**
     * 注入促销图片服务接口
     */
    @Autowired
    private MarketingPicService marketingPicService;

    /**
     * 注入促销分类服务接口
     */
    @Autowired
    private MarketingCateService marketingCateService;

    /**
     * 注入预售活动服务接口
     */
    @Autowired
    private PreSaleShowService preSaleShowService;


    /**
     * 查询预售促销图片信息
     *
     * @param storeId 店铺id
     * @return 预售促销图片信息
     */
    @RequestMapping(value = "/querypresalepic", method = RequestMethod.GET)
    @ApiOperation(value = "查询预售促销图片信息", notes = "查询预售促销图片信息（不需要认证）")
    @ResponseBody
    @UnAuth
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "storeId", value = "店铺id"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "预售促销图片信息", response = MarketingPic.class)
    })
    public AjaxResult queryPreSalePic(Long storeId) {
        return AjaxResult.success(marketingPicService.queryMarketingPic(CommonConstant.PRESALE_MARKETING_PIC_TYPE, storeId));
    }

    /**
     * 查询预售促销分类列表
     *
     * @param storeId 店铺id
     * @return 返回预售促销分类列表
     */
    @RequestMapping(value = "/querypresalecates", method = RequestMethod.GET)
    @ResponseBody
    @UnAuth
    @ApiOperation(value = "查询预售促销分类列表", notes = "查询预售促销分类列表（不需要认证）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "storeId", value = "店铺id"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "预售促销分类列表", response = MarketingCate.class)
    })
    public AjaxResult queryMarketingCateList(@RequestParam("storeId") Long storeId) {
        return AjaxResult.success(marketingCateService.queryMarketingCatesByTypeAndStoreId(CommonConstant.PRESALE_MARKETING_CATE, storeId));
    }

    /**
     * 分页查询预售促销列表
     *
     * @param pageHelper 分页帮助类
     * @param cateId     促销分类id
     * @return 返回预售促销列表
     */
    @RequestMapping(value = "/querypresales", method = RequestMethod.GET)
    @ResponseBody
    @UnAuth
    @ApiOperation(value = "分页查询预售促销列表", notes = "分页查询预售促销列表（不需要认证）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageNum", value = "当前页"),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageSize", value = "每页显示的记录数"),
            @ApiImplicitParam(paramType = "query", dataType = "long", name = "cateId", value = "促销分类id"),
            @ApiImplicitParam(paramType = "query", dataType = "long", name = "storeId", value = "店铺id"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回预售促销列表", response = PreSaleShow.class)
    })
    public AjaxResult queryPreSaleList(@ApiIgnore PageHelper<PreSaleShow> pageHelper, Long cateId, Long storeId) {
        return AjaxResult.success(preSaleShowService.queryPreSalesForSite(pageHelper, cateId, storeId));
    }

    /**
     * 查询预售促销规则信息
     *
     * @return 预售促销规则信息
     */
    @RequestMapping(value = "/querypresalerule", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "查询预售促销规则信息", notes = "查询预售促销规则信息（不需要认证）")
    @UnAuth
    @ApiResponses({
            @ApiResponse(code = 200, message = "预售促销规则信息", response = MarketingPic.class)
    })
    public AjaxResult queryPreSaleRule() {
        return AjaxResult.success(marketingPicService.queryMarketingPic(CommonConstant.PRESALE_MARKETING_PIC_TYPE, CommonConstant.ADMIN_STOREID));
    }

}

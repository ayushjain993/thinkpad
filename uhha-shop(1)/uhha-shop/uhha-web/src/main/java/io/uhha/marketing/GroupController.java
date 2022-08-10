package io.uhha.marketing;


import io.uhha.common.annotation.UnAuth;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.marketing.domain.GroupMarketingShow;
import io.uhha.marketing.domain.MarketingCate;
import io.uhha.marketing.domain.MarketingPic;
import io.uhha.marketing.service.GroupMarketingShowService;
import io.uhha.marketing.service.MarketingCateService;
import io.uhha.marketing.service.MarketingPicService;
import io.uhha.util.CommonConstant;
import io.uhha.util.PageHelper;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 拼团促销控制器
 *
 * @author mj created on 2020/6/2
 */
@RestController
@Api(tags = "拼团促销接口")
public class GroupController {

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
     * 注入拼团促销服务接口
     */
    @Autowired
    private GroupMarketingShowService groupMarketingShowService;


    /**
     * 查询拼团促销图片信息
     *
     * @param storeId 店铺id
     * @return 拼团促销图片信息
     */
    @RequestMapping(value = "/querygrouppic", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "查询拼团促销图片信息", notes = "查询拼团促销图片信息（不需要认证）")
    @UnAuth
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "storeId", value = "店铺id"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "拼团促销图片信息", response = MarketingPic.class)
    })
    public AjaxResult queryGroupPic(@RequestParam("storeId") Long storeId) {
        return AjaxResult.success(marketingPicService.queryMarketingPic(CommonConstant.GROUP_MARKETING_PIC_TYPE, storeId));
    }

    /**
     * 查询拼团促销分类列表
     *
     * @param storeId 店铺id
     * @return 返回拼团促销分类列表
     */
    @RequestMapping(value = "/querygroupcatelist", method = RequestMethod.GET)
    @ResponseBody
    @UnAuth
    @ApiOperation(value = "查询拼团促销分类列表", notes = "查询拼团促销分类列表（不需要认证）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "long", name = "storeId", value = "店铺id"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "拼团促销分类列表", response = MarketingCate.class)
    })
    public AjaxResult queryGroupCateList(@RequestParam("storeId") Long storeId) {
        return AjaxResult.success(marketingCateService.queryMarketingCatesByTypeAndStoreId(CommonConstant.GROUP_MARKETING_CATE, storeId));
    }

    /**
     * 分页查询拼团促销列表
     *
     * @param pageHelper 分页帮助类
     * @param cateId     促销分类id
     * @return 返回拼团促销列表
     */
    @RequestMapping(value = "/querygroupmarketinglist", method = RequestMethod.POST)
    @ResponseBody
    @UnAuth
    @ApiOperation(value = "分页查询拼团促销列表", notes = "分页查询拼团促销列表（不需要认证）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageNum", value = "当前页"),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageSize", value = "每页显示的记录数"),
            @ApiImplicitParam(paramType = "query", dataType = "long", name = "cateId", value = "促销分类id"),
            @ApiImplicitParam(paramType = "query", dataType = "long", name = "storeId", value = "店铺id"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回拼团促销列表", response = GroupMarketingShow.class)
    })
    public AjaxResult queryGroupMarketingList(@ApiIgnore PageHelper<GroupMarketingShow> pageHelper, Long cateId, Long storeId) {
        return AjaxResult.success(groupMarketingShowService.queryGroupsForSite(pageHelper, cateId, storeId));
    }

}

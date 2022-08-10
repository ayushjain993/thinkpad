package io.uhha.goods;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.*;
import io.uhha.appletsutil.AppletsLoginUtils;
import io.uhha.appletsutil.Claims;
import io.uhha.appletsutil.ResultCode;
import io.uhha.appletsutil.UnAuthorizedException;
import io.uhha.common.annotation.RateLimiter;
import io.uhha.common.annotation.UnAuth;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.redis.RedisCache;
import io.uhha.common.enums.LimitType;
import io.uhha.common.enums.StatusEnum;
import io.uhha.common.utils.bean.WeChatAppletCodeRequest;
import io.uhha.goods.domain.*;
import io.uhha.goods.service.*;
import io.uhha.goods.vo.*;
import io.uhha.order.service.RecommendSkuService;
import io.uhha.order.vo.RecommendSku;
import io.uhha.util.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * 商品控制器
 */
@RestController
@Api(tags = "商品接口")
@Slf4j
public class SpuController {

    /**
     * 注入评论服务接口
     */
    @Autowired
    private IPmsCommentService commentService;

    /**
     * 商品服务api接口
     */
    @Autowired
    private SpuServiceApi spuServiceApi;

    /**
     * 注入规格服务接口
     */
    @Autowired
    private IPmsSpecService specService;

    /**
     * 注入单品服务接口
     */
    @Autowired
    private IPmsSkuService skuService;

    /**
     * 注入redis服务
     */
    @Autowired
    private RedisCache redisService;
    @Autowired
    private IPmsGoodsService pmsGoodsService;
    /**
     * 注入分类服务接口
     */
    @Autowired
    private IPmsCategoryService categoryService;

    /**
     * 注入推荐单品服务
     */
    @Autowired
    private RecommendSkuService recommendSkuService;
    @Autowired
    private IPmsBrandService pmsBrandService;
    @Autowired
    private IPmsTypeService typeService;

    /**
     * 查询一级二级分类
     *
     * @return 返回分类集合
     */
    @UnAuth
    @GetMapping("/queryAllFirstAndSecondCategory")
    @ApiOperation(value = "查询一级二级分类", notes = "查询一级二级分类（不需要认证）")
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回分类集合", response = PmsCategory.class)
    })
    public AjaxResult queryAllFirstAndSecondCategory() {
        return AjaxResult.success(categoryService.queryAllFirstAndSecondCategory());
    }

    /**
     * 分页查询商品列表
     *
     * @param pageHelper    分页帮助类
     * @return 返回用户积分商城订单列表
     */
    @UnAuth
    @RequestMapping(value = "/goodsList")
    @ResponseBody
    @RateLimiter(key = "goodsList", time = 1, count = 100, limitType = LimitType.IP)
    public AjaxResult querySimpleSpus(@ApiIgnore PageHelper<PmsGoods> pageHelper, @ApiIgnore SpuSearchCondition spuSearchCondition) {

//        spuSearchCondition.setIsVirtual(0);
        spuSearchCondition.setShelvesStatus("1");
        spuSearchCondition.setStatus(StatusEnum.AuditType.SUCCESS.code().toString());
        return AjaxResult.success(pmsGoodsService.querySimpleSpus(pageHelper, spuSearchCondition));
    }

    /**
     * 分页查询可带货商品列表
     *
     * @param pageHelper    分页帮助类
     * @return 返回用户积分商城订单列表
     */
    @UnAuth
    @RequestMapping(value = "/dropshipGoodsList")
    @ResponseBody
    @RateLimiter(key = "dropshipGoodsList", time = 1, count = 100, limitType = LimitType.IP)
    public AjaxResult queryDropshipSimpleSpus(@ApiIgnore PageHelper<PmsGoods> pageHelper, @ApiIgnore SpuSearchCondition spuSearchCondition) {

        spuSearchCondition.setIsDropship(1);
        spuSearchCondition.setShelvesStatus("1");
        spuSearchCondition.setStatus(StatusEnum.AuditType.SUCCESS.code().toString());
        return AjaxResult.success(pmsGoodsService.querySimpleSpus(pageHelper, spuSearchCondition));
    }

    /**
     * 查询品牌列表
     * @param brand
     * @return
     */
    @UnAuth
    @RequestMapping(value = "/brandList")
    @ResponseBody
    public AjaxResult cateList(PmsBrand brand) {
        return AjaxResult.success(pmsBrandService.selectPmsBrandList(brand));
    }

    /**
     * 查询分类列表
     * @param type
     * @return
     */
    @RequestMapping(value = "/typeList")
    @ResponseBody
    public AjaxResult typeList(PmsType type) {
        return AjaxResult.success(typeService.selectPmsTypeList(type));
    }

    /**
     * 查询推荐的商品
     *
     * @param num 个数
     * @return 返回推荐的商品
     */
    @GetMapping("/recommendskus")
    @UnAuth
    @ResponseBody
    public AjaxResult queryRecommendSkus(@RequestParam(value = "num", defaultValue = "6") Integer num) {
        return AjaxResult.success(recommendSkuService.queryRecommendSkus(num));
    }

    /**
     * 查询商品详情
     *
     * @param skuId 单品id
     * @return 返回商品详情
     */
    @RequestMapping(value = "/queryspudetail")
    @UnAuth
    @ResponseBody
    @ApiOperation(value = "查询商品详情", notes = "查询商品详情（不需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "skuId", value = "单品id"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回商品详情", response = SpuDetail.class)
    })
    public AjaxResult querySpuDetail(HttpServletRequest request, String skuId) {
        long customerId = 0;
        if (!ObjectUtils.isEmpty(getClaims(getToken(request)))) {
            customerId = getClaims(getToken(request)).getCustomerId();
        }
        return AjaxResult.success(spuServiceApi.querySpuDetail(skuId, customerId, SpuDetailItem.COUPON, SpuDetailItem.FOLLOW, SpuDetailItem.STORE_SCORE, SpuDetailItem.MOBILE_DESC).orElseGet(() -> null));
    }

    /**
     * 查询商品详情
     *
     * @param goodsId 商品id
     * @return 返回商品详情
     */
    @RequestMapping(value = "/queryGoodsDetail")
    @UnAuth
    @ResponseBody
    @RateLimiter(key = "queryGoodsDetail", time = 1, count = 100, limitType = LimitType.IP)
    public AjaxResult queryGoodsDetail(HttpServletRequest request, Long goodsId) {
        long customerId = 0;
        if (!ObjectUtils.isEmpty(getClaims(getToken(request)))) {
            customerId = getClaims(getToken(request)).getCustomerId();
        }
        return AjaxResult.success(spuServiceApi.queryGoodsDetail(goodsId, customerId, SpuDetailItem.COUPON, SpuDetailItem.FOLLOW, SpuDetailItem.STORE_SCORE, SpuDetailItem.MOBILE_DESC).orElseGet(() -> null));
    }

    /**
     * 查询商品的规格信息
     *
     * @param spuId 商品id
     * @return 返回商品的规格信息
     */
    @RequestMapping(value = "queryspuspecs")
    @UnAuth
    @ResponseBody
    @ApiOperation(value = "查询商品的规格信息", notes = "查询商品的规格信息（不需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "long", name = "spuId", value = "商品id"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回商品的规格信息", response = PmsSpec.class)
    })
    public AjaxResult querySpuSpecs(long spuId) {
        return AjaxResult.success(specService.querySpuSpecs(spuId));
    }

    /**
     * 查询商品下所有单品的规格信息
     *
     * @param spuId 商品id
     * @return 返回商品下面所有单品的规格信息
     */
    @RequestMapping(value = "queryskuspecs")
    @UnAuth
    @ResponseBody
    @ApiOperation(value = "查询商品下所有单品的规格信息", notes = "查询商品下所有单品的规格信息（不需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "long", name = "spuId", value = "商品id"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回商品下面所有单品的规格信息", response = PmsSku.class)
    })
    public AjaxResult querySkuSpecs(long spuId) {
        return AjaxResult.success(skuService.queryAllSkuSpecs(spuId));
    }

    /**
     * 查询单品评论
     *
     * @param pageHelper 分类帮助类
     * @param spuId      单品id
     * @param type       评论类型 -1 全部 1 好评 2 中评 3 差评
     * @return 返回评论信息
     */
    @UnAuth
    @RequestMapping(value = "/queryskucomments")
    @ResponseBody
    @ApiOperation(value = "查询单品评论", notes = "查询单品评论（不需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "pageNum", value = "当前页"),
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "pageSize", value = "每页显示的记录数"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "spuId", value = "单品id"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "type", value = "评论类型 -1 全部 1 好评 2 中评 3 差评"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回评论信息", response = PmsComment.class)
    })
    public AjaxResult querySkuComments(@ApiIgnore PageHelper<PmsComment> pageHelper, @ApiIgnore String spuId, @ApiIgnore String type) {
        return AjaxResult.success(commentService.querySkuComments(pageHelper, spuId, type));
    }


    /**
     * 查询商品的评论概括
     *
     * @param spuId 单品id
     * @return 返回商品的评论概括
     */
    @UnAuth
    @RequestMapping(value = "/querycommentsummarize")
    @ResponseBody
    @ApiOperation(value = "查询商品的评论概括", notes = "查询商品的评论概括（不需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "skuId", value = "单品id"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回商品的评论概括", response = CommentSummarize.class)
    })
    public AjaxResult queryCommentSummarize(String spuId) {
        return AjaxResult.success(commentService.queryCommentSummarize(spuId));
    }


    /**
     * 查询商品组合
     *
     * @param skuId 单品id
     * @return 返回商品组合信息
     */
    @UnAuth
    @RequestMapping("/querycombination")
    @ResponseBody
    @ApiOperation(value = "查询商品组合", notes = "查询商品组合（不需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "skuId", value = "单品id"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回商品组合信息", response = CombinationDetail.class)
    })
    public AjaxResult queryCombination(HttpServletRequest request, String skuId) {
        return AjaxResult.success(spuServiceApi.queryGoodsCombinationBySkuId(skuId, AppletsLoginUtils.getInstance().getCustomerId(request)));
    }

    /**
     * 计算运费
     *
     * @param skuId   商品id
     * @param storeId 店铺id
     * @param cityId  城市id
     * @param num     商品数量
     * @return 运费
     */
    @UnAuth
    @GetMapping("/calculatefreight")
    @ResponseBody
//    @ApiOperation(value = "计算运费", notes = "计算运费（不需要认证）", httpMethod = "GET")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "form", dataType = "String", name = "skuId", value = "单品id"),
//            @ApiImplicitParam(paramType = "form", dataType = "Long", name = "storeId", value = "店铺id"),
//            @ApiImplicitParam(paramType = "form", dataType = "Long", name = "cityId", value = "城市id"),
//            @ApiImplicitParam(paramType = "form", dataType = "Integer", name = "num", value = "商品数量", defaultValue = "1"),
//    })
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "运费", response = BigDecimal.class)
//    })
    public AjaxResult calculateFreight(@RequestParam("skuId") String skuId,
                                       @RequestParam("storeId") Long storeId,
                                       @RequestParam("cityId") Long cityId,
                                       @RequestParam("num") Integer num) {
        return AjaxResult.success(spuServiceApi.calculateFreight(skuId, storeId, cityId, num));
    }

    /**
     * 获取分享微信小程序码
     *
     * @param weChatAppletCodeRequest 生成微信小程序码请求实体类
     * @return 分享微信小程序码
     */
    @UnAuth
    @RequestMapping("/getwechatappletcode")
    @ResponseBody
    @ApiOperation(value = "获取分享微信小程序码", notes = "获取分享微信小程序码（不需要认证）", httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(code = 200, message = "分享微信小程序码", response = String.class)
    })
    public AjaxResult getWeChatAppletCode(@RequestBody WeChatAppletCodeRequest weChatAppletCodeRequest) {
        return AjaxResult.success(spuServiceApi.getWeChatAppletCode(weChatAppletCodeRequest));
    }

    /**
     * 获取token
     *
     * @param request request
     * @return token
     */
    private String getToken(HttpServletRequest request) {
        // 认证信息在header 中的key
        final String authHeader = request.getHeader("Authorization");

        if (Objects.isNull(authHeader) || !authHeader.startsWith("Bearer")) {
            log.info("getClaims fail :Authorization fail ");
            throw new UnAuthorizedException(ResultCode.WX_NOT_AUTHORIZED);
        }
        return authHeader.length() >= 7 ? authHeader.substring(7) : authHeader.substring(6);
    }

    /**
     * 获取小程序凭证实体
     *
     * @param token token
     * @return 小程序凭证实体
     */
    private Claims getClaims(String token) {
        return JSONObject.parseObject(redisService.getCacheObject(token), Claims.class);
    }
}

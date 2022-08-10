package io.uhha.index;


import io.uhha.common.annotation.UnAuth;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.goods.domain.PmsGoods;
import io.uhha.goods.domain.PmsType;
import io.uhha.goods.service.IPmsGoodsService;
import io.uhha.goods.service.IPmsTypeService;
import io.uhha.sms.domain.*;
import io.uhha.sms.service.*;
import io.uhha.system.domain.SysNotice;
import io.uhha.system.service.ISysNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/2 15:02
 * @Description:
 */
@Slf4j
@RestController
@Api(tags = "app和h5首页控制器")
public class IndexController extends BaseController {


    @Resource
    private ISmsHomeNewProductService homeNewProductService;
    @Resource
    private ISmsHomeRecommendProductService homeRecommendProductService;
    @Resource
    private ISmsHomeBrandService homeBrandService;
    @Resource
    private ISmsHomeRecommendSubjectService homeRecommendSubjectService;
    @Resource
    private ISmsHomeAdvertiseService homeAdvertiseService;
    @Resource
    private ISysNoticeService sysNoticeService;
    @Resource
    private IPmsTypeService typeService;
    @Autowired
    private IPmsGoodsService pmsGoodsService;

    /**
     * 查询商品列表
     */
    @UnAuth
    @ResponseBody
    @GetMapping("/list")
    public AjaxResult list(PmsGoods pmsGoods) {
        List<PmsGoods> list = pmsGoodsService.querySpus(pmsGoods);
        return AjaxResult.success(list);
    }

    @UnAuth
    @ApiOperation(value = "查询首页商品属性分类")
    @GetMapping(value = "/type/list")
    public Object getTypeList(PmsType type,
                              @RequestParam(value = "pageSize", required = false, defaultValue = "4") Integer pageSize,
                              @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        // startPage();
        return AjaxResult.success(typeService.selectPmsTypeList(type));
    }

    @UnAuth
    @ApiOperation(value = "查询首页通知")
    @GetMapping(value = "/notice/list")
    public Object getNoticeList(SysNotice notice,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "4") Integer pageSize,
                                @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        startPage();
        notice.setStatus("1");
        return AjaxResult.success(sysNoticeService.selectNoticeList(notice));
    }

    @UnAuth
    @GetMapping(value = "/notice/detail")
    @ApiOperation(value = "查询公告详情信息")
    public Object getNoticeDetail(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        SysNotice goods = sysNoticeService.selectNoticeById(id);
        return AjaxResult.success(goods);
    }

    @UnAuth
    @GetMapping(value = "/homeNewProduct/list")
    public Object homeNewProduct(SmsHomeNewProduct homeNewProduct,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "4") Integer pageSize,
                                 @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                 @RequestParam(value = "storeId", required = false) Long storeId) {
        startPage();
        homeNewProduct.setStoreId(storeId);
        return AjaxResult.success(homeNewProductService.selectSmsHomeNewProductList(storeId));
    }

    @UnAuth
    @GetMapping(value = "/homeBrand/list")
    public Object homeBrand(SmsHomeBrand homeNewProduct,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "4") Integer pageSize,
                            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "storeId", required = false) Long storeId) {
        startPage();
        homeNewProduct.setStoreId(storeId);
        return AjaxResult.success(homeBrandService.selectSmsHomeBrandList(homeNewProduct));
    }

    @UnAuth
    @GetMapping(value = "/homeRecommendProduct/list")
    public Object homeRecommendProduct(SmsHomeRecommendProduct homeNewProduct,
                                       @RequestParam(value = "pageSize", required = false, defaultValue = "4") Integer pageSize,
                                       @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                       @RequestParam(value = "storeId", required = false) Long storeId) {
        startPage();
        return AjaxResult.success(homeRecommendProductService.selectSmsHomeRecommendProductList(storeId));
    }

    @UnAuth
    @GetMapping(value = "/homeRecommendSubject/list")
    public Object homeRecommendSubject(SmsHomeRecommendSubject homeNewSubject,
                                       @RequestParam(value = "pageSize", required = false, defaultValue = "4") Integer pageSize,
                                       @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                       @RequestParam(value = "storeId", required = false) Long storeId) {
        startPage();
        homeNewSubject.setStoreId(storeId);
        return AjaxResult.success(homeRecommendSubjectService.selectSmsHomeRecommendSubjectList(storeId));
    }

    /**
     * banner
     *
     * @return
     */
    @UnAuth
    @GetMapping("/bannerList")
    public Object bannerList(SmsHomeAdvertise advertise ,@RequestParam(value = "type", required = false) Integer type,
                             @RequestParam(value = "storeId", required = false) Long storeId) {
        startPage();
        advertise.setStatus(1);
        advertise.setStoreId(storeId);
        return AjaxResult.success(homeAdvertiseService.selectSmsHomeAdvertiseList(advertise));
    }

}

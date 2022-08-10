package io.uhha.web.controller.marketing;

import io.swagger.annotations.Api;
import io.uhha.common.core.controller.BaseController;
import io.uhha.marketing.domain.Coupon;
import io.uhha.marketing.service.CouponService;
import io.uhha.util.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "奖券Controller")
@RestController
@RequestMapping("/marketing/coupon")
public class CouponController  extends BaseController {
    @Autowired
    private CouponService couponService;


    /**
     * 查询站点的奖券列表
     */
    @PreAuthorize("@ss.hasPermi('marketing:coupon:list')")
    @GetMapping("/list")
    public PageHelper<Coupon> queryCouponForSite(PageHelper<Coupon> pageHelper,Long storeId) {
        return couponService.queryCouponForSite(pageHelper,storeId);
    }


}

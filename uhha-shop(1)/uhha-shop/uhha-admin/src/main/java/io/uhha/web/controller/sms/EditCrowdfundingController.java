package io.uhha.web.controller.sms;

import io.swagger.annotations.Api;
import io.uhha.common.core.controller.BaseController;
import io.uhha.goods.domain.PmsGoods;
import io.uhha.goods.service.IPmsGoodsService;
import io.uhha.goods.vo.SpuSearchCondition;
import io.uhha.util.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "Controller")
@RestController
@RequestMapping("/crowdfunding")
public class EditCrowdfundingController extends BaseController {

    @Autowired
    private IPmsGoodsService pmsGoodsService;

    @GetMapping("/spus")
    public PageHelper<PmsGoods> querySimpleSpus(@ApiIgnore PageHelper<PmsGoods> pageHelper, @ApiIgnore SpuSearchCondition spuSearchCondition) {
        return pmsGoodsService.querySimpleSpus(pageHelper, spuSearchCondition);
    }

}

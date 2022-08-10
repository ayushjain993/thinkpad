package io.uhha.web.controller.goods;

import io.swagger.annotations.Api;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.goods.domain.PmsBrand;
import io.uhha.goods.domain.PmsCategory;
import io.uhha.goods.domain.PmsGoods;
import io.uhha.goods.service.IPmsBrandService;
import io.uhha.goods.service.IPmsCategoryService;
import io.uhha.goods.service.IPmsGoodsService;
import io.uhha.goods.vo.SpuSearchCondition;
import io.uhha.util.CommonConstant;
import io.uhha.util.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * 商品佣金设置Controller
 * 
 * @author peter
 * @date 2021-09-10
 */
@Api(tags = "商品佣金设置Controller")
@RestController
@RequestMapping("/commission")
public class PmsCommissionSettingController extends BaseController
{
    @Autowired
    private IPmsGoodsService goodsService;

    @Autowired
    private IPmsBrandService pmsBrandService;

    @Autowired
    private IPmsCategoryService pmsCategoryService;

    /**
     * 查询佣金设置列表
     */
    @GetMapping
    public PageHelper<PmsGoods> list(@ApiIgnore PageHelper<PmsGoods> pageHelper, @ApiIgnore SpuSearchCondition spuSearchCondition)
    {
        spuSearchCondition.setStoreId(CommonConstant.ADMIN_STOREID);
        PageHelper<PmsGoods> list = goodsService.querySpus(pageHelper, spuSearchCondition);
        return list;
    }


    /**
     * 查询单个商品的佣金设置
     */
    @GetMapping("/spu/{spuId}")
    public AjaxResult queryspusforcommissionbyid(@PathVariable("spuId") Long spuId)
    {
        return AjaxResult.success(goodsService.selectPmsGoodsById(spuId));
    }

    /**
     * 设置一个商品的佣金设置
     */
    @PutMapping
    public AjaxResult setCommission(Long id,BigDecimal commissionRate, BigDecimal sCommissionRate, BigDecimal dropshipCommissionRate)
    {
        return AjaxResult.success(goodsService.updateCommission(id,
                commissionRate, sCommissionRate, dropshipCommissionRate, CommonConstant.ADMIN_STOREID));
    }

    /**
     * 设置一个商品的佣金设置
     */
    @PutMapping("/batch")
    public AjaxResult batchSetCommission(Long[] ids, BigDecimal commissionRate, BigDecimal sCommissionRate, BigDecimal dropshipCommissionRate)
    {
        return AjaxResult.success(goodsService.updateCommissions(Arrays.asList(ids),commissionRate, sCommissionRate, dropshipCommissionRate ,CommonConstant.ADMIN_STOREID));
    }

    /**
     * 查询设置列表
     */
    @GetMapping("/brand")
    public List<PmsBrand> queryBrand()
    {
        return pmsBrandService.queryAllBrands(CommonConstant.ADMIN_STOREID);
    }

    /**
     * 根据分类id查询所有子分类信息
     *
     * @param parentId 父级id
     * @return 返回该父级下的所有分类信息
     */
    @GetMapping("/category/{parentId}")
    public List<PmsCategory> queryCategoryByParentId(@PathVariable long parentId) {
        return pmsCategoryService.queryCategoryByParentId(parentId);
    }

}

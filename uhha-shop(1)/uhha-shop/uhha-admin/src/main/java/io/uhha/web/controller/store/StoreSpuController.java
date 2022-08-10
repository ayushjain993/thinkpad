package io.uhha.web.controller.store;

import io.swagger.annotations.Api;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.goods.domain.PmsGoods;
import io.uhha.goods.service.IPmsGoodsService;
import io.uhha.order.domain.OmsOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "管理后台门店Spu相关接口")
@RestController
public class StoreSpuController extends BaseController {
    @Autowired
    private IPmsGoodsService pmsGoodsService;

    /**
     * 查询门店订单列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreOrder:list')")
    @GetMapping("/storespu")
    public TableDataInfo storespus(PmsGoods pmsGoods)
    {
        startPage();
        List<PmsGoods> list = pmsGoodsService.querySpus(pmsGoods);
        return getDataTable(list);
    }

    /**
     * 查询门店订单详情
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreOrder:query')")
    @GetMapping("/store/spu/{spuId}")
    public AjaxResult storespudetail(@PathVariable("spuId") Long spuId)
    {
        PmsGoods sku = pmsGoodsService.selectPmsGoodsById(spuId);
        PmsGoods pmsGoods = pmsGoodsService.querySpu(spuId, sku.getStoreId());

        return AjaxResult.success(pmsGoods);
    }


}

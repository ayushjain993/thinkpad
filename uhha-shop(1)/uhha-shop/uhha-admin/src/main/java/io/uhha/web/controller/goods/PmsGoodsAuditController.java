package io.uhha.web.controller.goods;

import io.swagger.annotations.Api;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.goods.domain.PmsGoods;
import io.uhha.goods.service.IPmsGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 店铺申请审批
 */
@Api(tags = "店铺申请审批Controller")
@RestController
public class PmsGoodsAuditController extends BaseController {
    @Autowired
    private IPmsGoodsService pmsGoodsService;

    @GetMapping("/goods/audit/{status}")
    public TableDataInfo queryAllStoreToBeAuditdSpus(@PathVariable("status") String status){
        startPage();
        List<PmsGoods> spus = pmsGoodsService.queryAllStoreAuditdSpusByStatus(status);
        return getDataTable(spus);
    }

    @GetMapping("/category/{pid}")
    public TableDataInfo queryCategoryByParentId() {

        return null;
    }


    @GetMapping("/store/audit/spu/{id}")
    public AjaxResult querySpuByIdForStore(@PathVariable("id") Long id) {
        PmsGoods pmsGoods = pmsGoodsService.selectPmsGoodsById(id);
        if(pmsGoods==null){
            return AjaxResult.error(404, "Not found");
        }
        return AjaxResult.success(pmsGoodsService.querySpu(pmsGoods.getId(), pmsGoods.getStoreId()));
    }

    @GetMapping("/store/audit/servicesupport")
    public AjaxResult queryAllServiceSupportForstore(){
     return AjaxResult.error("not impeleted");
    }

    @PutMapping("/store/audit/{spuId}")
    public AjaxResult auditPass(@PathVariable("spuId") Long spuId){
        return AjaxResult.success(pmsGoodsService.auditPass(null, spuId));
    }

    @PutMapping("/store/audit/refuse")
    public AjaxResult auditRefuse(@RequestParam("spuId") Long spuId, @RequestParam("reason") String reason){
        return AjaxResult.success(pmsGoodsService.auditRefuse(reason, null, spuId));
    }

}

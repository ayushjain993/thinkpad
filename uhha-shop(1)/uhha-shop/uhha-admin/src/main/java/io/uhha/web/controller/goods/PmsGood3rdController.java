package io.uhha.web.controller.goods;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.goods.domain.PmsGoods;
import io.uhha.goods.domain.PmsGoodsImport;
import io.uhha.goods.service.IPmsGood3rdService;
import io.uhha.goods.service.IPmsGoodsImportService;
import io.uhha.web.utils.AdminLoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * 三方spu Controller
 *
 * @author mj
 * @date 2020-07-24
 */
@Api(tags = "三方spu Controller")
@RestController
@RequestMapping("/goods/3rdSpu")
public class PmsGood3rdController extends BaseController {

    @Autowired
    private IPmsGood3rdService pmsGood3rdService;

    @Autowired
    private IPmsGoodsImportService goodsImportService;

    /**
     * 新增商品信息
     *
     * @param spu 三方spu
     * @return 成功返回1  失败返回0 -1存在单品同时有会员价和批发规则
     */
    @PostMapping
    @PreAuthorize("@ss.hasPermi('goods:import:add')")
    @Log(title = "三方SPU", businessType = BusinessType.INSERT)
    public int addOneBoundSpu(@RequestBody PmsGoodsImport spu) {
        return goodsImportService.insertPmsGoodsImport(spu);
    }

    /**
     * 查询第三方商品信息
     *
     * @param keyword
     * @param page
     * @return
     */
    @GetMapping("/search")
    @PreAuthorize("@ss.hasPermi('goods:goods:query')")
    public TableDataInfo searchOneBoundSpu(@RequestParam("channel") String channel,
                                           @RequestParam("keyword") String keyword,
                                           @RequestParam("page") Integer page) {
        try {
            return pmsGood3rdService.searchSpu(channel,keyword, page);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

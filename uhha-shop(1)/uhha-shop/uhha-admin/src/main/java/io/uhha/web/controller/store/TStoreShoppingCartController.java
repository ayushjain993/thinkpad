package io.uhha.web.controller.store;

import java.util.List;

import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.enums.BusinessType;
import io.uhha.store.domain.TStoreShoppingCart;
import io.uhha.store.service.ITStoreShoppingCartService;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.common.core.page.TableDataInfo;

/**
 * 门店购物车Controller
 * 
 * @author ruoyi
 * @date 2021-05-23
 */
@Api(tags = "门店购物车Controller")
@RestController
@RequestMapping("/store/TStoreShoppingCart")
public class TStoreShoppingCartController extends BaseController
{
    @Autowired
    private ITStoreShoppingCartService tStoreShoppingCartService;

    /**
     * 查询门店购物车列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreShoppingCart:list')")
    @GetMapping("/list")
    public TableDataInfo list(TStoreShoppingCart tStoreShoppingCart)
    {
        startPage();
        List<TStoreShoppingCart> list = tStoreShoppingCartService.selectTStoreShoppingCartList(tStoreShoppingCart);
        return getDataTable(list);
    }

    /**
     * 导出门店购物车列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreShoppingCart:export')")
    @Log(title = "门店购物车", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TStoreShoppingCart tStoreShoppingCart)
    {
        List<TStoreShoppingCart> list = tStoreShoppingCartService.selectTStoreShoppingCartList(tStoreShoppingCart);
        ExcelUtil<TStoreShoppingCart> util = new ExcelUtil<TStoreShoppingCart>(TStoreShoppingCart.class);
        return util.exportExcel(list, "门店购物车数据");
    }

    /**
     * 获取门店购物车详细信息
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreShoppingCart:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tStoreShoppingCartService.selectTStoreShoppingCartById(id));
    }

    /**
     * 新增门店购物车
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreShoppingCart:add')")
    @Log(title = "门店购物车", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TStoreShoppingCart tStoreShoppingCart)
    {
        return toAjax(tStoreShoppingCartService.insertTStoreShoppingCart(tStoreShoppingCart));
    }

    /**
     * 修改门店购物车
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreShoppingCart:edit')")
    @Log(title = "门店购物车", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TStoreShoppingCart tStoreShoppingCart)
    {
        return toAjax(tStoreShoppingCartService.updateTStoreShoppingCart(tStoreShoppingCart));
    }

    /**
     * 删除门店购物车
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreShoppingCart:remove')")
    @Log(title = "门店购物车", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tStoreShoppingCartService.deleteTStoreShoppingCartByIds(ids));
    }
}

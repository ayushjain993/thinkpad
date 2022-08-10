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
import io.uhha.store.domain.TStorePaytype;
import io.uhha.store.service.ITStorePaytypeService;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.common.core.page.TableDataInfo;

/**
 * 门店支付类型Controller
 * 
 * @author ruoyi
 * @date 2021-05-23
 */
@Api(tags = "门店支付类型Controller")
@RestController
@RequestMapping("/store/TStorePaytype")
public class TStorePaytypeController extends BaseController
{
    @Autowired
    private ITStorePaytypeService tStorePaytypeService;

    /**
     * 查询门店支付类型列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStorePaytype:list')")
    @GetMapping("/list")
    public TableDataInfo list(TStorePaytype tStorePaytype)
    {
        startPage();
        List<TStorePaytype> list = tStorePaytypeService.selectTStorePaytypeList(tStorePaytype);
        return getDataTable(list);
    }

    /**
     * 导出门店支付类型列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStorePaytype:export')")
    @Log(title = "门店支付类型", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TStorePaytype tStorePaytype)
    {
        List<TStorePaytype> list = tStorePaytypeService.selectTStorePaytypeList(tStorePaytype);
        ExcelUtil<TStorePaytype> util = new ExcelUtil<TStorePaytype>(TStorePaytype.class);
        return util.exportExcel(list, "门店支付类型数据");
    }

    /**
     * 获取门店支付类型详细信息
     */
    @PreAuthorize("@ss.hasPermi('store:TStorePaytype:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tStorePaytypeService.selectTStorePaytypeById(id));
    }

    /**
     * 新增门店支付类型
     */
    @PreAuthorize("@ss.hasPermi('store:TStorePaytype:add')")
    @Log(title = "门店支付类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TStorePaytype tStorePaytype)
    {
        return toAjax(tStorePaytypeService.insertTStorePaytype(tStorePaytype));
    }

    /**
     * 修改门店支付类型
     */
    @PreAuthorize("@ss.hasPermi('store:TStorePaytype:edit')")
    @Log(title = "门店支付类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TStorePaytype tStorePaytype)
    {
        return toAjax(tStorePaytypeService.updateTStorePaytype(tStorePaytype));
    }

    /**
     * 删除门店支付类型
     */
    @PreAuthorize("@ss.hasPermi('store:TStorePaytype:remove')")
    @Log(title = "门店支付类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tStorePaytypeService.deleteTStorePaytypeByIds(ids));
    }
}

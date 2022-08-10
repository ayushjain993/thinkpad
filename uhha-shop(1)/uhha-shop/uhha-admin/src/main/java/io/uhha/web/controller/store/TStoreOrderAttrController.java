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
import io.uhha.store.domain.TStoreOrderAttr;
import io.uhha.store.service.ITStoreOrderAttrService;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.common.core.page.TableDataInfo;

/**
 * 门店订单附属信息Controller
 * 
 * @author ruoyi
 * @date 2021-05-23
 */
@Api(tags = "门店订单附属信息Controller")
@RestController
@RequestMapping("/store/TStoreOrderAttr")
public class TStoreOrderAttrController extends BaseController
{
    @Autowired
    private ITStoreOrderAttrService tStoreOrderAttrService;

    /**
     * 查询门店订单附属信息列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreOrderAttr:list')")
    @GetMapping("/list")
    public TableDataInfo list(TStoreOrderAttr tStoreOrderAttr)
    {
        startPage();
        List<TStoreOrderAttr> list = tStoreOrderAttrService.selectTStoreOrderAttrList(tStoreOrderAttr);
        return getDataTable(list);
    }

    /**
     * 导出门店订单附属信息列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreOrderAttr:export')")
    @Log(title = "门店订单附属信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TStoreOrderAttr tStoreOrderAttr)
    {
        List<TStoreOrderAttr> list = tStoreOrderAttrService.selectTStoreOrderAttrList(tStoreOrderAttr);
        ExcelUtil<TStoreOrderAttr> util = new ExcelUtil<TStoreOrderAttr>(TStoreOrderAttr.class);
        return util.exportExcel(list, "门店订单附属信息数据");
    }

    /**
     * 获取门店订单附属信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreOrderAttr:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tStoreOrderAttrService.selectTStoreOrderAttrById(id));
    }

    /**
     * 新增门店订单附属信息
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreOrderAttr:add')")
    @Log(title = "门店订单附属信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TStoreOrderAttr tStoreOrderAttr)
    {
        return toAjax(tStoreOrderAttrService.insertTStoreOrderAttr(tStoreOrderAttr));
    }

    /**
     * 修改门店订单附属信息
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreOrderAttr:edit')")
    @Log(title = "门店订单附属信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TStoreOrderAttr tStoreOrderAttr)
    {
        return toAjax(tStoreOrderAttrService.updateTStoreOrderAttr(tStoreOrderAttr));
    }

    /**
     * 删除门店订单附属信息
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreOrderAttr:remove')")
    @Log(title = "门店订单附属信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tStoreOrderAttrService.deleteTStoreOrderAttrByIds(ids));
    }
}

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
import io.uhha.store.domain.TStoreRedEnvelope;
import io.uhha.store.service.ITStoreRedEnvelopeService;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.common.core.page.TableDataInfo;

/**
 * 门店红包Controller
 * 
 * @author ruoyi
 * @date 2021-05-23
 */
@Api(tags = "门店红包Controller")
@RestController
@RequestMapping("/store/TStoreRedEnvelope")
public class TStoreRedEnvelopeController extends BaseController
{
    @Autowired
    private ITStoreRedEnvelopeService tStoreRedEnvelopeService;

    /**
     * 查询门店红包列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreRedEnvelope:list')")
    @GetMapping("/list")
    public TableDataInfo list(TStoreRedEnvelope tStoreRedEnvelope)
    {
        startPage();
        List<TStoreRedEnvelope> list = tStoreRedEnvelopeService.selectTStoreRedEnvelopeList(tStoreRedEnvelope);
        return getDataTable(list);
    }

    /**
     * 导出门店红包列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreRedEnvelope:export')")
    @Log(title = "门店红包", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TStoreRedEnvelope tStoreRedEnvelope)
    {
        List<TStoreRedEnvelope> list = tStoreRedEnvelopeService.selectTStoreRedEnvelopeList(tStoreRedEnvelope);
        ExcelUtil<TStoreRedEnvelope> util = new ExcelUtil<TStoreRedEnvelope>(TStoreRedEnvelope.class);
        return util.exportExcel(list, "门店红包数据");
    }

    /**
     * 获取门店红包详细信息
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreRedEnvelope:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tStoreRedEnvelopeService.selectTStoreRedEnvelopeById(id));
    }

    /**
     * 新增门店红包
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreRedEnvelope:add')")
    @Log(title = "门店红包", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TStoreRedEnvelope tStoreRedEnvelope)
    {
        return toAjax(tStoreRedEnvelopeService.insertTStoreRedEnvelope(tStoreRedEnvelope));
    }

    /**
     * 修改门店红包
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreRedEnvelope:edit')")
    @Log(title = "门店红包", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TStoreRedEnvelope tStoreRedEnvelope)
    {
        return toAjax(tStoreRedEnvelopeService.updateTStoreRedEnvelope(tStoreRedEnvelope));
    }

    /**
     * 删除门店红包
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreRedEnvelope:remove')")
    @Log(title = "门店红包", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tStoreRedEnvelopeService.deleteTStoreRedEnvelopeByIds(ids));
    }
}

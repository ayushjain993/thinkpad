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
import io.uhha.store.domain.TStoreRedEnvelopeCode;
import io.uhha.store.service.ITStoreRedEnvelopeCodeService;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.common.core.page.TableDataInfo;

/**
 * 门店红包卷吗Controller
 * 
 * @author ruoyi
 * @date 2021-05-23
 */
@Api(tags = "门店红包码Controller")
@RestController
@RequestMapping("/store/TStoreRedEnvelopeCode")
public class TStoreRedEnvelopeCodeController extends BaseController
{
    @Autowired
    private ITStoreRedEnvelopeCodeService tStoreRedEnvelopeCodeService;

    /**
     * 查询门店红包卷吗列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreRedEnvelopeCode:list')")
    @GetMapping("/list")
    public TableDataInfo list(TStoreRedEnvelopeCode tStoreRedEnvelopeCode)
    {
        startPage();
        List<TStoreRedEnvelopeCode> list = tStoreRedEnvelopeCodeService.selectTStoreRedEnvelopeCodeList(tStoreRedEnvelopeCode);
        return getDataTable(list);
    }

    /**
     * 导出门店红包卷吗列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreRedEnvelopeCode:export')")
    @Log(title = "门店红包卷吗", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TStoreRedEnvelopeCode tStoreRedEnvelopeCode)
    {
        List<TStoreRedEnvelopeCode> list = tStoreRedEnvelopeCodeService.selectTStoreRedEnvelopeCodeList(tStoreRedEnvelopeCode);
        ExcelUtil<TStoreRedEnvelopeCode> util = new ExcelUtil<TStoreRedEnvelopeCode>(TStoreRedEnvelopeCode.class);
        return util.exportExcel(list, "门店红包卷吗数据");
    }

    /**
     * 获取门店红包卷吗详细信息
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreRedEnvelopeCode:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tStoreRedEnvelopeCodeService.selectTStoreRedEnvelopeCodeById(id));
    }

    /**
     * 新增门店红包卷吗
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreRedEnvelopeCode:add')")
    @Log(title = "门店红包卷吗", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TStoreRedEnvelopeCode tStoreRedEnvelopeCode)
    {
        return toAjax(tStoreRedEnvelopeCodeService.insertTStoreRedEnvelopeCode(tStoreRedEnvelopeCode));
    }

    /**
     * 修改门店红包卷吗
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreRedEnvelopeCode:edit')")
    @Log(title = "门店红包卷吗", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TStoreRedEnvelopeCode tStoreRedEnvelopeCode)
    {
        return toAjax(tStoreRedEnvelopeCodeService.updateTStoreRedEnvelopeCode(tStoreRedEnvelopeCode));
    }

    /**
     * 删除门店红包卷吗
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreRedEnvelopeCode:remove')")
    @Log(title = "门店红包卷吗", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tStoreRedEnvelopeCodeService.deleteTStoreRedEnvelopeCodeByIds(ids));
    }
}

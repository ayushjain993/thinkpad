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
import io.uhha.store.domain.TStoreEvaluation;
import io.uhha.store.service.ITStoreEvaluationService;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.common.core.page.TableDataInfo;

/**
 * 门店评价Controller
 * 
 * @author ruoyi
 * @date 2021-05-23
 */
@Api(tags = "门店评价Controller")
@RestController
@RequestMapping("/store/TStoreEvaluation")
public class TStoreEvaluationController extends BaseController
{
    @Autowired
    private ITStoreEvaluationService tStoreEvaluationService;

    /**
     * 查询门店评价列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreEvaluation:list')")
    @GetMapping("/list")
    public TableDataInfo list(TStoreEvaluation tStoreEvaluation)
    {
        startPage();
        List<TStoreEvaluation> list = tStoreEvaluationService.selectTStoreEvaluationList(tStoreEvaluation);
        return getDataTable(list);
    }

    /**
     * 导出门店评价列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreEvaluation:export')")
    @Log(title = "门店评价", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TStoreEvaluation tStoreEvaluation)
    {
        List<TStoreEvaluation> list = tStoreEvaluationService.selectTStoreEvaluationList(tStoreEvaluation);
        ExcelUtil<TStoreEvaluation> util = new ExcelUtil<TStoreEvaluation>(TStoreEvaluation.class);
        return util.exportExcel(list, "门店评价数据");
    }

    /**
     * 获取门店评价详细信息
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreEvaluation:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tStoreEvaluationService.selectTStoreEvaluationById(id));
    }

    /**
     * 新增门店评价
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreEvaluation:add')")
    @Log(title = "门店评价", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TStoreEvaluation tStoreEvaluation)
    {
        return toAjax(tStoreEvaluationService.insertTStoreEvaluation(tStoreEvaluation));
    }

    /**
     * 修改门店评价
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreEvaluation:edit')")
    @Log(title = "门店评价", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TStoreEvaluation tStoreEvaluation)
    {
        return toAjax(tStoreEvaluationService.updateTStoreEvaluation(tStoreEvaluation));
    }

    /**
     * 删除门店评价
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreEvaluation:remove')")
    @Log(title = "门店评价", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tStoreEvaluationService.deleteTStoreEvaluationByIds(ids));
    }
}

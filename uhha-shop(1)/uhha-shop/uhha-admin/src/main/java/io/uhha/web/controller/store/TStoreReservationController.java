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
import io.uhha.store.domain.TStoreReservation;
import io.uhha.store.service.ITStoreReservationService;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.common.core.page.TableDataInfo;

/**
 * 门店预约Controller
 * 
 * @author ruoyi
 * @date 2021-05-23
 */
@Api(tags = "门店预约Controller")
@RestController
@RequestMapping("/store/TStoreReservation")
public class TStoreReservationController extends BaseController
{
    @Autowired
    private ITStoreReservationService tStoreReservationService;

    /**
     * 查询门店预约列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreReservation:list')")
    @GetMapping("/list")
    public TableDataInfo list(TStoreReservation tStoreReservation)
    {
        startPage();
        List<TStoreReservation> list = tStoreReservationService.selectTStoreReservationList(tStoreReservation);
        return getDataTable(list);
    }

    /**
     * 导出门店预约列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreReservation:export')")
    @Log(title = "门店预约", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TStoreReservation tStoreReservation)
    {
        List<TStoreReservation> list = tStoreReservationService.selectTStoreReservationList(tStoreReservation);
        ExcelUtil<TStoreReservation> util = new ExcelUtil<TStoreReservation>(TStoreReservation.class);
        return util.exportExcel(list, "门店预约数据");
    }

    /**
     * 获取门店预约详细信息
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreReservation:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tStoreReservationService.selectTStoreReservationById(id));
    }

    /**
     * 新增门店预约
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreReservation:add')")
    @Log(title = "门店预约", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TStoreReservation tStoreReservation)
    {
        return toAjax(tStoreReservationService.insertTStoreReservation(tStoreReservation));
    }

    /**
     * 修改门店预约
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreReservation:edit')")
    @Log(title = "门店预约", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TStoreReservation tStoreReservation)
    {
        return toAjax(tStoreReservationService.updateTStoreReservation(tStoreReservation));
    }

    /**
     * 删除门店预约
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreReservation:remove')")
    @Log(title = "门店预约", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tStoreReservationService.deleteTStoreReservationByIds(ids));
    }
}

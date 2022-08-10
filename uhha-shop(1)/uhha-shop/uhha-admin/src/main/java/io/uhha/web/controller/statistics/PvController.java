package io.uhha.web.controller.statistics;

import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.statistics.bean.Pv;
import io.uhha.statistics.service.IPvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * PVController
 *
 * @author peter
 * @date 2020-11-17
 */
@RestController
@RequestMapping("/system/pv")
public class PvController extends BaseController {
    @Autowired
    private IPvService pvService;

    /**
     * 查询PV列表
     */
    @PreAuthorize("@ss.hasPermi('system:pv:list')")
    @GetMapping("/list")
    public TableDataInfo list(Pv pv) {
        startPage();
        List<Pv> list = pvService.selectPvList(pv);
        return getDataTable(list);
    }

    /**
     * 导出PV列表
     */
    @PreAuthorize("@ss.hasPermi('system:pv:export')")
    @Log(title = "PV", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Pv pv) throws IOException {
        List<Pv> list = pvService.selectPvList(pv);
        ExcelUtil<Pv> util = new ExcelUtil<Pv>(Pv.class);
        util.exportExcel(response, list, "pv");
    }

    /**
     * 获取PV详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:pv:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(pvService.selectPvById(id));
    }

    /**
     * 新增PV
     */
    @PreAuthorize("@ss.hasPermi('system:pv:add')")
    @Log(title = "PV", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Pv pv) {
        return toAjax(pvService.save(pv));
    }

    /**
     * 修改PV
     */
    @PreAuthorize("@ss.hasPermi('system:pv:edit')")
    @Log(title = "PV", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Pv pv) {
        return toAjax(pvService.updateById(pv));
    }

    /**
     * 删除PV
     */
    @PreAuthorize("@ss.hasPermi('system:pv:remove')")
    @Log(title = "PV", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(pvService.removeByIds(Arrays.asList(ids)));
    }
}

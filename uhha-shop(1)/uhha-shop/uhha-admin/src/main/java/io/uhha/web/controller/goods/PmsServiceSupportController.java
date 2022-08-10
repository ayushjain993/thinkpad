package io.uhha.web.controller.goods;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.goods.domain.PmsServiceSupport;
import io.uhha.goods.service.IPmsServiceSupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 服务支持Controller
 *
 * @author mj
 * @date 2020-07-24
 */
@Api(tags = "服务支持Controller")
@RestController
@RequestMapping("/goods/support")
public class PmsServiceSupportController extends BaseController {
    @Autowired
    private IPmsServiceSupportService pmsServiceSupportService;

    /**
     * 查询服务支持列表
     */
    @PreAuthorize("@ss.hasPermi('goods:support:list')")
    @GetMapping("/list")
    public TableDataInfo list(PmsServiceSupport pmsServiceSupport) {
        startPage();
        List<PmsServiceSupport> list = pmsServiceSupportService.selectPmsServiceSupportList(pmsServiceSupport);
        return getDataTable(list);
    }

    /**
     * 导出服务支持列表
     */
    @PreAuthorize("@ss.hasPermi('goods:support:export')")
    @Log(title = "服务支持", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(PmsServiceSupport pmsServiceSupport) {
        List<PmsServiceSupport> list = pmsServiceSupportService.selectPmsServiceSupportList(pmsServiceSupport);
        ExcelUtil<PmsServiceSupport> util = new ExcelUtil<PmsServiceSupport>(PmsServiceSupport.class);
        return util.exportExcel(list, "support");
    }

    /**
     * 获取服务支持详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:support:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(pmsServiceSupportService.selectPmsServiceSupportById(id));
    }

    /**
     * 新增服务支持
     */
    @PreAuthorize("@ss.hasPermi('goods:support:add')")
    @Log(title = "服务支持", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PmsServiceSupport pmsServiceSupport) {
        return toAjax(pmsServiceSupportService.insertPmsServiceSupport(pmsServiceSupport));
    }

    /**
     * 修改服务支持
     */
    @PreAuthorize("@ss.hasPermi('goods:support:edit')")
    @Log(title = "服务支持", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PmsServiceSupport pmsServiceSupport) {
        return toAjax(pmsServiceSupportService.updatePmsServiceSupport(pmsServiceSupport));
    }

    /**
     * 删除服务支持
     */
    @PreAuthorize("@ss.hasPermi('goods:support:remove')")
    @Log(title = "服务支持", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(pmsServiceSupportService.deletePmsServiceSupportByIds(ids));
    }
}

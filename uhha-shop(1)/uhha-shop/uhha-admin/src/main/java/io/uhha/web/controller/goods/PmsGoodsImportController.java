package io.uhha.web.controller.goods;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.goods.domain.PmsGoodsImport;
import io.uhha.goods.service.IPmsGood3rdService;
import io.uhha.goods.service.IPmsGoodsImportService;
import io.uhha.util.CommonConstant;
import io.uhha.web.utils.AdminLoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * 商品导入Controller
 *
 * @author mj
 * @date 2020-07-24
 */
@Api(tags = "商品导入Controller")
@RestController
@RequestMapping("/goods/import")
public class PmsGoodsImportController extends BaseController {
    @Autowired
    private IPmsGoodsImportService pmsGoodsImportService;

    /**
     * 查询商品导入列表
     */
    @PreAuthorize("@ss.hasPermi('goods:import:list')")
    @GetMapping("/list")
    public TableDataInfo list(PmsGoodsImport pmsGoodsImport) {
        startPage();
        List<PmsGoodsImport> list = pmsGoodsImportService.selectPmsGoodsImportList(pmsGoodsImport);
        return getDataTable(list);
    }

    /**
     * 导出商品导入列表
     */
    @PreAuthorize("@ss.hasPermi('goods:import:export')")
    @Log(title = "商品导入", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(PmsGoodsImport pmsGoodsImport) {
        List<PmsGoodsImport> list = pmsGoodsImportService.selectPmsGoodsImportList(pmsGoodsImport);
        ExcelUtil<PmsGoodsImport> util = new ExcelUtil<PmsGoodsImport>(PmsGoodsImport.class);
        return util.exportExcel(list, "import");
    }

    /**
     * 获取商品导入详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:import:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(pmsGoodsImportService.selectPmsGoodsImportById(id));
    }

    /**
     * 新增商品导入
     */
    @PreAuthorize("@ss.hasPermi('goods:import:add')")
    @Log(title = "商品导入", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PmsGoodsImport pmsGoodsImport) {
        return toAjax(pmsGoodsImportService.insertPmsGoodsImport(pmsGoodsImport));
    }

    /**
     * 发布商品导入
     */
    @PreAuthorize("@ss.hasPermi('goods:import:add')")
    @Log(title = "商品发布", businessType = BusinessType.INSERT)
    @PostMapping("/publish")
    public AjaxResult publish(@RequestParam("id") Long id) throws IOException {
        return toAjax(pmsGoodsImportService.publishPmsGoodsImport(id, CommonConstant.ADMIN_STOREID));
    }

    /**
     * 修改商品导入
     */
    @PreAuthorize("@ss.hasPermi('goods:import:edit')")
    @Log(title = "商品导入", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PmsGoodsImport pmsGoodsImport) {
        return toAjax(pmsGoodsImportService.updatePmsGoodsImport(pmsGoodsImport));
    }

    /**
     * 删除商品导入
     */
    @PreAuthorize("@ss.hasPermi('goods:import:remove')")
    @Log(title = "商品导入", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(pmsGoodsImportService.deletePmsGoodsImportByIds(ids));
    }
}

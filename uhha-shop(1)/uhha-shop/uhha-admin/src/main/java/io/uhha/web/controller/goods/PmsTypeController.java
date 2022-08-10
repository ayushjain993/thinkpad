package io.uhha.web.controller.goods;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.goods.domain.PmsCategory;
import io.uhha.goods.domain.PmsType;
import io.uhha.goods.service.IPmsCategoryService;
import io.uhha.goods.service.IPmsTypeService;
import io.uhha.web.utils.AdminLoginUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品类型Controller
 *
 * @author mj
 * @date 2020-07-24
 */
@Api(tags = "商品类型Controller")
@RestController
@RequestMapping("/goods/type")
public class PmsTypeController extends BaseController {
    @Autowired
    private IPmsTypeService pmsTypeService;
    /**
     * 注入分类服务接口
     */
    @Autowired
    private IPmsCategoryService categoryService;

    /**
     * 查询商品类型列表
     */
    @PreAuthorize("@ss.hasPermi('goods:type:list')")
    @GetMapping("/list")
    public TableDataInfo list(PmsType pmsType) {
        startPage();
        List<PmsType> list = pmsTypeService.selectPmsTypeList(pmsType);
        return getDataTable(list);
    }

    /**
     * 导出商品类型列表
     */
    @PreAuthorize("@ss.hasPermi('goods:type:export')")
    @Log(title = "商品类型", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(PmsType pmsType) {
        List<PmsType> list = pmsTypeService.selectPmsTypeList(pmsType);
        ExcelUtil<PmsType> util = new ExcelUtil<PmsType>(PmsType.class);
        return util.exportExcel(list, "type");
    }

    /**
     * 获取商品类型详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:type:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(pmsTypeService.queryTypeDetail(id));
    }

    /**
     * 新增商品类型
     */
    @PreAuthorize("@ss.hasPermi('goods:type:add')")
    @Log(title = "商品类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PmsType pmsType) {
        return toAjax(pmsTypeService.addType(pmsType.setDefaultValuesForAdd(AdminLoginUtils.getInstance().getManagerName())));
    }

    /**
     * 修改商品类型
     */
    @PreAuthorize("@ss.hasPermi('goods:type:edit')")
    @Log(title = "商品类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PmsType pmsType) {
        return toAjax(pmsTypeService.updateType(pmsType.setDefalutValuesForModify(AdminLoginUtils.getInstance().getManagerName())));
    }

    /**
     * 删除商品类型
     */
    @PreAuthorize("@ss.hasPermi('goods:type:remove')")
    @Log(title = "商品类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(pmsTypeService.batchDeleteTypes(Arrays.stream(ids).map(id -> PmsType.buildForDelete(id, AdminLoginUtils.getInstance().getManagerName())).collect(Collectors.toList())));
    }

    /**
     * 校验类型是否有商品关联
     *
     * @return 关联商品数量
     */
    @GetMapping("/checktypeassociated/{id}")
    @ApiOperation(value = "校验类型是否有商品关联", notes = "校验类型是否有商品关联（需要认证）")
    public int checkTypeAssociated(@PathVariable long id) {
        return pmsTypeService.checkTypeAssociated(id);
    }

    /**
     * 查找所有三级分类
     *
     * @return 所有三级分类
     */
    @GetMapping("/thirdcategorys")
    public List<PmsCategory> queryThirdCategory() {
        return categoryService.queryThirdCategory();
    }
}

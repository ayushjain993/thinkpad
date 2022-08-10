package io.uhha.web.controller.order;

import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.order.domain.OmsLogisticsTemplate;
import io.uhha.order.service.IOmsLogisticsTemplateService;
import io.uhha.order.service.LogisticsTemplateServiceApi;
import io.uhha.setting.domain.LsProvince;
import io.uhha.setting.service.AreaService;
import io.uhha.setting.vo.AreaItem;
import io.uhha.web.utils.AdminLoginUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 物流模版Controller
 *
 * @author mj
 * @date 2020-07-24
 */
@RestController
@RequestMapping("/order/OmsLogisticsTemplate")
public class OmsLogisticsTemplateController extends BaseController {
    @Autowired
    private IOmsLogisticsTemplateService omsLogisticsTemplateService;
    /**
     * 注入物流模版服务api接口
     */
    @Autowired
    private LogisticsTemplateServiceApi logisticsTemplateServiceApi;

    /**
     * 注入地区服务接口
     */
    @Autowired
    private AreaService areaService;
    /**
     * 查询物流模版列表
     */
    @PreAuthorize("@ss.hasPermi('order:OmsLogisticsTemplate:list')")
    @GetMapping("/list")
    public TableDataInfo list(OmsLogisticsTemplate omsLogisticsTemplate) {
        omsLogisticsTemplate.setStoreId(AdminLoginUtils.getInstance().getStoreId());
        startPage();
        List<OmsLogisticsTemplate> list = omsLogisticsTemplateService.selectOmsLogisticsTemplateList(omsLogisticsTemplate);
        return getDataTable(list);
    }

    /**
     * 导出物流模版列表
     */
    @PreAuthorize("@ss.hasPermi('order:OmsLogisticsTemplate:export')")
    @Log(title = "物流模版", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(OmsLogisticsTemplate omsLogisticsTemplate) {
        List<OmsLogisticsTemplate> list = omsLogisticsTemplateService.selectOmsLogisticsTemplateList(omsLogisticsTemplate);
        ExcelUtil<OmsLogisticsTemplate> util = new ExcelUtil<OmsLogisticsTemplate>(OmsLogisticsTemplate.class);
        return util.exportExcel(list, "OmsLogisticsTemplate");
    }

    /**
     * 获取物流模版详细信息
     */
    @PreAuthorize("@ss.hasPermi('order:OmsLogisticsTemplate:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(omsLogisticsTemplateService.queryLogisticsTemplate(id, AdminLoginUtils.getInstance().getStoreId()));
    }

    /**
     * 新增物流模版
     */
    @PreAuthorize("@ss.hasPermi('order:OmsLogisticsTemplate:add')")
    @Log(title = "物流模版", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OmsLogisticsTemplate omsLogisticsTemplate) {
        omsLogisticsTemplate.setStoreId(AdminLoginUtils.getInstance().getStoreId());
        return toAjax(omsLogisticsTemplateService.insertOmsLogisticsTemplate(omsLogisticsTemplate));
    }

    /**
     * 修改物流模版
     */
    @PreAuthorize("@ss.hasPermi('order:OmsLogisticsTemplate:edit')")
    @Log(title = "物流模版", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OmsLogisticsTemplate omsLogisticsTemplate) {
        omsLogisticsTemplate.setStoreId(AdminLoginUtils.getInstance().getStoreId());
        return toAjax(omsLogisticsTemplateService.updateOmsLogisticsTemplate(omsLogisticsTemplate));
    }

    /**
     * 删除物流模版
     */
    @PreAuthorize("@ss.hasPermi('order:OmsLogisticsTemplate:remove')")
    @Log(title = "物流模版", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(logisticsTemplateServiceApi.deleteLogisticsTemplate(ids[0], AdminLoginUtils.getInstance().getStoreId()));
    }
    /**
     * 设置默认运费模版
     *
     * @param id 模版id
     * @return 成功返回1 失败返回0
     */
    @PutMapping("/logisticstemplate/default/{id}")
    @ApiOperation(value = "设置默认运费模版", notes = "设置默认运费模版(需要认证)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "id", value = "运费模版id"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功返回1 失败返回0", response = Integer.class)
    })
    public int setDefaultTemplate(@PathVariable long id) {
        return omsLogisticsTemplateService.setDefaultTemplate(id, AdminLoginUtils.getInstance().getStoreId());
    }


    /**
     * 查询所有省份  包括省份下面的市
     *
     * @return 返回所有省份
     */
    @GetMapping("/allprovinceswithcity")
    @ApiOperation(value = "查询所有省份  包括省份下面的市", notes = "查询所有省份  包括省份下面的市(需要认证)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回所有省份", response = LsProvince.class)
    })
    public List<LsProvince> queryAllProvincesWithCity() {
        return areaService.queryAllProvinces(AreaItem.CITY);
    }

    /**
     * 新增物流模版
     *
     * @param logisticsTemplate 物流模版
     * @return 成功返回1 失败返回0 -1 参数为空 -2 名称已存在
     */
    @PostMapping("/logisticstemplate")
    @ApiOperation(value = "新增物流模版", notes = "新增物流模版(需要认证)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功返回1 失败返回0 -1 参数为空 -2 名称已存在", response = Integer.class)
    })
    public int addLogisticsTemplate(@RequestBody OmsLogisticsTemplate logisticsTemplate) {
        logisticsTemplate.setStoreId(AdminLoginUtils.getInstance().getStoreId());
        return omsLogisticsTemplateService.addLogisticsTemplate(logisticsTemplate);
    }




    /**
     * 查询所有省份  包括省份下面的市(修改物流模版用)
     *
     * @return 返回所有省份
     */
    @GetMapping("/update/allprovinceswithcity")
    @ApiOperation(value = "查询所有省份  包括省份下面的市(修改物流模版用)", notes = "查询所有省份  包括省份下面的市(修改物流模版用)(需要认证)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回所有省份", response = LsProvince.class)
    })
    public List<LsProvince> queryAllProvincesWithCityForUpdate() {
        return areaService.queryAllProvinces(AreaItem.CITY);
    }
}

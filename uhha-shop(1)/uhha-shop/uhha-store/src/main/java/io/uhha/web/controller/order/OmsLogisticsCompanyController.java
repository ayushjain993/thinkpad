package io.uhha.web.controller.order;

import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.order.domain.OmsLogisticsCompany;
import io.uhha.order.domain.OmsLogisticsCompanyUse;
import io.uhha.order.service.IOmsLogisticsCompanyService;
import io.uhha.order.service.IOmsLogisticsCompanyUseService;
import io.uhha.order.vo.OmsLogisticsCompanyVo;
import io.uhha.util.CommonConstant;
import io.uhha.web.utils.AdminLoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 物流公司Controller
 *
 * @author mj
 * @date 2020-07-24
 */
@RestController
@RequestMapping("/order/OmsLogisticsCompany")
public class OmsLogisticsCompanyController extends BaseController {
    @Autowired
    private IOmsLogisticsCompanyService omsLogisticsCompanyService;

    @Autowired
    private IOmsLogisticsCompanyUseService omsLogisticsCompanyUseService;

    /**
     * 查询物流公司列表
     */
    @PreAuthorize("@ss.hasPermi('order:OmsLogisticsCompany:list')")
    @GetMapping("/list")
    public TableDataInfo list() {
        Long storeId = AdminLoginUtils.getInstance().getStoreId();
        startPage();
        List<OmsLogisticsCompany> list = omsLogisticsCompanyService.selectOmsLogisticsCompanyListByStoreId(storeId);
        return getDataTable(list);
    }


    /**
     * 查询物流公司列表
     */
    @PreAuthorize("@ss.hasPermi('order:OmsLogisticsCompany:list')")
    @GetMapping("/listForStore")
    public TableDataInfo listForStore() {
        Long storeId = AdminLoginUtils.getInstance().getStoreId();
        startPage();
        List<OmsLogisticsCompanyVo> list = omsLogisticsCompanyService.selectOmsLogisticsCompanyForStore(storeId);
        return getDataTable(list);
    }

    /**
     * 获取物流公司详细信息
     */
    @PreAuthorize("@ss.hasPermi('order:OmsLogisticsCompany:query')")
    @GetMapping(value = "/listByCode/{code}")
    public TableDataInfo getLogisticCompanyByCode(@PathVariable("code") String code) {
        startPage();
        List<OmsLogisticsCompany> list = omsLogisticsCompanyService.selectOmsLogisticsCompanyListByCode(code);
        return getDataTable(list);
    }

    /**
     * 导出物流公司列表
     */
    @PreAuthorize("@ss.hasPermi('order:OmsLogisticsCompany:export')")
    @Log(title = "物流公司", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(OmsLogisticsCompany omsLogisticsCompany) {
        List<OmsLogisticsCompany> list = omsLogisticsCompanyService.selectOmsLogisticsCompanyList(omsLogisticsCompany);
        ExcelUtil<OmsLogisticsCompany> util = new ExcelUtil<OmsLogisticsCompany>(OmsLogisticsCompany.class);
        return util.exportExcel(list, "OmsLogisticsCompany");
    }

    /**
     * 获取物流公司详细信息
     */
    @PreAuthorize("@ss.hasPermi('order:OmsLogisticsCompany:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(omsLogisticsCompanyService.selectOmsLogisticsCompanyById(id));
    }

    /**
     * 新增物流公司
     */
    @PreAuthorize("@ss.hasPermi('order:OmsLogisticsCompany:add')")
    @Log(title = "物流公司", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OmsLogisticsCompany omsLogisticsCompany) {
        Long storeId = AdminLoginUtils.getInstance().getStoreId();

        //保持一条记录
        OmsLogisticsCompanyUse omsLogisticsCompanyUse = new OmsLogisticsCompanyUse();
        omsLogisticsCompanyUse.setStoreId(storeId);
        omsLogisticsCompanyUse.setCompanyId(omsLogisticsCompany.getId());
        return toAjax(omsLogisticsCompanyUseService.insertOmsLogisticsCompanyUse(omsLogisticsCompanyUse));
    }

    /**
     * 删除物流公司
     */
    @PreAuthorize("@ss.hasPermi('order:OmsLogisticsCompany:remove')")
    @Log(title = "物流公司", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(omsLogisticsCompanyUseService.deleteOmsLogisticsCompanyUseByIds(ids));
    }
}

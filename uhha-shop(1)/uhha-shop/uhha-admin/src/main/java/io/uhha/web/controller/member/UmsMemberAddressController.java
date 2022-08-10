package io.uhha.web.controller.member;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.member.domain.UmsMemberAddress;
import io.uhha.member.service.IUmsMemberAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户收货地址Controller
 *
 * @author mj
 * @date 2020-07-25
 */
@Api(tags = "用户收货地址Controller")
@RestController
@RequestMapping("/member/UmsMemberAddress")
public class UmsMemberAddressController extends BaseController {
    @Autowired
    private IUmsMemberAddressService umsMemberAddressService;

    /**
     * 查询用户收货地址列表
     */
    @PreAuthorize("@ss.hasPermi('member:UmsMemberAddress:list')")
    @GetMapping("/list")
    public TableDataInfo list(UmsMemberAddress umsMemberAddress) {
        startPage();
        List<UmsMemberAddress> list = umsMemberAddressService.selectUmsMemberAddressList(umsMemberAddress);
        return getDataTable(list);
    }

    /**
     * 导出用户收货地址列表
     */
    @PreAuthorize("@ss.hasPermi('member:UmsMemberAddress:export')")
    @Log(title = "用户收货地址", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(UmsMemberAddress umsMemberAddress) {
        List<UmsMemberAddress> list = umsMemberAddressService.selectUmsMemberAddressList(umsMemberAddress);
        ExcelUtil<UmsMemberAddress> util = new ExcelUtil<UmsMemberAddress>(UmsMemberAddress.class);
        return util.exportExcel(list, "UmsMemberAddress");
    }

    /**
     * 获取用户收货地址详细信息
     */
    @PreAuthorize("@ss.hasPermi('member:UmsMemberAddress:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(umsMemberAddressService.selectUmsMemberAddressById(id));
    }

    /**
     * 新增用户收货地址
     */
    @PreAuthorize("@ss.hasPermi('member:UmsMemberAddress:add')")
    @Log(title = "用户收货地址", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UmsMemberAddress umsMemberAddress) {
        return toAjax(umsMemberAddressService.insertUmsMemberAddress(umsMemberAddress));
    }

    /**
     * 修改用户收货地址
     */
    @PreAuthorize("@ss.hasPermi('member:UmsMemberAddress:edit')")
    @Log(title = "用户收货地址", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UmsMemberAddress umsMemberAddress) {
        return toAjax(umsMemberAddressService.updateUmsMemberAddress(umsMemberAddress));
    }

    /**
     * 删除用户收货地址
     */
    @PreAuthorize("@ss.hasPermi('member:UmsMemberAddress:remove')")
    @Log(title = "用户收货地址", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(umsMemberAddressService.deleteUmsMemberAddressByIds(ids));
    }
}

package io.uhha.web.controller.member;

import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.enums.RealnameScreenStatusEnum;
import io.uhha.common.utils.DateUtils;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.member.domain.RealnameApplicationForm;
import io.uhha.member.service.IRealnameApplicationFormService;
import io.uhha.member.service.IUmsMemberService;
import io.uhha.web.utils.AdminLoginUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 实名认证申请Controller
 *
 * @author uhha
 * @date 2022-03-13
 */
@RestController
@RequestMapping("/member/realnameAppForm")
@Slf4j
public class RealnameApplicationFormController extends BaseController {
    @Autowired
    private IRealnameApplicationFormService realnameApplicationFormService;


    /**
     * 查询实名认证申请列表
     */
    @PreAuthorize("@ss.hasPermi('member:realnameAppForm:list')")
    @GetMapping("/list")
    public TableDataInfo list(RealnameApplicationForm realnameApplicationForm) {
        startPage();
        List<RealnameApplicationForm> list = realnameApplicationFormService.selectRealnameApplicationFormList(realnameApplicationForm);
        return getDataTable(list);
    }

    /**
     * 导出实名认证申请列表
     */
    @PreAuthorize("@ss.hasPermi('member:realnameAppForm:export')")
    @Log(title = "实名认证申请", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(RealnameApplicationForm realnameApplicationForm) {
        List<RealnameApplicationForm> list = realnameApplicationFormService.selectRealnameApplicationFormList(realnameApplicationForm);
        ExcelUtil<RealnameApplicationForm> util = new ExcelUtil<RealnameApplicationForm>(RealnameApplicationForm.class);
        return util.exportExcel(list, "实名认证申请数据");
    }

    /**
     * 获取实名认证申请详细信息
     */
    @PreAuthorize("@ss.hasPermi('member:realnameAppForm:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(realnameApplicationFormService.selectRealnameApplicationFormById(id));
    }

    /**
     * 新增实名认证申请
     */
    @PreAuthorize("@ss.hasPermi('member:realnameAppForm:add')")
    @Log(title = "实名认证申请", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RealnameApplicationForm realnameApplicationForm) {
        return toAjax(realnameApplicationFormService.insertRealnameApplicationForm(realnameApplicationForm));
    }

    /**
     * 修改实名认证申请
     */
    @PreAuthorize("@ss.hasPermi('member:realnameAppForm:edit')")
    @Log(title = "实名认证申请", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RealnameApplicationForm realnameApplicationForm) {
        return toAjax(realnameApplicationFormService.updateRealnameApplicationForm(realnameApplicationForm));
    }

    /**
     * 删除实名认证申请
     */
    @PreAuthorize("@ss.hasPermi('member:realnameAppForm:remove')")
    @Log(title = "实名认证申请", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(realnameApplicationFormService.deleteRealnameApplicationFormByIds(ids));
    }

    /**
     * 实名认证申请通过
     */
    @PreAuthorize("@ss.hasPermi('member:realnameAppForm:edit')")
    @Log(title = "实名认证申请通过", businessType = BusinessType.UPDATE)
    @PutMapping("/approve")
    public AjaxResult approve(@RequestParam("id") Long id) {
        return toAjax(realnameApplicationFormService.approveRealname(id, AdminLoginUtils.getInstance().getManagerName()));
    }

    /**
     * 实名认证申请拒绝
     */
    @PreAuthorize("@ss.hasPermi('member:realnameAppForm:edit')")
    @Log(title = "实名认证申请拒绝", businessType = BusinessType.UPDATE)
    @PutMapping("/reject")
    public AjaxResult reject(@RequestParam("id") Long id, @RequestParam("reason") String reason) {
        return toAjax(realnameApplicationFormService.rejectRealname(id, reason, AdminLoginUtils.getInstance().getManagerName()));
    }


}
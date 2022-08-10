package io.uhha.web.controller.member;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.member.domain.UmsMemberPanicRecord;
import io.uhha.member.service.IUmsMemberPanicRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户抢购记录Controller
 *
 * @author mj
 * @date 2020-07-25
 */
@Api(tags = "用户抢购记录Controller")
@RestController
@RequestMapping("/member/UmsMemberPanicRecord")
public class UmsMemberPanicRecordController extends BaseController {
    @Autowired
    private IUmsMemberPanicRecordService umsMemberPanicRecordService;

    /**
     * 查询用户抢购记录列表
     */
    @PreAuthorize("@ss.hasPermi('member:UmsMemberPanicRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(UmsMemberPanicRecord umsMemberPanicRecord) {
        startPage();
        List<UmsMemberPanicRecord> list = umsMemberPanicRecordService.selectUmsMemberPanicRecordList(umsMemberPanicRecord);
        return getDataTable(list);
    }

    /**
     * 导出用户抢购记录列表
     */
    @PreAuthorize("@ss.hasPermi('member:UmsMemberPanicRecord:export')")
    @Log(title = "用户抢购记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(UmsMemberPanicRecord umsMemberPanicRecord) {
        List<UmsMemberPanicRecord> list = umsMemberPanicRecordService.selectUmsMemberPanicRecordList(umsMemberPanicRecord);
        ExcelUtil<UmsMemberPanicRecord> util = new ExcelUtil<UmsMemberPanicRecord>(UmsMemberPanicRecord.class);
        return util.exportExcel(list, "UmsMemberPanicRecord");
    }

    /**
     * 获取用户抢购记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('member:UmsMemberPanicRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(umsMemberPanicRecordService.selectUmsMemberPanicRecordById(id));
    }

    /**
     * 新增用户抢购记录
     */
    @PreAuthorize("@ss.hasPermi('member:UmsMemberPanicRecord:add')")
    @Log(title = "用户抢购记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UmsMemberPanicRecord umsMemberPanicRecord) {
        return toAjax(umsMemberPanicRecordService.insertUmsMemberPanicRecord(umsMemberPanicRecord));
    }

    /**
     * 修改用户抢购记录
     */
    @PreAuthorize("@ss.hasPermi('member:UmsMemberPanicRecord:edit')")
    @Log(title = "用户抢购记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UmsMemberPanicRecord umsMemberPanicRecord) {
        return toAjax(umsMemberPanicRecordService.updateUmsMemberPanicRecord(umsMemberPanicRecord));
    }

    /**
     * 删除用户抢购记录
     */
    @PreAuthorize("@ss.hasPermi('member:UmsMemberPanicRecord:remove')")
    @Log(title = "用户抢购记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(umsMemberPanicRecordService.deleteUmsMemberPanicRecordByIds(ids));
    }
}

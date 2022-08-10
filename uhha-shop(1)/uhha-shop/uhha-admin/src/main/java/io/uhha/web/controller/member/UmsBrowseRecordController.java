package io.uhha.web.controller.member;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.member.domain.UmsBrowseRecord;
import io.uhha.member.service.IUmsBrowseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 会员浏览记录Controller
 *
 * @author mj
 * @date 2020-07-25
 */
@Api(tags = "会员浏览记录Controller")
@RestController
@RequestMapping("/member/UmsBrowseRecord")
public class UmsBrowseRecordController extends BaseController {
    @Autowired
    private IUmsBrowseRecordService umsBrowseRecordService;

    /**
     * 查询会员浏览记录列表
     */
    @PreAuthorize("@ss.hasPermi('member:UmsBrowseRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(UmsBrowseRecord umsBrowseRecord) {
        startPage();
        List<UmsBrowseRecord> list = umsBrowseRecordService.selectUmsBrowseRecordList(umsBrowseRecord);
        return getDataTable(list);
    }

    /**
     * 导出会员浏览记录列表
     */
    @PreAuthorize("@ss.hasPermi('member:UmsBrowseRecord:export')")
    @Log(title = "会员浏览记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(UmsBrowseRecord umsBrowseRecord) {
        List<UmsBrowseRecord> list = umsBrowseRecordService.selectUmsBrowseRecordList(umsBrowseRecord);
        ExcelUtil<UmsBrowseRecord> util = new ExcelUtil<UmsBrowseRecord>(UmsBrowseRecord.class);
        return util.exportExcel(list, "UmsBrowseRecord");
    }

    /**
     * 获取会员浏览记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('member:UmsBrowseRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(umsBrowseRecordService.selectUmsBrowseRecordById(id));
    }

    /**
     * 新增会员浏览记录
     */
    @PreAuthorize("@ss.hasPermi('member:UmsBrowseRecord:add')")
    @Log(title = "会员浏览记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UmsBrowseRecord umsBrowseRecord) {
        return toAjax(umsBrowseRecordService.insertUmsBrowseRecord(umsBrowseRecord));
    }

    /**
     * 修改会员浏览记录
     */
    @PreAuthorize("@ss.hasPermi('member:UmsBrowseRecord:edit')")
    @Log(title = "会员浏览记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UmsBrowseRecord umsBrowseRecord) {
        return toAjax(umsBrowseRecordService.updateUmsBrowseRecord(umsBrowseRecord));
    }

    /**
     * 删除会员浏览记录
     */
    @PreAuthorize("@ss.hasPermi('member:UmsBrowseRecord:remove')")
    @Log(title = "会员浏览记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(umsBrowseRecordService.deleteUmsBrowseRecordByIds(ids));
    }
}

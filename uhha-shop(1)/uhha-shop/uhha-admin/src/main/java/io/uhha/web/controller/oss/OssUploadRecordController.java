package io.uhha.web.controller.oss;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.oss.domain.OssUploadRecord;
import io.uhha.oss.service.IOssUploadRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * OSS对象存储系统上传记录Controller
 * 
 * @author uhha
 * @date 2021-09-16
 */
@Api(tags = "OSS对象存储系统上传记录Controller")
@RestController
@RequestMapping("/system/ossUploadRecord")
public class OssUploadRecordController extends BaseController
{
    @Autowired
    private IOssUploadRecordService ossUploadRecordService;

    /**
     * 查询OSS对象存储系统上传记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:ossUploadRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(OssUploadRecord ossUploadRecord)
    {
        startPage();
        List<OssUploadRecord> list = ossUploadRecordService.selectOssUploadRecordList(ossUploadRecord);
        return getDataTable(list);
    }

    /**
     * 导出OSS对象存储系统上传记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:ossUploadRecord:export')")
    @Log(title = "OSS对象存储系统上传记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(OssUploadRecord ossUploadRecord)
    {
        List<OssUploadRecord> list = ossUploadRecordService.selectOssUploadRecordList(ossUploadRecord);
        ExcelUtil<OssUploadRecord> util = new ExcelUtil<OssUploadRecord>(OssUploadRecord.class);
        return util.exportExcel(list, "OSS对象存储系统上传记录数据");
    }

    /**
     * 获取OSS对象存储系统上传记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:ossUploadRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(ossUploadRecordService.selectOssUploadRecordById(id));
    }

    /**
     * 新增OSS对象存储系统上传记录
     */
    @PreAuthorize("@ss.hasPermi('system:ossUploadRecord:add')")
    @Log(title = "OSS对象存储系统上传记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OssUploadRecord ossUploadRecord)
    {
        return toAjax(ossUploadRecordService.insertOssUploadRecord(ossUploadRecord));
    }

    /**
     * 修改OSS对象存储系统上传记录
     */
    @PreAuthorize("@ss.hasPermi('system:ossUploadRecord:edit')")
    @Log(title = "OSS对象存储系统上传记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OssUploadRecord ossUploadRecord)
    {
        return toAjax(ossUploadRecordService.updateOssUploadRecord(ossUploadRecord));
    }

    /**
     * 删除OSS对象存储系统上传记录
     */
    @PreAuthorize("@ss.hasPermi('system:ossUploadRecord:remove')")
    @Log(title = "OSS对象存储系统上传记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(ossUploadRecordService.deleteOssUploadRecordByIds(ids));
    }
}

package io.uhha.web.controller.shortvideo;

import java.util.List;

import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.enums.BusinessType;
import io.uhha.shortvideo.domain.ShortVideoWatchRecord;
import io.uhha.shortvideo.service.IShortVideoWatchRecordService;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.common.core.page.TableDataInfo;

/**
 * 短视频观看记录Controller
 * 
 * @author ruoyi
 * @date 2021-08-06
 */
@Api(tags = "短视频观看记录Controller")
@RestController
@RequestMapping("/shortvideo/watchRecord")
public class ShortVideoWatchRecordController extends BaseController
{
    @Autowired
    private IShortVideoWatchRecordService shortVideoWatchRecordService;

    /**
     * 查询短视频观看记录列表
     */
    @PreAuthorize("@ss.hasPermi('shortvideo:watchRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(ShortVideoWatchRecord shortVideoWatchRecord)
    {
        startPage();
        List<ShortVideoWatchRecord> list = shortVideoWatchRecordService.selectShortVideoWatchRecordList(shortVideoWatchRecord);
        return getDataTable(list);
    }

    /**
     * 导出短视频观看记录列表
     */
    @PreAuthorize("@ss.hasPermi('shortvideo:watchRecord:export')")
    @Log(title = "短视频观看记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ShortVideoWatchRecord shortVideoWatchRecord)
    {
        List<ShortVideoWatchRecord> list = shortVideoWatchRecordService.selectShortVideoWatchRecordList(shortVideoWatchRecord);
        ExcelUtil<ShortVideoWatchRecord> util = new ExcelUtil<ShortVideoWatchRecord>(ShortVideoWatchRecord.class);
        return util.exportExcel(list, "短视频观看记录数据");
    }

    /**
     * 获取短视频观看记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('shortvideo:watchRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(shortVideoWatchRecordService.selectShortVideoWatchRecordById(id));
    }

    /**
     * 新增短视频观看记录
     */
    @PreAuthorize("@ss.hasPermi('shortvideo:watchRecord:add')")
    @Log(title = "短视频观看记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ShortVideoWatchRecord shortVideoWatchRecord)
    {
        return toAjax(shortVideoWatchRecordService.insertShortVideoWatchRecord(shortVideoWatchRecord));
    }

    /**
     * 修改短视频观看记录
     */
    @PreAuthorize("@ss.hasPermi('shortvideo:watchRecord:edit')")
    @Log(title = "短视频观看记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ShortVideoWatchRecord shortVideoWatchRecord)
    {
        return toAjax(shortVideoWatchRecordService.updateShortVideoWatchRecord(shortVideoWatchRecord));
    }

    /**
     * 删除短视频观看记录
     */
    @PreAuthorize("@ss.hasPermi('shortvideo:watchRecord:remove')")
    @Log(title = "短视频观看记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(shortVideoWatchRecordService.deleteShortVideoWatchRecordByIds(ids));
    }
}

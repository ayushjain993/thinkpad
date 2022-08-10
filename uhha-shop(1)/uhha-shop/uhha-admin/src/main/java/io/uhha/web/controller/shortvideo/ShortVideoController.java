package io.uhha.web.controller.shortvideo;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.shortvideo.domain.ShortVideo;
import io.uhha.shortvideo.service.IShortVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 短视频Controller
 * 
 * @author ruoyi
 * @date 2021-08-06
 */
@Api(tags = "短视频Controller")
@RestController
@RequestMapping("/shortvideo/video")
public class ShortVideoController extends BaseController
{
    @Autowired
    private IShortVideoService shortVideoService;

    /**
     * 查询短视频列表
     */
    @PreAuthorize("@ss.hasPermi('shortvideo:video:list')")
    @GetMapping("/list")
    public TableDataInfo list(ShortVideo shortVideo)
    {
        startPage();
        List<ShortVideo> list = shortVideoService.selectShortVideoList(shortVideo);
        return getDataTable(list);
    }

    /**
     * 导出短视频列表
     */
    @PreAuthorize("@ss.hasPermi('shortvideo:video:export')")
    @Log(title = "短视频", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ShortVideo shortVideo)
    {
        List<ShortVideo> list = shortVideoService.selectShortVideoList(shortVideo);
        ExcelUtil<ShortVideo> util = new ExcelUtil<ShortVideo>(ShortVideo.class);
        return util.exportExcel(list, "短视频数据");
    }

    /**
     * 获取短视频详细信息
     */
    @PreAuthorize("@ss.hasPermi('shortvideo:video:query')")
    @GetMapping(value = "/{videoId}")
    public AjaxResult getInfo(@PathVariable("videoId") String videoId)
    {
        return AjaxResult.success(shortVideoService.selectShortVideoById(videoId));
    }

    /**
     * 新增短视频
     */
    @PreAuthorize("@ss.hasPermi('shortvideo:video:add')")
    @Log(title = "短视频", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ShortVideo shortVideo)
    {
        return toAjax(shortVideoService.insertShortVideo(shortVideo));
    }

    /**
     * 修改短视频
     */
    @PreAuthorize("@ss.hasPermi('shortvideo:video:edit')")
    @Log(title = "短视频", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ShortVideo shortVideo)
    {
        return toAjax(shortVideoService.updateShortVideo(shortVideo));
    }

    /**
     * 删除短视频
     */
    @PreAuthorize("@ss.hasPermi('shortvideo:video:remove')")
    @Log(title = "短视频", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(shortVideoService.deleteShortVideoByIds(ids));
    }
}

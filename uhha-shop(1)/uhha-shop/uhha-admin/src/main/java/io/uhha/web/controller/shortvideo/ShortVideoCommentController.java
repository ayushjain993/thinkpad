package io.uhha.web.controller.shortvideo;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.shortvideo.domain.ShortVideoComment;
import io.uhha.shortvideo.service.IShortVideoCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 短视频评论Controller
 * 
 * @author ruoyi
 * @date 2021-08-06
 */
@Api(tags = "短视频评论Controller")
@RestController
@RequestMapping("/shortvideo/comment")
public class ShortVideoCommentController extends BaseController
{
    @Autowired
    private IShortVideoCommentService shortVideoCommentService;

    /**
     * 查询短视频评论列表
     */
    @PreAuthorize("@ss.hasPermi('shortvideo:comment:list')")
    @GetMapping("/list")
    public TableDataInfo list(ShortVideoComment shortVideoComment)
    {
        startPage();
        List<ShortVideoComment> list = shortVideoCommentService.selectShortVideoCommentList(shortVideoComment);
        return getDataTable(list);
    }

    /**
     * 导出短视频评论列表
     */
    @PreAuthorize("@ss.hasPermi('shortvideo:comment:export')")
    @Log(title = "短视频评论", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ShortVideoComment shortVideoComment)
    {
        List<ShortVideoComment> list = shortVideoCommentService.selectShortVideoCommentList(shortVideoComment);
        ExcelUtil<ShortVideoComment> util = new ExcelUtil<ShortVideoComment>(ShortVideoComment.class);
        return util.exportExcel(list, "短视频评论数据");
    }

    /**
     * 获取短视频评论详细信息
     */
    @PreAuthorize("@ss.hasPermi('shortvideo:comment:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(shortVideoCommentService.selectShortVideoCommentById(id));
    }

    /**
     * 新增短视频评论
     */
    @PreAuthorize("@ss.hasPermi('shortvideo:comment:add')")
    @Log(title = "短视频评论", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ShortVideoComment shortVideoComment)
    {
        return toAjax(shortVideoCommentService.insertShortVideoComment(shortVideoComment));
    }

    /**
     * 修改短视频评论
     */
    @PreAuthorize("@ss.hasPermi('shortvideo:comment:edit')")
    @Log(title = "短视频评论", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ShortVideoComment shortVideoComment)
    {
        return toAjax(shortVideoCommentService.updateShortVideoComment(shortVideoComment));
    }

    /**
     * 删除短视频评论
     */
    @PreAuthorize("@ss.hasPermi('shortvideo:comment:remove')")
    @Log(title = "短视频评论", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(shortVideoCommentService.deleteShortVideoCommentByIds(ids));
    }
}

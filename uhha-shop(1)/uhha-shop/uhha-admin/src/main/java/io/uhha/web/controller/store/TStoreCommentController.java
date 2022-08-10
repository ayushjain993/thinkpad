package io.uhha.web.controller.store;

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
import io.uhha.store.domain.TStoreComment;
import io.uhha.store.service.ITStoreCommentService;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.common.core.page.TableDataInfo;

/**
 * 店铺评论Controller
 * 
 * @author ruoyi
 * @date 2021-05-23
 */
@Api(tags = "店铺评论Controller")
@RestController
@RequestMapping("/store/TStoreComment")
public class TStoreCommentController extends BaseController
{
    @Autowired
    private ITStoreCommentService tStoreCommentService;

    /**
     * 查询店铺评论列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreComment:list')")
    @GetMapping("/list")
    public TableDataInfo list(TStoreComment tStoreComment)
    {
        startPage();
        List<TStoreComment> list = tStoreCommentService.selectTStoreCommentList(tStoreComment);
        return getDataTable(list);
    }

    /**
     * 导出店铺评论列表
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreComment:export')")
    @Log(title = "店铺评论", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TStoreComment tStoreComment)
    {
        List<TStoreComment> list = tStoreCommentService.selectTStoreCommentList(tStoreComment);
        ExcelUtil<TStoreComment> util = new ExcelUtil<TStoreComment>(TStoreComment.class);
        return util.exportExcel(list, "店铺评论数据");
    }

    /**
     * 获取店铺评论详细信息
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreComment:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tStoreCommentService.selectTStoreCommentById(id));
    }

    /**
     * 新增店铺评论
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreComment:add')")
    @Log(title = "店铺评论", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TStoreComment tStoreComment)
    {
        return toAjax(tStoreCommentService.insertTStoreComment(tStoreComment));
    }

    /**
     * 修改店铺评论
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreComment:edit')")
    @Log(title = "店铺评论", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TStoreComment tStoreComment)
    {
        return toAjax(tStoreCommentService.updateTStoreComment(tStoreComment));
    }

    /**
     * 删除店铺评论
     */
    @PreAuthorize("@ss.hasPermi('store:TStoreComment:remove')")
    @Log(title = "店铺评论", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tStoreCommentService.deleteTStoreCommentByIds(ids));
    }
}

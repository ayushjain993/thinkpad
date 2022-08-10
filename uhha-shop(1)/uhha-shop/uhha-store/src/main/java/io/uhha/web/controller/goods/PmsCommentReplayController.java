package io.uhha.web.controller.goods;

import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.goods.domain.PmsCommentReplay;
import io.uhha.goods.service.IPmsCommentReplayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评论回复Controller
 *
 * @author mj
 * @date 2020-07-24
 */
@RestController
@RequestMapping("/goods/replay")
public class PmsCommentReplayController extends BaseController {
    @Autowired
    private IPmsCommentReplayService pmsCommentReplayService;

    /**
     * 查询评论回复列表
     */
    @PreAuthorize("@ss.hasPermi('goods:replay:list')")
    @GetMapping("/list")
    public TableDataInfo list(PmsCommentReplay pmsCommentReplay) {
        startPage();
        List<PmsCommentReplay> list = pmsCommentReplayService.selectPmsCommentReplayList(pmsCommentReplay);
        return getDataTable(list);
    }

    /**
     * 导出评论回复列表
     */
    @PreAuthorize("@ss.hasPermi('goods:replay:export')")
    @Log(title = "评论回复", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(PmsCommentReplay pmsCommentReplay) {
        List<PmsCommentReplay> list = pmsCommentReplayService.selectPmsCommentReplayList(pmsCommentReplay);
        ExcelUtil<PmsCommentReplay> util = new ExcelUtil<PmsCommentReplay>(PmsCommentReplay.class);
        return util.exportExcel(list, "replay");
    }

    /**
     * 获取评论回复详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:replay:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(pmsCommentReplayService.selectPmsCommentReplayById(id));
    }

    /**
     * 新增评论回复
     */
    @PreAuthorize("@ss.hasPermi('goods:replay:add')")
    @Log(title = "评论回复", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PmsCommentReplay pmsCommentReplay) {
        return toAjax(pmsCommentReplayService.insertPmsCommentReplay(pmsCommentReplay));
    }

    /**
     * 修改评论回复
     */
    @PreAuthorize("@ss.hasPermi('goods:replay:edit')")
    @Log(title = "评论回复", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PmsCommentReplay pmsCommentReplay) {
        return toAjax(pmsCommentReplayService.updatePmsCommentReplay(pmsCommentReplay));
    }

    /**
     * 删除评论回复
     */
    @PreAuthorize("@ss.hasPermi('goods:replay:remove')")
    @Log(title = "评论回复", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(pmsCommentReplayService.deletePmsCommentReplayByIds(ids));
    }
}

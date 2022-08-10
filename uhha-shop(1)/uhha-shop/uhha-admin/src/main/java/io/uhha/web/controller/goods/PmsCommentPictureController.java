package io.uhha.web.controller.goods;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.goods.domain.PmsCommentPicture;
import io.uhha.goods.service.IPmsCommentPictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 单品评论下的图片Controller
 *
 * @author mj
 * @date 2020-07-24
 */
@Api(tags = "单品评论下的图片Controller")
@RestController
@RequestMapping("/goods/picture")
public class PmsCommentPictureController extends BaseController {
    @Autowired
    private IPmsCommentPictureService pmsCommentPictureService;

    /**
     * 查询单品评论下的图片列表
     */
    @PreAuthorize("@ss.hasPermi('goods:picture:list')")
    @GetMapping("/list")
    public TableDataInfo list(PmsCommentPicture pmsCommentPicture) {
        startPage();
        List<PmsCommentPicture> list = pmsCommentPictureService.selectPmsCommentPictureList(pmsCommentPicture);
        return getDataTable(list);
    }

    /**
     * 导出单品评论下的图片列表
     */
    @PreAuthorize("@ss.hasPermi('goods:picture:export')")
    @Log(title = "单品评论下的图片", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(PmsCommentPicture pmsCommentPicture) {
        List<PmsCommentPicture> list = pmsCommentPictureService.selectPmsCommentPictureList(pmsCommentPicture);
        ExcelUtil<PmsCommentPicture> util = new ExcelUtil<PmsCommentPicture>(PmsCommentPicture.class);
        return util.exportExcel(list, "picture");
    }

    /**
     * 获取单品评论下的图片详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:picture:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(pmsCommentPictureService.selectPmsCommentPictureById(id));
    }

    /**
     * 新增单品评论下的图片
     */
    @PreAuthorize("@ss.hasPermi('goods:picture:add')")
    @Log(title = "单品评论下的图片", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PmsCommentPicture pmsCommentPicture) {
        return toAjax(pmsCommentPictureService.insertPmsCommentPicture(pmsCommentPicture));
    }

    /**
     * 修改单品评论下的图片
     */
    @PreAuthorize("@ss.hasPermi('goods:picture:edit')")
    @Log(title = "单品评论下的图片", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PmsCommentPicture pmsCommentPicture) {
        return toAjax(pmsCommentPictureService.updatePmsCommentPicture(pmsCommentPicture));
    }

    /**
     * 删除单品评论下的图片
     */
    @PreAuthorize("@ss.hasPermi('goods:picture:remove')")
    @Log(title = "单品评论下的图片", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(pmsCommentPictureService.deletePmsCommentPictureByIds(ids));
    }
}

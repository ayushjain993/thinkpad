package io.uhha.web.controller.marketing;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.enums.BusinessType;
import io.uhha.integral.domain.PointCate;
import io.uhha.integral.service.PointCateService;
import io.uhha.util.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 积分商城分类Controller
 *
 * @author uhha
 * @date 2021-11-09
 */
@Api(tags = "积分商城分类Controller")
@RestController
@RequestMapping("/marketing/pointCate")
public class PointCateController extends BaseController
{
    @Autowired
    private PointCateService pointCateService;

    /**
     * 查询积分商城分类列表
     */
    @PreAuthorize("@ss.hasPermi('marketing:pointCate:list')")
    @GetMapping
    public PageHelper list(PageHelper<PointCate> pageHelper,String name)
    {
       return pointCateService.queryPointCates(pageHelper,name);
    }

    /**
     * 获取积分商城分类详细信息
     */
    @PreAuthorize("@ss.hasPermi('marketing:pointCate:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(pointCateService.queryPointCateById(id));
    }

    /**
     * 新增积分商城分类
     */
    @PreAuthorize("@ss.hasPermi('marketing:pointCate:add')")
    @Log(title = "积分商城分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PointCate pointCate)
    {
        return toAjax(pointCateService.addPointCate(pointCate));
    }

    /**
     * 修改积分商城分类
     */
    @PreAuthorize("@ss.hasPermi('marketing:pointCate:edit')")
    @Log(title = "积分商城分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PointCate pointCate)
    {
        return toAjax(pointCateService.updatePointCate(pointCate));
    }

    /**
     * 删除积分商城分类
     */
    @PreAuthorize("@ss.hasPermi('marketing:pointCate:remove')")
    @Log(title = "积分商城分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(pointCateService.deletePointCates(ids));
    }
}

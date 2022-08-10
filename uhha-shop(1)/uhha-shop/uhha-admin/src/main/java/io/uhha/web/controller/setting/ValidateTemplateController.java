package io.uhha.web.controller.setting;

import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.validate.service.IValidateTemplateService;
import io.uhha.validate.bean.ValidateTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 校验Controller
 *
 * @author peter
 * @date 2022-01-27
 */
@RestController
@RequestMapping("/setting/validateTemplate")
public class ValidateTemplateController extends BaseController
{
    @Autowired
    private IValidateTemplateService validateTemplateService;

    /**
     * 查询校验列表
     */
    @PreAuthorize("@ss.hasPermi('setting:validateTemplate:list')")
    @GetMapping("/list")
    public TableDataInfo list(ValidateTemplate validateTemplate)
    {
        startPage();
        Map<String, Object> map = new HashMap<>();
        map.put("platform", validateTemplate.getPlatform());
        map.put("sendType", validateTemplate.getSendType());
        map.put("language", validateTemplate.getLanguage());
        map.put("businessType", validateTemplate.getBusinessType());
        List<ValidateTemplate> list = validateTemplateService.selectValidateTemplateList(map);
        return getDataTable(list);
    }

    /**
     * 获取校验详细信息
     */
    @PreAuthorize("@ss.hasPermi('setting:validateTemplate:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return AjaxResult.success(validateTemplateService.selectValidateTemplateById(id));
    }

    /**
     * 新增校验
     */
    @PreAuthorize("@ss.hasPermi('setting:validateTemplate:add')")
    @Log(title = "校验", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ValidateTemplate validateTemplate)
    {
        return toAjax(validateTemplateService.insertValidateTemplate(validateTemplate));
    }

    /**
     * 修改校验
     */
    @PreAuthorize("@ss.hasPermi('setting:validateTemplate:edit')")
    @Log(title = "校验", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ValidateTemplate validateTemplate)
    {
        return toAjax(validateTemplateService.updateValidateTemplate(validateTemplate));
    }

    /**
     * 删除校验
     */
    @PreAuthorize("@ss.hasPermi('setting:validateTemplate:remove')")
    @Log(title = "校验", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(validateTemplateService.deleteValidateTemplateByIds(ids));
    }
}

package io.uhha.web.controller.sms;

import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.BusinessType;
import io.uhha.common.enums.StatusEnum;
import io.uhha.common.utils.poi.ExcelUtil;
import io.uhha.sms.domain.SmsHomeRecommendSubject;
import io.uhha.sms.service.ISmsHomeRecommendSubjectService;
import io.uhha.web.utils.AdminLoginUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 首页推荐专题Controller
 *
 * @author ruoyi
 * @date 2020-08-06
 */
@RestController
@RequestMapping("/sms/SmsHomeRecommendSubject")
public class SmsHomeRecommendSubjectController extends BaseController {
    @Autowired
    private ISmsHomeRecommendSubjectService smsHomeRecommendSubjectService;

    /**
     * 查询首页推荐专题列表
     */
    @PreAuthorize("@ss.hasPermi('sms:SmsHomeRecommendSubject:list')")
    @GetMapping("/list")
    public TableDataInfo list(SmsHomeRecommendSubject smsHomeRecommendSubject) {
        startPage();
        smsHomeRecommendSubject.setStoreId(AdminLoginUtils.getInstance().getStoreId());
        List<SmsHomeRecommendSubject> list = smsHomeRecommendSubjectService.selectSmsHomeRecommendSubjectList(smsHomeRecommendSubject);
        return getDataTable(list);
    }

    /**
     * 导出首页推荐专题列表
     */
    @PreAuthorize("@ss.hasPermi('sms:SmsHomeRecommendSubject:export')")
    @Log(title = "首页推荐专题", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SmsHomeRecommendSubject smsHomeRecommendSubject) {
        smsHomeRecommendSubject.setStoreId(AdminLoginUtils.getInstance().getStoreId());
        List<SmsHomeRecommendSubject> list = smsHomeRecommendSubjectService.selectSmsHomeRecommendSubjectList(smsHomeRecommendSubject);
        ExcelUtil<SmsHomeRecommendSubject> util = new ExcelUtil<SmsHomeRecommendSubject>(SmsHomeRecommendSubject.class);
        return util.exportExcel(list, "SmsHomeRecommendSubject");
    }

    /**
     * 获取首页推荐专题详细信息
     */
    @PreAuthorize("@ss.hasPermi('sms:SmsHomeRecommendSubject:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(smsHomeRecommendSubjectService.selectSmsHomeRecommendSubjectById(id));
    }

    /**
     * 新增首页推荐专题
     */
    @PreAuthorize("@ss.hasPermi('sms:SmsHomeRecommendSubject:add')")
    @Log(title = "首页推荐专题", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody List<SmsHomeRecommendSubject> recommendSubjectList) {
        int added = 0;
        for (SmsHomeRecommendSubject recommendSubject : recommendSubjectList) {
            recommendSubject.setRecommendStatus(StatusEnum.YesNoType.YES.code());
            recommendSubject.setSort(0);
            recommendSubject.setStoreId(AdminLoginUtils.getInstance().getStoreId());

            SmsHomeRecommendSubject query = SmsHomeRecommendSubject.builder()
                    .recommendStatus(StatusEnum.YesNoType.YES.code())
                    .subjectId(recommendSubject.getSubjectId())
                    .storeId(AdminLoginUtils.getInstance().getStoreId())
                    .build();

            List<SmsHomeRecommendSubject> result = smsHomeRecommendSubjectService.selectSmsHomeRecommendSubjectList(query);
           if(CollectionUtils.isEmpty(result)){
               added = added +smsHomeRecommendSubjectService.insertSmsHomeRecommendSubject(recommendSubject);
           }
        }
        return toAjax(added);
    }

    /**
     * 修改首页推荐专题
     */
    @PreAuthorize("@ss.hasPermi('sms:SmsHomeRecommendSubject:edit')")
    @Log(title = "首页推荐专题", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SmsHomeRecommendSubject smsHomeRecommendSubject) {
        return toAjax(smsHomeRecommendSubjectService.updateSmsHomeRecommendSubject(smsHomeRecommendSubject));
    }

    /**
     * 删除首页推荐专题
     */
    @PreAuthorize("@ss.hasPermi('sms:SmsHomeRecommendSubject:remove')")
    @Log(title = "首页推荐专题", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(smsHomeRecommendSubjectService.deleteSmsHomeRecommendSubjectByIds(ids));
    }
    @ApiOperation("修改推荐排序")
    @RequestMapping(value = "/update/sort/{id}/{sort}", method = RequestMethod.POST)
    @ResponseBody
    public Object updateSort(@PathVariable Long id, @PathVariable Integer sort) {
        int count = smsHomeRecommendSubjectService.updateSort(id, sort);
        return toAjax(count);
    }

    @ApiOperation("批量修改推荐状态")
    @RequestMapping(value = "/update/recommendStatus", method = RequestMethod.GET)
    @ResponseBody
    public Object updateRecommendStatus(@RequestParam("ids") List<Long> ids, @RequestParam Integer recommendStatus) {
        int count = smsHomeRecommendSubjectService.updateRecommendStatus(ids, recommendStatus);
        return toAjax(count);
    }
}

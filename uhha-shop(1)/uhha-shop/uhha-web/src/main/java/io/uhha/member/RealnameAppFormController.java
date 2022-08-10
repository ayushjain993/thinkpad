package io.uhha.member;

import io.uhha.appletsutil.AppletsLoginUtils;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.utils.DateUtils;
import io.uhha.member.domain.RealnameApplicationForm;
import io.uhha.member.service.IRealnameApplicationFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/realnameAppForm")
public class RealnameAppFormController extends BaseController {

    @Autowired
    private IRealnameApplicationFormService realnameApplicationFormService;

    /**
     * 新增实名认证申请
     */
    @PostMapping
    public AjaxResult submitform(@RequestBody RealnameApplicationForm realnameApplicationForm, HttpServletRequest request)
    {
        Long userId = AppletsLoginUtils.getInstance().getCustomerId(request);
        RealnameApplicationForm form = realnameApplicationFormService.selectRealnameApplicationFormByUid(userId);
        if(form==null){
            return toAjax(realnameApplicationFormService.insertRealnameApplicationForm(realnameApplicationForm));
        }else{
            realnameApplicationForm.setUpdateBy(Long.toString(userId));
            realnameApplicationForm.setUpdateTime(DateUtils.getNowDate());
            return toAjax(realnameApplicationFormService.updateRealnameApplicationForm(realnameApplicationForm));
        }
    }

    /**
     * 查询实名认证申请
     */
    @GetMapping("/{uid}")
    public AjaxResult queryform(@PathVariable("uid") Long uid)
    {
        return AjaxResult.success(realnameApplicationFormService.selectRealnameApplicationFormByUid(uid));
    }
}

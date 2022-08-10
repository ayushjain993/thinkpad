package io.uhha.member;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.uhha.appletsutil.AppletsLoginUtils;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.enums.RealnameScreenStatusEnum;
import io.uhha.common.utils.DateUtils;
import io.uhha.member.domain.RealnameApplicationForm;
import io.uhha.member.service.IRealnameApplicationFormService;
import io.uhha.member.service.UserBankInfoService;
import io.uhha.member.vo.UserBankInfoVo;
import io.uhha.setting.domain.LsBankSetting;
import io.uhha.setting.service.ILsBankSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "银行Controller")
@RestController
@RequestMapping("/bank")
@Slf4j
public class BankController {

    @Autowired
    private ILsBankSettingService bankSettingService;

    @Autowired
    private UserBankInfoService userBankInfoService;

    @Autowired
    private IRealnameApplicationFormService realnameApplicationFormService;


    /**
     * 获取银行列表
     */
    @GetMapping("/ByCountry/{countryId}")
    public AjaxResult listByCountryId(@PathVariable("countryId") Long countryId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", "0");
        queryWrapper.eq("country_id", countryId);
        List<LsBankSetting> bankSettings = bankSettingService.list(queryWrapper);
        return AjaxResult.success(bankSettings);
    }

    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/user")
    public AjaxResult saveUserBankInfo(HttpServletRequest request, @RequestBody @Validated UserBankInfoVo userBankinfo){

        Long userId =AppletsLoginUtils.getInstance().getCustomerId(request);
        UserBankInfoVo vo = userBankInfoService.getUserBankInfoByUid(userId);

        int ret;

        if(vo ==null){
            userBankinfo.setUserId(userId);
            ret = userBankInfoService.addUserBankInfo(userBankinfo);
        }else{
            ret = userBankInfoService.updateUserBankInfo(userBankinfo);
        }

        if(ret!=1){
            log.error("fail to update user bank info");
            return AjaxResult.error();
        }

        UserBankInfoVo vo1 = userBankInfoService.getUserBankInfoByUid(userId);
        if(vo1==null){
            log.error("internal error, user bank info for {} not exists.", userId);
            return AjaxResult.error();
        }

        //提交申请表
        RealnameApplicationForm form = realnameApplicationFormService.selectRealnameApplicationFormByUid(userId);
        if(form==null){
            form = new RealnameApplicationForm();
            form.setUserBankId(vo1.getBankId());
            form.setUid(userId);
            form.setCreateBy(Long.toString(userId));
            form.setStatus(RealnameScreenStatusEnum.SUBMITTED.getCode());
            return AjaxResult.success(realnameApplicationFormService.insertRealnameApplicationForm(form));
        }else{
            form.setUserBankId(vo1.getBankId());
            form.setUpdateBy(Long.toString(userId));
            form.setUpdateTime(DateUtils.getNowDate());
            return AjaxResult.success(realnameApplicationFormService.updateRealnameApplicationForm(form));
        }
    }

    @GetMapping("/user")
    public AjaxResult getUserBank(HttpServletRequest request){
        Long userId =AppletsLoginUtils.getInstance().getCustomerId(request);
        UserBankInfoVo vo = userBankInfoService.getUserBankInfoByUid(userId);
        if(vo ==null){
            return AjaxResult.success("not found", "-1");
        }else{
            return AjaxResult.success(vo);
        }
    }

}
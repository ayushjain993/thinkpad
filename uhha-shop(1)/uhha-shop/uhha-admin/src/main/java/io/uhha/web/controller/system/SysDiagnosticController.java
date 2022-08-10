package io.uhha.web.controller.system;

import io.uhha.common.exception.BCException;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.diagno.service.SysDiagnosticService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 系统自检控制器
 */
@RestController
@RequestMapping("/system/diagnostic")
@Slf4j
public class SysDiagnosticController {

    @Autowired
    private SysDiagnosticService diagnosticService;


    /**
     * 检查全部
     */
    @PreAuthorize("@ss.hasPermi('system:diagnostic:list')")
    @GetMapping("/all")
    public AjaxResult diagnosticAll(@RequestParam(value = "uid", required = false) Long uid,
                                    @RequestParam(value = "email", required = false) String email,
                                    @RequestParam(value = "phone", required = false) String phone) throws BCException {
        return AjaxResult.success(diagnosticService.diagnosticAll(uid,email,phone ));
    }

    /**
     * 检查全部
     */
    @PreAuthorize("@ss.hasPermi('system:diagnostic:list')")
    @GetMapping("/all/progress")
    public AjaxResult diagnosticProgress() {
        throw new NotImplementedException();
    }


    /**
     * 检查rCrypto情况
     */
    @PreAuthorize("@ss.hasPermi('system:diagnostic:list')")
    @GetMapping("/crypto")
    public AjaxResult diagnosticCrypto() {
        return AjaxResult.success(diagnosticService.diagnosticCrypto());
    }

    /**
     * 检查短消息接口
     */
    @PreAuthorize("@ss.hasPermi('system:diagnostic:list')")
    @GetMapping("/sms")
    public AjaxResult diagnosticSms(@RequestParam("uid") Long uid, @RequestParam("phone") String phone, @RequestParam("code") String code) throws BCException {
        Boolean result = diagnosticService.diagnosticSms(uid, phone);
        return result? AjaxResult.success():AjaxResult.error();
    }

    /**
     * 检查邮件发送接口
     */
    @PreAuthorize("@ss.hasPermi('system:diagnostic:list')")
    @GetMapping("/email/{email}")
    public AjaxResult diagnosticEmail(@PathVariable("email") String email) {
        return AjaxResult.success(diagnosticService.diagnosticEmail(email));
    }

    /**
     * 检查支付接口
     */
    @PreAuthorize("@ss.hasPermi('system:diagnostic:list')")
    @GetMapping("/pay")
    public AjaxResult diagnosticPayment() {
        return AjaxResult.success(diagnosticService.diagnosticPayment());
    }
}

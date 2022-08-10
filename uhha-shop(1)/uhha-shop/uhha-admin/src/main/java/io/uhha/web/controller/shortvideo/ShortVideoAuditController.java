package io.uhha.web.controller.shortvideo;

import io.swagger.annotations.Api;
import io.uhha.common.annotation.Log;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.enums.BusinessType;
import io.uhha.shortvideo.domain.ShortVideo;
import io.uhha.shortvideo.service.IShortVideoService;
import io.uhha.shortvideo.vo.ShortVideoApplyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 短视频审批Controller
 *
 * @author ruoyi
 * @date 2021-08-06
 */
@Api(tags = "短视频审批Controller")
@RestController
@RequestMapping("/videoaudit")
public class ShortVideoAuditController extends BaseController {

    @Autowired
    private IShortVideoService shortVideoService;

    /**
     * 短视频审批拒绝
     */
    @PreAuthorize("@ss.hasPermi('shortvideo:video:edit')")
    @Log(title = "短视频审批拒绝", businessType = BusinessType.UPDATE)
    @PutMapping("/refuse")
    public AjaxResult refuseVideoAudit(@RequestBody ShortVideoApplyVo shortVideo)
    {
        return toAjax(shortVideoService.refuseVideoAudit(shortVideo));
    }

    /**
     * 短视频审批通过
     */
    @PreAuthorize("@ss.hasPermi('shortvideo:video:edit')")
    @Log(title = "短视频审批通过", businessType = BusinessType.UPDATE)
    @PutMapping("/pass/{videoId}")
    public AjaxResult passVideoAudit(@PathVariable("videoId") String videoId)
    {
        return toAjax(shortVideoService.passVideoAudit(videoId));
    }

    //TODO 解决批量审批问题
//    /**
//     * 短视频批量审批拒绝
//     */
//    @PreAuthorize("@ss.hasPermi('shortvideo:video:edit')")
//    @Log(title = "短视频批量审批拒绝", businessType = BusinessType.UPDATE)
//    @PutMapping("/batchrefuse")
//    public AjaxResult batchrefuse(String[] ids, String reason)
//    {
//        return toAjax(shortVideoService.batchRefuse(ids, reason));
//    }
//
//    /**
//     * 短视频批量审批通过
//     */
//    @PreAuthorize("@ss.hasPermi('shortvideo:video:edit')")
//    @Log(title = "短视频批量审批通过", businessType = BusinessType.UPDATE)
//    @PutMapping("/pass")
//    public AjaxResult batchpass(String[] ids)
//    {
//        return toAjax(shortVideoService.batchPass(ids));
//    }
}

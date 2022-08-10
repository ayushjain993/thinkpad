package io.uhha.pv;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.uhha.appletsutil.AppletsLoginUtils;
import io.uhha.common.annotation.RateLimiter;
import io.uhha.common.annotation.UnAuth;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.enums.LimitType;
import io.uhha.statistics.service.PVQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * PV/UV记录接口
 */
@RestController
@Api(tags = "PV/UV记录接口")
@Slf4j
public class WebPvController {
    @Autowired
    private PVQueueService pvQueueService;

    /**
     * 记录pvuv
     */
    @UnAuth
    @GetMapping(value = "/pvuv")
    @RateLimiter(key = "pvuv", time = 1, count = 100, limitType = LimitType.IP)
    public AjaxResult pvuv(HttpServletRequest request) {
        Long uid = null;
        try{
            uid = AppletsLoginUtils.getInstance().getCustomerId(request);
        }catch (Exception e){
        }
        pvQueueService.pushPvQueue(request, uid);
        return AjaxResult.success();
    }
}

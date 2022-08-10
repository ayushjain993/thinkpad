package io.uhha.web.controller.system;

import io.uhha.common.core.domain.AjaxResult;
import io.uhha.coin.system.service.ICoinRedisInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Service
@RestController
@RequestMapping("/system/redis")
public class SysRedisInitController {
    @Autowired
    private ICoinRedisInitService redisInitService;

    /**
     * 重置redis
     */
    @GetMapping(value = "/reset")
    public AjaxResult getInfo(@PathVariable Long postId)
    {
        redisInitService.redisInit();
        return AjaxResult.success();
    }

}

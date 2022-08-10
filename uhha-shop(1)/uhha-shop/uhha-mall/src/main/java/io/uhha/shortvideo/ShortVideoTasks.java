package io.uhha.shortvideo;

import io.uhha.common.core.redis.RedisCache;
import io.uhha.shortvideo.service.IShortVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("videoTasks")
public class ShortVideoTasks {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IShortVideoService shortVideoService;

    /**
     * 自动缓存标签视频
     */
    public void autoCacheTaggedVideoList(){

    }

}

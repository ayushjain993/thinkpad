package io.uhha.config;

import io.uhha.coin.common.framework.redis.RedisCryptoHelper;
import io.uhha.coin.common.oss.OssHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OssConfig {
    @Bean
    public OssHelper ossHelper(RedisCryptoHelper redisCryptoHelper) {
        OssHelper ossHelper = new OssHelper();
        ossHelper.setRedisHelper(redisCryptoHelper);
        return ossHelper;
    }
}

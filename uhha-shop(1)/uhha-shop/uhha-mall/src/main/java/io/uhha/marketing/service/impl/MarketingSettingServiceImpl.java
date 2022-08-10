package io.uhha.marketing.service.impl;


import io.uhha.common.core.redis.RedisConstant;
import io.uhha.marketing.domain.MarketingSetting;
import io.uhha.marketing.mapper.MarketingSettingMapper;
import io.uhha.marketing.service.MarketingSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 营销设置服务实现类
 */
@Service
public class MarketingSettingServiceImpl implements MarketingSettingService {


    /**
     * 注入营销设置数据库接口
     */
    @Autowired
    private MarketingSettingMapper marketingSettingMapper;

    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(MarketingSettingServiceImpl.class);

    @Cacheable(value = RedisConstant.MARKETING_SETTING, key = "'marketingSetting'")
    @Override
    public MarketingSetting queryMarketingSetting() {
        logger.debug("queryMarketingSetting ......");
        return marketingSettingMapper.queryMarketingSetting();
    }

    @CacheEvict(value = RedisConstant.MARKETING_SETTING, allEntries = true)
    @Override
    public int saveOrUpdateMarketingSetting(MarketingSetting marketingSetting) {
        logger.debug("saveOrUpdateMarketingSetting and marketingSetting:{}", marketingSetting);
        if (Objects.isNull(marketingSetting)) {
            logger.error("saveOrUpdateMarketingSetting fail : marketingSetting is null");
            return -1;
        }
        if (Objects.nonNull(marketingSetting.getId()) && -1 != marketingSetting.getId()) {
            logger.info("saveOrUpdateMarketingSetting :update");
            return marketingSettingMapper.updateMarketingSetting(marketingSetting);
        }
        logger.info("saveOrUpdateMarketingSetting :save");
        return marketingSettingMapper.addMarketingSetting(marketingSetting);
    }
}

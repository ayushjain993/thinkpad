package io.uhha.integral.service.impl;


import io.uhha.integral.domain.PointSetting;
import io.uhha.integral.mapper.PointSettingMapper;
import io.uhha.integral.service.PointSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Created by mj on 17/5/23.
 * 积分设置服务实现接口
 */
@Service
public class PointSettingServiceImpl implements PointSettingService {

    /**
     * 调试日志
     */
    private Logger logger = LoggerFactory.getLogger(PointSettingServiceImpl.class);

    /**
     * 注入积分设置数据库接口
     */
    @Autowired
    private PointSettingMapper pointSettingMapper;

    @Override
    public int updatePointSetting(PointSetting pointSetting) {
        logger.debug("updatePointSetting and pointSetting");

        if (Objects.isNull(pointSetting)) {
            logger.error("updatePointSetting fail due to params is null...");
            return 0;
        }
        return pointSettingMapper.updatePointSetting(pointSetting);
    }

    @Override
    public PointSetting queryPointSetting() {
        logger.debug("begin to queryPointSetting");
        return pointSettingMapper.queryPointSetting();
    }
}

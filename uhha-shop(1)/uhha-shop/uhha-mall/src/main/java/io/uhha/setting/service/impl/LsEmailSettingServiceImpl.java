package io.uhha.setting.service.impl;

import io.uhha.common.core.redis.RedisCache;
import io.uhha.common.core.redis.RedisConstant;
import io.uhha.setting.domain.LsEmailSetting;
import io.uhha.setting.mapper.LsEmailSettingMapper;
import io.uhha.setting.service.ILsEmailSettingService;
import io.uhha.system.service.IMallRedisInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 邮箱设置Service业务层处理
 *
 * @author mj
 * @date 2020-07-28
 */
@Service
public class LsEmailSettingServiceImpl implements ILsEmailSettingService {
    @Autowired
    private LsEmailSettingMapper lsEmailSettingMapper;

    @Autowired
    private IMallRedisInitService redisInitService;

    @Override
    public LsEmailSetting queryActiveEmailSetting() {
        return lsEmailSettingMapper.selectActiveEmailSetting();
    }

    /**
     * 查询邮箱设置
     *
     * @param id 邮箱设置ID
     * @return 邮箱设置
     */
    @Override
    public LsEmailSetting selectLsEmailSettingById(Long id) {
        return lsEmailSettingMapper.selectLsEmailSettingById(id);
    }

    /**
     * 查询邮箱设置列表
     *
     * @param lsEmailSetting 邮箱设置
     * @return 邮箱设置
     */
    @Override
    public List<LsEmailSetting> selectLsEmailSettingList(LsEmailSetting lsEmailSetting) {
        return lsEmailSettingMapper.selectLsEmailSettingList(lsEmailSetting);
    }

    /**
     * 新增邮箱设置
     *
     * @param lsEmailSetting 邮箱设置
     * @return 结果
     */
    @Override
    public int insertLsEmailSetting(LsEmailSetting lsEmailSetting) {
        return lsEmailSettingMapper.insertLsEmailSetting(lsEmailSetting);
    }

    /**
     * 修改邮箱设置
     *
     * @param lsEmailSetting 邮箱设置
     * @return 结果
     */
    @Override
    public int updateLsEmailSetting(LsEmailSetting lsEmailSetting) {
        lsEmailSettingMapper.updateLsEmailSetting(lsEmailSetting);
        redisInitService.initEmailSettings();
        return 1;
    }
}

package io.uhha.setting.service.impl;

import io.uhha.setting.domain.LsSmsSetting;
import io.uhha.setting.mapper.LsSmsSettingMapper;
import io.uhha.setting.service.ILsSmsSettingService;
import io.uhha.system.service.IMallRedisInitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 短信接口设置Service业务层处理
 *
 * @author mj
 * @date 2020-07-28
 */
@Service
@Slf4j
public class LsSmsSettingServiceImpl implements ILsSmsSettingService {
    @Autowired
    private LsSmsSettingMapper lsSmsSettingMapper;

    @Autowired
    private IMallRedisInitService redisInitService;

    @Override
    public LsSmsSetting queryActiveSmsSetting() {
        List<LsSmsSetting> smsSettings = lsSmsSettingMapper.selectActiveSmsSetting();
        if(smsSettings.size()>=1){
            log.warn("more than 1 SMS setting are active!");
            return smsSettings.get(0);
        }else{
            log.error("No SMS setting is active!");
            return null;
        }
    }

    /**
     * 查询短信接口设置
     *
     * @param id 短信接口设置ID
     * @return 短信接口设置
     */
    @Override
    public LsSmsSetting selectLsSmsSettingById(Long id) {
        return lsSmsSettingMapper.selectLsSmsSettingById(id);
    }

    /**
     * 查询短信接口设置列表
     *
     * @param lsSmsSetting 短信接口设置
     * @return 短信接口设置
     */
    @Override
    public List<LsSmsSetting> selectLsSmsSettingList(LsSmsSetting lsSmsSetting) {
        return lsSmsSettingMapper.selectLsSmsSettingList(lsSmsSetting);
    }

    /**
     * 新增短信接口设置
     *
     * @param lsSmsSetting 短信接口设置
     * @return 结果
     */
    @Override
    public int insertLsSmsSetting(LsSmsSetting lsSmsSetting) {
        return lsSmsSettingMapper.insertLsSmsSetting(lsSmsSetting);
    }

    /**
     * 修改短信接口设置
     *
     * @param lsSmsSetting 短信接口设置
     * @return 结果
     */
    @Override
    public int updateLsSmsSetting(LsSmsSetting lsSmsSetting) {
        lsSmsSettingMapper.updateLsSmsSetting(lsSmsSetting);
        redisInitService.initSmsSettings();
        return 1;
    }
}

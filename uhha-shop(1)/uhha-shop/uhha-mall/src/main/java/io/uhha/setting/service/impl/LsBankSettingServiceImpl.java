package io.uhha.setting.service.impl;

import java.util.List;
import io.uhha.common.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.uhha.setting.mapper.LsBankSettingMapper;
import io.uhha.setting.domain.LsBankSetting;
import io.uhha.setting.service.ILsBankSettingService;

/**
 * 银行配置信息Service业务层处理
 * 
 * @author uhha
 * @date 2022-03-12
 */
@Service
public class LsBankSettingServiceImpl extends ServiceImpl<LsBankSettingMapper, LsBankSetting> implements ILsBankSettingService
{
    @Autowired
    private LsBankSettingMapper lsBankSettingMapper;

    /**
     * 查询银行配置信息
     * 
     * @param id 银行配置信息主键
     * @return 银行配置信息
     */
    @Override
    public LsBankSetting selectLsBankSettingById(Long id)
    {
        return lsBankSettingMapper.selectLsBankSettingById(id);
    }

    /**
     * 查询银行配置信息列表
     * 
     * @param lsBankSetting 银行配置信息
     * @return 银行配置信息
     */
    @Override
    public List<LsBankSetting> selectLsBankSettingList(LsBankSetting lsBankSetting)
    {
        return lsBankSettingMapper.selectLsBankSettingList(lsBankSetting);
    }

    /**
     * 新增银行配置信息
     * 
     * @param lsBankSetting 银行配置信息
     * @return 结果
     */
    @Override
    public int insertLsBankSetting(LsBankSetting lsBankSetting)
    {
        lsBankSetting.setCreateTime(DateUtils.getNowDate());
        return lsBankSettingMapper.insertLsBankSetting(lsBankSetting);
    }

    /**
     * 修改银行配置信息
     * 
     * @param lsBankSetting 银行配置信息
     * @return 结果
     */
    @Override
    public int updateLsBankSetting(LsBankSetting lsBankSetting)
    {
        return lsBankSettingMapper.updateLsBankSetting(lsBankSetting);
    }

    /**
     * 批量删除银行配置信息
     * 
     * @param ids 需要删除的银行配置信息主键
     * @return 结果
     */
    @Override
    public int deleteLsBankSettingByIds(Long[] ids)
    {
        return lsBankSettingMapper.deleteLsBankSettingByIds(ids);
    }

    /**
     * 删除银行配置信息信息
     * 
     * @param id 银行配置信息主键
     * @return 结果
     */
    @Override
    public int deleteLsBankSettingById(Long id)
    {
        return lsBankSettingMapper.deleteLsBankSettingById(id);
    }
}

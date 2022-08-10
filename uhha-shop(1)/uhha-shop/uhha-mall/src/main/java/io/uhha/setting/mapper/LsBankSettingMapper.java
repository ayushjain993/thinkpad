package io.uhha.setting.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.uhha.setting.domain.LsBankSetting;

/**
 * 银行配置信息Mapper接口
 * 
 * @author uhha
 * @date 2022-03-12
 */
public interface LsBankSettingMapper extends BaseMapper<LsBankSetting>
{
    /**
     * 查询银行配置信息
     * 
     * @param id 银行配置信息主键
     * @return 银行配置信息
     */
    public LsBankSetting selectLsBankSettingById(Long id);

    /**
     * 查询银行配置信息列表
     * 
     * @param lsBankSetting 银行配置信息
     * @return 银行配置信息集合
     */
    public List<LsBankSetting> selectLsBankSettingList(LsBankSetting lsBankSetting);

    /**
     * 新增银行配置信息
     * 
     * @param lsBankSetting 银行配置信息
     * @return 结果
     */
    public int insertLsBankSetting(LsBankSetting lsBankSetting);

    /**
     * 修改银行配置信息
     * 
     * @param lsBankSetting 银行配置信息
     * @return 结果
     */
    public int updateLsBankSetting(LsBankSetting lsBankSetting);

    /**
     * 删除银行配置信息
     * 
     * @param id 银行配置信息主键
     * @return 结果
     */
    public int deleteLsBankSettingById(Long id);

    /**
     * 批量删除银行配置信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLsBankSettingByIds(Long[] ids);
}

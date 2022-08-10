package io.uhha.setting.mapper;

import io.uhha.setting.domain.LsSmsSetting;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 短信接口设置Mapper接口
 *
 * @author mj
 * @date 2020-07-28
 */
@Mapper
public interface LsSmsSettingMapper {
    /**
     * 查询短信接口设置
     *
     * @param id 短信接口设置ID
     * @return 短信接口设置
     */
    public LsSmsSetting selectLsSmsSettingById(Long id);

    /**
     * 查询短信接口设置列表
     *
     * @param lsSmsSetting 短信接口设置
     * @return 短信接口设置集合
     */
    public List<LsSmsSetting> selectLsSmsSettingList(LsSmsSetting lsSmsSetting);

    /**
     * 新增短信接口设置
     *
     * @param lsSmsSetting 短信接口设置
     * @return 结果
     */
    public int insertLsSmsSetting(LsSmsSetting lsSmsSetting);

    /**
     * 修改短信接口设置
     *
     * @param lsSmsSetting 短信接口设置
     * @return 结果
     */
    public int updateLsSmsSetting(LsSmsSetting lsSmsSetting);

    /**
     * 删除短信接口设置
     *
     * @param id 短信接口设置ID
     * @return 结果
     */
    public int deleteLsSmsSettingById(Long id);

    /**
     * 批量删除短信接口设置
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteLsSmsSettingByIds(Long[] ids);

    public List<LsSmsSetting> selectActiveSmsSetting();
}

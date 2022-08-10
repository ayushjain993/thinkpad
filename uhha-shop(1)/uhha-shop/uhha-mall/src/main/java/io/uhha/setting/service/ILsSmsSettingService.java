package io.uhha.setting.service;

import io.uhha.setting.domain.LsSmsSetting;

import java.util.List;

/**
 * 短信接口设置Service接口
 *
 * @author mj
 * @date 2020-07-28
 */
public interface ILsSmsSettingService {

    public LsSmsSetting queryActiveSmsSetting();

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

}

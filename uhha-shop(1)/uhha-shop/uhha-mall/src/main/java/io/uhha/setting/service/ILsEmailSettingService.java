package io.uhha.setting.service;

import io.uhha.setting.domain.LsEmailSetting;

import java.util.List;

/**
 * 邮箱设置Service接口
 *
 * @author mj
 * @date 2020-07-28
 */
public interface ILsEmailSettingService {

    /**
     * 查询活跃的邮箱设置
     *
     * @return 邮箱设置
     */
    public LsEmailSetting queryActiveEmailSetting();

    /**
     * 查询邮箱设置
     *
     * @param id 邮箱设置ID
     * @return 邮箱设置
     */
    public LsEmailSetting selectLsEmailSettingById(Long id);

    /**
     * 查询邮箱设置列表
     *
     * @param lsEmailSetting 邮箱设置
     * @return 邮箱设置集合
     */
    public List<LsEmailSetting> selectLsEmailSettingList(LsEmailSetting lsEmailSetting);

    /**
     * 新增邮箱设置
     *
     * @param lsEmailSetting 邮箱设置
     * @return 结果
     */
    public int insertLsEmailSetting(LsEmailSetting lsEmailSetting);

    /**
     * 修改邮箱设置
     *
     * @param lsEmailSetting 邮箱设置
     * @return 结果
     */
    public int updateLsEmailSetting(LsEmailSetting lsEmailSetting);

}

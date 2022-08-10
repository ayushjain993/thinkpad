package io.uhha.setting.service;

import io.uhha.setting.bean.OssSetCommon;
import io.uhha.setting.domain.LsOssSetting;

import java.util.List;

/**
 * Oss设置Service接口
 *
 * @author mj
 * @date 2020-07-28
 */
public interface ILsOssSettingService {
    /**
     * 查询所有支付接口设置
     *
     * @return 返回OssSetCommon
     */
    OssSetCommon queryOssSet();

    /**
     * 编辑支付接口设置
     *
     * @return -1编辑出错 1成功
     */
    int editOssSet(OssSetCommon OssSetCommon, String codeType);

    /**
     * 查询支付设置
     *
     * @param id 支付设置ID
     * @return 支付设置
     */
    public LsOssSetting selectLsOssSettingById(Long id);

    /**
     * 查询支付设置列表
     *
     * @param lsOssSetting 支付设置
     * @return 支付设置集合
     */
    public List<LsOssSetting> selectLsOssSettingList(LsOssSetting lsOssSetting);

    /**
     * 新增支付设置
     *
     * @param lsOssSetting 支付设置
     * @return 结果
     */
    public int insertLsOssSetting(LsOssSetting lsOssSetting);

    /**
     * 修改支付设置
     *
     * @param lsOssSetting 支付设置
     * @return 结果
     */
    public int updateLsOssSetting(LsOssSetting lsOssSetting);

    /**
     * 批量删除支付设置
     *
     * @param ids 需要删除的支付设置ID
     * @return 结果
     */
    public int deleteLsOssSettingByIds(Long[] ids);

    /**
     * 删除支付设置信息
     *
     * @param id 支付设置ID
     * @return 结果
     */
    public int deleteLsOssSettingById(Long id);
}

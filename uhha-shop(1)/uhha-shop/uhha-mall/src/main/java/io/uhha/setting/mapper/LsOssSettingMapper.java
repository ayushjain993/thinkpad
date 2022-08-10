package io.uhha.setting.mapper;

import io.uhha.setting.bean.OssSetting;
import io.uhha.setting.domain.LsOssSetting;

import java.util.List;

/**
 * Oss设置Mapper接口
 *
 * @author mj
 * @date 2020-07-28
 */
public interface LsOssSettingMapper {
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
     * 删除支付设置
     *
     * @param id 支付设置ID
     * @return 结果
     */
    public int deleteLsOssSettingById(Long id);

    /**
     * 批量删除支付设置
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteLsOssSettingByIds(Long[] ids);

    List<OssSetting> queryOssSet();

    /**
     * 根据codeType删除支付接口设置
     *
     * @param codeType 1支付宝 2微信 3银联
     * @return 返回删除行数
     */

    int deleteOssSet(String codeType);

    /**
     * 批量插入支付接口设置
     *
     * @param list PaySet集合
     * @return 返回插入行数
     */

    int addOssSet(List<OssSetting> list);
}

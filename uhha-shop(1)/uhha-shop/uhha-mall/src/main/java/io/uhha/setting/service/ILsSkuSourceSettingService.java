package io.uhha.setting.service;

import io.uhha.setting.bean.OssSetCommon;
import io.uhha.setting.bean.SkuSourceCommon;
import io.uhha.setting.domain.LsOssSetting;
import io.uhha.setting.domain.LsSkuSourceSetting;

import java.util.List;

/**
 * Oss设置Service接口
 *
 * @author mj
 * @date 2020-07-28
 */
public interface ILsSkuSourceSettingService {
    /**
     * 查询所有支付接口设置
     *
     * @return 返回SkuSourceCommon
     */
    SkuSourceCommon querySkuSourceSet();

    /**
     * 编辑支付接口设置
     *
     * @return -1编辑出错 1成功
     */
    int editSkuSourceSet(SkuSourceCommon skuSourceCommon, String codeType);

    /**
     * 查询支付设置
     *
     * @param id 支付设置ID
     * @return 支付设置
     */
    public LsSkuSourceSetting selectLsSkuSourceSettingById(Long id);

    /**
     * 查询支付设置列表
     *
     * @param skuSourceSetting 支付设置
     * @return 支付设置集合
     */
    public List<LsSkuSourceSetting> selectLsSkuSourceSettingList(LsSkuSourceSetting skuSourceSetting);

    /**
     * 新增支付设置
     *
     * @param skuSourceSetting 支付设置
     * @return 结果
     */
    public int insertLsSkuSourceSetting(LsSkuSourceSetting skuSourceSetting);

    /**
     * 修改支付设置
     *
     * @param skuSourceSetting 支付设置
     * @return 结果
     */
    public int updateLsSkuSourceSetting(LsSkuSourceSetting skuSourceSetting);

    /**
     * 批量删除支付设置
     *
     * @param ids 需要删除的支付设置ID
     * @return 结果
     */
    public int deleteLsSkuSourceSettingByIds(Long[] ids);

    /**
     * 删除支付设置信息
     *
     * @param id 支付设置ID
     * @return 结果
     */
    public int deleteLsSkuSourceSettingById(Long id);
}

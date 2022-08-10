package io.uhha.setting.mapper;

import io.uhha.setting.bean.OssSetting;
import io.uhha.setting.bean.SkuSourceSetting;
import io.uhha.setting.domain.LsSkuSourceSetting;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 第三方SKU来源设置对象Mapper接口
 *
 * @author uhha
 * @date 2021-07-28
 */
@Mapper
public interface LsSkuSourceSettingMapper {
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
     * @param LsSkuSourceSetting 支付设置
     * @return 支付设置集合
     */
    public List<LsSkuSourceSetting> selectLsSkuSourceSettingList(LsSkuSourceSetting LsSkuSourceSetting);

    /**
     * 新增支付设置
     *
     * @param LsSkuSourceSetting 支付设置
     * @return 结果
     */
    public int insertLsSkuSourceSetting(LsSkuSourceSetting LsSkuSourceSetting);

    /**
     * 修改支付设置
     *
     * @param LsSkuSourceSetting 支付设置
     * @return 结果
     */
    public int updateLsSkuSourceSetting(LsSkuSourceSetting LsSkuSourceSetting);

    /**
     * 删除支付设置
     *
     * @param id 支付设置ID
     * @return 结果
     */
    public int deleteLsSkuSourceSettingById(Long id);

    /**
     * 批量删除支付设置
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteLsSkuSourceSettingByIds(Long[] ids);

    List<SkuSourceSetting> querySkuSettingSet();

    /**
     * 根据codeType删除支付接口设置
     *
     * @param codeType 1支付宝 2微信 3银联
     * @return 返回删除行数
     */

    int deleteSkuSettingSet(String codeType);

    /**
     * 批量插入支付接口设置
     *
     * @param list PaySet集合
     * @return 返回插入行数
     */

    int addSkuSettingSet(List<SkuSourceSetting> list);
}

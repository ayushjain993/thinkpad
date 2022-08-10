package io.uhha.setting.mapper;

import io.uhha.setting.bean.PaySet;
import io.uhha.setting.domain.LsPaySetting;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 支付设置Mapper接口
 *
 * @author mj
 * @date 2020-07-28
 */
@Mapper
public interface LsPaySettingMapper {
    /**
     * 查询支付设置
     *
     * @param id 支付设置ID
     * @return 支付设置
     */
    public LsPaySetting selectLsPaySettingById(Long id);

    /**
     * 查询支付设置列表
     *
     * @param lsPaySetting 支付设置
     * @return 支付设置集合
     */
    public List<LsPaySetting> selectLsPaySettingList(LsPaySetting lsPaySetting);

    /**
     * 新增支付设置
     *
     * @param lsPaySetting 支付设置
     * @return 结果
     */
    public int insertLsPaySetting(LsPaySetting lsPaySetting);

    /**
     * 修改支付设置
     *
     * @param lsPaySetting 支付设置
     * @return 结果
     */
    public int updateLsPaySetting(LsPaySetting lsPaySetting);

    /**
     * 删除支付设置
     *
     * @param id 支付设置ID
     * @return 结果
     */
    public int deleteLsPaySettingById(Long id);

    /**
     * 批量删除支付设置
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteLsPaySettingByIds(Long[] ids);

    List<PaySet> queryPaySet();

    /**
     * 根据codeType删除支付接口设置
     *
     * @param codeType 1支付宝 2微信 3银联
     * @return 返回删除行数
     */

    int deletePaySet(String codeType);

    /**
     * 批量插入支付接口设置
     *
     * @param list PaySet集合
     * @return 返回插入行数
     */

    int addPaySet(List<PaySet> list);
}

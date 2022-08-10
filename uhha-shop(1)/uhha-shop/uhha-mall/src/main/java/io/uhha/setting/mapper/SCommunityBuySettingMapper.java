package io.uhha.setting.mapper;

import io.uhha.setting.domain.SCommunityBuySetting;

import java.util.List;

/**
 * 社区团购设置Mapper接口
 *
 * @author mj
 * @date 2020-07-28
 */
public interface SCommunityBuySettingMapper {
    /**
     * 查询社区团购设置
     *
     * @param id 社区团购设置ID
     * @return 社区团购设置
     */
    public SCommunityBuySetting selectSCommunityBuySettingById(Long id);

    /**
     * 查询社区团购设置列表
     *
     * @param sCommunityBuySetting 社区团购设置
     * @return 社区团购设置集合
     */
    public List<SCommunityBuySetting> selectSCommunityBuySettingList(SCommunityBuySetting sCommunityBuySetting);

    /**
     * 新增社区团购设置
     *
     * @param sCommunityBuySetting 社区团购设置
     * @return 结果
     */
    public int insertSCommunityBuySetting(SCommunityBuySetting sCommunityBuySetting);

    /**
     * 修改社区团购设置
     *
     * @param sCommunityBuySetting 社区团购设置
     * @return 结果
     */
    public int updateSCommunityBuySetting(SCommunityBuySetting sCommunityBuySetting);

    /**
     * 删除社区团购设置
     *
     * @param id 社区团购设置ID
     * @return 结果
     */
    public int deleteSCommunityBuySettingById(Long id);

    /**
     * 批量删除社区团购设置
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSCommunityBuySettingByIds(Long[] ids);
}

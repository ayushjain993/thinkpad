package io.uhha.setting.mapper;

import io.uhha.setting.domain.LsDistrict;

import java.util.List;

/**
 * 区域区Mapper接口
 *
 * @author mj
 * @date 2020-07-29
 */
public interface LsDistrictMapper {
    /**
     * 查询区域区
     *
     * @param id 区域区ID
     * @return 区域区
     */
    public LsDistrict selectLsDistrictById(Long id);

    /**
     * 查询区域区列表
     *
     * @param lsDistrict 区域区
     * @return 区域区集合
     */
    public List<LsDistrict> selectLsDistrictList(LsDistrict lsDistrict);

    /**
     * 新增区域区
     *
     * @param lsDistrict 区域区
     * @return 结果
     */
    public int insertLsDistrict(LsDistrict lsDistrict);

    /**
     * 修改区域区
     *
     * @param lsDistrict 区域区
     * @return 结果
     */
    public int updateLsDistrict(LsDistrict lsDistrict);

    /**
     * 删除区域区
     *
     * @param id 区域区ID
     * @return 结果
     */
    public int deleteLsDistrictById(Long id);

    /**
     * 批量删除区域区
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteLsDistrictByIds(Long[] ids);
}

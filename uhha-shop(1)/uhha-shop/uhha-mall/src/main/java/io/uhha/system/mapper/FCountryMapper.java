package io.uhha.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.uhha.system.domain.FCountry;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 国家表Mapper接口
 *
 * @author uhha
 * @date 2021-10-11
 */
@Mapper
public interface FCountryMapper extends BaseMapper<FCountry>
{
    /**
     * 查询国家表
     *
     * @param id 国家表主键
     * @return 国家表
     */
    public FCountry selectFCountryById(Long id);

    /**
     * 查询国家表列表，管理后台使用
     *
     * @param fCountry 国家表
     * @return 国家表集合
     */
    public List<FCountry> selectFCountryList(FCountry fCountry);

    /**
     * 查询enabled的国家表列表，前台使用
     *
     * @return 国家表集合
     */
    public List<FCountry> selectAllCountries();

    /**
     * 新增国家表
     *
     * @param fCountry 国家表
     * @return 结果
     */
    public int insertFCountry(FCountry fCountry);

    /**
     * 修改国家表
     *
     * @param fCountry 国家表
     * @return 结果
     */
    public int updateFCountry(FCountry fCountry);

    /**
     * 删除国家表
     *
     * @param id 国家表主键
     * @return 结果
     */
    public int deleteFCountryById(Long id);

    /**
     * 批量删除国家表
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFCountryByIds(Long[] ids);
}
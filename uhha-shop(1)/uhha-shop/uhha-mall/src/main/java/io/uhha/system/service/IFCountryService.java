package io.uhha.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.uhha.system.domain.FCountry;

import java.util.List;

public interface IFCountryService extends IService <FCountry>{

    public FCountry selectFCountryById(Long id);

    public List<FCountry> selectAllCountries();

    public List<FCountry> selectFCountryList(FCountry fCountry);

    /**
     * 新增国家
     *
     * @param fCountry 增国家
     * @return 结果
     */
    public int insertFCountry(FCountry fCountry);

    /**
     * 修改增国家
     *
     * @param fCountry 增国家
     * @return 结果
     */
    public int updateFCountry(FCountry fCountry);

    /**
     * 批量删除增国家
     *
     * @param ids 需要删除的增国家主键集合
     * @return 结果
     */
    public int deleteFCountryByIds(Long[] ids);

    /**
     * 删除增国家信息
     *
     * @param id 增国家主键
     * @return 结果
     */
    public int deleteFCountryById(Long id);
}

package io.uhha.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.uhha.common.redis.RedisMallHelper;
import io.uhha.system.domain.FCountry;
import io.uhha.system.mapper.FCountryMapper;
import io.uhha.system.service.IFCountryService;
import io.uhha.system.service.IMallRedisInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FCountryServiceImpl extends ServiceImpl<FCountryMapper,FCountry> implements IFCountryService {

    @Autowired
    private FCountryMapper fCountryMapper;

    @Autowired
    private IMallRedisInitService redisInitService;

    @Override
    public FCountry selectFCountryById(Long id)
    {
        return fCountryMapper.selectFCountryById(id);
    }


    @Override
    public List<FCountry> selectAllCountries() {
        return fCountryMapper.selectAllCountries();
    }

    /**
     * 查询国家列表
     *
     * @param fCountry 国家
     * @return 国家
     */
    @Override
    public List<FCountry> selectFCountryList(FCountry fCountry)
    {
        return fCountryMapper.selectFCountryList(fCountry);
    }

    /**
     * 新增国家
     *
     * @param fCountry 国家
     * @return 结果
     */
    @Override
    public int insertFCountry(FCountry fCountry)
    {
        int i= fCountryMapper.insertFCountry(fCountry);
        if(i==1){
            redisInitService.initCountries();
        }
        return i;
    }

    /**
     * 修改国家
     *
     * @param fCountry 国家
     * @return 结果
     */
    @Override
    public int updateFCountry(FCountry fCountry)
    {
        int i= fCountryMapper.updateFCountry(fCountry);
        if(i==1){
            redisInitService.initCountries();
        }
        return i;
    }

    /**
     * 批量删除国家
     *
     * @param ids 需要删除的国家主键
     * @return 结果
     */
    @Override
    public int deleteFCountryByIds(Long[] ids)
    {
        int i= fCountryMapper.deleteFCountryByIds(ids);
        if(i==ids.length){
            redisInitService.initCountries();
        }
        return i;
    }

    /**
     * 删除国家信息
     *
     * @param id 国家主键
     * @return 结果
     */
    @Override
    public int deleteFCountryById(Long id)
    {
        int i=  fCountryMapper.deleteFCountryById(id);
        if(i==1){
            redisInitService.initCountries();
        }
        return i;
    }
}

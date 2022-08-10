package io.uhha.sms.service.impl;

import io.uhha.sms.domain.SmsHomeAdvertise;
import io.uhha.sms.mapper.SmsHomeAdvertiseMapper;
import io.uhha.sms.service.ISmsHomeAdvertiseService;
import io.uhha.util.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 首页轮播广告Service业务层处理
 *
 * @author ruoyi
 * @date 2020-08-06
 */
@Slf4j
@Service
public class SmsHomeAdvertiseServiceImpl implements ISmsHomeAdvertiseService {

    @Autowired
    private SmsHomeAdvertiseMapper smsHomeAdvertiseMapper;

    /**
     * 查询首页轮播广告
     *
     * @param id 首页轮播广告ID
     * @return 首页轮播广告
     */
    @Override
    public SmsHomeAdvertise selectSmsHomeAdvertiseById(Long id) {
        return smsHomeAdvertiseMapper.selectSmsHomeAdvertiseById(id);
    }

    /**
     * 查询首页轮播广告列表
     *
     * @param smsHomeAdvertise 首页轮播广告
     * @return 首页轮播广告
     */
    @Override
    public List<SmsHomeAdvertise> selectSmsHomeAdvertiseList(SmsHomeAdvertise smsHomeAdvertise) {
        return smsHomeAdvertiseMapper.selectSmsHomeAdvertiseList(smsHomeAdvertise);
    }

    /**
     * 新增首页轮播广告
     *
     * @param smsHomeAdvertise 首页轮播广告
     * @return 结果
     */
    @Override
    public int insertSmsHomeAdvertise(SmsHomeAdvertise smsHomeAdvertise) {
        //TODO 是否需要广告首页审批？当前不审批
//        if(smsHomeAdvertise.getStoreId() == CommonConstant.ADMIN_STOREID){
//        }
        smsHomeAdvertise.setStatus(1);
        return smsHomeAdvertiseMapper.insertSmsHomeAdvertise(smsHomeAdvertise);
    }

    /**
     * 修改首页轮播广告
     *
     * @param smsHomeAdvertise 首页轮播广告
     * @return 结果
     */
    @Override
    public int updateSmsHomeAdvertise(SmsHomeAdvertise smsHomeAdvertise) {
        return smsHomeAdvertiseMapper.updateSmsHomeAdvertise(smsHomeAdvertise);
    }

    /**
     * 批量删除首页轮播广告
     *
     * @param ids 需要删除的首页轮播广告ID
     * @return 结果
     */
    @Override
    public int deleteSmsHomeAdvertiseByIds(Long[] ids) {
        return smsHomeAdvertiseMapper.deleteSmsHomeAdvertiseByIds(ids);
    }

    /**
     * 删除首页轮播广告信息
     *
     * @param id 首页轮播广告ID
     * @return 结果
     */
    @Override
    public int deleteSmsHomeAdvertiseById(Long id) {
        return smsHomeAdvertiseMapper.deleteSmsHomeAdvertiseById(id);
    }
}

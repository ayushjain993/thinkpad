package io.uhha.sms.service.impl;

import io.uhha.common.core.redis.RedisConstant;
import io.uhha.common.redis.RedisMallHelper;
import io.uhha.sms.domain.SmsHomeNewProduct;
import io.uhha.sms.domain.SmsHomeRecommendProduct;
import io.uhha.sms.mapper.SmsHomeRecommendProductMapper;
import io.uhha.sms.service.ISmsHomeRecommendProductService;
import io.uhha.util.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 人气推荐商品Service业务层处理
 *
 * @author ruoyi
 * @date 2020-08-06
 */
@Slf4j
@Service
public class SmsHomeRecommendProductServiceImpl implements ISmsHomeRecommendProductService {

    @Autowired
    private SmsHomeRecommendProductMapper smsHomeRecommendProductMapper;

    @Autowired
    private RedisMallHelper redisMallHelper;

    /**
     * 查询人气推荐商品
     *
     * @param id 人气推荐商品ID
     * @return 人气推荐商品
     */
    @Override
    public SmsHomeRecommendProduct selectSmsHomeRecommendProductById(Long id) {
        return smsHomeRecommendProductMapper.selectSmsHomeRecommendProductById(id);
    }

    @Override
    public int updateSort(Long id, Integer sort, Long storeId) {
        SmsHomeRecommendProduct recommendProduct = new SmsHomeRecommendProduct();
        recommendProduct.setId(id);
        recommendProduct.setSort(sort);
        redisMallHelper.remove(RedisConstant.RECOMMEND_PRODUCT+storeId);
        return smsHomeRecommendProductMapper.updateSmsHomeRecommendProduct(recommendProduct);
    }

    @Override
    public int updateRecommendStatus(Long[] ids, Integer recommendStatus, Long storeId) {
        redisMallHelper.remove(RedisConstant.RECOMMEND_PRODUCT+storeId);
        return smsHomeRecommendProductMapper.batchUpdateSmsHomeRecommendProduct(ids, recommendStatus);
    }

    /**
     * 查询人气推荐商品列表
     *
     * @return 人气推荐商品
     */
    @Override
    public List<SmsHomeRecommendProduct> selectSmsHomeRecommendProductList(Long storeId) {
        List<SmsHomeRecommendProduct> recommendProductList = redisMallHelper.getHomeRecommendProductList(storeId);
        if (CollectionUtils.isEmpty(recommendProductList)) {
            SmsHomeRecommendProduct param = new SmsHomeRecommendProduct();
            param.setStoreId(storeId);
            param.setRecommendStatus(1);
            recommendProductList = smsHomeRecommendProductMapper.selectSmsHomeRecommendProductList(param);
            redisMallHelper.setHomeRecommendProductList(recommendProductList, storeId);
        }

        return recommendProductList;
    }

    /**
     * 查询人气推荐商品列表
     *
     * @return 人气推荐商品
     */
    @Override
    public List<SmsHomeRecommendProduct> selectSmsHomeRecommendProductList(SmsHomeRecommendProduct smsHomeRecommendProduct) {
        List<SmsHomeRecommendProduct> recommendProductList = new ArrayList<>();
        smsHomeRecommendProduct.setStoreId(smsHomeRecommendProduct.getStoreId());
        recommendProductList = smsHomeRecommendProductMapper.selectSmsHomeRecommendProductList(smsHomeRecommendProduct);
        return recommendProductList;
    }

    /**
     * 新增人气推荐商品
     *
     * @param smsHomeRecommendProduct 人气推荐商品
     * @return 结果
     */
    @Override
    public int insertSmsHomeRecommendProduct(SmsHomeRecommendProduct smsHomeRecommendProduct) {
        redisMallHelper.remove(RedisConstant.RECOMMEND_PRODUCT+smsHomeRecommendProduct.getStoreId());
        return smsHomeRecommendProductMapper.insertSmsHomeRecommendProduct(smsHomeRecommendProduct);
    }

    /**
     * 修改人气推荐商品
     *
     * @param smsHomeRecommendProduct 人气推荐商品
     * @return 结果
     */
    @Override
    public int updateSmsHomeRecommendProduct(SmsHomeRecommendProduct smsHomeRecommendProduct) {
        redisMallHelper.remove(RedisConstant.RECOMMEND_PRODUCT+smsHomeRecommendProduct.getStoreId());
        return smsHomeRecommendProductMapper.updateSmsHomeRecommendProduct(smsHomeRecommendProduct);
    }

    /**
     * 批量删除人气推荐商品
     *
     * @param ids 需要删除的人气推荐商品ID
     * @return 结果
     */
    @Override
    public int deleteSmsHomeRecommendProductByIds(Long[] ids, Long storeId) {
        redisMallHelper.remove(RedisConstant.RECOMMEND_PRODUCT+storeId);
        return smsHomeRecommendProductMapper.deleteSmsHomeRecommendProductByIds(ids);
    }

    /**
     * 删除人气推荐商品信息
     *
     * @param id 人气推荐商品ID
     * @return 结果
     */
    @Override
    public int deleteSmsHomeRecommendProductById(Long id, Long storeId) {
        redisMallHelper.remove(RedisConstant.RECOMMEND_PRODUCT+storeId);
        return smsHomeRecommendProductMapper.deleteSmsHomeRecommendProductById(id);
    }
}

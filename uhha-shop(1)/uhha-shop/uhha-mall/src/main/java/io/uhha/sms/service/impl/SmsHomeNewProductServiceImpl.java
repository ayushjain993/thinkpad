package io.uhha.sms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.uhha.common.core.redis.RedisConstant;
import io.uhha.common.redis.RedisMallHelper;
import io.uhha.sms.domain.SmsHomeNewProduct;
import io.uhha.sms.mapper.SmsHomeNewProductMapper;
import io.uhha.sms.service.ISmsHomeNewProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 新鲜好物Service业务层处理
 *
 * @author ruoyi
 * @date 2020-08-06
 */
@Slf4j
@Service
public class SmsHomeNewProductServiceImpl extends ServiceImpl<SmsHomeNewProductMapper, SmsHomeNewProduct> implements ISmsHomeNewProductService {

    @Autowired
    private SmsHomeNewProductMapper smsHomeNewProductMapper;

    @Autowired
    private RedisMallHelper redisMallHelper;

    /**
     * 查询新鲜好物
     *
     * @param id 新鲜好物ID
     * @return 新鲜好物
     */
    @Override
    public SmsHomeNewProduct selectSmsHomeNewProductById(Long id) {
        return smsHomeNewProductMapper.selectSmsHomeNewProductById(id);
    }

    @Override
    public int updateRecommendStatus(Long[] ids, Integer recommendStatus, Long storeId) {
        redisMallHelper.remove(RedisConstant.NEW_PRODUCT+storeId);
        return smsHomeNewProductMapper.batchUpdateSmsHomeNewProduct(ids, recommendStatus);
    }

    @Override
    public int updateSort(Long id, Integer sort, Long storeId) {
        SmsHomeNewProduct homeNewProduct = new SmsHomeNewProduct();
        homeNewProduct.setId(id);
        homeNewProduct.setSort(sort);
        redisMallHelper.remove(RedisConstant.NEW_PRODUCT+storeId);
        return smsHomeNewProductMapper.updateSmsHomeNewProduct(homeNewProduct);
    }

    /**
     * 查询新鲜好物列表（后端使用）
     *
     * @param smsHomeNewProduct 新鲜好物
     * @return 新鲜好物
     */
    @Override
    public List<SmsHomeNewProduct> selectSmsHomeNewProductList(SmsHomeNewProduct smsHomeNewProduct) {
        SmsHomeNewProduct param = new SmsHomeNewProduct();
        param.setStoreId(smsHomeNewProduct.getStoreId());
        List<SmsHomeNewProduct> newProductList = smsHomeNewProductMapper.selectSmsHomeNewProductList(param);
        return newProductList;
    }

    /**
     * 查询新鲜好物列表
     *
     * @param storeId 店铺id
     * @return 新鲜好物
     */
    @Override
    public List<SmsHomeNewProduct> selectSmsHomeNewProductList(Long storeId) {
        List<SmsHomeNewProduct> newProductList = redisMallHelper.getHomeNewProductList(storeId);
        if (CollectionUtils.isEmpty(newProductList)) {
            SmsHomeNewProduct param = new SmsHomeNewProduct();
            param.setRecommendStatus(1);
            param.setStoreId(storeId);
            newProductList = smsHomeNewProductMapper.selectSmsHomeNewProductList(param);
            redisMallHelper.setHomeNewProductList(newProductList, storeId);
        }
        return newProductList;
    }

    /**
     * 新增新鲜好物
     *
     * @param smsHomeNewProduct 新鲜好物
     * @return 结果
     */
    @Override
    public int insertSmsHomeNewProduct(SmsHomeNewProduct smsHomeNewProduct) {
        redisMallHelper.remove(RedisConstant.NEW_PRODUCT+smsHomeNewProduct.getStoreId());
        return smsHomeNewProductMapper.insertSmsHomeNewProduct(smsHomeNewProduct);
    }

    /**
     * 修改新鲜好物
     *
     * @param smsHomeNewProduct 新鲜好物
     * @return 结果
     */
    @Override
    public int updateSmsHomeNewProduct(SmsHomeNewProduct smsHomeNewProduct) {
        redisMallHelper.remove(RedisConstant.NEW_PRODUCT+smsHomeNewProduct.getStoreId());
        return smsHomeNewProductMapper.updateSmsHomeNewProduct(smsHomeNewProduct);
    }

    /**
     * 批量删除新鲜好物
     *
     * @param ids 需要删除的新鲜好物ID
     * @return 结果
     */
    @Override
    public int deleteSmsHomeNewProductByIds(Long[] ids, Long storeId) {
        redisMallHelper.remove(RedisConstant.NEW_PRODUCT+storeId);
        return smsHomeNewProductMapper.deleteSmsHomeNewProductByIds(ids);
    }

    /**
     * 删除新鲜好物信息
     *
     * @param id 新鲜好物ID
     * @return 结果
     */
    @Override
    public int deleteSmsHomeNewProductById(Long id, Long storeId) {
        redisMallHelper.remove(RedisConstant.NEW_PRODUCT+storeId);
        return smsHomeNewProductMapper.deleteSmsHomeNewProductById(id);
    }
}

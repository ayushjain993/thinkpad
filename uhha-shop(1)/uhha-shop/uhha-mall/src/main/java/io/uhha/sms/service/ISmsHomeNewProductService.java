package io.uhha.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.uhha.sms.domain.SmsHomeNewProduct;

import java.util.List;

/**
 * 新鲜好物Service接口
 *
 * @author ruoyi
 * @date 2020-08-06
 */
public interface ISmsHomeNewProductService extends IService<SmsHomeNewProduct> {
    /**
     * 查询新鲜好物
     *
     * @param id 新鲜好物ID
     * @return 新鲜好物
     */
    public SmsHomeNewProduct selectSmsHomeNewProductById(Long id);

    /**
     * 查询新鲜好物列表
     *
     * @param smsHomeNewProduct 新鲜好物
     * @return 新鲜好物集合
     */
    public List<SmsHomeNewProduct> selectSmsHomeNewProductList(SmsHomeNewProduct smsHomeNewProduct);

    /**
     * 查询新鲜好物列表
     *
     * @param storeId 店铺id
     * @return 新鲜好物集合
     */
    public List<SmsHomeNewProduct> selectSmsHomeNewProductList(Long storeId);

    /**
     * 新增新鲜好物
     *
     * @param smsHomeNewProduct 新鲜好物
     * @return 结果
     */
    public int insertSmsHomeNewProduct(SmsHomeNewProduct smsHomeNewProduct);

    /**
     * 修改新鲜好物
     *
     * @param smsHomeNewProduct 新鲜好物
     * @return 结果
     */
    public int updateSmsHomeNewProduct(SmsHomeNewProduct smsHomeNewProduct);

    /**
     * 批量删除新鲜好物
     *
     * @param ids 需要删除的新鲜好物ID
     * @return 结果
     */
    public int deleteSmsHomeNewProductByIds(Long[] ids, Long storeId);

    /**
     * 删除新鲜好物信息
     *
     * @param id 新鲜好物ID
     * @return 结果
     */
    public int deleteSmsHomeNewProductById(Long id, Long storeId);

    /**
     * 修改推荐排序
     */
    int updateSort(Long id, Integer sort, Long storeId);

    /**
     * 更新推荐状态
     */
    public int updateRecommendStatus(Long[] ids, Integer recommendStatus, Long storeId);
}

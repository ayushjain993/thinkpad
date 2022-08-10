package io.uhha.sms.mapper;

import io.uhha.sms.domain.SmsHomeRecommendProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 人气推荐商品Mapper接口
 *
 * @author ruoyi
 * @date 2020-08-06
 */
@Mapper
public interface SmsHomeRecommendProductMapper {
    /**
     * 查询人气推荐商品
     *
     * @param id 人气推荐商品ID
     * @return 人气推荐商品
     */
    public SmsHomeRecommendProduct selectSmsHomeRecommendProductById(Long id);

    /**
     * 查询人气推荐商品列表
     *
     * @param smsHomeRecommendProduct 人气推荐商品
     * @return 人气推荐商品集合
     */
    public List<SmsHomeRecommendProduct> selectSmsHomeRecommendProductList(SmsHomeRecommendProduct smsHomeRecommendProduct);

    /**
     * 新增人气推荐商品
     *
     * @param smsHomeRecommendProduct 人气推荐商品
     * @return 结果
     */
    public int insertSmsHomeRecommendProduct(SmsHomeRecommendProduct smsHomeRecommendProduct);

    /**
     * 修改人气推荐商品
     *
     * @param smsHomeRecommendProduct 人气推荐商品
     * @return 结果
     */
    public int updateSmsHomeRecommendProduct(SmsHomeRecommendProduct smsHomeRecommendProduct);


    /**
     * 批量修改人气推荐商品
     * @param ids ids
     * @param status 状态
     * @return 修改的数量
     */
    public int batchUpdateSmsHomeRecommendProduct(@Param("ids")Long[] ids, @Param("status")Integer status);

    /**
     * 删除人气推荐商品
     *
     * @param id 人气推荐商品ID
     * @return 结果
     */
    public int deleteSmsHomeRecommendProductById(Long id);

    /**
     * 批量删除人气推荐商品
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSmsHomeRecommendProductByIds(Long[] ids);
}

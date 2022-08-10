package io.uhha.sms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.uhha.sms.domain.SmsHomeNewProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 新鲜好物Mapper接口
 *
 * @author ruoyi
 * @date 2020-08-06
 */
@Mapper
public interface SmsHomeNewProductMapper extends BaseMapper<SmsHomeNewProduct> {
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
     * 批量修改新鲜好物
     * @param ids
     * @param recommendStatus
     * @return
     */
    public int batchUpdateSmsHomeNewProduct(@Param("ids")Long[] ids, @Param("status")Integer recommendStatus);

    /**
     * 删除新鲜好物
     *
     * @param id 新鲜好物ID
     * @return 结果
     */
    public int deleteSmsHomeNewProductById(Long id);

    /**
     * 批量删除新鲜好物
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSmsHomeNewProductByIds(Long[] ids);
}

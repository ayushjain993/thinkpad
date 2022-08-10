package io.uhha.store.mapper;

import io.uhha.goods.domain.PmsBrand;
import io.uhha.goods.domain.PmsBrandApply;
import io.uhha.store.domain.TStoreCustomizeBrand;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 店铺自定义品牌列Mapper接口
 *
 * @author mj
 * @date 2020-07-28
 */
@Mapper
public interface TStoreCustomizeBrandMapper {
    /**
     * 查询店铺自定义品牌列
     *
     * @param id 店铺自定义品牌列ID
     * @return 店铺自定义品牌列
     */
    public TStoreCustomizeBrand selectTStoreCustomizeBrandById(Long id);

    /**
     * 查询店铺自定义品牌列列表
     *
     * @param tStoreCustomizeBrand 店铺自定义品牌列
     * @return 店铺自定义品牌列集合
     */
    public List<TStoreCustomizeBrand> selectTStoreCustomizeBrandList(TStoreCustomizeBrand tStoreCustomizeBrand);

    /**
     * 新增店铺自定义品牌列
     *
     * @param tStoreCustomizeBrand 店铺自定义品牌列
     * @return 结果
     */
    public int insertTStoreCustomizeBrand(TStoreCustomizeBrand tStoreCustomizeBrand);

    /**
     * 修改店铺自定义品牌列
     *
     * @param tStoreCustomizeBrand 店铺自定义品牌列
     * @return 结果
     */
    public int updateTStoreCustomizeBrand(TStoreCustomizeBrand tStoreCustomizeBrand);

    /**
     * 删除店铺自定义品牌列
     *
     * @param id 店铺自定义品牌列ID
     * @return 结果
     */
    public int deleteTStoreCustomizeBrandById(Long id);

    /**
     * 批量删除店铺自定义品牌列
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTStoreCustomizeBrandByIds(Long[] ids);

    /**
     * 根据品牌名称和storeId查询品牌
     *
     * @param params 查询参数
     * @return 返回品牌信息
     */
    TStoreCustomizeBrand queryBrandByNameAndStoreId(Map<String, Object> params);


    /**
     * 通过品牌审核
     *
     * @param id 品牌审核id
     * @return 成功返回1，失败返回0
     */

    int passBrandAudit(Long id);

    /**
     * 批量通过品牌审核
     *
     * @param ids 品牌审核id数组
     * @return 成功返回>=1，失败返回0
     */

    int batchPassBrandAudit(Long[] ids);

    /**
     * 拒绝品牌审核
     *
     * @param brandApply 商品审核实例
     * @return 成功返回1，失败返回0
     */

    int refuseBrandAudit(TStoreCustomizeBrand brandApply);

    /**
     * 批量拒绝品牌审核
     *
     * @param params 品牌审核id数组及拒绝原因
     * @return 成功返回>=1，失败返回0
     */

    int batchRefuseBrandAudit(Map<String, Object> params);
}

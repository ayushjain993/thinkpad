package io.uhha.store.service;

import io.uhha.goods.domain.PmsBrandApply;
import io.uhha.store.domain.TStoreCustomizeBrand;

import java.util.List;

/**
 * 店铺自定义品牌列Service接口
 *
 * @author mj
 * @date 2020-07-28
 */
public interface ITStoreCustomizeBrandService {
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
     * 批量删除店铺自定义品牌列
     *
     * @param ids 需要删除的店铺自定义品牌列ID
     * @return 结果
     */
    public int deleteTStoreCustomizeBrandByIds(Long[] ids);

    /**
     * 删除店铺自定义品牌列信息
     *
     * @param id 店铺自定义品牌列ID
     * @return 结果
     */
    public int deleteTStoreCustomizeBrandById(Long id);


    public TStoreCustomizeBrand queryBrandByNameAndStoreId(String name, long storeId);


    /**
     * 通过自定义品牌审核
     *
     * @param id 自定义品牌审核id
     * @return 成功返回1，失败返回0
     */
    int passBrandAudit(long id);

    /**
     * 批量通过自定义品牌审核
     *
     * @param ids 自定义品牌审核id数组
     * @return 成功返回>=1，失败返回0
     */
    int batchPassBrandAudit(Long[] ids);

    /**
     * 拒绝自定义品牌审核
     *
     * @param brandApply 商品自定义审核实例
     * @return 成功返回1，失败返回0
     */
    int refuseBrandAudit(TStoreCustomizeBrand brandApply);

    /**
     * 批量拒绝自定义品牌审核
     *
     * @param ids    品牌审核id数组
     * @param reason 拒绝原因
     * @return 成功返回>=1，失败返回0
     */
    int batchRefuseBrandAudit(Long[] ids, String reason);

}

package io.uhha.goods.service;

import io.uhha.goods.domain.PmsSkuSpecValue;

import java.util.List;

/**
 * 单品的规格值Service接口
 *
 * @author mj
 * @date 2020-07-24
 */
public interface IPmsSkuSpecValueService {
    /**
     * 查询单品的规格值
     *
     * @param id 单品的规格值ID
     * @return 单品的规格值
     */
    public PmsSkuSpecValue selectPmsSkuSpecValueById(Long id);

    /**
     * 查询单品的规格值列表
     *
     * @param pmsSkuSpecValue 单品的规格值
     * @return 单品的规格值集合
     */
    public List<PmsSkuSpecValue> selectPmsSkuSpecValueList(PmsSkuSpecValue pmsSkuSpecValue);

    /**
     * 新增单品的规格值
     *
     * @param pmsSkuSpecValue 单品的规格值
     * @return 结果
     */
    public int insertPmsSkuSpecValue(PmsSkuSpecValue pmsSkuSpecValue);

    /**
     * 修改单品的规格值
     *
     * @param pmsSkuSpecValue 单品的规格值
     * @return 结果
     */
    public int updatePmsSkuSpecValue(PmsSkuSpecValue pmsSkuSpecValue);

    /**
     * 批量删除单品的规格值
     *
     * @param ids 需要删除的单品的规格值ID
     * @return 结果
     */
    public int deletePmsSkuSpecValueByIds(Long[] ids);

    /**
     * 删除单品的规格值信息
     *
     * @param id 单品的规格值ID
     * @return 结果
     */
    public int deletePmsSkuSpecValueById(Long id);

    /**
     * 新增单品规格值
     *
     * @param skuSpecValues 单品规格值
     */
    void addSkuSpecValues(List<PmsSkuSpecValue> skuSpecValues);

    /**
     * 根据单品id查询单品规格值
     *
     * @param skuId 单品id
     * @return 返回单品规格值
     */
    List<PmsSkuSpecValue> queryBySkuId(String skuId);

    /**
     * 根据商品id删除单品规格值
     *
     * @param spuId 商品id
     */
    void deleteBySpuId(long spuId);

    /**
     * 根据商品id删除单品规格值 (物理删除)
     *
     * @param spuId 商品id
     */
    void deleteBySpuIdPhysical(long spuId);

    /**
     * 根据商品id查询单品的规格值信息
     *
     * @param spuId 商品id
     * @return 返回单品的规格值信息
     */
    List<PmsSkuSpecValue> queryBySpuId(long spuId);
}

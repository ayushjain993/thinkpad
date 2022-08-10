package io.uhha.goods.mapper;

import io.uhha.goods.domain.PmsGoodsImage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 商品的图片Mapper接口
 *
 * @author mj
 * @date 2020-07-24
 */
@Mapper
public interface PmsGoodsImageMapper {
    /**
     * 添加商品图片
     *
     * @param spuImages 商品图片集合
     */

    void addSpuImages(List<PmsGoodsImage> spuImages);

    /**
     * 根据商品id删除商品图片
     *
     * @param spuId 商品id
     */

    void deleteBySpuId(long spuId);

    /**
     * 根据商品id删除商品图片(物理)
     *
     * @param spuId 商品id
     */

    void deleteBySpuIdPhysical(long spuId);

    /**
     * 根据商品id查询商品图片
     *
     * @param spuId 商品id
     * @return 返回商品图片
     */

    List<PmsGoodsImage> queryBySpuId(long spuId);

    /**
     * 查询商品的图片
     *
     * @param id 商品的图片ID
     * @return 商品的图片
     */
    public PmsGoodsImage selectPmsGoodsImageById(Long id);

    /**
     * 查询商品的图片列表
     *
     * @param pmsGoodsImage 商品的图片
     * @return 商品的图片集合
     */
    public List<PmsGoodsImage> selectPmsGoodsImageList(PmsGoodsImage pmsGoodsImage);

    /**
     * 新增商品的图片
     *
     * @param pmsGoodsImage 商品的图片
     * @return 结果
     */
    public int insertPmsGoodsImage(PmsGoodsImage pmsGoodsImage);

    /**
     * 修改商品的图片
     *
     * @param pmsGoodsImage 商品的图片
     * @return 结果
     */
    public int updatePmsGoodsImage(PmsGoodsImage pmsGoodsImage);

    /**
     * 删除商品的图片
     *
     * @param id 商品的图片ID
     * @return 结果
     */
    public int deletePmsGoodsImageById(Long id);

    /**
     * 批量删除商品的图片
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deletePmsGoodsImageByIds(Long[] ids);
}

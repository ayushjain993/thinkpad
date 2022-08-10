package io.uhha.goods.service.impl;

import io.uhha.goods.domain.PmsCombinationSku;
import io.uhha.goods.mapper.PmsCombinationSkuMapper;
import io.uhha.goods.service.IPmsCombinationSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品组合下的单品Service业务层处理
 *
 * @author mj
 * @date 2020-07-24
 */
@Service
public class PmsCombinationSkuServiceImpl implements IPmsCombinationSkuService {
    @Autowired
    private PmsCombinationSkuMapper pmsCombinationSkuMapper;

    /**
     * 查询商品组合下的单品
     *
     * @param id 商品组合下的单品ID
     * @return 商品组合下的单品
     */
    @Override
    public PmsCombinationSku selectPmsCombinationSkuById(Long id) {
        return pmsCombinationSkuMapper.selectPmsCombinationSkuById(id);
    }

    /**
     * 查询商品组合下的单品列表
     *
     * @param pmsCombinationSku 商品组合下的单品
     * @return 商品组合下的单品
     */
    @Override
    public List<PmsCombinationSku> selectPmsCombinationSkuList(PmsCombinationSku pmsCombinationSku) {
        return pmsCombinationSkuMapper.selectPmsCombinationSkuList(pmsCombinationSku);
    }

    /**
     * 新增商品组合下的单品
     *
     * @param pmsCombinationSku 商品组合下的单品
     * @return 结果
     */
    @Override
    public int insertPmsCombinationSku(PmsCombinationSku pmsCombinationSku) {
        return pmsCombinationSkuMapper.insertPmsCombinationSku(pmsCombinationSku);
    }

    /**
     * 修改商品组合下的单品
     *
     * @param pmsCombinationSku 商品组合下的单品
     * @return 结果
     */
    @Override
    public int updatePmsCombinationSku(PmsCombinationSku pmsCombinationSku) {
        return pmsCombinationSkuMapper.updatePmsCombinationSku(pmsCombinationSku);
    }

    /**
     * 批量删除商品组合下的单品
     *
     * @param ids 需要删除的商品组合下的单品ID
     * @return 结果
     */
    @Override
    public int deletePmsCombinationSkuByIds(Long[] ids) {
        return pmsCombinationSkuMapper.deletePmsCombinationSkuByIds(ids);
    }

    /**
     * 删除商品组合下的单品信息
     *
     * @param id 商品组合下的单品ID
     * @return 结果
     */
    @Override
    public int deletePmsCombinationSkuById(Long id) {
        return pmsCombinationSkuMapper.deletePmsCombinationSkuById(id);
    }
}

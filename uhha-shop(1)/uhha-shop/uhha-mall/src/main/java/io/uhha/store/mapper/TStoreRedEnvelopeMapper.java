package io.uhha.store.mapper;

import io.uhha.store.domain.TStoreRedEnvelope;

import java.util.List;

/**
 * 门店红包Mapper接口
 *
 * @author mj
 * @date 2020-07-28
 */
public interface TStoreRedEnvelopeMapper {
    /**
     * 查询门店红包
     *
     * @param id 门店红包ID
     * @return 门店红包
     */
    public TStoreRedEnvelope selectTStoreRedEnvelopeById(Long id);

    /**
     * 查询门店红包列表
     *
     * @param tStoreRedEnvelope 门店红包
     * @return 门店红包集合
     */
    public List<TStoreRedEnvelope> selectTStoreRedEnvelopeList(TStoreRedEnvelope tStoreRedEnvelope);

    /**
     * 新增门店红包
     *
     * @param tStoreRedEnvelope 门店红包
     * @return 结果
     */
    public int insertTStoreRedEnvelope(TStoreRedEnvelope tStoreRedEnvelope);

    /**
     * 修改门店红包
     *
     * @param tStoreRedEnvelope 门店红包
     * @return 结果
     */
    public int updateTStoreRedEnvelope(TStoreRedEnvelope tStoreRedEnvelope);

    /**
     * 删除门店红包
     *
     * @param id 门店红包ID
     * @return 结果
     */
    public int deleteTStoreRedEnvelopeById(Long id);

    /**
     * 批量删除门店红包
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTStoreRedEnvelopeByIds(Long[] ids);
}

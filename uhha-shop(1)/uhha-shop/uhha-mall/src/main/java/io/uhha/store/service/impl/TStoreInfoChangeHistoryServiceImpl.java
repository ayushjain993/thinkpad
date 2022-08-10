package io.uhha.store.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.uhha.common.enums.StoreChangeEventEnum;
import io.uhha.common.utils.DateUtils;
import io.uhha.store.service.ITStoreInfoChangeHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.uhha.store.mapper.TStoreInfoChangeHistoryMapper;
import io.uhha.store.domain.TStoreInfoChangeHistory;

/**
 * 店铺信息更改日志Service业务层处理
 * 
 * @author uhha
 * @date 2022-01-26
 */
@Service
public class TStoreInfoChangeHistoryServiceImpl extends ServiceImpl<TStoreInfoChangeHistoryMapper, TStoreInfoChangeHistory> implements ITStoreInfoChangeHistoryService
{
    @Autowired
    private TStoreInfoChangeHistoryMapper tStoreInfoChangeHistoryMapper;

//    /**
//     * 查询店铺信息更改日志
//     *
//     * @param id 店铺信息更改日志主键
//     * @return 店铺信息更改日志
//     */
//    @Override
//    public TStoreInfoChangeHistory selectTStoreInfoChangeHistoryById(Long id)
//    {
//        return tStoreInfoChangeHistoryMapper.selectTStoreInfoChangeHistoryById(id);
//    }

    @Override
    public List<TStoreInfoChangeHistory> selectTStoreInfoChangeHistoryByStoreId(Long storeId) {
        QueryWrapper<TStoreInfoChangeHistory> historyQueryWrapper = new QueryWrapper<>();
        historyQueryWrapper.eq("store_id", storeId);
        return tStoreInfoChangeHistoryMapper.selectList(historyQueryWrapper);
    }

//    /**
//     * 查询店铺信息更改日志列表
//     *
//     * @param tStoreInfoChangeHistory 店铺信息更改日志
//     * @return 店铺信息更改日志
//     */
//    @Override
//    public List<TStoreInfoChangeHistory> selectTStoreInfoChangeHistoryList(TStoreInfoChangeHistory tStoreInfoChangeHistory)
//    {
//        return tStoreInfoChangeHistoryMapper.selectTStoreInfoChangeHistoryList(tStoreInfoChangeHistory);
//    }
//
//    /**
//     * 新增店铺信息更改日志
//     *
//     * @param tStoreInfoChangeHistory 店铺信息更改日志
//     * @return 结果
//     */
//    @Override
//    public int insertTStoreInfoChangeHistory(TStoreInfoChangeHistory tStoreInfoChangeHistory)
//    {
//        tStoreInfoChangeHistory.setCreateTime(DateUtils.getNowDate());
//        return tStoreInfoChangeHistoryMapper.insertTStoreInfoChangeHistory(tStoreInfoChangeHistory);
//    }
//
//    /**
//     * 修改店铺信息更改日志
//     *
//     * @param tStoreInfoChangeHistory 店铺信息更改日志
//     * @return 结果
//     */
//    @Override
//    public int updateTStoreInfoChangeHistory(TStoreInfoChangeHistory tStoreInfoChangeHistory)
//    {
//        return tStoreInfoChangeHistoryMapper.updateTStoreInfoChangeHistory(tStoreInfoChangeHistory);
//    }
//
//    /**
//     * 批量删除店铺信息更改日志
//     *
//     * @param ids 需要删除的店铺信息更改日志主键
//     * @return 结果
//     */
//    @Override
//    public int deleteTStoreInfoChangeHistoryByIds(Long[] ids)
//    {
//        return tStoreInfoChangeHistoryMapper.deleteTStoreInfoChangeHistoryByIds(ids);
//    }
//
//    /**
//     * 删除店铺信息更改日志信息
//     *
//     * @param id 店铺信息更改日志主键
//     * @return 结果
//     */
//    @Override
//    public int deleteTStoreInfoChangeHistoryById(Long id)
//    {
//        return tStoreInfoChangeHistoryMapper.deleteTStoreInfoChangeHistoryById(id);
//    }

    @Override
    public void logStoreEvent(StoreChangeEventEnum eventEnum, Long storeId, String createBy) {
        TStoreInfoChangeHistory history = TStoreInfoChangeHistory.builder()
                .eventName(eventEnum.name())
                .eventContent(eventEnum.getDescription())
                .createTime(DateUtils.getNowDate())
                .storeId(storeId)
                .createBy(createBy)
                .build();
        tStoreInfoChangeHistoryMapper.insertTStoreInfoChangeHistory(history);

    }


}

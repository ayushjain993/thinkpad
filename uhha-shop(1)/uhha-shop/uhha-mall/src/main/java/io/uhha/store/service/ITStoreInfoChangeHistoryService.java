package io.uhha.store.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import io.uhha.common.enums.StoreChangeEventEnum;
import io.uhha.common.enums.UmsMemberChangeEventEnum;
import io.uhha.store.domain.TStoreInfoChangeHistory;

/**
 * 店铺信息更改日志Service接口
 * 
 * @author uhha
 * @date 2022-01-26
 */
public interface ITStoreInfoChangeHistoryService extends IService<TStoreInfoChangeHistory>
{
//    /**
//     * 查询店铺信息更改日志
//     *
//     * @param id 店铺信息更改日志主键
//     * @return 店铺信息更改日志
//     */
//    public TStoreInfoChangeHistory selectTStoreInfoChangeHistoryById(Long id);

    /**
     * 查询店铺信息更改日志
     *
     * @param storeId 店铺id
     * @return 店铺信息更改日志
     */
    public List<TStoreInfoChangeHistory> selectTStoreInfoChangeHistoryByStoreId(Long storeId);

//    /**
//     * 查询店铺信息更改日志列表
//     *
//     * @param tStoreInfoChangeHistory 店铺信息更改日志
//     * @return 店铺信息更改日志集合
//     */
//    public List<TStoreInfoChangeHistory> selectTStoreInfoChangeHistoryList(TStoreInfoChangeHistory tStoreInfoChangeHistory);
//
//    /**
//     * 新增店铺信息更改日志
//     *
//     * @param tStoreInfoChangeHistory 店铺信息更改日志
//     * @return 结果
//     */
//    public int insertTStoreInfoChangeHistory(TStoreInfoChangeHistory tStoreInfoChangeHistory);
//
//    /**
//     * 修改店铺信息更改日志
//     *
//     * @param tStoreInfoChangeHistory 店铺信息更改日志
//     * @return 结果
//     */
//    public int updateTStoreInfoChangeHistory(TStoreInfoChangeHistory tStoreInfoChangeHistory);
//
//    /**
//     * 批量删除店铺信息更改日志
//     *
//     * @param ids 需要删除的店铺信息更改日志主键集合
//     * @return 结果
//     */
//    public int deleteTStoreInfoChangeHistoryByIds(Long[] ids);
//
//    /**
//     * 删除店铺信息更改日志信息
//     *
//     * @param id 店铺信息更改日志主键
//     * @return 结果
//     */
//    public int deleteTStoreInfoChangeHistoryById(Long id);

    /**
     * 记录店铺修改事件
     * @param eventEnum 事件枚举
     * @param storeId 店铺Id
     * @param createBy 创建者名称
     */
    public void logStoreEvent(StoreChangeEventEnum eventEnum, Long storeId, String createBy);
}

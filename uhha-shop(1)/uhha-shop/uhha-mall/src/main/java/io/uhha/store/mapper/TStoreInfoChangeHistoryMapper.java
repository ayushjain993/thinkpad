package io.uhha.store.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.uhha.store.domain.TStoreInfoChangeHistory;
import org.apache.ibatis.annotations.Mapper;

/**
 * 店铺信息更改日志Mapper接口
 * 
 * @author uhha
 * @date 2022-01-26
 */
@Mapper
public interface TStoreInfoChangeHistoryMapper extends BaseMapper<TStoreInfoChangeHistory>
{
    /**
     * 查询店铺信息更改日志
     * 
     * @param id 店铺信息更改日志主键
     * @return 店铺信息更改日志
     */
    public TStoreInfoChangeHistory selectTStoreInfoChangeHistoryById(Long id);

    /**
     * 查询店铺信息更改日志列表
     * 
     * @param tStoreInfoChangeHistory 店铺信息更改日志
     * @return 店铺信息更改日志集合
     */
    public List<TStoreInfoChangeHistory> selectTStoreInfoChangeHistoryList(TStoreInfoChangeHistory tStoreInfoChangeHistory);

    /**
     * 新增店铺信息更改日志
     * 
     * @param tStoreInfoChangeHistory 店铺信息更改日志
     * @return 结果
     */
    public int insertTStoreInfoChangeHistory(TStoreInfoChangeHistory tStoreInfoChangeHistory);

    /**
     * 修改店铺信息更改日志
     * 
     * @param tStoreInfoChangeHistory 店铺信息更改日志
     * @return 结果
     */
    public int updateTStoreInfoChangeHistory(TStoreInfoChangeHistory tStoreInfoChangeHistory);

    /**
     * 删除店铺信息更改日志
     * 
     * @param id 店铺信息更改日志主键
     * @return 结果
     */
    public int deleteTStoreInfoChangeHistoryById(Long id);

    /**
     * 批量删除店铺信息更改日志
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTStoreInfoChangeHistoryByIds(Long[] ids);
}

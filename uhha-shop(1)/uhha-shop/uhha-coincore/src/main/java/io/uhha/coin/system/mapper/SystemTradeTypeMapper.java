package io.uhha.coin.system.mapper;

import io.uhha.coin.system.domain.SystemTradeType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/*
 * 交易信息数据操作接口
 */
@Mapper
public interface SystemTradeTypeMapper {

    /**
     * 删除
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入
     */
    int insert(SystemTradeType record);

    /**
     * 查找
     */
    SystemTradeType selectByPrimaryKey(Integer id);

    /**
     * 通过币种和交易类型查找
     */
    List<SystemTradeType> selectBySellCoinId(SystemTradeType trade);

    /**
     * 查询所有
     */
    List<SystemTradeType> selectAll();

    /**
     * 更新
     */
    int updateByPrimaryKey(SystemTradeType record);

    /**
     * 分页查询列表
     */
    List<SystemTradeType> selectSystemTradeTypeList(Map<String, Object> map);

    /**
     * 分页查询条数
     */
    int selectSystemTradeTypeCount(Map<String, Object> map);

    /**
     * 获取交易对
     * 
     * @param tradeId
     * @return
     */
    SystemTradeType getByTradeId(Integer tradeId);
}

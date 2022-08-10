package io.uhha.coin.dailylog.mapper;

import io.uhha.coin.dailylog.domain.FCoinBalance;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface FDayCoinBalanceMapper {
    /**
     * 分页统计
     *
     * @param map 参数map
     * @return 统计记录列表
     */
    List<FCoinBalance> getBalancePageList(Map<String, Object> map);

    /**
     * 分页查询记录
     *
     * @param map 参数map
     * @return 查询记录数
     */
    int countBalancePageList(Map<String, Object> map);
}

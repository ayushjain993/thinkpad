package io.uhha.coin.dailylog.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 按月统计
 *
 * @author ZKF
 */
@Mapper
public interface FStatisticsMapper {


    /**
     * 虚拟币充提
     *
     * @param map 参数
     * @return 统计数值
     */
    public BigDecimal sumRWcoin(Map<String, Object> map);

}

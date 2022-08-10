package io.uhha.coin.system.service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 按月统计充提买卖
 *
 * @author ZKF
 */
public interface IAdminStatisticsService {

    /**
     * 虚拟币充提
     */
    public BigDecimal sumRWcoin(Integer type, Map<String, Object> map, Integer coinid);

}

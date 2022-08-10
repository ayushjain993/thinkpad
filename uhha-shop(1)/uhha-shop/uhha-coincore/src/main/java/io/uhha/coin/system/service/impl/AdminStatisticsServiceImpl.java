package io.uhha.coin.system.service.impl;

import io.uhha.coin.dailylog.mapper.FStatisticsMapper;
import io.uhha.coin.system.service.IAdminStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service("adminStatisticsService")
public class AdminStatisticsServiceImpl implements IAdminStatisticsService {

    @Autowired
    private FStatisticsMapper statisticsMapper;


    public BigDecimal sumRWcoin(Integer type, Map<String, Object> map, Integer coinid) {
        map.put("ftype", type);
        map.put("fcoinid", coinid);

        return statisticsMapper.sumRWcoin(map);
    }

}

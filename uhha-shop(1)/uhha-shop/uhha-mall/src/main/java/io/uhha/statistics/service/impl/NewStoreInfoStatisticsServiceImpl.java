package io.uhha.statistics.service.impl;


import io.uhha.statistics.service.NewStoreInfoStatisticsService;
import io.uhha.store.mapper.TStoreInfoMapper;
import io.uhha.store.vo.NewStoreInfoStatistics;
import io.uhha.util.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mj on 18/2/5
 * 新增店铺统计服务接口实现
 */
@Service
public class NewStoreInfoStatisticsServiceImpl implements NewStoreInfoStatisticsService {

    /**
     * 调试日志
     */
    private Logger logger = LoggerFactory.getLogger(NewStoreInfoStatisticsServiceImpl.class);

    /**
     * 注入店铺数据库接口
     */
    @Autowired
    private TStoreInfoMapper storeInfoMapper;

    @Override
    public List<NewStoreInfoStatistics> queryNewStoreInfoStatistics(String startTime, String endTime) {
        logger.debug("queryNewStoreInfoStatistics and startTime :{} \r\n endTime :{}", startTime, endTime);
        Map<String, Object> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        return storeInfoMapper.queryNewStoreInfoStatistics(params);
    }

    @Override
    public PageHelper<NewStoreInfoStatistics> queryNewStoreInfoStatisticsWithPage(PageHelper<NewStoreInfoStatistics> pageHelper, String startTime, String endTime) {
        logger.debug("queryNewStoreInfoStatisticsWithPage pageHelper :{} \r\n and startTime :{} \r\n endTime :{}", pageHelper, startTime, endTime);
        Map<String, Object> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        return pageHelper.setListDates(storeInfoMapper.queryNewStoreInfoStatisticsWithPage(pageHelper.getQueryParams(params, storeInfoMapper.queryNewStoreInfoStatisticsCount(params))));
    }

}

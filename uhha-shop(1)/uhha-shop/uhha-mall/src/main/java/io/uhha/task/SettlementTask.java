package io.uhha.task;

import com.alibaba.fastjson.JSONObject;
import io.uhha.common.exception.CustomException;
import io.uhha.common.utils.DateUtils;
import io.uhha.common.utils.StringUtils;
import io.uhha.settlement.service.ISettlementManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 定时任务调度测试
 */
@Component("settlementTask")
@Slf4j
public class SettlementTask {
    @Autowired
    private ISettlementManager settlementManager;

    public void autoSettlementJob(String beginTime, String endTime) {
        if (StringUtils.isNotBlank(beginTime) && StringUtils.isNotBlank(endTime) && !DateUtils.isValidDate(beginTime) && !DateUtils.isValidDate(endTime)) {
            throw new CustomException("参数异常，应传入日期范围格式例如 2022/1/22-2022/1/23");
        }
        log.info("结算任务执行开始,参数为开始时间{},结束时间{}", beginTime, endTime);
        Map<String, Object> dateRange = queryDateRange(beginTime, endTime);
        Long storeId = null;
        settlementJob(storeId,"Auto", dateRange);
        log.info("结算任务执行结束");
    }

    public boolean settlementJob(Long storeId, String operator, Map<String, Object> dateRange) {
        log.info("结算任务执行开始,参数为{}", storeId, JSONObject.toJSONString(dateRange));
        boolean res = settlementManager.autoSettlementJob(storeId,operator, dateRange);
        log.info("结算任务执行结束,状态为", res);
        return res;
    }

    private Map<String, Object> queryDateRange(String beginTime, String endTime) {
        Map<String, Object> paramsMap = new HashMap<>();
        if (StringUtils.isBlank(beginTime) || StringUtils.isBlank(endTime)) {
            Date yesterday = DateUtils.getYesterday();
            paramsMap.put("beginTime", DateUtils.parseDateToStr(DateUtils.YYYYMMDDHHMMSS, yesterday));
            paramsMap.put("endTime", DateUtils.parseDateToStr(DateUtils.YYYYMMDDHHMMSS, yesterday));
        } else {
            Date startTime = Optional.ofNullable(DateUtils.parseDate(beginTime)).orElseThrow(() -> new CustomException("参数异常"));
            Date endTime1 = Optional.ofNullable(DateUtils.parseDate(endTime)).orElseThrow(() -> new CustomException("参数异常"));
            if (startTime.after(endTime1)) {
                throw new CustomException("参数异常,开始时间不能大于结束时间");
            }
            paramsMap.put("beginTime", DateUtils.parseDateToStr(DateUtils.YYYYMMDDHHMMSS, startTime));
            paramsMap.put("endTime", DateUtils.parseDateToStr(DateUtils.YYYYMMDDHHMMSS, endTime1));
        }
        return paramsMap;
    }
}

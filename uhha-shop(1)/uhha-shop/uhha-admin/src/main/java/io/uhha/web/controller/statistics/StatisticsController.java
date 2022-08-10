package io.uhha.web.controller.statistics;

import io.swagger.annotations.Api;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.order.vo.CustomerConsumption;
import io.uhha.order.vo.CustomerOrderAmount;
import io.uhha.statistics.service.StatisticsServiceApi;
import io.uhha.util.CommonConstant;
import io.uhha.util.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author peter
 */
@RestController
@RequestMapping("/statistics")
@Api(tags = "统计接口")
public class StatisticsController {
    @Autowired
    private StatisticsServiceApi statisticsServiceApi;

    /**
     * 查询自营店铺统计
     */
    @GetMapping("/index")
    public AjaxResult mallStatistics()
    {
        return AjaxResult.success(statisticsServiceApi.queryIndexStatistics(CommonConstant.ADMIN_STOREID));
    }

    /**
     * 查询店铺统计
     */
    @GetMapping("/index/{storeId}")
    public AjaxResult storeStatistics(@PathVariable("storeId") Long storeId)
    {
        return AjaxResult.success(statisticsServiceApi.queryIndexStatistics(storeId));
    }

    /**
     * 查询用户下单数目
     */
    @PostMapping("/customerOrderAmounts")
    public AjaxResult getCustomerOrderAmounts(PageHelper<CustomerOrderAmount> pageHelper, String startTime, String endTime){
        return AjaxResult.success(statisticsServiceApi.queryCustomerOrderAmounts(pageHelper, startTime, endTime));
    }

    /**
     * 查询用户成交金额
     */
    @PostMapping("/customerConsumption")
    public AjaxResult getCustomerConsumption(PageHelper<CustomerConsumption> pageHelper, String startTime, String endTime){
        return AjaxResult.success(statisticsServiceApi.queryCustomerConsumption(pageHelper, startTime, endTime));
    }

    /**
     * 查询pvuv
     */
    @GetMapping("/pvuv")
    public AjaxResult getPvUv(String startTime, String endTime){
        return AjaxResult.success(statisticsServiceApi.queryPvUvStatistics( startTime, endTime));
    }
}

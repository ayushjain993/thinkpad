package io.uhha.statistics.service.impl;


import io.uhha.coin.common.util.DateUtils;
import io.uhha.goods.domain.PmsSku;
import io.uhha.goods.service.IPmsSkuService;
import io.uhha.member.domain.UmsMember;
import io.uhha.member.service.IUmsMemberService;
import io.uhha.order.service.IOmsBackOrderService;
import io.uhha.order.service.IOmsOrderService;
import io.uhha.order.service.RecommendSkuService;
import io.uhha.order.vo.*;
import io.uhha.setting.domain.LsProvince;
import io.uhha.setting.service.AreaService;
import io.uhha.statistics.bean.IndexStatistics;
import io.uhha.statistics.bean.PvuvStatistics;
import io.uhha.statistics.bean.StoreAppOrderStatistics;
import io.uhha.statistics.bean.StoreAppStatistics;
import io.uhha.statistics.service.*;
import io.uhha.store.domain.TStoreInfo;
import io.uhha.store.service.ITStoreInfoService;
import io.uhha.store.service.ITStoreOrderService;
import io.uhha.store.vo.NewCustomerStatistics;
import io.uhha.store.vo.NewStoreInfoStatistics;
import io.uhha.store.vo.StoreInfoAreaStatistics;
import io.uhha.util.CommonConstant;
import io.uhha.util.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by mj on 18/2/2.
 * 统计服务接口实现
 */
@Service
public class StatisticsServiceApiImpl implements StatisticsServiceApi {

    /**
     * 调试日志
     */
    private Logger logger = LoggerFactory.getLogger(StatisticsServiceApiImpl.class);

    /**
     * 注入订单统计服务接口
     */
    @Autowired
    private OrderStatisticsService orderStatisticsService;

    /**
     * 注入会员服务接口
     */
    @Autowired
    private IUmsMemberService customerService;


    /**
     * 注入单品服务
     */
    @Autowired
    private IPmsSkuService skuService;

    /**
     * 注入店铺信息服务
     */
    @Autowired
    private ITStoreInfoService storeInfoService;

    /**
     * 注入新增会员服务接口
     */
    @Autowired
    private NewCustomerStatisticsService newCustomerStatisticsService;

    /**
     * 注入新增店铺服务接口
     */
    @Autowired
    private NewStoreInfoStatisticsService newStoreInfoStatisticsService;

    /**
     * 注入店铺地区统计服务接口
     */
    @Autowired
    private StoreInfoAreaStatisticsService storeInfoAreaStatisticsService;

    /**
     * 注入订单服务接口
     */
    @Autowired
    private IOmsOrderService orderService;

    /**
     * 注入推荐商品服务接口
     */
    @Autowired
    private RecommendSkuService recommendSkuService;

    /**
     * 注入门店订单服务接口
     */
    @Autowired
    private ITStoreOrderService storeOrderService;

    /**
     * 注入退单服务接口
     */
    @Autowired
    private IOmsBackOrderService backOrderService;

    /**
     * 注入地区服务接口
     */
    @Autowired
    private AreaService areaService;

    @Autowired
    private IPvService pvuvService;

    @Override
    public PageHelper<CustomerOrderAmount> queryCustomerOrderAmounts(PageHelper<CustomerOrderAmount> pageHelper, String startTime, String endTime) {
        logger.debug("queryCustomerOrderAmounts and startTime:{} \r\n endTime:{}", startTime, endTime);
        return setCustomerInfo(orderStatisticsService.queryCustomerOrderAmounts(pageHelper, startTime, endTime), customerOrderAmount -> {
            // 查询会员信息
            UmsMember customer = customerService.queryCustomerWithNoPasswordById(customerOrderAmount.getCustomerId());

            if (Objects.nonNull(customer)) {
                customerOrderAmount.setCustomerName(customer.getUsername());
            }
        });
    }

    @Override
    public PageHelper<CustomerConsumption> queryCustomerConsumption(PageHelper<CustomerConsumption> pageHelper, String startTime, String endTime) {
        logger.debug("queryCustomerConsumption and startTime:{} \r\n endTime:{}", startTime, endTime);
        return setCustomerInfo(orderStatisticsService.queryCustomerConsumption(pageHelper, startTime, endTime), customerConsumption -> {
            // 查询会员信息
            UmsMember customer = customerService.queryCustomerWithNoPasswordById(customerConsumption.getCustomerId());

            if (Objects.nonNull(customer)) {
                customerConsumption.setCustomerName(customer.getUsername());
            }
        });
    }

    @Override
    public PageHelper<SkuSaleAmount> querySkuSaleVolume(PageHelper<SkuSaleAmount> pageHelper, String startTime, String endTime, long storeId) {
        logger.debug("querySkuSaleVolume and startTime:{} \r\n endTime:{} \r\n storeId:{}", startTime, endTime, storeId);
        return setSkuAndStoreInfo(orderStatisticsService.querySkuSaleVolume(pageHelper, startTime, endTime, storeId), storeId);
    }

    @Override
    public PageHelper<SkuSaleAmount> querySkuSaleAmount(PageHelper<SkuSaleAmount> pageHelper, String startTime, String endTime, long storeId) {
        logger.debug("querySkuSaleAmount and startTime:{} \r\n endTime:{} \r\n storeId:{}", startTime, endTime, storeId);
        return setSkuAndStoreInfo(orderStatisticsService.querySkuSaleAmount(pageHelper, startTime, endTime, storeId), storeId);
    }

    @Override
    public PageHelper<StoreSaleAmount> queryStoreSaleVolume(PageHelper<StoreSaleAmount> pageHelper, String startTime, String endTime) {
        logger.debug("queryStoreSaleVolume and startTime:{} \r\n endTime:{}", startTime, endTime);
        return setStoreInfo(orderStatisticsService.queryStoreSaleVolume(pageHelper, startTime, endTime));
    }

    @Override
    public PageHelper<StoreSaleAmount> queryStoreSaleAmount(PageHelper<StoreSaleAmount> pageHelper, String startTime, String endTime) {
        logger.debug("queryStoreSaleAmount and startTime:{} \r\n endTime:{}", startTime, endTime);
        return setStoreInfo(orderStatisticsService.queryStoreSaleAmount(pageHelper, startTime, endTime));
    }

    @Override
    public List<NewCustomerStatistics> queryNewCustomerStatistics(String startTime, String endTime) {
        logger.debug("queryNewCustomerStatistics and startTime:{} \r\n endTime:{}", startTime, endTime);
        return newCustomerStatisticsService.queryNewCustomerStatistics(startTime, endTime);
    }

    @Override
    public PageHelper<NewCustomerStatistics> queryNewCustomerStatisticsWithPage(PageHelper<NewCustomerStatistics> pageHelper, String startTime, String endTime) {
        logger.debug("queryNewCustomerStatisticsWithPage and pageHelper :{} \r\n and startTime:{} \r\n endTime:{}", pageHelper, startTime, endTime);
        return newCustomerStatisticsService.queryNewCustomerStatisticsWithPage(pageHelper, startTime, endTime);

    }

    @Override
    public List<NewStoreInfoStatistics> queryNewStoreInfoStatistics(String startTime, String endTime) {
        logger.debug("queryNewStoreInfoStatistics and startTime :{} \r\n and endTime :{}", startTime, endTime);
        return newStoreInfoStatisticsService.queryNewStoreInfoStatistics(startTime, endTime);
    }

    @Override
    public PageHelper<NewStoreInfoStatistics> queryNewStoreInfoStatisticsWithPage(PageHelper<NewStoreInfoStatistics> pageHelper, String startTime, String endTime) {
        logger.debug("queryNewStoreInfoStatisticsWithPage and pageHelper :{} \r\n and startTime :{} \r\n and endTime :{}", pageHelper, startTime, endTime);
        return newStoreInfoStatisticsService.queryNewStoreInfoStatisticsWithPage(pageHelper, startTime, endTime);
    }

    @Override
    public List<StoreInfoAreaStatistics> queryStoreInfoAreaStatistics(String startTime, String endTime) {
        logger.debug("queryStoreInfoAreaStatistics and startTime :{} \r\n and endTime :{}", startTime, endTime);
        return storeInfoAreaStatisticsService.queryStoreInfoAreaStatistics(startTime, endTime).stream().peek(storeInfoAreaStatistics -> {
            LsProvince province = areaService.queryProvinceById(storeInfoAreaStatistics.getCountryId());
            if (Objects.nonNull(province)) {
                storeInfoAreaStatistics.setProvinceName(province.getName());
            }
        }).collect(Collectors.toList());
    }

    @Override
    public IndexStatistics queryIndexStatistics(long storeId) {
        logger.debug("queryIndexStatistics and storeId :{}", storeId);
        return IndexStatistics.buildIndexStatistics(customerService.queryNewCustomerToday(), orderService.querySaleAmountToday(storeId), orderService.querySaleCountToday(storeId), orderService.querySaleAmountThisWeek(storeId), orderService.querySaleCountThisWeek(storeId), customerService.queryNewCustomerThisWeek(), orderService.querySaleAmountThisWeekGroupByDay(storeId), orderService.querySaleCountThisWeekGroupByDay(storeId), customerService.queryNewCustomerThisWeekGroupByDay());
    }

    @Override
    public IndexStatistics queryIndexStatisticsForStore(long storeId) {
        logger.debug("queryIndexStatisticsForStore and storeId :{}", storeId);
        TStoreInfo storeInfo = storeInfoService.queryStoreInfo(storeId);
        if (storeInfo.isOutLetStore()) {
            return IndexStatistics.buildIndexStatisticsForStore(storeOrderService.queryStoreSaleAmountToday(storeId), storeOrderService.queryStoreSaleCountToday(storeId), storeOrderService.queryStoreSaleAmountThisWeek(storeId), storeOrderService.queryStoreSaleAmountThisWeekGroupByDay(storeId), recommendSkuService.queryStoreRecommentSkusThirtyDays(storeId), storeInfo.isOutLetStore());
        } else {
            return IndexStatistics.buildIndexStatisticsForStore(orderService.querySaleAmountToday(storeId), orderService.querySaleCountToday(storeId), orderService.querySaleAmountThisWeek(storeId), orderService.querySaleAmountThisWeekGroupByDay(storeId), recommendSkuService.queryRecommentSkusThirtyDays(storeId), storeInfo.isOutLetStore());
        }
    }


    @Override
    public StoreAppStatistics queryStoreAppStatistics(long storeId) {
        logger.debug("queryStoreAppStatistics and storeId:{}", storeId);


        // 查询今日销售额
        return StoreAppStatistics.builder().storeName(storeInfoService.queryStoreInfo(storeId).getStoreName())
                .saleMoney(orderService.querySaleAmountToday(storeId)).orderNum(orderService.querySaleCountToday(storeId))
                .payedOrderNum(orderService.queryTodayPayedOrderNum(storeId)).toDeliveryOrderNum(orderService.queryToDeliveryOrderNum(storeId)).build();
    }


    @Override
    public StoreAppOrderStatistics queryStoreAppOrderStatistics(long storeId) {
        logger.debug("queryStoreAppOrderStatistics and storeId:{}", storeId);
        return StoreAppOrderStatistics.builder().toDelivered(orderService.queryToDeliveryOrderNum(storeId)).toPayed(orderService.queryToPayedOrderNum(storeId)).processingBackOrder(backOrderService.queryInProcessBackOrderForStoreApp(storeId)).build();
    }

    @Override
    public MarketingStatistics queryMarketingStatistics(String startTime, String endTime, long storeId) {
        logger.debug("queryMarketingStatistics and startTime:{} \r\n endTime:{} \r\n storeId:{} \r\n", startTime, endTime, storeId);
        return MarketingStatistics.buildMarketingStatistics(orderStatisticsService.queryMarketingOrderStatistics(startTime, endTime, "1", storeId).addMarketingName("定金预售"),
                orderStatisticsService.queryMarketingOrderStatistics(startTime, endTime, "2", storeId).addMarketingName("全款预售"),
                orderStatisticsService.queryMarketingOrderStatistics(startTime, endTime, "3", storeId).addMarketingName("拼团"),
                orderStatisticsService.queryMarketingOrderStatistics(startTime, endTime, "4", storeId).addMarketingName("众筹全款"),
                orderStatisticsService.queryMarketingOrderStatistics(startTime, endTime, "5", storeId).addMarketingName("众筹1元"),
                orderStatisticsService.queryMarketingOrderStatistics(startTime, endTime, "6", storeId).addMarketingName("众筹无回报"));
    }

    /**
     * 统计PvUv数量（按日期分组）
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 返回按日期分组的统计PvUv数量
     */
    @Override
    public HashMap<String, List<Object>> queryPvUvStatistics(String startTime, String endTime){
        logger.debug("queryPvUvStatistics and startTime:{} \r\n endTime:{} ", startTime, endTime);
        List<PvuvStatistics> statistics =pvuvService.queryPvUvStatistics(startTime,endTime);

        HashMap<String, List<Object>> hashMap = new HashMap<>();
        List<Object> uvList = new ArrayList<>();
        List<Object> pvList = new ArrayList<>();
        List<Object> createTimeList = new ArrayList<>();
        statistics.forEach(x->{
            uvList.add(x.getUv());
            pvList.add(x.getPv());
            createTimeList.add(DateUtils.format(x.getCreateTime(),"YYYY-MM-dd"));
        });
        hashMap.put("pv", pvList);
        hashMap.put("uv", uvList);
        hashMap.put("createTime", createTimeList);
        return hashMap;

    }

    /**
     * 设置会员信息
     *
     * @param pageHelper 结果集
     * @return 返回结果集
     */
    private <T> PageHelper<T> setCustomerInfo(PageHelper<T> pageHelper, Consumer<T> consumer) {

        // 如果没有数据 则直接返回
        if (CollectionUtils.isEmpty(pageHelper.getList())) {
            return pageHelper;
        }

        pageHelper.getList().stream().forEach(consumer);

        return pageHelper;
    }

    /**
     * 设置单品及店铺信息
     *
     * @param pageHelper 结果集
     * @return 返回结果集
     */
    private PageHelper<SkuSaleAmount> setSkuAndStoreInfo(PageHelper<SkuSaleAmount> pageHelper, long storeId) {

        // 如果没有数据 则直接返回
        if (CollectionUtils.isEmpty(pageHelper.getList())) {
            return pageHelper;
        }

        pageHelper.getList().stream().forEach(skuSaleAmount -> {
            // 查询单品信息
            PmsSku sku = skuService.querySkuById(skuSaleAmount.getSkuId());
            if (Objects.nonNull(sku)) {
                skuSaleAmount.setSkuName(sku.getName());
            }
            if (CommonConstant.QUERY_WITH_NO_STORE == storeId) {
                // 查询店铺信息
                TStoreInfo storeInfo = storeInfoService.queryStoreInfo(skuSaleAmount.getStoreId());
                if (Objects.nonNull(storeInfo)) {
                    skuSaleAmount.setStoreName(storeInfo.getStoreName());
                }
            }
        });
        return pageHelper;
    }

    /**
     * 设置店铺信息
     *
     * @param pageHelper 结果集
     * @return 返回结果集
     */
    private PageHelper<StoreSaleAmount> setStoreInfo(PageHelper<StoreSaleAmount> pageHelper) {

        // 如果没有数据 则直接返回
        if (CollectionUtils.isEmpty(pageHelper.getList())) {
            return pageHelper;
        }
        pageHelper.getList().stream().forEach(storeSaleAmount -> {
            // 查询店铺信息
            TStoreInfo storeInfo = storeInfoService.queryStoreInfo(storeSaleAmount.getStoreId());
            if (Objects.nonNull(storeInfo)) {
                storeSaleAmount.setStoreName(storeInfo.getStoreName());
            }
        });
        return pageHelper;
    }


}

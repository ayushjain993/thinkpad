package io.uhha.marketing.service;


import io.uhha.marketing.domain.TryMarketing;
import io.uhha.util.PageHelper;

/**
 * 试用促销聚合服务接口
 *
 * @author mj created on 2020/6/9
 */
public interface TryMarketingServiceApi {

    /**
     * 分页查询试用促销列表
     *
     * @param pageHelper 分页帮助类
     * @param name       单品名称
     * @param skuNo      单品编号
     * @param storeId    店铺id
     * @return 返回试用促销列表
     */
    PageHelper<TryMarketing> queryTryMarketingList(PageHelper<TryMarketing> pageHelper, String name, String skuNo, long storeId);

}

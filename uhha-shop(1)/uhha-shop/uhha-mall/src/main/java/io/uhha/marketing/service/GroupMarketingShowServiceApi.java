package io.uhha.marketing.service;


import io.uhha.marketing.domain.GroupMarketingShow;
import io.uhha.util.PageHelper;

/**
 * 拼团活动聚合服务接口
 *
 * @author mj created on 2020/6/1
 */
public interface GroupMarketingShowServiceApi {

    /**
     * 分页查询拼团活动列表
     *
     * @param pageHelper 分页帮助类
     * @param name       单品名称
     * @param skuNo      单品编号
     * @param storeId    店铺id
     * @return 返回拼团活动列表
     */
    PageHelper<GroupMarketingShow> queryGroupMarketingShowList(PageHelper<GroupMarketingShow> pageHelper, String name, String skuNo, long storeId);

}

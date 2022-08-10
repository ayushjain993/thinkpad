package io.uhha.marketing.service;


import io.uhha.marketing.domain.GroupMarketing;
import io.uhha.util.PageHelper;

/**
 * 拼团促销聚合服务接口
 *
 * @author mj created on 2020/6/1
 */
public interface GroupMarketingServiceApi {

    /**
     * 分页查询拼团促销列表
     *
     * @param pageHelper 分页帮助类
     * @param name       单品名称
     * @param skuNo      单品编号
     * @param storeId    店铺id
     * @return 返回拼团促销列表
     */
    PageHelper<GroupMarketing> queryGroupMarketingList(PageHelper<GroupMarketing> pageHelper, String name, String skuNo, long storeId);

}

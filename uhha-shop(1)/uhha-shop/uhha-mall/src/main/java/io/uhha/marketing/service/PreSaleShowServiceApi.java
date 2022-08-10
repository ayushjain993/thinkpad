package io.uhha.marketing.service;


import io.uhha.marketing.domain.PreSaleShow;
import io.uhha.util.PageHelper;

/**
 * 预售活动聚合服务接口
 *
 * @author mj created on 2020/6/12
 */
public interface PreSaleShowServiceApi {

    /**
     * 分页查询预售活动列表
     *
     * @param pageHelper 分页帮助类
     * @param name       单品名称
     * @param skuNo      单品编号
     * @param storeId    店铺id
     * @return 返回预售活动列表
     */
    PageHelper<PreSaleShow> queryPreSaleShowList(PageHelper<PreSaleShow> pageHelper, String name, String skuNo, long storeId);

}

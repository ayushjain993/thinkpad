package io.uhha.marketing.service;

import io.uhha.marketing.domain.PreSale;
import io.uhha.util.PageHelper;

/**
 * 预售促销服务接口
 *
 * @author mj created on 2020/5/28
 */
public interface PreSaleService {

    /**
     * 分页查询预售促销列表
     *
     * @param pageHelper 分页帮助类
     * @param name       单品名称
     * @param skuNo      单品编号
     * @param storeId    店铺id
     * @return 返回预售促销列表
     */
    PageHelper<PreSale> queryPreSaleList(PageHelper<PreSale> pageHelper, String name, String skuNo, long storeId);

    /**
     * 根据id查询预售促销信息
     *
     * @param preSaleId 预售促销id
     * @param storeId   店铺id
     * @return 预售促销信息
     */
    PreSale queryPreSaleById(long preSaleId, long storeId);

}

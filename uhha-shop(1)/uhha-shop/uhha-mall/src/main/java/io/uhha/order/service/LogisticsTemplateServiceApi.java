package io.uhha.order.service;


/**
 * 物流模版服务api接口
 *
 * @author mj created on 2020/4/24
 */
public interface LogisticsTemplateServiceApi {

    /**
     * 删除运费模版
     *
     * @param id      运费模版id
     * @param storeId 店铺id
     * @return 成功返回1 失败返回0
     */
    int deleteLogisticsTemplate(long id, long storeId);

}

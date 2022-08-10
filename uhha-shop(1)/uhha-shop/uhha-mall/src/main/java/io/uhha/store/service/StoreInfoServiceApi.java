package io.uhha.store.service;


import io.uhha.store.vo.AppStoreInfo;
import io.uhha.store.vo.StoreItem;
import io.uhha.store.vo.StoreResponse;

/**
 * Created by mj on 17/11/23.
 * 店铺服务接口
 */
public interface StoreInfoServiceApi {

    /**
     * 查询店铺信息
     *
     * @param storeId    店铺id
     * @param storeItems 查询条件
     * @return 返回店铺信息
     */
    StoreResponse queryStoreInfo(long storeId, StoreItem... storeItems);

    /**
     * 查询会员的店铺信息
     *
     * @param customerId 会员id
     * @return 返回会员的店铺信息
     */
    AppStoreInfo queryCustomerStoreInfo(long customerId);

    /**
     * 校验用户和店铺是否存在
     *
     * @param customerId 用户id
     * @return 存在返回true  不存在返回false
     */
    boolean validateStoreApp(long customerId);
}

package io.uhha.store.service;


import io.uhha.store.domain.AttentionStore;
import io.uhha.util.PageHelper;

/**
 * 关注的店铺service
 *
 * @author mj on 2017/7/4.
 */
public interface AttentionStoreService {
    /**
     * 根据会员id查询关注的店铺
     *
     * @param customerId 会员id
     * @return 关注的店铺信息集合
     */
    PageHelper<AttentionStore> queryAttentionByCustomerId(PageHelper<AttentionStore> pageHelper, long customerId);

    /**
     * 根据店铺id和会员id取消关注
     *
     * @param storeId    店铺id
     * @param customerId 会员id
     * @return 删除返回码
     */
    int cancelStoreAttention(long storeId, long customerId);

    /**
     * 店铺被关注的数量
     *
     * @param storeId 店铺id
     * @return 返回关注的数量
     */
    int queryNumByStore(long storeId);

    /**
     * 查询会员关注店铺的数量
     *
     * @param customerId 会员id
     * @return 返回会员关注店铺的数量
     */
    int queryCustomerAttentionStoreCount(long customerId);

    /**
     * 关注店铺
     *
     * @param customerId 用户id
     * @param storeId    店铺id
     * @return -1 已经关注过 1 关注成功 0 失败
     */
    int attentionStore(long customerId, long storeId);
}

package io.uhha.goods.service;


import io.uhha.common.utils.bean.WeChatAppletCodeRequest;
import io.uhha.goods.domain.PmsGoods;
import io.uhha.goods.domain.PmsSku;
import io.uhha.goods.vo.CombinationDetail;
import io.uhha.goods.vo.SpuDetail;
import io.uhha.goods.vo.SpuDetailItem;
import io.uhha.marketing.vo.CrowdFundingSpuDetail;
import io.uhha.order.vo.SkuMarketPriceDetail;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by mj on 17/11/22.
 * 商品服务接口
 */
public interface SpuServiceApi {

    /**
     * 根据单品id查询商品详情(给详情页使用)
     *
     * @param skuId          单品id
     * @param customerId     会员id
     * @param spuDetailItems 商品详情查询枚举
     * @return 返回商品详情
     */
    Optional<SpuDetail> querySpuDetail(String skuId, long customerId, SpuDetailItem... spuDetailItems);

    Optional<SpuDetail> queryGoodsDetail(Long goodsId, long customerId, SpuDetailItem... spuDetailItems);


    /**
     * 根据单品id查询众筹商品详情(给众筹详情页使用)
     *
     * @param marketingId           促销id
     * @param skuId                 单品id
     * @param isNeedMarketingDetail 是否需要促销详情
     * @param spuDetailItems        查询枚举
     * @return 返回众筹商品详情
     */
    CrowdFundingSpuDetail queryCrowdFundingSpuDetail(long marketingId, String skuId, boolean isNeedMarketingDetail, SpuDetailItem... spuDetailItems);


    /**
     * 计算单品的价格(会员价格,抢购价格和直降)
     * 规则 抢购>直降>会员价格
     *
     * @param sku        单品
     * @param price      原价格
     * @param customerId 会员id
     * @return 返回单品价格详情
     */
    SkuMarketPriceDetail calcMarketingPrice(PmsSku sku, BigDecimal price, long customerId);

    /**
     * 计算会员价格
     *
     * @param skuId      单品id
     * @param price      价格
     * @param customerId 会员id
     * @return 返回会员价格
     */
    SkuMarketPriceDetail calcMemberPrice(String skuId, BigDecimal price, long customerId);

    /**
     * 查询单品的商品组合信息
     *
     * @param skuId      单品id
     * @param customerId 会员id
     * @return 返回单品的商品这信息
     */
    CombinationDetail queryGoodsCombinationBySkuId(String skuId, long customerId);

    /**
     * 根据单品id查询商品详情(给比较页使用)
     *
     * @param skuIds         单品id数组
     * @param spuDetailItems 商品详情查询枚举
     * @return 返回商品详情集合
     */
    List<SpuDetail> querySpuDetailList(String[] skuIds, SpuDetailItem... spuDetailItems);

    /**
     * 更新商品信息
     *
     * @param spu      商品信息
     * @param consumer 回调通知es 建立索引
     * @return 成功返回1 失败返回0 -1单品有促销与会员价互斥 -2单品有促销与批发互斥 -3存在同时有会员价和批发规则的单品
     */
    int updateSpu(PmsGoods spu, Consumer<Long> consumer);


    /**
     * 计算运费
     *
     * @param skuId   商品id
     * @param storeId 店铺id
     * @param cityId  城市id
     * @param num     商品数量
     * @return 运费
     */
    BigDecimal calculateFreight(String skuId, long storeId, long cityId, int num);

    /**
     * 导出选中的商品
     *
     * @param os      输出流
     * @param ids     商品id数组
     * @param storeId 店铺id
     */
    Void exportCheckedSpu(OutputStream os, Long[] ids, long storeId);

    /**
     * 导出所有商品
     *
     * @param os      输出流
     * @param storeId 店铺id
     */
    Void exportAllSpu(OutputStream os, long storeId);

    /**
     * 获取分享微信小程序码
     *
     * @param weChatAppletCodeRequest 生成微信小程序码请求实体类
     * @return 分享微信小程序码
     */
    String getWeChatAppletCode(WeChatAppletCodeRequest weChatAppletCodeRequest);
}

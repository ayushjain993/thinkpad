package io.uhha.store.service.impl;


import io.uhha.common.enums.StoreStatusEnum;
import io.uhha.goods.service.IPmsBrandService;
import io.uhha.goods.service.IPmsSkuService;
import io.uhha.member.domain.UmsMember;
import io.uhha.member.service.IUmsMemberService;
import io.uhha.order.service.IOmsOrderService;
import io.uhha.store.domain.TStoreInfo;
import io.uhha.store.service.AttentionStoreService;
import io.uhha.store.service.ITStoreInfoService;
import io.uhha.store.service.StoreInfoServiceApi;
import io.uhha.store.vo.AppStoreInfo;
import io.uhha.store.vo.StoreItem;
import io.uhha.store.vo.StoreResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Created by mj on 17/11/23.
 * 店铺服务接口实现
 */
@Service
public class StoreInfoServiceApiImpl implements StoreInfoServiceApi {

    /**
     * 调试日志
     */
    private Logger logger = LoggerFactory.getLogger(StoreInfoServiceApiImpl.class);

    /**
     * 注入店铺服务接口
     */
    @Autowired
    private ITStoreInfoService storeInfoService;

    /**
     * es搜索服务
     */
    //  @Autowired
    //  private EsSearchService esSearchService;

    /**
     * 注入单品服务接口
     */
    @Autowired
    private IPmsSkuService skuService;

    /**
     * 注入品牌服务接口
     */
    @Autowired
    private IPmsBrandService brandService;

    /**
     * 注入订单服务接口
     */
    @Autowired
    private IOmsOrderService orderService;

    /**
     * 注入店铺关注服务接口
     */
    @Autowired
    private AttentionStoreService attentionStoreService;

    /**
     * 注入会员服务接口
     */
    @Autowired
    private IUmsMemberService customerService;


    @Override
    public StoreResponse queryStoreInfo(long storeId, StoreItem... storeItems) {
        logger.debug("queryStoreInfo and storeId:{} \r\n storeItems:{}", storeId, storeItems);


        // 设置店铺信息
        StoreResponse storeResponse = StoreResponse.build(storeInfoService.selStoreInfo(storeId));

        // 设置店铺的总商品数量
        if (ArrayUtils.contains(storeItems, StoreItem.SKUNUM)) {
            //  storeResponse.setAllSkuNum(esSearchService.search(EsSearchRequest.buildSearchStoreSkuNumRequest(storeId)).getTotal());
        }

        // 设置店铺的上架的新品数量
        if (ArrayUtils.contains(storeItems, StoreItem.NEWSKUNUM)) {
            // 查询店铺最近7天上架的单品数量
            skuService.lastUpSkusNum(skuService.lastUpSkusNum(storeId));
        }

        // 设置店铺的促销商品数量
        if (ArrayUtils.contains(storeItems, StoreItem.MARKETSKUNUM)) {
            storeResponse.setMarketSkuNum(skuService.marketSkusNum(storeId));
        }

        // 设置店铺的品牌
        if (ArrayUtils.contains(storeItems, StoreItem.BRAND)) {
            storeResponse.setBrands(brandService.queryAllBrands(storeId));
        }

        // 设置店铺被关注的数量
        if (ArrayUtils.contains(storeItems, StoreItem.ATTENNUM)) {
            storeResponse.setFollowNum(attentionStoreService.queryNumByStore(storeId));
        }

        // 设置店铺总的销量
        if (ArrayUtils.contains(storeItems, StoreItem.SALENUM)) {
            storeResponse.setSaleNum(orderService.saleNum(storeId));
        }

        return storeResponse;
    }

    @Override
    public AppStoreInfo queryCustomerStoreInfo(long customerId) {
        logger.debug("queryCustomerStoreInfo and customerId:{}", customerId);
        return AppStoreInfo.builder().username(customerService.queryCustomerWithNoPasswordById(customerId).getUsername()).
                build();
        // roleName(storeRoleService.queryCustomerStoreRole(customerId).getRoleName()).build();
    }

    @Override
    public boolean validateStoreApp(long customerId) {
        logger.debug("validateStoreApp and customerId:{}", customerId);
        UmsMember customer = customerService.queryCustomerWithNoPasswordById(customerId);

        // 会员不存在 直接返回失败
        if (Objects.isNull(customer)) {
            logger.error("validateStoreApp fail due to member is not exist...");
            return false;
        }

        if ("2".equals(customer.getStatus()) || "3".equals(customer.getStatus())) {//1 正常 2冻结 3未启用
            logger.error("validateStoreApp fail due to member status is not right...");
            return false;
        }


        // 查询店铺信息
        TStoreInfo storeInfo = storeInfoService.queryStoreInfo(customer.getStoreId());

        if (Objects.isNull(storeInfo) || StoreStatusEnum.CLOSED.getCode().equalsIgnoreCase(storeInfo.getStatus())) {
            logger.error("validateStoreApp fail due to store status is not right");
            return false;
        }

        return true;
    }
}

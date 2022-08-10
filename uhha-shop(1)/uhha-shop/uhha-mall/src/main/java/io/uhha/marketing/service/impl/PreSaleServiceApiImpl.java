package io.uhha.marketing.service.impl;


import io.uhha.marketing.domain.PreSale;
import io.uhha.marketing.service.PreSaleService;
import io.uhha.marketing.service.PreSaleServiceApi;
import io.uhha.store.domain.TStoreInfo;
import io.uhha.store.service.ITStoreInfoService;
import io.uhha.util.CommonConstant;
import io.uhha.util.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 预售促销聚合服务接口实现类
 *
 * @author mj created on 2020/6/12
 */
@Service
@Slf4j
public class PreSaleServiceApiImpl implements PreSaleServiceApi {

    /**
     * 注入预售促销服务接口
     */
    @Autowired
    private PreSaleService preSaleService;

    /**
     * 注入店铺信息服务接口
     */
    @Autowired
    private ITStoreInfoService storeInfoService;


    @Override
    public PageHelper<PreSale> queryPreSaleList(PageHelper<PreSale> pageHelper, String name, String skuNo, long storeId) {
        log.debug("queryPreSaleList and pageHelper :{} \r\n name :{} \r\n skuNo :{} \r\n storeId :{}");
        return pageHelper.setListDates(preSaleService.queryPreSaleList(pageHelper, name, skuNo, storeId).getList()
                .stream().peek(preSale -> setOtherInfo(preSale, storeId)).collect(Collectors.toList()));
    }


    /**
     * 设置列表其他信息
     *
     * @param preSale 预售促销
     * @param storeId 店铺id
     */
    private void setOtherInfo(PreSale preSale, long storeId) {
        log.debug("setOtherInfo and preSale :{} \r\n storeId :{}", preSale, storeId);

        if (Objects.isNull(preSale)) {
            return;
        }

        // 平台查询，则设置店铺名称
        if (CommonConstant.ADMIN_STOREID == storeId) {

            if (CommonConstant.ADMIN_STOREID == preSale.getStoreId()) {

                preSale.setStoreName("平台自营");

            } else {

                // 设置店铺名称
                TStoreInfo storeInfo = storeInfoService.queryStoreInfo(preSale.getStoreId());

                if (Objects.nonNull(storeInfo)) {
                    preSale.setStoreName(storeInfo.getStoreName());
                }
            }

        }

    }

}

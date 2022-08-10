package io.uhha.marketing.service.impl;


import io.uhha.marketing.domain.PreSaleShow;
import io.uhha.marketing.service.PreSaleShowService;
import io.uhha.marketing.service.PreSaleShowServiceApi;
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
 * 预售活动聚合服务接口实现类
 *
 * @author mj created on 2020/6/12
 */
@Service
@Slf4j
public class PreSaleShowServiceApiImpl implements PreSaleShowServiceApi {

    /**
     * 注入预售活动服务接口
     */
    @Autowired
    private PreSaleShowService preSaleShowService;

    /**
     * 注入店铺信息服务接口
     */
    @Autowired
    private ITStoreInfoService storeInfoService;


    @Override
    public PageHelper<PreSaleShow> queryPreSaleShowList(PageHelper<PreSaleShow> pageHelper, String name, String skuNo, long storeId) {
        log.debug("queryPreSaleShowList and pageHelper :{} \r\n name :{} \r\n skuNo :{} \r\n storeId :{}");

        return pageHelper.setListDates(preSaleShowService.queryPreSaleShowList(pageHelper, name, skuNo, storeId).getList()
                .stream().peek(preSaleShow -> setOtherInfo(preSaleShow, storeId)).collect(Collectors.toList()));
    }


    /**
     * 设置列表其他信息
     *
     * @param preSaleShow 预售活动
     * @param storeId     店铺id
     */
    private void setOtherInfo(PreSaleShow preSaleShow, long storeId) {
        log.debug("setOtherInfo and preSaleShow :{} \r\n storeId :{}", preSaleShow, storeId);

        if (Objects.isNull(preSaleShow)) {
            return;
        }

        // 平台查询，则设置店铺名称
        if (CommonConstant.ADMIN_STOREID == storeId) {

            if (CommonConstant.ADMIN_STOREID == preSaleShow.getOldStoreId()) {

                preSaleShow.setStoreName("平台自营");

            } else {

                // 设置店铺名称
                TStoreInfo storeInfo = storeInfoService.queryStoreInfo(preSaleShow.getOldStoreId());

                if (Objects.nonNull(storeInfo)) {
                    preSaleShow.setStoreName(storeInfo.getStoreName());
                }
            }

        }

    }

}

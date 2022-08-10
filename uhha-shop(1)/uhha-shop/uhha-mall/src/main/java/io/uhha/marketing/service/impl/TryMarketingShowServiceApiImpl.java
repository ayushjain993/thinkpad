package io.uhha.marketing.service.impl;


import io.uhha.marketing.domain.TryMarketingShow;
import io.uhha.marketing.service.TryMarketingShowService;
import io.uhha.marketing.service.TryMarketingShowServiceApi;
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
 * 试用活动聚合服务接口实现类
 *
 * @author mj created on 2020/6/9
 */
@Service
@Slf4j
public class TryMarketingShowServiceApiImpl implements TryMarketingShowServiceApi {

    /**
     * 注入试用活动服务接口
     */
    @Autowired
    private TryMarketingShowService tryMarketingShowService;

    /**
     * 注入店铺信息服务接口
     */
    @Autowired
    private ITStoreInfoService storeInfoService;


    @Override
    public PageHelper<TryMarketingShow> queryTryMarketingShowList(PageHelper<TryMarketingShow> pageHelper, String name, String skuNo, long storeId) {
        log.debug("queryTryMarketingShowList and pageHelper :{} \r\n name :{} \r\n skuNo :{} \r\n storeId :{}");

        return pageHelper.setListDates(tryMarketingShowService.queryTryMarketingShowList(pageHelper, name, skuNo, storeId).getList()
                .stream().peek(tryMarketingShow -> setOtherInfo(tryMarketingShow, storeId)).collect(Collectors.toList()));
    }


    /**
     * 设置列表其他信息
     *
     * @param tryMarketingShow 试用活动
     * @param storeId          店铺id
     */
    private void setOtherInfo(TryMarketingShow tryMarketingShow, long storeId) {
        log.debug("setOtherInfo and tryMarketingShow :{} \r\n storeId :{}", tryMarketingShow, storeId);

        if (Objects.isNull(tryMarketingShow)) {
            return;
        }

        // 平台查询，则设置店铺名称
        if (CommonConstant.ADMIN_STOREID == storeId) {

            if (CommonConstant.ADMIN_STOREID == tryMarketingShow.getOldStoreId()) {

                tryMarketingShow.setStoreName("平台自营");

            } else {

                // 设置店铺名称
                TStoreInfo storeInfo = storeInfoService.queryStoreInfo(tryMarketingShow.getOldStoreId());

                if (Objects.nonNull(storeInfo)) {
                    tryMarketingShow.setStoreName(storeInfo.getStoreName());
                }
            }

        }

    }

}

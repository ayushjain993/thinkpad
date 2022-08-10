package io.uhha.marketing.service.impl;


import io.uhha.marketing.domain.GroupMarketingShow;
import io.uhha.marketing.service.GroupMarketingShowService;
import io.uhha.marketing.service.GroupMarketingShowServiceApi;
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
 * 拼团活动聚合服务接口实现类
 *
 * @author mj created on 2020/6/1
 */
@Service
@Slf4j
public class GroupMarketingShowServiceApiImpl implements GroupMarketingShowServiceApi {

    /**
     * 注入拼团活动服务接口
     */
    @Autowired
    private GroupMarketingShowService groupMarketingShowService;

    /**
     * 注入店铺信息服务接口
     */
    @Autowired
    private ITStoreInfoService storeInfoService;


    @Override
    public PageHelper<GroupMarketingShow> queryGroupMarketingShowList(PageHelper<GroupMarketingShow> pageHelper, String name, String skuNo, long storeId) {
        log.debug("queryGroupMarketingShowList and pageHelper :{} \r\n name :{} \r\n skuNo :{} \r\n storeId :{}");

        return pageHelper.setListDates(groupMarketingShowService.queryGroupMarketingShowList(pageHelper, name, skuNo, storeId).getList()
                .stream().peek(groupMarketingShow -> setOtherInfo(groupMarketingShow, storeId)).collect(Collectors.toList()));
    }


    /**
     * 设置列表其他信息
     *
     * @param groupMarketingShow 拼团活动
     * @param storeId            店铺id
     */
    private void setOtherInfo(GroupMarketingShow groupMarketingShow, long storeId) {
        log.debug("setOtherInfo and groupMarketingShow :{} \r\n storeId :{}", groupMarketingShow, storeId);

        if (Objects.isNull(groupMarketingShow)) {
            return;
        }

        // 平台查询，则设置店铺名称
        if (CommonConstant.ADMIN_STOREID == storeId) {

            if (CommonConstant.ADMIN_STOREID == groupMarketingShow.getOldStoreId()) {

                groupMarketingShow.setStoreName("平台自营");

            } else {

                // 设置店铺名称
                TStoreInfo storeInfo = storeInfoService.queryStoreInfo(groupMarketingShow.getOldStoreId());

                if (Objects.nonNull(storeInfo)) {
                    groupMarketingShow.setStoreName(storeInfo.getStoreName());
                }
            }

        }

    }

}

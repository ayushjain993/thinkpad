package io.uhha.support.impl;

import io.uhha.common.core.domain.entity.SysUser;
import io.uhha.common.enums.ImUserTypeEnum;
import io.uhha.im.domain.ImUser;
import io.uhha.im.service.ImUserService;
import io.uhha.store.domain.TStoreInfo;
import io.uhha.store.service.ITStoreInfoService;
import io.uhha.support.ICustomerSupportService;
import io.uhha.system.service.ISysStoreUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@Slf4j
public class CustomerSupportServiceImpl implements ICustomerSupportService {

    @Autowired
    private ITStoreInfoService storeInfoService;

    @Autowired
    private ISysStoreUserService sysStoreUserService;

    @Autowired
    private ImUserService imUserService;

    public String getSupportByStoreId(Long storeId){
        //设置店铺信息
        TStoreInfo storeInfo = storeInfoService.queryStoreInfo(storeId);
        //设置店铺关注人数
        if (!ObjectUtils.isEmpty(storeInfo)) {
            //如果平台自营，设置默认关注人数
            ImUserTypeEnum imUserType = null;
            List<SysUser> sysUsers;

            sysUsers = sysStoreUserService.getStoreSysUserByStoreId(storeId);
            imUserType = ImUserTypeEnum.STORE;
            if (!CollectionUtils.isEmpty(sysUsers)) {
                ImUserTypeEnum finalImUserType = imUserType;
                sysUsers.stream().anyMatch(sysUser -> {
                    ImUser imUser = imUserService.queryImUserInfo(finalImUserType, String.valueOf(sysUser.getUserId()));
                    boolean flag = false;
                    if (imUser != null) {
                        storeInfo.setUserCode(imUser.getUserCode());
                        flag = true;
                    }
                    return flag;
                });
            }
        }
        return storeInfo.getUserCode();
    }

}

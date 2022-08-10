package io.uhha.store.service.impl;

import io.uhha.common.core.domain.entity.SysUser;
import io.uhha.common.notify.ValidateNotifyHelper;
import io.uhha.common.utils.SecurityUtils;
import io.uhha.store.service.IStoreSecurityService;
import io.uhha.system.domain.vo.UpdateStorePwdBean;
import io.uhha.system.service.ISysStoreUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StoreSecurityServiceImpl implements IStoreSecurityService {

    @Autowired
    private ISysStoreUserService storeUserService;

    @Override
    public int recoverPassword(UpdateStorePwdBean updatePwdBean) {
        SysUser sysStoreUser = storeUserService.selectUserByMobile(updatePwdBean.getMobile());
        if(sysStoreUser==null){
            log.error("user not found for modifying password");
            return 0;
        }
        return storeUserService.resetUserPwd(updatePwdBean.getMobile(), SecurityUtils.encryptPassword(updatePwdBean.getPassword()));
    }
}

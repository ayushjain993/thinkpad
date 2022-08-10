package io.uhha.web.service;

import io.uhha.common.core.domain.entity.SysUser;

public interface SysStoreLoginService {
    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid);

    public String storeVerifyCodelogin(String phoneNumber);
}

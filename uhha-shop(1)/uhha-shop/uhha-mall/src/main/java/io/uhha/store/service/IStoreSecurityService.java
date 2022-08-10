package io.uhha.store.service;

import io.uhha.system.domain.vo.UpdateStorePwdBean;

public interface IStoreSecurityService {
    /**
     * 恢复用户密码
     *
     * @param updatePwdBean 修改密码实体
     * @return -1 参数错误  -2 验证码错误 -3 用户不匹配 -4 图片验证码错误 0 失败 1 成功
     */
    public int recoverPassword(UpdateStorePwdBean updatePwdBean);
}

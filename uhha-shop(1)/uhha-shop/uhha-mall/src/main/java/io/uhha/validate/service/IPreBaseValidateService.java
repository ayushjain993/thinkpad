package io.uhha.validate.service;

import io.uhha.coin.common.result.Result;
import io.uhha.member.domain.UmsMember;

/**
 * 前置验证接口
 * Created by ZKF on 2017/4/19.
 */
public interface IPreBaseValidateService {

    /**
     * 验证用户禁用状态
     * true:禁用，false:正常
     */
    Boolean validateUserStatus(UmsMember user);

    /**
     * 验证交易密码修改后可用性
     * true:不可用，false:可用
     */
    Boolean validateUserTradePasswordIsAvailable(UmsMember user);

    /**
     * 验证手机或谷歌是否都未绑定
     * true:未绑定，false:已存在绑定
     */
    Boolean validatePhoneBindAndGoogleBind(UmsMember user);

    /**
     * 验证用户是否实名认证
     * true:未实名，false:已实名
     */
    Boolean validateUserIdentityAuth(UmsMember user);

    /**
     * 验证交易密码是否设置
     * true:未设置，false:已设置
     */
    Boolean validateUserTradePasswordIsSetting(UmsMember user);

    /**
     * 验证用户禁止提现
     */
    Boolean validateUserWithdrawFiat(UmsMember user);

    /**
     * 验证用户禁止提币Coin
     */
    Boolean validateUserWithdraw(UmsMember user);

    /**
     * 提现验证
     */
    Result validateCapital(UmsMember user, String type);



}

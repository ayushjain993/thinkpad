package io.uhha.validate.service.impl;

import io.uhha.coin.common.Enum.UserStatusEnum;
import io.uhha.coin.common.result.Result;
import io.uhha.coin.common.result.ResultErrorCode;
import io.uhha.validate.service.IPreBaseValidateService;
import io.uhha.member.domain.UmsMember;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by ZKF on 2017/4/19.
 */
@Service("preBaseValidateService")
public class PreBaseValidateServiceImpl implements IPreBaseValidateService {

    @Override
    public Boolean validateUserStatus(UmsMember user) {
        return user.getStatus().equalsIgnoreCase(Integer.toString(UserStatusEnum.FORBBIN_VALUE));
    }

    @Override
    public Boolean validateUserTradePasswordIsAvailable(UmsMember user) {
        return user.getPayPasswordModifyTime() != null && ((System.currentTimeMillis() - user.getPayPasswordModifyTime().getTime())/1000) <= 24 * 60 * 60;
    }

    @Override
    public Boolean validatePhoneBindAndGoogleBind(UmsMember user) {
        return !user.isMobileVerified() && !user.isGoogleVerified();
    }

    @Override
    public Boolean validateUserIdentityAuth(UmsMember user) {
        return !user.isRealNameScreened();
    }

    @Override
    public Boolean validateUserTradePasswordIsSetting(UmsMember user) {
        return StringUtils.isEmpty(user.getPaypassword());
    }

    @Override
    public Boolean validateUserWithdrawFiat(UmsMember user) {
        return "1".equalsIgnoreCase(user.getAllowFiatWithdraw());
    }

    @Override
    public Boolean validateUserWithdraw(UmsMember user) {
        return "1".equalsIgnoreCase(user.getAllowCryptoWithdraw());
    }

    @Override
    public Result validateCapital(UmsMember user, String type) {

//        if(validatePhoneBindAndGoogleBind(user)){
//            return Result.failure(ResultErrorCode.USER_BIND_PHONEANDGOOGLE.getCode(), ResultErrorCode.USER_BIND_PHONEANDGOOGLE.getValue());
//        }

        if(validateUserTradePasswordIsSetting(user)){
            return Result.failure(ResultErrorCode.TRADEPASSWORD_SETTING.getCode(), ResultErrorCode.TRADEPASSWORD_SETTING.getValue());
        }

        if(validateUserIdentityAuth(user)){
            return Result.failure(ResultErrorCode.USER_IDENTITYAUTH.getCode(), ResultErrorCode.USER_IDENTITYAUTH.getValue());
        }

        if(validateUserTradePasswordIsAvailable(user)){
            return Result.failure(ResultErrorCode.TRADEPASSWORD_AVAILABLE.getCode(), ResultErrorCode.TRADEPASSWORD_AVAILABLE.getValue());
        }

        if(type.equals("coin")){
            if(validateUserWithdraw(user)){
                return Result.failure(ResultErrorCode.USER_FORBIDDEN_COIN.getCode(), ResultErrorCode.USER_FORBIDDEN_COIN.getValue());
            }
        } else if(type.equals("fiat")){
            if(validateUserWithdrawFiat(user)){
                return Result.failure(ResultErrorCode.USER_FORBIDDEN_CNY.getCode(), ResultErrorCode.USER_FORBIDDEN_CNY.getValue());
            }
        }

        return Result.success();
    }
}

package io.uhha.validate.service.impl;

import io.uhha.coin.common.Enum.CountLimitTypeEnum;
import io.uhha.coin.user.domain.FLogCountlimit;
import io.uhha.coin.user.domain.FUserLoginlimit;
import io.uhha.common.exception.BCException;
import io.uhha.coin.common.google.GoogleAuth;
import io.uhha.coin.common.result.Result;
import io.uhha.coin.common.result.ResultErrorCode;
import io.uhha.common.constant.Constant;
import io.uhha.common.utils.Utils;
import io.uhha.coin.user.mapper.FLogCountlimitMapper;
import io.uhha.validate.service.IValidityCheckService;
import io.uhha.common.notify.ValidateNotifyHelper;
import io.uhha.common.core.domain.entity.SysUser;
import io.uhha.member.domain.UmsMember;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 有效性验证接口实现
 */
@Service("validityCheckService")
public class ValidityCheckServiceImpl implements IValidityCheckService {
    @Autowired
    private FLogCountlimitMapper countLimitMapper;
    @Autowired
    private ValidateNotifyHelper validateNotifyHelper;

    private final Logger logger = LoggerFactory.getLogger(ValidityCheckServiceImpl.class);

    @Override
    public Result getPhoneCodeCheck(String areaCode, String phone, String code, Integer messageType, String ip, Integer platform) {
        Boolean checkResult = getCountLimitCheck(ip, CountLimitTypeEnum.TELEPHONE);
        ResultErrorCode countErrorCode = ResultErrorCode.PHONE_LIMIT_COUNT_ERROR;
        ResultErrorCode beyondErrorCode = ResultErrorCode.PHONE_LIMIT_BEYOND_ERROR;
        if (!checkResult) {
            return Result.failure(beyondErrorCode.getCode(), beyondErrorCode.getValue());
        }
        boolean mobilValidate = validateNotifyHelper.validateSmsCode(areaCode, phone, platform, messageType, code);
        if (mobilValidate) {
            deleteCountLimit(ip, CountLimitTypeEnum.TELEPHONE);
            return Result.success();
        }
        Integer limitCount = updateCountLimit(ip, CountLimitTypeEnum.TELEPHONE);
        return Result.failure(countErrorCode.getCode(), String.format(countErrorCode.getValue(), limitCount), limitCount);
    }

    @Override
    public Result getGoogleCodeCheck(String secret, String code, String ip) {
        Boolean checkResult = getCountLimitCheck(ip, CountLimitTypeEnum.GOOGLE);
        ResultErrorCode countErrorCode = ResultErrorCode.GOOGLE_LIMIT_COUNT_ERROR;
        ResultErrorCode beyondErrorCode = ResultErrorCode.GOOGLE_LIMIT_BEYOND_ERROR;
        if (!checkResult) {
            return Result.failure(beyondErrorCode.getCode(), beyondErrorCode.getValue());
        }
        boolean googleAuth = GoogleAuth.auth(Long.parseLong(code), secret);
        if (googleAuth) {
            deleteCountLimit(ip, CountLimitTypeEnum.GOOGLE);
            return Result.success();
        }
        Integer limitCount = updateCountLimit(ip, CountLimitTypeEnum.GOOGLE);
        return Result.failure(countErrorCode.getCode(), String.format(countErrorCode.getValue(), limitCount), limitCount);
    }

    @Override
    public Result getTradePasswordCheck(String oldPassword, String newPassword, String ip) {
        int limitType = CountLimitTypeEnum.TRADE_PASSWORD;
        ResultErrorCode countErrorCode = ResultErrorCode.TRADE_LIMIT_COUNT_ERROR;
        ResultErrorCode beyondErrorCode = ResultErrorCode.TRADE_LIMIT_BEYOND_ERROR;
        Boolean checkResult = getCountLimitCheck(ip, limitType);
        if (!checkResult) {
            return Result.failure(beyondErrorCode.getCode(), beyondErrorCode.getValue());
        }
        boolean isEquals = false;
        try {
            isEquals = oldPassword.equals(Utils.MD5(newPassword));
        } catch (BCException e) {
            e.printStackTrace();
        }
        if (isEquals) {
            deleteCountLimit(ip, limitType);
            return Result.success();
        }
        Integer limitCount = updateCountLimit(ip, limitType);
        return Result.failure(countErrorCode.getCode(), String.format(countErrorCode.getValue(), limitCount), limitCount);
    }

    @Override
    public Result getCapitalCheck(UmsMember user, String phoneCode, Integer messageType, String googleCode
            , String tradePassword, String ip, Integer platform) {
        Result result = getTradePasswordCheck(user.getPaypassword(),tradePassword,ip);
        if (!result.getSuccess()) {
            return result;
        }
        if (user.isGoogleVerified()) {
            result = getGoogleCodeCheck(user.getGoogleAuthenticator(), googleCode, ip);
            if (!result.getSuccess()) {
                return result;
            }
        }else if (user.isMobileVerified()) {
            result = getPhoneCodeCheck(user.getAreacode(), user.getMobile(), phoneCode, messageType, ip, platform);
            if (!result.getSuccess()) {
                return result;
            }
        }
        return result;
    }

    @Override
    public Result getChangeCheck(UmsMember user, String phoneCode, Integer messageType, String googleCode, String ip
            , Integer platform) {
        Result result = Result.success();
        /*if (user.getFgooglebind()) {
            result = getGoogleCodeCheck(user.getFgoogleauthenticator(), googleCode, ip);
            if (!result.getSuccess()) {
                return result;
            }
        }*/
        if (user.isMobileVerified()) {
            result = getPhoneCodeCheck(user.getAreacode(), user.getMobile(), phoneCode, messageType, ip, platform);
            if (!result.getSuccess()) {
                return result;
            }
        }
        return result;
    }

    @Override
    public Result getChangeCheck(SysUser user, String phoneCode, Integer messageType, String googleCode, String ip
            , Integer platform){
        throw new NotImplementedException();
    }


    @Override
    public Boolean getCountLimitCheck(String ip, Integer limitType) {
        FLogCountlimit countlimit = countLimitMapper.selectByLimit(ip, limitType);
        return countlimit == null || countlimit.getFcount() < Constant.ErrorCountLimit;
    }


    @Override
    public Boolean selectCountByfuid(Integer fuid){
        FUserLoginlimit fUserLoginlimit = countLimitMapper.selectCountByfuid(fuid);
        if (fUserLoginlimit == null || fUserLoginlimit.getFcount() < Constant.UserErrorLoginLimit ){
            return true ;
        }else if(fUserLoginlimit.getFcount() >= Constant.UserErrorLoginLimit
                && ( !new SimpleDateFormat("yyyy-MM-dd").format(new Date()) .equals( new SimpleDateFormat("yyyy-MM-dd").format(fUserLoginlimit.getFcreatetime()) )  ) ){
            //  此时重新统计登陆错误次数
            countLimitMapper.deleteByFuid(fuid);
            return true ;
        }else {
            return false ;
        }

    }

    @Override
    public int deleteByFuid(Integer fuid){
        return countLimitMapper.deleteByFuid(fuid);
    }

    @Override
    public Integer getLoginFailureCount(String ip, Integer limitType) {
        FLogCountlimit countlimit = countLimitMapper.selectByLimit(ip, limitType);
        return countlimit == null ? 0 : countlimit.getFcount();
    }

    @Override
    public Boolean deleteCountLimit(String ip, Integer limitType) {
        return countLimitMapper.deleteByIp(ip, limitType) > 0;
    }

    @Override
    public Integer updateCountLimit(String ip, Integer limitType) {
        FLogCountlimit countlimit = countLimitMapper.selectByLimit(ip, limitType);
        if (countlimit != null) {
            countlimit.setFcount(countlimit.getFcount() + 1);
            countlimit.setFcreatetime(Utils.getTimestamp());
            if (countLimitMapper.updateByLimit(countlimit) <= 0) {
                return 1;
            }
            return Constant.ErrorCountLimit - countlimit.getFcount();
        }
        countlimit = new FLogCountlimit();
        countlimit.setFip(ip);
        countlimit.setFtype(limitType);
        countlimit.setFcount(1);
        countlimit.setFcreatetime(Utils.getTimestamp());
        countlimit.setVersion(0);
        if (countLimitMapper.insert(countlimit) <= 0) {
            return 1;
        }
        return Constant.ErrorCountLimit - countlimit.getFcount();
    }

    @Override
    public Integer updateUserLoginLimit(String ip,Integer fuid ) throws Exception{
        FUserLoginlimit fUserLoginlimit = countLimitMapper.selectCountByfuid(fuid);
        if (fUserLoginlimit != null){
            fUserLoginlimit.setFcreatetime(new Date());  // 错误次数 + 1
            if (countLimitMapper.updateByFuid(fUserLoginlimit) <= 0){
                logger.info("更改FUserLoginlimit失败,{}",fuid);
                throw new Exception("更改FUserLoginlimit失败,"+fuid);
            }
            return Constant.UserErrorLoginLimit - fUserLoginlimit.getFcount() - 1 ;
        }
        fUserLoginlimit = new FUserLoginlimit();
        fUserLoginlimit.setFcreatetime(new Date());
        fUserLoginlimit.setFcount(1);
        fUserLoginlimit.setFip(ip);
        fUserLoginlimit.setFuid(fuid);
        fUserLoginlimit.setVersion(1);
        if (countLimitMapper.insertByUser(fUserLoginlimit) <= 0){
            logger.info("新增FUserLoginlimit失败,{}",fuid);
            throw new Exception("新增FUserLoginlimit失败,"+fuid);
        }
        return Constant.UserErrorLoginLimit - 1 ;
    }

    @Override
    public Result getCapitalCheckGoogleBeforePhone(UmsMember user, String phoneCode, Integer messageType, String googleCode
            , String tradePassword, String ip, Integer platform) {
        Result result = getTradePasswordCheck(user.getPaypassword(),tradePassword,ip);
        if (!result.getSuccess()) {
            return result;
        }
        if (user.isGoogleVerified()) {
            result = getGoogleCodeCheckError(user.getGoogleAuthenticator(), googleCode, ip);
            if (!result.getSuccess()) {
                return result;
            }
        }else if (user.isMobileVerified()) {
            result = getPhoneCodeCheck(user.getAreacode(), user.getMobile(), phoneCode, messageType, ip, platform);
            if (!result.getSuccess()) {
                return result;
            }
        }
        return result;
    }

    @Override
    public Result getChangeCheckGoogleBeforePhone(UmsMember user, String phoneCode, Integer messageType, String googleCode, String ip
            , Integer platform) {
        Result result = Result.success();
        if (user.isGoogleVerified()) {
            result = getGoogleCodeCheckError(user.getGoogleAuthenticator(), googleCode, ip);
            if (!result.getSuccess()) {
                return result;
            }
        }else if (user.isMobileVerified()) {
            result = getPhoneCodeCheck(user.getAreacode(), user.getMobile(), phoneCode, messageType, ip, platform);
            if (!result.getSuccess()) {
                return result;
            }
        }
        return result;
    }

    @Override
    public Result getGoogleCodeCheckError(String secret, String code, String ip) {
        ResultErrorCode checkError = ResultErrorCode.GOOGLE_CHECK_ERROR;
        boolean googleAuth = GoogleAuth.auth(Long.parseLong(code), secret);
        if (googleAuth) {
            deleteCountLimit(ip, CountLimitTypeEnum.GOOGLE);
            return Result.success();
        }
        return Result.failure(checkError.getCode(), checkError.getValue());
    }


}

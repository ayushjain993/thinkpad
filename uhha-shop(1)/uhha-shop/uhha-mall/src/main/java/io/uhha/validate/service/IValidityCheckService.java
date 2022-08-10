package io.uhha.validate.service;

import io.uhha.coin.common.result.Result;
import io.uhha.common.core.domain.entity.SysUser;
import io.uhha.member.domain.UmsMember;

/**
 * 有效性验证接口
 */
public interface IValidityCheckService {

    /**
     * 验证手机验证码是否正确
     *
     * @param areaCode    区号
     * @param phone       手机号
     * @param code        验证码
     * @param messageType 消息类型
     * @param ip          IP地址
     * @param platform    平台
     * @return 服务端返回消息
     */
    Result getPhoneCodeCheck(String areaCode, String phone, String code, Integer messageType, String ip, Integer platform);

    /**
     * 验证谷歌验证码是否正确
     *
     * @param secret 谷歌KEY
     * @param code   验证码
     * @param ip     IP地址
     * @return 服务端返回消息
     */
    Result getGoogleCodeCheck(String secret, String code, String ip);

    /**
     * 验证交易密码是否正确
     *
     * @param oldPassword 原密码
     * @param newPassword 输入密码
     * @param ip          IP地址
     * @return 服务端返回消息
     */
    Result getTradePasswordCheck(String oldPassword, String newPassword, String ip);

    /**
     * 资金操作验证
     *
     * @param user          用户实体
     * @param phoneCode     手机验证码
     * @param messageType   消息类型
     * @param googleCode    谷歌验证码
     * @param tradePassword 交易密码
     * @param ip            IP地址
     * @param platform    平台
     * @return 服务端返回消息
     */
    Result getCapitalCheck(UmsMember user, String phoneCode, Integer messageType, String googleCode, String tradePassword
            , String ip, Integer platform);

    /**
     * 变更操作验证
     *
     * @param user        用户实体
     * @param phoneCode   手机验证码
     * @param messageType 消息类型
     * @param googleCode  谷歌验证码
     * @param ip          IP地址
     * @param platform    平台
     * @return 服务端返回消息
     */
    Result getChangeCheck(UmsMember user, String phoneCode, Integer messageType, String googleCode, String ip
            , Integer platform);

    /**
     * 商品管理员用户变更操作验证
     *
     * @param user        用户实体
     * @param phoneCode   手机验证码
     * @param messageType 消息类型
     * @param googleCode  谷歌验证码
     * @param ip          IP地址
     * @param platform    平台
     * @return 服务端返回消息
     */
    Result getChangeCheck(SysUser user, String phoneCode, Integer messageType, String googleCode, String ip
            , Integer platform);

    /**
     * 验证IP限制
     *
     * @param ip        IP地址
     * @param limitType 限制类型
     * @return true 验证通过，false 验证失败
     */
    Boolean getCountLimitCheck(String ip, Integer limitType);


    /**
     * 登陸失败次数
     *
     * @param ip        IP地址
     * @param limitType 限制类型
     * @return count
     */
    Integer getLoginFailureCount(String ip, Integer limitType);

    /**
     * 删除IP限制
     *
     * @param ip        IP地址
     * @param limitType 限制类型
     * @return true 成功，false 失败
     */
    Boolean deleteCountLimit(String ip, Integer limitType);

    /**
     * 更新IP限制，无数据则新增
     *
     * @param ip        IP地址
     * @param limitType 限制类型
     * @return 可以重试次数
     */
    Integer updateCountLimit(String ip, Integer limitType);


    /**
     * 更新user登陆限制，无数据则新增
     * @param ip        IP地址
     * @param fuid 用户id
     * @return 可以重试次数
     */
    Integer updateUserLoginLimit(String ip,Integer fuid)  throws Exception;

    /**
     * 验证user登陆限制
     *
     * @param fuid  用户id
     * @return true 验证通过，false 验证失败
     */
    Boolean selectCountByfuid(Integer fuid);


    /**
     * 删除用户登陆错误次数
     * @param fuid
     * @return
     */
    int deleteByFuid(Integer fuid);

    /**
     * 验证谷歌验证码是否正确 取消错误次数验证
     *
     * @param secret 谷歌KEY
     * @param code   验证码
     * @param ip     IP地址
     * @return 服务端返回消息
     */
    Result getGoogleCodeCheckError(String secret, String code, String ip);

    /**
     * 变更操作验证
     *
     * @param user        用户实体
     * @param phoneCode   手机验证码
     * @param messageType 消息类型
     * @param googleCode  谷歌验证码
     * @param ip          IP地址
     * @param platform    平台
     * @return 服务端返回消息
     */
    Result getChangeCheckGoogleBeforePhone(UmsMember user, String phoneCode, Integer messageType, String googleCode, String ip
            , Integer platform);

    /**
     * 资金操作验证
     *
     * @param user          用户实体
     * @param phoneCode     手机验证码
     * @param messageType   消息类型
     * @param googleCode    谷歌验证码
     * @param tradePassword 交易密码
     * @param ip            IP地址
     * @param platform    平台
     * @return 服务端返回消息
     */
    Result getCapitalCheckGoogleBeforePhone(UmsMember user, String phoneCode, Integer messageType, String googleCode, String tradePassword
            , String ip, Integer platform);

}

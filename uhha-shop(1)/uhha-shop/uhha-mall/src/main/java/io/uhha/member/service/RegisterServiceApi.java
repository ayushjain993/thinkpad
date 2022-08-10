package io.uhha.member.service;

/**
 * 注册服务聚合接口
 */
public interface RegisterServiceApi {

    /**
     * 用户注册
     *
     * @param countryCode   国家码
     * @param mobile        手机号码
     * @param password      密码
     * @param code          用户输入的手机验证码
     * @param recommondCode 推荐吗
     * @return -1 手机验证码错误 -2 参数错误 0 失败  成功>0 -3 手机号码已存在 -10  推荐人不存在
     */
    int registerCustomer(String countryCode, String mobile, String password, String code, String recommondCode);

}

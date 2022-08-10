package io.uhha.system.service;

public interface IMallRedisInitService {
    /**
     * 初始化
     */
    public void redisInit();

    /**
     * 初始化国家列表
     */
    public void initCountries();

    /**
     * 初始化物流企业列表
     */
    public void initLogisticCompany();

    public void initValidateTemplate();

    public void initEmailSettings();

    public void initSmsSettings();

}

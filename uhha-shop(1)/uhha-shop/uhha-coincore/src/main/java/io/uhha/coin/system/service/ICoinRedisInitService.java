package io.uhha.coin.system.service;

/**
 * redis接口
 * @author TT
 */
public interface ICoinRedisInitService {

    /**
     * 初始化
     */
    public void redisInit();

    /**
     * 重置redis数据
     */
    public void resetRedis();

    /**
     * 初始币种设置
     */
    public void initCoinSetting();


    /**
     * 初始化api
     */
    public void initApi();

    /**
     * 初始化交易信息列表
     */
    public void initSystemTradeType();

    /**
     * 初始化初始化币种列表
     */
    public void initSystemCoinType();

    /**
     * 初始化系统参数
     */
    public void initSystemArgs();



}


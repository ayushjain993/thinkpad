package io.uhha.coin.system.service.impl;

import com.alibaba.fastjson.JSON;
import io.uhha.coin.common.Enum.SystemArgsTypeEnum;
import io.uhha.coin.system.domain.FSystemArgs;
import io.uhha.coin.system.mapper.FSystemArgsMapper;
import io.uhha.common.core.redis.RedisCache;
import io.uhha.common.core.redis.RedisConstant;
import io.uhha.common.core.redis.RedisObject;
import io.uhha.coin.system.domain.SystemCoinSetting;
import io.uhha.coin.system.domain.SystemCoinType;
import io.uhha.coin.system.domain.SystemTradeType;
import io.uhha.coin.system.mapper.SystemCoinSettingMapper;
import io.uhha.coin.system.mapper.SystemCoinTypeMapper;
import io.uhha.coin.system.mapper.SystemTradeTypeMapper;
import io.uhha.coin.system.service.ICoinRedisInitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 初始化Redis参数
 *
 * @author TT
 */
@Service
public class CoinRedisInitServiceImpl implements ICoinRedisInitService {

    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(CoinRedisInitServiceImpl.class);

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private SystemCoinTypeMapper systemCoinTypeMapper;
    @Autowired
    private SystemTradeTypeMapper systemTradeTypeMapper;
    @Autowired
    private SystemCoinSettingMapper systemCoinSettingMapper;
    @Autowired
    private FSystemArgsMapper systemArgsMapper;

    /**
     * 初始化
     */
    @Override
    public void redisInit() {

        // 系统参数
        logger.info("--------------> 系统参数开始初始化");
        initSystemArgs();

        // 币种
        logger.info("--------------> 币种类型开始初始化");
        initSystemCoinType();

        // 交易
        logger.info("--------------> 交易类型开始初始化");
        initSystemTradeType();

        // API交易对
        logger.info("--------------> API交易对开始初始化");
        initApi();

        // 币种设置
        logger.info("--------------> 币种设置开始初始化");
        initCoinSetting();


        logger.info("--------------> 初始化完成");
    }

    /**
     * 重置redis数据
     */
    @Override
    public void resetRedis() {
        logger.info("----> resetRedis start");
        redisInit();
        logger.info("----> resetRedis success");
    }

    /**
     * 初始化初始化币种列表
     */
    @Override
    public void initSystemCoinType() {
        // 劵商
        List<SystemCoinType> coinTypeList = systemCoinTypeMapper.selectAll();
        // 全币种
        RedisObject allcoins = new RedisObject();
        allcoins.setExtObject(coinTypeList);
        setNoExpire(RedisConstant.COIN_LIST_KEY, allcoins);
        // 币种
        for (SystemCoinType coin : coinTypeList) {
            RedisObject coinObj = new RedisObject();
            coinObj.setExtObject(coin);
            setNoExpire(RedisConstant.COIN_KEY + coin.getId(), coinObj);
        }
    }

    /**
     * 初始化交易信息列表
     */
    @Override
    public void initSystemTradeType() {
        List<SystemTradeType> tradeTypeList = systemTradeTypeMapper.selectAll();
        // 扩展数据加载
        for (SystemTradeType tradeType : tradeTypeList) {
            SystemCoinType buyCoin = systemCoinTypeMapper.selectByPrimaryKey(tradeType.getBuyCoinId());
            SystemCoinType sellCoin = systemCoinTypeMapper.selectByPrimaryKey(tradeType.getSellCoinId());
            if(buyCoin!=null ||sellCoin!=null) {
                tradeType.setBuySymbol(buyCoin.getSymbol());
                tradeType.setBuyName(buyCoin.getName());
                tradeType.setBuyShortName(buyCoin.getShortName());
                tradeType.setBuyWebLogo(buyCoin.getWebLogo());
                tradeType.setSellSymbol(sellCoin.getSymbol());
                tradeType.setSellName(sellCoin.getName());
                tradeType.setSellShortName(sellCoin.getShortName());
                tradeType.setSellWebLogo(sellCoin.getWebLogo());
                tradeType.setSellAppLogo(sellCoin.getAppLogo());
            }
        }
        // 全币种
        RedisObject allcoins = new RedisObject();
        allcoins.setExtObject(tradeTypeList);
        setNoExpire(RedisConstant.TRADE_LIST_KEY, allcoins);
        // 币种
        for (SystemTradeType tradeType : tradeTypeList) {
            RedisObject coinObj = new RedisObject();
            coinObj.setExtObject(tradeType);
            setNoExpire(RedisConstant.TRADE_KEY + tradeType.getId(), coinObj);
        }
    }

    /**
     * 初始化api
     */
    @Override
    public void initApi() {
        List<SystemTradeType> tradeTypeList = systemTradeTypeMapper.selectAll();
        // 扩展数据加载
        for (SystemTradeType tradeType : tradeTypeList) {
            SystemCoinType buyCoin = systemCoinTypeMapper.selectByPrimaryKey(tradeType.getBuyCoinId());
            SystemCoinType sellCoin = systemCoinTypeMapper.selectByPrimaryKey(tradeType.getSellCoinId());

            if(buyCoin!=null ||sellCoin!=null) {
                RedisObject apiTradeType = new RedisObject();
                apiTradeType.setExtObject(tradeType);
                setNoExpire(RedisConstant.API_SYMBOL + sellCoin.getSymbol().toUpperCase() + "_" + buyCoin.getSymbol().toUpperCase(), apiTradeType);
            }
        }
    }

    /**
     * 初始币种设置
     */
    @Override
    public void initCoinSetting() {
        List<SystemCoinSetting> coinSettingList = systemCoinSettingMapper.selectAll();
        for (SystemCoinSetting coinSetting : coinSettingList) {
            RedisObject virtualFees = new RedisObject();
            virtualFees.setExtObject(coinSetting);
            setNoExpire(RedisConstant.SYS_VIRTUALCOINFEES_KEY + coinSetting.getCoinId() + "_"
                    + coinSetting.getLevelVip(), virtualFees);
        }
    }

    /**
     * 初始化系统参数
     */
    @Override
    public void initSystemArgs() {
        // 网站前台需要的系统参数
        Map<String, Object> webSysArgs = new HashMap<String, Object>();
        List<FSystemArgs> argsList = systemArgsMapper.selectAll();
        // 系统单一参数存储
        for (FSystemArgs fSystemArgs : argsList) {
            RedisObject args = new RedisObject();
            args.setExtObject(fSystemArgs.getFvalue());
            setNoExpire(RedisConstant.ARGS_KET + fSystemArgs.getFkey(), args);
            // 记录网站前台需要的参数
            if (fSystemArgs.getFtype().equals(SystemArgsTypeEnum.FRONT.getCode())) {
                webSysArgs.put(fSystemArgs.getFkey(), fSystemArgs.getFvalue());
            }
        }

        // 网站前台的系统参数
//        RedisObject webSystemArgs = new RedisObject();
//        webSystemArgs.setExtObject(webSysArgs);
        redisCache.setCacheMap(RedisConstant.ARGS_KET_WEB, webSysArgs);
    }



    /**
     * Redis设置
     *
     * @param key key
     * @param obj obj
     */
    public void setNoExpire(String key, RedisObject obj) {
        redisCache.setCacheObject(key, JSON.toJSONString(obj));
    }

    /**
     * Redis删除（重置之前，批量删除）
     *
     * @param key key
     */
    public void deleteNoExpire(String key) {
        redisCache.removeByPattern(key);
    }

}

package io.uhha.coin.common.framework.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.uhha.coin.common.Enum.SystemCoinStatusEnum;
import io.uhha.coin.common.Enum.SystemCoinTypeEnum;
import io.uhha.coin.common.Enum.SystemTradeStatusEnum;
import io.uhha.coin.common.crypto.MD5Util;
import io.uhha.coin.common.dto.system.FSystemBankinfoRecharge;
import io.uhha.coin.common.dto.system.FSystemBankinfoWithdraw;
import io.uhha.coin.market.domain.TickerData;
import io.uhha.coin.system.domain.SystemCoinSetting;
import io.uhha.coin.system.domain.SystemCoinType;
import io.uhha.coin.system.domain.SystemTradeType;
import io.uhha.coin.system.service.ICoinRedisInitService;
import io.uhha.common.constant.Constant;
import io.uhha.common.core.redis.RedisCache;
import io.uhha.common.core.redis.RedisConstant;
import io.uhha.common.core.redis.RedisObject;
import io.uhha.common.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisCryptoHelper {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ICoinRedisInitService coinRedisInitService;

    /**
     * 默认过期时间为1个小时
     */
    private static final int expire_time = 60 * 60;

    /**
     * 新增不过期的数据
     *
     * @param key   键
     * @param value 值
     * @return ok
     */
    public void setNoExpire(String key, String value) {
        redisCache.setCacheObject(key, value);
    }

    public void set(String key, RedisObject t) {
        set(key, t, expire_time);
    }

    public void setNoExpire(String key, RedisObject t) {
        redisCache.setCacheObject(key, JSON.toJSONString(t));
    }

    public void set(String key, RedisObject t, int seconds) {
        redisCache.setCacheObject(key, JSON.toJSONString(t), seconds, TimeUnit.SECONDS);
    }


    public void remove(String key) {
        redisCache.deleteObject(key);
    }

    public String get(String key) {
        return redisCache.getCacheObject(key);
    }

    public void delete(String key) {
        redisCache.deleteObject(key);
    }


    /***************** SystemArgs操作 *******************/
    /**
     * 根据key设置系统参数
     *
     * @param key
     * @param value
     * @return
     */
    public void setSystemArgs(String key, String value) {
        RedisObject args = new RedisObject();
        args.setExtObject(value);
        redisCache.setCacheObject(RedisConstant.ARGS_KET + key, JSON.toJSONString(args));
    }

    /**
     * 根據key获取系统参数
     *
     * @param key
     * @return
     */
    public String getSystemArgs(String key) {
        String args = redisCache.getCacheObject(RedisConstant.ARGS_KET + key);
        if (ObjectUtils.isEmpty(args)) {
            coinRedisInitService.initSystemArgs();
            args = redisCache.getCacheObject(RedisConstant.ARGS_KET + key);
            if (ObjectUtils.isEmpty(args)) {
                log.error("SystemArgs data corrupted!!");
                return null;
            }
        }
        JSONObject obj = JSON.parseObject(args);
        String value = obj.get("extObject").toString();
        return value;
    }

    /**
     * 获取所有的系统参数
     *
     * @return
     */
    public Map<String, Object> getSystemArgsList() {
        Map<String, Object> sysArgsMap;
        sysArgsMap = redisCache.getCacheMap(RedisConstant.ARGS_KET_WEB);
        return sysArgsMap;
    }

    /**
     * 重置系统币列表
     * @return string
     */
    String resetAndGetSysCoinTypes() {
        coinRedisInitService.initSystemCoinType();
        String coins = redisCache.getCacheObject(RedisConstant.COIN_LIST_KEY);
        if (StringUtils.isEmpty(coins)) {
            log.error("SystemCoinType data corrupted!!");
            return null;
        }
        return coins;
    }


    // CoinType操作

    /**
     * 获取所有币种列表(禁用，未禁用)
     *
     * @return
     */
    public List<SystemCoinType> getCoinTypeListAll() {
        String coins = redisCache.getCacheObject(RedisConstant.COIN_LIST_KEY);
        if (StringUtils.isEmpty(coins)) {
            coins = resetAndGetSysCoinTypes();
            if (StringUtils.isEmpty(coins)) {
                log.error("SystemCoinType data corrupted!!");
                return null;
            }
        }
        JSONObject obj = JSON.parseObject(coins);
        return JSONArray.parseArray(obj.getJSONArray("extObject").toString(), SystemCoinType.class);
    }

    /**
     * 获取未禁用的币种列表
     *
     * @return
     */
    public List<SystemCoinType> getCoinTypeList() {
        String coins = redisCache.getCacheObject(RedisConstant.COIN_LIST_KEY);
        if (StringUtils.isEmpty(coins)) {
            coins = resetAndGetSysCoinTypes();
            if (StringUtils.isEmpty(coins)) {
                log.error("SystemCoinType data corrupted!!");
                return null;
            }
        }
        JSONObject obj = JSON.parseObject(coins);
        List<SystemCoinType> coinList = JSONArray.parseArray(obj.getJSONArray("extObject").toString(), SystemCoinType.class);
        List<SystemCoinType> newCoinList = new ArrayList();
        for (SystemCoinType systemCoinType : coinList) {
            if (!systemCoinType.getStatus().equals(SystemCoinStatusEnum.ABNORMAL.getCode())) {
                newCoinList.add(systemCoinType);
            }
        }
        return newCoinList;
    }

    /**
     * 获取未禁用的第一个币种
     *
     * @return
     */
    public SystemCoinType getCoinTypeFirst() {
        String coins = redisCache.getCacheObject(RedisConstant.COIN_LIST_KEY);
        if (StringUtils.isEmpty(coins)) {
            coins = resetAndGetSysCoinTypes();
            if (StringUtils.isEmpty(coins)) {
                log.error("SystemCoinType data corrupted!!");
                return null;
            }
        }
        JSONObject obj = JSON.parseObject(coins);
        List<SystemCoinType> coinList = JSONArray.parseArray(obj.getJSONArray("extObject").toString(), SystemCoinType.class);
        for (SystemCoinType systemCoinType : coinList) {
            if (!systemCoinType.getStatus().equals(SystemCoinStatusEnum.ABNORMAL.getCode())) {
                return systemCoinType;
            }
        }
        return null;
    }

    /**
     * 获取币种信息
     *
     * @param coinId 主键ID
     * @return
     */
    public SystemCoinType getCoinType(Integer coinId) {
        String strCoin = redisCache.getCacheObject(RedisConstant.COIN_KEY + coinId);
        if (StringUtils.isEmpty(strCoin)) {
            String coinList = resetAndGetSysCoinTypes();
            if (StringUtils.isEmpty(coinList)) {
                log.error("SystemCoinType data corrupted!!");
                return null;
            }
        }
        strCoin = redisCache.getCacheObject(RedisConstant.COIN_KEY + coinId);
        JSONObject obj = JSON.parseObject(strCoin);
        SystemCoinType coinType = obj.getObject("extObject", SystemCoinType.class);
        return coinType;
    }

    /**
     * 获取币种信息
     *
     * @param shortName 简称
     * @return
     */
    public SystemCoinType getCoinTypeShortName(String shortName) {
        String coins = redisCache.getCacheObject(RedisConstant.COIN_LIST_KEY);
        if (StringUtils.isEmpty(coins)) {
            coins = resetAndGetSysCoinTypes();
            if (StringUtils.isEmpty(coins)) {
                log.error("SystemCoinType data corrupted!!");
                return null;
            }
        }
        JSONObject obj = JSON.parseObject(coins);
        List<SystemCoinType> coinList = JSONArray.parseArray(obj.getJSONArray("extObject").toString(),
                SystemCoinType.class);
        for (SystemCoinType systemCoinType : coinList) {
            if (!systemCoinType.getStatus().equals(SystemCoinStatusEnum.ABNORMAL.getCode()) && systemCoinType.getShortName().equals(shortName)) {
                return systemCoinType;
            }
        }
        return null;
    }

    /**
     * 获取币种信息(可提现)
     *
     * @param type 币种类型
     * @return
     */
    public SystemCoinType getCoinTypeIsWithdrawFirst(Integer type) {
        String coins = redisCache.getCacheObject(RedisConstant.COIN_LIST_KEY);
        if (StringUtils.isEmpty(coins)) {
            coins = resetAndGetSysCoinTypes();
            if (StringUtils.isEmpty(coins)) {
                log.error("SystemCoinType data corrupted!!");
                return null;
            }
        }
        JSONObject obj = JSON.parseObject(coins);
        List<SystemCoinType> coinList = JSONArray.parseArray(obj.getJSONArray("extObject").toString(),
                SystemCoinType.class);
        for (SystemCoinType systemCoinType : coinList) {
            if (!systemCoinType.getStatus().equals(SystemCoinStatusEnum.ABNORMAL.getCode()) && systemCoinType.getIsWithdraw()
                    && systemCoinType.getType().equals(type)) {
                return systemCoinType;
            }
        }
        return null;
    }

    /**
     * 获取币种信息(可提现)
     *
     * @param type 币种类型
     * @return
     */
    public List<SystemCoinType> getCoinTypeIsWithdrawList(Integer type) {
        String coins = redisCache.getCacheObject(RedisConstant.COIN_LIST_KEY);
        if (StringUtils.isEmpty(coins)) {
            coins = resetAndGetSysCoinTypes();
            if (StringUtils.isEmpty(coins)) {
                log.error("SystemCoinType data corrupted!!");
                return null;
            }
        }
        JSONObject obj = JSON.parseObject(coins);
        List<SystemCoinType> coinList = JSONArray.parseArray(obj.getJSONArray("extObject").toString(),
                SystemCoinType.class);
        List<SystemCoinType> newCoinList = new ArrayList();
        for (SystemCoinType systemCoinType : coinList) {
            if (!systemCoinType.getStatus().equals(SystemCoinStatusEnum.ABNORMAL.getCode()) && systemCoinType.getIsWithdraw()
                    && systemCoinType.getType().equals(type)) {
                newCoinList.add(systemCoinType);
            }
        }
        return newCoinList;
    }

    /**
     * 获取币种信息(可充值)
     *
     * @param type 币种类型
     * @return
     */
    public SystemCoinType getCoinTypeIsRechargeFirst(Integer type) {
        String coins = redisCache.getCacheObject(RedisConstant.COIN_LIST_KEY);
        if (StringUtils.isEmpty(coins)) {
            coins = resetAndGetSysCoinTypes();
            if (StringUtils.isEmpty(coins)) {
                log.error("SystemCoinType data corrupted!!");
                return null;
            }
        }
        JSONObject obj = JSON.parseObject(coins);
        List<SystemCoinType> coinList = JSONArray.parseArray(obj.getJSONArray("extObject").toString(),
                SystemCoinType.class);
        for (SystemCoinType systemCoinType : coinList) {
            if (!systemCoinType.getStatus().equals(SystemCoinStatusEnum.ABNORMAL.getCode()) && systemCoinType.getIsRecharge()
                    && systemCoinType.getType().equals(type)) {
                return systemCoinType;
            }
        }
        return null;
    }

    /**
     * 获取币种信息(可充值)
     *
     * @param type 币种类型
     * @return
     */
    public List<SystemCoinType> getCoinTypeIsRechargeList(Integer type) {
        String coins = redisCache.getCacheObject(RedisConstant.COIN_LIST_KEY);
        if (StringUtils.isEmpty(coins)) {
            coins = resetAndGetSysCoinTypes();
            if (StringUtils.isEmpty(coins)) {
                log.error("SystemCoinType data corrupted!!");
                return null;
            }
        }
        JSONObject obj = JSON.parseObject(coins);
        List<SystemCoinType> coinList = JSONArray.parseArray(obj.getJSONArray("extObject").toString(),
                SystemCoinType.class);
        List<SystemCoinType> newCoinList = new ArrayList();
        for (SystemCoinType systemCoinType : coinList) {
            if (!systemCoinType.getStatus().equals(SystemCoinStatusEnum.ABNORMAL.getCode()) && systemCoinType.getIsRecharge()
                    && systemCoinType.getType().equals(type)) {
                newCoinList.add(systemCoinType);
            }
        }
        return newCoinList;
    }


    /**
     * 获取币种信息(虚拟币)
     *
     * @return
     */
    public List<SystemCoinType> getCoinTypeCoinList() {
        String coins = redisCache.getCacheObject(RedisConstant.COIN_LIST_KEY);
        if (StringUtils.isEmpty(coins)) {
            coins = resetAndGetSysCoinTypes();
            if (StringUtils.isEmpty(coins)) {
                log.error("SystemCoinType data corrupted!!");
                return null;
            }
        }
        JSONObject obj = JSON.parseObject(coins);
        List<SystemCoinType> coinList = JSONArray.parseArray(obj.getJSONArray("extObject").toString(),
                SystemCoinType.class);
        List<SystemCoinType> newCoinList = new ArrayList<>();
        for (SystemCoinType systemCoinType : coinList) {
            if (!systemCoinType.getStatus().equals(SystemCoinStatusEnum.ABNORMAL.getCode())
                    && systemCoinType.getType().equals(SystemCoinTypeEnum.COIN.getCode())) {
                newCoinList.add(systemCoinType);
            }
        }
        return newCoinList;
    }

    /**
     * 获取币种信息(法币)
     *
     * @return
     */
    public List<SystemCoinType> getCoinTypeCnyList() {
        String coins = redisCache.getCacheObject(RedisConstant.COIN_LIST_KEY);
        if (StringUtils.isEmpty(coins)) {
            coins = resetAndGetSysCoinTypes();
            if (StringUtils.isEmpty(coins)) {
                log.error("SystemCoinType data corrupted!!");
                return null;
            }
        }
        JSONObject obj = JSON.parseObject(coins);
        List<SystemCoinType> coinList = JSONArray.parseArray(obj.getJSONArray("extObject").toString(),
                SystemCoinType.class);
        List<SystemCoinType> newCoinList = new ArrayList<>();
        for (SystemCoinType systemCoinType : coinList) {
            if (!systemCoinType.getStatus().equals(SystemCoinStatusEnum.ABNORMAL.getCode())
                    && systemCoinType.getType().equals(SystemCoinTypeEnum.CNY.getCode())) {
                newCoinList.add(systemCoinType);
            }
        }
        return newCoinList;
    }

    /**
     * 验证是否本平台币种
     *
     * @return true 是,false 否
     */
    public boolean hasCoinId(int coinId) {
        SystemCoinType coinType = getCoinType(coinId);
        return coinType != null;
    }

    /**
     * 查询虚拟币手续费
     *
     * @param coinId 币种ID
     * @param level  用户等级
     * @return FVirtualFees
     */
    public SystemCoinSetting getCoinSetting(int coinId, String level) {
        String fees = redisCache.getCacheObject(RedisConstant.SYS_VIRTUALCOINFEES_KEY + coinId + "_" + level);
        if (StringUtils.isEmpty(fees)) {
            return null;
        }
        JSONObject obj = JSON.parseObject(fees);
        SystemCoinSetting coinSetting = JSON.parseObject(obj.get("extObject").toString(), SystemCoinSetting.class);
        return coinSetting;
    }

    /**
     * 获取系统所有币种名称MAP
     *
     * @return Map<Integer, String>
     */
    public Map<Integer, String> getCoinTypeNameMap() {
        Map<Integer, String> coinMap = new TreeMap<Integer, String>(new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                // 降序排序
                return o1.compareTo(o2);
            }
        });
        List<SystemCoinType> coinTypes = getCoinTypeListAll();
        for (SystemCoinType coinType : coinTypes) {
            coinMap.put(coinType.getId(), coinType.getName());
        }
        return coinMap;
    }

    /**
     * 获取系统所有未禁用的币种名称MAP
     *
     * @return Map<Integer, String>
     */
    public Map<Integer, String> getCoinNameStateMap() {
        Map<Integer, String> coinMap = new TreeMap<Integer, String>(new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                // 降序排序
                return o1.compareTo(o2);
            }
        });
        List<SystemCoinType> coinTypes = getCoinTypeList();
        for (SystemCoinType coinType : coinTypes) {
            coinMap.put(coinType.getId(), coinType.getName());
        }
        return coinMap;
    }

    /*****************TradeType操作 *******************/
    /**
     * 获取未禁用的交易列表(所有)
     *
     * @return
     */
    public List<SystemTradeType> getTradeTypeList() {
        String coins = redisCache.getCacheObject(RedisConstant.TRADE_LIST_KEY);
        if (StringUtils.isEmpty(coins)) {
            return null;
        }
        JSONObject obj = JSON.parseObject(coins);
        List<SystemTradeType> coinList = JSONArray.parseArray(obj.getJSONArray("extObject").toString(), SystemTradeType.class);
        List<SystemTradeType> newCoinList = new ArrayList<>();
        for (SystemTradeType systemTradeType : coinList) {
            if (!systemTradeType.getStatus().equals(SystemTradeStatusEnum.ABNORMAL.getCode())) {
                newCoinList.add(systemTradeType);
            }
        }
        return newCoinList;
    }

    /**
     * 获取未禁用的交易列表(可交易)
     *
     * @return
     */
    public List<SystemTradeType> getTradeTypeShare() {
        String coins = redisCache.getCacheObject(RedisConstant.TRADE_LIST_KEY);
        if (StringUtils.isEmpty(coins)) {
            return null;
        }
        JSONObject obj = JSON.parseObject(coins);
        List<SystemTradeType> coinList = JSONArray.parseArray(obj.getJSONArray("extObject").toString(), SystemTradeType.class);
        List<SystemTradeType> newCoinList = new ArrayList<>();
        for (SystemTradeType systemTradeType : coinList) {
            if (!systemTradeType.getStatus().equals(SystemTradeStatusEnum.ABNORMAL.getCode()) && systemTradeType.getIsShare()) {
                newCoinList.add(systemTradeType);
            }
        }
        return newCoinList;
    }

    /**
     * 获取币种信息(可PUSH)
     *
     * @return
     */
    public List<SystemCoinType> getCoinTypePushList() {
        String coins = redisCache.getCacheObject(RedisConstant.COIN_LIST_KEY);
        if (StringUtils.isEmpty(coins)) {
            coins = resetAndGetSysCoinTypes();
            if (StringUtils.isEmpty(coins)) {
                log.error("SystemCoinType data corrupted!!");
                return null;
            }
        }
        JSONObject obj = JSON.parseObject(coins);
        List<SystemCoinType> coinList = JSONArray.parseArray(obj.getJSONArray("extObject").toString(),
                SystemCoinType.class);
        List<SystemCoinType> newCoinList = new ArrayList<>();
        for (SystemCoinType systemCoinType : coinList) {
            if (!systemCoinType.getStatus().equals(SystemCoinStatusEnum.ABNORMAL.getCode()) && systemCoinType.getIsPush()) {
                newCoinList.add(systemCoinType);
            }
        }
        return newCoinList;
    }

    /**
     * 获取币种信息(存币理财)
     *
     * @return List<SystemCoinType>
     */
    public List<SystemCoinType> getCoinTypeFinancesList() {
        List<SystemCoinType> coinList = getCoinTypeListAll();
        if (coinList == null) {
            return null;
        }
        List<SystemCoinType> newCoinList = new ArrayList<>();
        for (SystemCoinType systemCoinType : coinList) {
            if (!systemCoinType.getStatus().equals(SystemCoinStatusEnum.ABNORMAL.getCode()) && systemCoinType.getIsFinances()) {
                newCoinList.add(systemCoinType);
            }
        }
        return newCoinList;
    }

    /**
     * 获取未禁用的交易列表(根据交易类型)
     *
     * @return
     */
    public List<SystemTradeType> getTradeTypeSort(Integer type) {
        String coins = redisCache.getCacheObject(RedisConstant.TRADE_LIST_KEY);
        if (StringUtils.isEmpty(coins)) {
            return null;
        }
        JSONObject obj = JSON.parseObject(coins);
        List<SystemTradeType> coinList = JSONArray.parseArray(obj.getJSONArray("extObject").toString(), SystemTradeType.class);
        List<SystemTradeType> newCoinList = new ArrayList<>();
        for (SystemTradeType systemTradeType : coinList) {
            if (!systemTradeType.getStatus().equals(SystemTradeStatusEnum.ABNORMAL.getCode()) && systemTradeType.getType().equals(type)) {
                newCoinList.add(systemTradeType);
            }
        }
        return newCoinList;
    }

    /**
     * 获取未禁用的交易(第一条)
     *
     * @return
     */
    public SystemTradeType getTradeTypeFirst(Integer type) {
        String coins = redisCache.getCacheObject(RedisConstant.TRADE_LIST_KEY);
        if (StringUtils.isEmpty(coins)) {
            return null;
        }
        JSONObject obj = JSON.parseObject(coins);
        List<SystemTradeType> coinList = JSONArray.parseArray(obj.getJSONArray("extObject").toString(), SystemTradeType.class);
        for (SystemTradeType systemTradeType : coinList) {
            if (!systemTradeType.getStatus().equals(SystemTradeStatusEnum.ABNORMAL.getCode())
                    && systemTradeType.getIsShare() && systemTradeType.getType().equals(type)) {
                return systemTradeType;
            }
        }
        return null;
    }

    /**
     * 获取交易币种信息
     *
     * @param tradeId 主键ID
     * @return
     */
    public SystemTradeType getTradeType(Integer tradeId) {
        String coins = redisCache.getCacheObject(RedisConstant.TRADE_KEY + tradeId);
        if (StringUtils.isEmpty(coins)) {
            return null;
        }
        JSONObject obj = JSON.parseObject(coins);
        SystemTradeType tradeType = obj.getObject("extObject", SystemTradeType.class);
        return tradeType;
    }

    /**
     * 获取币种与交易映射
     *
     * @return Map<coinId, tradeId>
     */
    public Map<Integer, Integer> getCoinIdToTradeId() {
        Map<Integer, Integer> coinIdToTradeId = new HashMap<>();
        List<SystemTradeType> tradeTypes = getTradeTypeList();
        if (tradeTypes == null) {
            return coinIdToTradeId;
        }
        for (SystemTradeType tradeType : tradeTypes) {
            coinIdToTradeId.put(tradeType.getSellCoinId(), tradeType.getId());
        }
        return coinIdToTradeId;
    }

    /***************** 火币 *******************/

    /**
     * 获取K线数据
     *
     * @param id
     * @param stepid 时间
     * @return
     */
    public JSONArray getHuobiKline(Integer id, Integer stepid) {
        String huobi = redisCache.getCacheObject(RedisConstant.HUOBI_KLINE + stepid + id);
        if (StringUtils.isEmpty(huobi)) {
            return null;
        }
        JSONObject obj = JSON.parseObject(huobi);
        net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray.fromObject(obj.getString("extObject").toString());
        JSONArray jsonKile = new JSONArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            jsonKile.add(jsonArray.get(i));
        }
        return jsonKile;
    }

    /**
     * 涨跌
     *
     * @param id
     * @return
     */
    public JSONObject getHuobiRost(Integer id) {
        String huobi = redisCache.getCacheObject(RedisConstant.HUOBI_ROSE + id);
        if (StringUtils.isEmpty(huobi)) {
            return null;
        }
        JSONObject obj = JSON.parseObject(huobi);
        JSONObject huobiRose = JSON.parseObject(obj.getString("extObject").toString());
        return huobiRose;
    }


    /**
     * 涨跌
     *
     * @param id
     * @return
     */
    public JSONObject getZhongBiRost(Integer id) {
        String zhongbi = redisCache.getCacheObject(RedisConstant.ZHONGBI_ROSE + id);
        if (StringUtils.isEmpty(zhongbi)) {
            return null;
        }
        JSONObject obj = JSON.parseObject(zhongbi);
        JSONObject huobiRose = JSON.parseObject(obj.getString("extObject").toString());
        return huobiRose;
    }


    /**
     * 获取聚合行情(Ticker)
     *
     * @param id
     * @return
     */
    public JSONObject getHuobiMerged(Integer id) {
        String huobi = redisCache.getCacheObject(RedisConstant.HUOBI_MERGED + id);
        if (StringUtils.isEmpty(huobi)) {
            return null;
        }
        JSONObject obj = JSON.parseObject(huobi);
        JSONObject huobiMerged = JSON.parseObject(obj.getString("extObject").toString());
        return huobiMerged;
    }

    /**
     * 获取火币买深度
     *
     * @param id
     * @return
     */
    public JSONArray getHuobiBuyDepth(Integer id, int num) {
        String huobi = redisCache.getCacheObject(RedisConstant.HUOBI_DEPTH + id);
        if (StringUtils.isEmpty(huobi)) {
            return null;
        }
        JSONObject obj = JSON.parseObject(huobi);
        JSONObject huobiDepth = JSON.parseObject(obj.getString("extObject").toString());
        JSONArray jsonArray = huobiDepth.getJSONArray("bids");
        int forCount = jsonArray.size() <= num ? huobiDepth.size() : num;
        JSONArray json = new JSONArray();
        for (int i = 0; i < forCount; i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", i + 1);
            jsonObject.put("price", JSONArray.parseArray(jsonArray.get(i).toString()).get(0));
            jsonObject.put("amount", JSONArray.parseArray(jsonArray.get(i).toString()).get(1));
            json.add(jsonObject);
        }
        return json;
    }

    /**
     * 获取火币卖深度
     *
     * @param id
     * @return
     */
    public JSONArray getHuobiSellDepth(Integer id, int num) {
        String huobi = redisCache.getCacheObject(RedisConstant.HUOBI_DEPTH + id);
        if (StringUtils.isEmpty(huobi)) {
            return null;
        }
        JSONObject obj = JSON.parseObject(huobi);
        JSONObject huobiDepth = JSON.parseObject(obj.getString("extObject").toString());
        JSONArray jsonArray = huobiDepth.getJSONArray("asks");
        int forCount = jsonArray.size() <= num ? huobiDepth.size() : num;
        JSONArray json = new JSONArray();
        for (int i = 0; i < forCount; i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", i + 1);
            jsonObject.put("price", JSONArray.parseArray(jsonArray.get(i).toString()).get(0));
            jsonObject.put("amount", JSONArray.parseArray(jsonArray.get(i).toString()).get(1));
            json.add(jsonObject);
        }
        return json;
    }

    /**
     * 获取 Market Depth 数据
     *
     * @param id
     * @return
     */
    public JSONObject getHuobiDepth(Integer id) {
        String huobi = redisCache.getCacheObject(RedisConstant.HUOBI_DEPTH + id);
        if (StringUtils.isEmpty(huobi)) {
            return null;
        }
        JSONObject obj = JSON.parseObject(huobi);
        JSONObject huobiDepth = JSON.parseObject(obj.getString("extObject").toString());
        return huobiDepth;
    }

    /**
     * 批量获取最近的交易记录
     *
     * @param id
     * @return
     */
    public JSONArray getHuobiTrade(Integer id, int num, int tid) {
        String huobi = redisCache.getCacheObject(RedisConstant.HUOBI_TRADE + id);
        if (StringUtils.isEmpty(huobi)) {
            return null;
        }
        JSONObject obj = JSON.parseObject(huobi);
        net.sf.json.JSONArray huobiDepth = net.sf.json.JSONArray.fromObject(obj.getString("extObject").toString());
        int forCount = huobiDepth.size() <= (num + tid) ? huobiDepth.size() : (num + tid);
        JSONArray returnHuobi = new JSONArray();
        for (int i = tid; i < forCount; i++) {
            returnHuobi.add(huobiDepth.get(i));
        }
        return returnHuobi;
    }


    /*****************中币******************/

    /**
     * 获取中币K线数据
     *
     * @param id
     * @param stepid 时间
     * @return
     */
    public JSONArray getZhongbiKline(Integer id, Integer stepid) {
        String zhongbi = redisCache.getCacheObject(RedisConstant.ZHONGBI_KLINE + stepid + id);
        if (StringUtils.isEmpty(zhongbi)) {
            return null;
        }
        JSONObject obj = JSON.parseObject(zhongbi);
        net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray.fromObject(obj.getString("extObject").toString());
        JSONArray jsonKile = new JSONArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            jsonKile.add(jsonArray.get(i));
        }
        return jsonKile;
    }


    /**
     * 涨跌
     *
     * @param id
     * @return
     */
    public JSONObject getZhongbiRost(Integer id) {
        String zhongbi = redisCache.getCacheObject(RedisConstant.ZHONGBI_ROSE + id);
        if (StringUtils.isEmpty(zhongbi)) {
            return null;
        }
        JSONObject obj = JSON.parseObject(zhongbi);
        JSONObject zhongbiRose = JSON.parseObject(obj.getString("extObject").toString());
        return zhongbiRose;
    }

    /**
     * 获取聚合行情(Ticker)
     *
     * @param id
     * @return
     */
    public JSONObject getZhongBiMerged(Integer id) {
        String zhongbi = redisCache.getCacheObject(RedisConstant.ZHONGBI_MERGED + id);
        if (StringUtils.isEmpty(zhongbi)) {
            return null;
        }
        JSONObject obj = JSON.parseObject(zhongbi);
        JSONObject huobiMerged = JSON.parseObject(obj.getString("extObject").toString());
        return huobiMerged;
    }

    /**
     * 获取中币买深度
     *
     * @param id
     * @return
     */
    public JSONArray getZhongBiBuyDepth(Integer id, int num) {
        String zhongbi = redisCache.getCacheObject(RedisConstant.ZHONGBI_DEPTH + id);
        if (StringUtils.isEmpty(zhongbi)) {
            return null;
        }
        JSONObject obj = JSON.parseObject(zhongbi);
        JSONObject zhongbiDepth = JSON.parseObject(obj.getString("extObject").toString());
        JSONArray jsonArray = zhongbiDepth.getJSONArray("bids");
        int forCount = jsonArray.size() <= num ? zhongbiDepth.size() : num;
        JSONArray json = new JSONArray();
        for (int i = 0; i < forCount; i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", i + 1);
            jsonObject.put("price", JSONArray.parseArray(jsonArray.get(i).toString()).get(0));
            jsonObject.put("amount", JSONArray.parseArray(jsonArray.get(i).toString()).get(1));
            json.add(jsonObject);
        }
        return json;
    }

    /**
     * 获取火币卖深度
     *
     * @param id
     * @return
     */
    public JSONArray getZhongBiSellDepth(Integer id, int num) {
        String huobi = redisCache.getCacheObject(RedisConstant.ZHONGBI_DEPTH + id);
        if (StringUtils.isEmpty(huobi)) {
            return null;
        }
        JSONObject obj = JSON.parseObject(huobi);
        JSONObject huobiDepth = JSON.parseObject(obj.getString("extObject").toString());
        JSONArray jsonArray = huobiDepth.getJSONArray("asks");
        int forCount = jsonArray.size() <= num ? huobiDepth.size() : num;
        JSONArray json = new JSONArray();
        for (int i = 0; i < forCount; i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", i + 1);
            jsonObject.put("price", JSONArray.parseArray(jsonArray.get(i).toString()).get(0));
            jsonObject.put("amount", JSONArray.parseArray(jsonArray.get(i).toString()).get(1));
            json.add(jsonObject);
        }
        return json;
    }

    /**
     * 获取 Market Depth 数据
     *
     * @param id
     * @return
     */
    public JSONObject getZhongBiDepth(Integer id) {
        String huobi = redisCache.getCacheObject(RedisConstant.ZHONGBI_DEPTH + id);
        if (StringUtils.isEmpty(huobi)) {
            return null;
        }
        JSONObject obj = JSON.parseObject(huobi);
        JSONObject huobiDepth = JSON.parseObject(obj.getString("extObject").toString());
        return huobiDepth;
    }

    /**
     * 批量获取最近的交易记录
     *
     * @param id
     * @return
     */
    public JSONArray getZhongBiTrade(Integer id, int num, int tid) {
        String huobi = redisCache.getCacheObject(RedisConstant.ZHONGBI_TRADE + id);
        if (StringUtils.isEmpty(huobi)) {
            return null;
        }
        JSONObject obj = JSON.parseObject(huobi);
        net.sf.json.JSONArray huobiDepth = net.sf.json.JSONArray.fromObject(obj.getString("extObject").toString());
        int forCount = huobiDepth.size() <= (num + tid) ? huobiDepth.size() : (num + tid);
        JSONArray returnHuobi = new JSONArray();
        for (int i = tid; i < forCount; i++) {
            returnHuobi.add(huobiDepth.get(i));
        }
        return returnHuobi;
    }


    /***************** BankInfo操作 *******************/
    /**
     * 获取充值银行卡列表
     *
     * @param type 类型
     * @return List<FSystemBankinfoRecharge>
     */
    public List<FSystemBankinfoRecharge> getRechargeBank(int type) {
        String bankinfo = redisCache.getCacheObject(RedisConstant.SYS_RECHARGEBANK_KEY + type);
        if (StringUtils.isEmpty(bankinfo)) {
            return null;
        }
        JSONObject obj = JSON.parseObject(bankinfo);
        List<FSystemBankinfoRecharge> bankinfos = JSONArray.parseArray(obj.getJSONArray("extObject").toJSONString(), FSystemBankinfoRecharge.class);
        return bankinfos;
    }

    /**
     * 获取可提现银行列表
     *
     * @return List<FSystemBankinfoWithdraw>
     */
    public List<FSystemBankinfoWithdraw> getWithdrawBankList() {
        String bankinfo = redisCache.getCacheObject(RedisConstant.SYS_WITHDRAWBANKLIST_KEY);
        if (StringUtils.isEmpty(bankinfo)) {
            return null;
        }
        JSONObject obj = JSON.parseObject(bankinfo);
        List<FSystemBankinfoWithdraw> bankinfos = JSONArray.parseArray(obj.getJSONArray("extObject").toJSONString(), FSystemBankinfoWithdraw.class);
        return bankinfos;
    }

    /**
     * 根据ID获取提现银行信息
     *
     * @param id 提现银行ID
     * @return FSystemBankinfoWithdraw
     */
    public FSystemBankinfoWithdraw getWithdrawBank(int id) {
        String bankinfo = redisCache.getCacheObject(RedisConstant.SYS_WITHDRAWBANK_KEY + id);
        if (StringUtils.isEmpty(bankinfo)) {
            return null;
        }
        JSONObject obj = JSON.parseObject(bankinfo);
        FSystemBankinfoWithdraw bi = JSON.parseObject(obj.get("extObject").toString(), FSystemBankinfoWithdraw.class);
        return bi;
    }

    /***************** tradepassword操作 *******************/
    /**
     * 设置交易密码
     *
     * @param fuid 用户ID
     */
    public void setNeedTradePassword(int fuid) {
        if (getNeedTradePassword(fuid)) {
            String args = redisCache.getCacheObject(RedisConstant.ARGS_KET + "tradePasswordHour");
            String value = "5";
            if (!StringUtils.isEmpty(args)) {
                JSONObject obj = JSON.parseObject(args);
                value = obj.get("extObject").toString();
            }
            int tradePasswordHour = Integer.valueOf(value);
            RedisObject obj = new RedisObject();
            obj.setExtObject(true);
            String token = RedisConstant.TRADE_NEED_PASSWORD + MD5Util.md5(String.valueOf(fuid));
            redisCache.setCacheObject(token, JSON.toJSONString(obj), tradePasswordHour * 60 * 60, TimeUnit.SECONDS);
        }
    }

    /**
     * 获取是否需要交易密码
     *
     * @param fuid 用户ID
     * @return boolean
     */
    public boolean getNeedTradePassword(int fuid) {
        String token = RedisConstant.TRADE_NEED_PASSWORD + MD5Util.md5(String.valueOf(fuid));
        String args = redisCache.getCacheObject(token);
        if (!StringUtils.isEmpty(args)) {
            return false;
        }
        return true;
    }

    /**
     * 根据id查询提现银行
     *
     * @param fid 银行卡Id
     * @return FSystemBankinfoWithdraw
     */
    public FSystemBankinfoWithdraw getWithdrawBankById(int fid) {
        String bankinfoStr = redisCache.getCacheObject(RedisConstant.SYS_WITHDRAWBANK_KEY + fid);
        if (StringUtils.isEmpty(bankinfoStr)) {
            return null;
        }
        JSONObject obj = JSON.parseObject(bankinfoStr);
        FSystemBankinfoWithdraw bankinfo = JSON.parseObject(obj.get("extObject").toString(), FSystemBankinfoWithdraw.class);
        return bankinfo;
    }

    /**
     * 根据id查询充值银行
     *
     * @param fid 银行卡Id
     * @return FSystemBankinfoRecharge
     */
    public FSystemBankinfoRecharge getRechargeBankById(int fid) {
        String bankinfoStr = redisCache.getCacheObject(RedisConstant.SYS_RECHARGEBANKID_KEY + fid);
        if (StringUtils.isEmpty(bankinfoStr)) {
            return null;
        }
        JSONObject obj = JSON.parseObject(bankinfoStr);
        FSystemBankinfoRecharge bankinfo = JSON.parseObject(obj.get("extObject").toString(), FSystemBankinfoRecharge.class);
        return bankinfo;
    }

    /**
     * 获取成交价
     *
     * @param tradeId
     * @return
     */
    public BigDecimal getLastPrice(int tradeId) {
        String result = redisCache.getCacheObject(RedisConstant.TICKERE_KEY + tradeId);
        if (result == null || "".equals(result)) {
            return BigDecimal.ZERO;
        }
        RedisObject redisObject = JSON.parseObject(result, RedisObject.class);
        TickerData tickerData = JSON.parseObject(redisObject.getExtObject().toString(), TickerData.class);
        if (tickerData == null) {
            return BigDecimal.ZERO;
        }
        return tickerData.getLast();
    }

    /**
     * 获取开盘价
     *
     * @param tradeId
     * @return
     */
    public BigDecimal getKaiPrice(int tradeId) {
        String result = redisCache.getCacheObject(RedisConstant.TICKERE_KEY + tradeId);
        if (result == null || "".equals(result)) {
            return BigDecimal.ZERO;
        }
        RedisObject redisObject = JSON.parseObject(result, RedisObject.class);
        TickerData tickerData = JSON.parseObject(redisObject.getExtObject().toString(), TickerData.class);
        if (tickerData == null) {
            return BigDecimal.ZERO;
        }
        return tickerData.getKai();
    }


    /**
     * 设置缓存数据
     */
    public void setRedisData(String token, Object restInfo) {
        RedisObject obj = new RedisObject();
        obj.setExtObject(restInfo);
        // 30分钟过期时间
        redisCache.setCacheObject(token, JSON.toJSONString(obj), Constant.RESTPASSEXPIRETIME, TimeUnit.SECONDS);
    }

    /**
     * 设置缓存数据
     *
     * @param token
     * @param restInfo
     * @param expireSeconds
     */
    public void setRedisData(String token, Object restInfo, int expireSeconds) {
        RedisObject obj = new RedisObject();
        obj.setExtObject(restInfo);
        // 30分钟过期时间
        redisCache.setCacheObject(token, JSON.toJSONString(obj), expireSeconds, TimeUnit.SECONDS);
    }

    /**
     * 删除缓存数据
     */
    public void deleteRedisData(String token) {
        if (StringUtils.isEmpty(token)) {
            return;
        }
        redisCache.deleteObject(token);
    }

    /**
     * 获取缓存数据
     */
    public String getRedisData(String token) {
        if (token == null || StringUtils.isEmpty(token)) {
            return null;
        }
        String rest = redisCache.getCacheObject(token);
        if (StringUtils.isEmpty(rest)) {
            return null;
        }
        return rest;
    }

    /**
     * 获取缓存数据
     */
    public RedisObject getRedisObject(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        String rest = redisCache.getCacheObject(key);
        if (StringUtils.isEmpty(rest)) {
            return null;
        }
        RedisObject redisObject = JSON.parseObject(rest, RedisObject.class);
        return redisObject;
    }


    public void addAdsSourceCounter(String source) {
        String token = RedisConstant.ADS_SOURCE_KEY + "pv" + "_" + source;

        String json = redisCache.getCacheObject(token);

        RedisObject obj = null;
        if (!StringUtils.isEmpty(json)) {
            obj = JSON.parseObject(json, RedisObject.class);
            if (obj != null) {
                int counter = (Integer) obj.getExtObject() + 1;
                obj.setLastActiveDateTime(Utils.getTimestamp().getTime() / 1000);
                obj.setExtObject(counter);
                redisCache.setCacheObject(token, JSON.toJSONString(obj));
            }
        } else {
            obj = new RedisObject();
            obj.setLastActiveDateTime(Utils.getTimestamp().getTime() / 1000);
            obj.setExtObject(1);

            redisCache.setCacheObject(token, JSON.toJSONString(obj));
        }
    }


    /**
     * 设置缓存汇率
     *
     * @param rateKey
     * @param rate
     */
    public void setJuHeRateData(String rateKey, String rate) {
        redisCache.setCacheObject(rateKey, rate);
    }


    /***
     * 公司信息查询
     * @return
     */
    public RedisObject getCompanyInfo() {
        String companyInfo = redisCache.getCacheObject(RedisConstant.COMPANY_MERGED);

        if (StringUtils.isEmpty(companyInfo)) {
            return null;
        }
        RedisObject redisObject = JSON.parseObject(companyInfo, RedisObject.class);
        return redisObject;
    }
}

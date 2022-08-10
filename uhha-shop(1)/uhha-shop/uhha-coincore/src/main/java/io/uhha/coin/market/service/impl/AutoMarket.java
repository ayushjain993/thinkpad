package io.uhha.coin.market.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.uhha.coin.common.Enum.SystemTradeStatusEnum;
import io.uhha.coin.market.domain.TickerData;
import io.uhha.coin.common.framework.redis.RedisCryptoHelper;
import io.uhha.common.core.redis.RedisConstant;
import io.uhha.coin.system.domain.SystemTradeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("autoMarket")
public class AutoMarket {

	private static Logger logger = LoggerFactory.getLogger(AutoMarket.class);
	@Autowired
	private RedisCryptoHelper redisCryptoHelper;

	/**
	 * K线时间类型
	 */
	private final int[] TIME_KIND = { 1, 3, 5, 15, 30, // 分钟
			60, 2 * 60, 4 * 60, 6 * 60, 12 * 60, // 小时
			24 * 60, 7 * 24 * 60 // 天
	};
	/**
	 * K线最大存储数量
	 */
	private static final int MAX_LEN = 300;
	/**
	 * 实时数据
	 */
	private static Map<Integer, TickerData> TickerJson = new HashMap<Integer, TickerData>();

	/**
	 * 买卖深度
	 */
	private static Map<Integer, JSONArray> BuyDepthJson = new HashMap<Integer, JSONArray>();
	private static Map<Integer, JSONArray> SellDepthJson = new HashMap<Integer, JSONArray>();

	/**
	 * K线数据
	 */
	private static Map<Integer, Map<Integer, JSONArray>> KlineJson = new HashMap<Integer, Map<Integer, JSONArray>>();
	private static Map<Integer, Map<Integer, JSONArray>> lastKlineJson = new HashMap<Integer, Map<Integer, JSONArray>>();
	private static Map<Integer, JSONArray> indexKlineJson = new HashMap<Integer, JSONArray>();

	/**
	 * 已成交数据
	 */
	private static Map<Integer, JSONArray> SuccessJson = new HashMap<Integer, JSONArray>();

	/**
	 * 初始化线程
	 */
	public void init() {
		initKline();
		new Thread(new Work()).start();
	}

	/**
	 * 线程
	 */
	class Work implements Runnable {
		public void run() {
			while (true) {
				// Redis获取交易
				List<SystemTradeType> tradeTypes = redisCryptoHelper.getTradeTypeList();
				if(tradeTypes == null){
					continue;
				}
				for (SystemTradeType tradeType : tradeTypes) {
					int tradeId = tradeType.getId();
					// 实时行情
					initTicker(tradeId);
					// IndexKline
					initIndexKline(tradeId);
					// 平台撮合执行
					if (tradeType.getStatus().equals(SystemTradeStatusEnum.NORMAL.getCode())) {
						// 深度
						initDepth(tradeId);
						// 最新成交
						initSuccess(tradeId);
						// LastKline
						initLastKline(tradeId);
					}
				}
				try {
					Thread.sleep(20L);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 初始化k线
	 */
	private void initKline() {
		List<SystemTradeType> tradeTypes = redisCryptoHelper.getTradeTypeList();
		if(tradeTypes == null){
			logger.error("-->autoMarket initKline null");
			return;
		}
		for (SystemTradeType tradeType : tradeTypes) {
			int tradeId = tradeType.getId();
			for (int i : TIME_KIND) {
				// Kline
				String klineStr = redisCryptoHelper.getRedisData(RedisConstant.KLINE_KEY + tradeId + "_" + i);
				Map<Integer, JSONArray> klineJsonMap = KlineJson.get(tradeId);
				if (klineJsonMap == null) {
					klineJsonMap = new HashMap<Integer, JSONArray>();
				}
				if (klineStr == null || klineStr.isEmpty()) {
					klineJsonMap.put(i, new JSONArray());
				} else {
					klineJsonMap.put(i, JSON.parseArray(klineStr));
				}
				KlineJson.put(tradeId, klineJsonMap);
			}
		}
	}

	/**
	 * 初始化深度
	 * 
	 * @param tradeId
	 */
	private void initDepth(Integer tradeId) {
		// 买深度
		String buyDepthStr = redisCryptoHelper.getRedisData(RedisConstant.BUYDEPTH_KEY + tradeId);
		if (buyDepthStr == null || buyDepthStr.isEmpty()) {
			BuyDepthJson.put(tradeId, new JSONArray());
		} else {
			JSONArray tmpArray = JSON.parseArray(buyDepthStr);
			// 数据过滤
			JSONArray buyDepth = new JSONArray();
			for (Object object : tmpArray) {
				JSONArray array = JSON.parseArray(object.toString());
				if (Double.valueOf(array.get(1).toString()) > 0d) {
					buyDepth.add(array);
				}
			}
			BuyDepthJson.put(tradeId, buyDepth);
		}
		// 卖深度
		String sellDepthStr = redisCryptoHelper.getRedisData(RedisConstant.SELLDEPTH_KEY + tradeId);
		if (sellDepthStr == null || sellDepthStr.isEmpty()) {
			SellDepthJson.put(tradeId, new JSONArray());
		} else {
			JSONArray tmpArray = JSON.parseArray(sellDepthStr);
			// 数据过滤
			JSONArray sellDepth = new JSONArray();
			for (Object object : tmpArray) {
				JSONArray array = JSON.parseArray(object.toString());
				if (Double.valueOf(array.get(1).toString()) > 0d) {
					sellDepth.add(array);
				}
			}
			SellDepthJson.put(tradeId, sellDepth);
		}
	}

	/**
	 * 初始化实时行情
	 * 
	 * @param tradeId
	 */
	public void initTicker(Integer tradeId) {
		// 行情
		String tickerStr = redisCryptoHelper.getRedisData(RedisConstant.TICKERE_KEY + tradeId);
		TickerData tickerData = new TickerData(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
		if (tickerStr != null && !tickerStr.isEmpty()) {
			tickerData = JSON.parseObject(tickerStr, TickerData.class);
		}
		TickerJson.put(tradeId, tickerData);
	}

	/**
	 * 初始化最新成交
	 * 
	 * @param tradeId
	 */
	public void initSuccess(Integer tradeId) {
		String successStr = redisCryptoHelper.getRedisData(RedisConstant.SUCCESSENTRUST_KEY + tradeId);
		if (successStr == null || successStr.isEmpty()) {
			SuccessJson.put(tradeId, new JSONArray());
		} else {
			SuccessJson.put(tradeId, JSON.parseArray(successStr));
		}
	}

	/**
	 * 初始化最后一条K线
	 * 
	 * @param tradeId
	 */
	public void initLastKline(Integer tradeId) {
		for (int i : TIME_KIND) {
			// lastKline
			String lastKlineStr = redisCryptoHelper.getRedisData(RedisConstant.LASTKLINE_KEY + tradeId + "_" + i);
			Map<Integer, JSONArray> lastLlineJsonMap = lastKlineJson.get(tradeId);
			JSONArray lastLlineJson = null;
			if (lastLlineJsonMap == null) {
				lastLlineJsonMap = new HashMap<Integer, JSONArray>();
			}
			if (lastKlineStr == null || lastKlineStr.isEmpty()) {
				lastLlineJsonMap.put(i, new JSONArray());
			} else {
				lastLlineJson = JSON.parseArray(lastKlineStr);
				lastLlineJsonMap.put(i, lastLlineJson);
			}
			lastKlineJson.put(tradeId, lastLlineJsonMap);
			// 增量更新KLine
			Map<Integer, JSONArray> klineJsonMap = KlineJson.get(tradeId);
			if (klineJsonMap == null) {
				klineJsonMap = new HashMap<Integer, JSONArray>();
				klineJsonMap.put(i, new JSONArray());
				KlineJson.put(tradeId, klineJsonMap);
			} else {
				JSONArray klineJson = klineJsonMap.get(i);
				if (klineJson == null) {
					klineJsonMap.put(i, new JSONArray());
				} else if (klineJson.size() >= 1 && lastLlineJson != null) {
					JSONArray klineTemp1 = JSONArray.parseArray(klineJson.get(klineJson.size() - 1).toString());
					JSONArray klineTemp2 = JSONArray.parseArray(lastLlineJson.get(0).toString());
					if (klineTemp1.get(0).toString().equals(klineTemp2.get(0).toString())) {
						klineJson.remove(klineJson.size() - 1);
						klineJson.add(klineTemp2);
					} else {
						if (Long.valueOf(klineTemp2.get(0).toString()) - Long.valueOf(klineTemp1.get(0).toString()) > (i * 60 * 1000)) {
							String klineStr = redisCryptoHelper.getRedisData(RedisConstant.KLINE_KEY + tradeId + "_" + i);
							if (klineStr == null || klineStr.isEmpty()) {
								klineJsonMap.put(i, new JSONArray());
							} else {
								klineJsonMap.put(i, JSON.parseArray(klineStr));
							}
							KlineJson.put(tradeId, klineJsonMap);
						} else {
							klineJson.add(klineTemp2);
						}
					}
					// 大于指定条数移除
					if (klineJson.size() > MAX_LEN) {
						klineJson.remove(0);
					}
				} else if (klineJson.size() == 0) {
					String klineStr = redisCryptoHelper.getRedisData(RedisConstant.KLINE_KEY + tradeId + "_" + i);
					if (klineStr == null || klineStr.isEmpty()) {
						klineJsonMap.put(i, new JSONArray());
					} else {
						klineJsonMap.put(i, JSON.parseArray(klineStr));
					}
					KlineJson.put(tradeId, klineJsonMap);
				}
			}
		}
	}

	/**
	 * 初始化首页行情
	 * 
	 * @param tradeId
	 */
	public void initIndexKline(Integer tradeId) {
		String indexKlineStr = redisCryptoHelper.getRedisData(RedisConstant.THREETICKERE_KEY + tradeId);
		if (indexKlineStr == null || indexKlineStr.isEmpty()) {
			indexKlineJson.put(tradeId, new JSONArray());
		} else {
			indexKlineJson.put(tradeId, JSON.parseArray(indexKlineStr));
		}
	}

	/**
	 * 获取实时行情
	 * 
	 * @param tradeId
	 * @return
	 */
	public TickerData getTickerData(Integer tradeId) {
		TickerData tickerData = TickerJson.get(tradeId);
		if (tickerData == null) {
			return new TickerData(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
		} else {
			return tickerData;
		}
	}

	/**
	 * 获取买深度
	 * 
	 * @param tradeId
	 * @return
	 */
	public JSONArray getBuyDepthJson(Integer tradeId) {
		return BuyDepthJson.get(tradeId);
	}

	/**
	 * 获取买深度
	 * 
	 * @param tradeId
	 * @param num
	 * @return
	 */
	public JSONArray getBuyDepthJson(Integer tradeId, int num) {
		JSONArray jsonArray = BuyDepthJson.get(tradeId);
		JSONArray itemArray = new JSONArray();
		if(jsonArray == null){
			return itemArray;
		}
		int forCount = jsonArray.size() <= num ? jsonArray.size() : num;
		for (int i = 0; i < forCount; i++) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", i + 1);
			jsonObject.put("price", JSONArray.parseArray(jsonArray.get(i).toString()).get(0));
			jsonObject.put("amount", JSONArray.parseArray(jsonArray.get(i).toString()).get(1));
			itemArray.add(jsonObject);
		}
		return itemArray;
	}

	/**
	 * 获取卖深度
	 * 
	 * @param tradeId
	 * @return
	 */
	public JSONArray getSellDepthJson(Integer tradeId) {
		return SellDepthJson.get(tradeId);
	}

	/**
	 * 获取卖深度
	 * 
	 * @param tradeId
	 * @param num
	 * @return
	 */
	public JSONArray getSellDepthJson(Integer tradeId, int num) {
		JSONArray jsonArray = SellDepthJson.get(tradeId);
		JSONArray itemArray = new JSONArray();
		if(jsonArray == null){
			return itemArray;
		}
		int forCount = jsonArray.size() <= num ? jsonArray.size() : num;
		for (int i = 0; i < forCount; i++) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", i + 1);
			jsonObject.put("price", JSONArray.parseArray(jsonArray.get(i).toString()).get(0));
			jsonObject.put("amount", JSONArray.parseArray(jsonArray.get(i).toString()).get(1));
			itemArray.add(jsonObject);
		}
		return itemArray;
	}

	/**
	 * 获取最新成交
	 * 
	 * @param tradeId
	 * @return
	 */
	public JSONArray getSuccessJson(Integer tradeId) {
		return SuccessJson.get(tradeId);
	}

	/**
	 * 获取最新成交
	 * 
	 * @param tradeId
	 * @param num
	 * @return
	 */
	public JSONArray getSuccessJson(Integer tradeId, int num) {
		JSONArray jsonArray = SuccessJson.get(tradeId);
		JSONArray itemArray = new JSONArray();
		if(jsonArray == null){
			return itemArray;
		}
		int forCount = jsonArray.size() <= num ? jsonArray.size() : num;
		for (int i = 0; i < forCount; i++) {
			JSONObject jsonObject = new JSONObject();
			JSONArray listArray = JSONArray.parseArray(jsonArray.get(i).toString());
			jsonObject.put("price", listArray.get(0));
			jsonObject.put("amount", listArray.get(1));
			jsonObject.put("id", i + 1);
			jsonObject.put("time", listArray.get(2));
			int type = (int) listArray.get(3);
			jsonObject.put("en_type", type == 0 ? "bid" : "ask");
			jsonObject.put("type", type == 0 ? "买入" : "卖出");
			itemArray.add(jsonObject);
		}
		return itemArray;
	}

	/**
	 * 获取指定tid之后最新成交数据
	 *
	 * @param tradeId
	 * @param num
	 * @param tid
	 * @return
	 */
	public JSONArray getSuccessJson(Integer tradeId, int num, Integer tid) {
		JSONArray jsonArray = SuccessJson.get(tradeId);
		JSONArray itemArray = new JSONArray();
		if(jsonArray == null){
			return itemArray;
		}
		int forCount = jsonArray.size() <= (num + tid) ? jsonArray.size() : (num + tid);
		for (int i = tid; i < forCount; i++) {
			JSONObject jsonObject = new JSONObject();
			JSONArray listArray = JSONArray.parseArray(jsonArray.get(i).toString());
			jsonObject.put("price", listArray.get(0));
			jsonObject.put("amount", listArray.get(1));
			jsonObject.put("id", i + 1);
			jsonObject.put("time", listArray.get(2));
			int type = (int) listArray.get(3);
			jsonObject.put("en_type", type == 0 ? "bid" : "ask");
			jsonObject.put("type", type == 0 ? "买入" : "卖出");
			itemArray.add(jsonObject);
		}
		return itemArray;
	}

	/**
	 * 获取Kline
	 * 
	 * @param tradeId
	 * @param stepid
	 * @return
	 */
	public JSONArray getKlineJson(Integer tradeId, Integer stepid) {
		if (KlineJson == null) {
			return new JSONArray();
		}
		Map<Integer, JSONArray> tempMap = KlineJson.get(tradeId);
		if (tempMap != null) {
			return tempMap.get(stepid);
		}
		return new JSONArray();
	}

	/**
	 * 获取LastKline
	 * 
	 * @param tradeId
	 * @param stepid
	 * @return
	 */
	public JSONArray getLastKlineJson(Integer tradeId, Integer stepid) {
		if (lastKlineJson == null) {
			return new JSONArray();
		}
		Map<Integer, JSONArray> tempMap = lastKlineJson.get(tradeId);
		if (tempMap != null) {
			return tempMap.get(stepid);
		}
		return new JSONArray();
	}

	/**
	 * 获取IndexKline
	 * 
	 * @param tradeId
	 * @return
	 */
	public JSONArray getIndexKlineJson(Integer tradeId) {
		if (indexKlineJson == null) {
			return new JSONArray();
		}
		return indexKlineJson.get(tradeId);
	}
}

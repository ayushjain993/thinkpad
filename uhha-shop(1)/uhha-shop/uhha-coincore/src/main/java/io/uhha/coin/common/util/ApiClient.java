package io.uhha.coin.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import io.uhha.coin.common.dto.request.CreateOrderRequest;
import io.uhha.coin.common.dto.request.DepthRequest;
import io.uhha.coin.common.dto.response.*;
import okhttp3.*;
import okhttp3.OkHttpClient.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * API client.
 *
 * @Date 2018/1/14
 * @Time 16:02
 */
public class ApiClient {

	private Logger logger = LoggerFactory.getLogger(ApiClient.class);
	static final int CONN_TIMEOUT = 5;
	static final int READ_TIMEOUT = 5;
	static final int WRITE_TIMEOUT = 5;

	static final String API_HOST = "api.huobipro.com";

	static final String API_URL = "https://" + API_HOST;
	static final MediaType JSON = MediaType.parse("application/json");
	static final OkHttpClient client = createOkHttpClient();

	final String accessKeyId;
	final String accessKeySecret;
	final String assetPassword;

 	public static final String API_KEY = "****";
	public static final String API_SECRET = "****";

	/**
	 * 创建一个ApiClient实例
	 *
	 * @param accessKeyId     AccessKeyId
	 * @param accessKeySecret AccessKeySecret
	 */
	public ApiClient(String accessKeyId, String accessKeySecret) {
		this.accessKeyId = accessKeyId;
		this.accessKeySecret = accessKeySecret;
		this.assetPassword = null;
	}

	/**
	 * 创建一个ApiClient实例
	 *
	 * @param accessKeyId     AccessKeyId
	 * @param accessKeySecret AccessKeySecret
	 * @param assetPassword   AssetPassword
	 */
	public ApiClient(String accessKeyId, String accessKeySecret, String assetPassword) {
		this.accessKeyId = accessKeyId;
		this.accessKeySecret = accessKeySecret;
		this.assetPassword = assetPassword;
	}

	/**
	 * GET /market/history/kline 获取K线数据
	 *
	 * @param symbol
	 * @param period
	 * @param size
	 * @return
	 */
	public KlineResponse kline(String symbol, String period, String size) {
		HashMap map = new HashMap();
		map.put("symbol", symbol);
		map.put("period", period);
		map.put("size", size);
		map.put("AccessKeyId", accessKeyId);
		KlineResponse resp = get("/market/history/kline", map, new TypeReference<KlineResponse<List<Kline>>>() {
		});
		return resp;
	}

	/**
	 * GET /market/detail/merged 获取聚合行情(Ticker)
	 *
	 * @param symbol
	 * @return
	 */
	public MergedResponse merged(String symbol) {
		HashMap map = new HashMap();
		map.put("symbol", symbol);
		MergedResponse resp = get("/market/detail/merged", map, new TypeReference<MergedResponse<Merged>>() {
		});
		return resp;
	}

	/**
	 * GET /market/depth 获取 Market Depth 数据
	 *
	 * @param request
	 * @return
	 */
	public DepthResponse depth(DepthRequest request) {
		HashMap map = new HashMap();
		map.put("symbol", request.getSymbol());
		map.put("type", request.getType());

		DepthResponse resp = get("/market/depth", map, new TypeReference<DepthResponse<List<Depth>>>() {
		});
		return resp;
	}

	/**
	 * GET /market/history/trade 批量获取最近的交易记录
	 *
	 * @param symbol
	 * @param size
	 * @return
	 */
	public HistoryTradeResponse historyTrade(String symbol, String size) {
		HashMap map = new HashMap();
		map.put("symbol", symbol);
		map.put("size", size);
		HistoryTradeResponse resp = get("/market/history/trade", map, new TypeReference<HistoryTradeResponse>() {
		});
		return resp;
	}

	/**
	 * 下单
	 *
	 * @return Order id.
	 */
	public String placeOrder(CreateOrderRequest createOrderReq) {
		ApiResponse<String> resp = post("/v1/order/orders/place", createOrderReq,
				new TypeReference<ApiResponse<String>>() {
				});
		return resp.checkAndReturn();
	}

	/**
	 * GET /v1/order/orders/{order-id} 查询某个订单详情
	 *
	 * @param orderId
	 * @return
	 */
	public OrdersDetailResponse ordersDetail(String orderId) {
		OrdersDetailResponse resp = get("/v1/order/orders/" + orderId, null, new TypeReference<OrdersDetailResponse>() {
		});
		return resp;
	}

	/**
	 * POST v1/order/orders/{order-id}/submitcancel 申请撤销一个订单请求
	 *
	 * @param orderId
	 * @return
	 */
	public SubmitcancelResponse submitcancel(String orderId) {
		SubmitcancelResponse resp = post("/v1/order/orders/" + orderId + "/submitcancel", null, new TypeReference<SubmitcancelResponse>() {
		});
		return resp;
	}

	// send a GET request.
	<T> T get(String uri, Map<String, String> params, TypeReference<T> ref) {
		if (params == null) {
			params = new HashMap<>();
		}
		return call("GET", uri, null, params, ref);
	}

	// send a POST request.
	<T> T post(String uri, Object object, TypeReference<T> ref) {
		return call("POST", uri, object, new HashMap<String, String>(), ref);
	}

	// call api by endpoint.
	<T> T call(String method, String uri, Object object, Map<String, String> params,
			   TypeReference<T> ref) {
		ApiSignature sign = new ApiSignature();
		sign.createSignature(this.accessKeyId, this.accessKeySecret, method, API_HOST, uri, params);
		try {
			Request.Builder builder = null;
			if ("POST".equals(method)) {
				RequestBody body = RequestBody.create(JSON, JsonUtil.writeValue(object));
				builder = new Request.Builder().url(API_URL + uri + "?" + toQueryString(params)).post(body);
			} else {
				builder = new Request.Builder().url(API_URL + uri + "?" + toQueryString(params)).get();
			}
			if (this.assetPassword != null) {
				builder.addHeader("AuthData", authData());
			}
			Request request = builder.build();
			Response response = client.newCall(request).execute();
			if (response.code() != 200) {
				logger.warn("{} {} response code {}", method, uri, response.code());
				response.close();
				return null;
			}
			String s = response.body().string();
			return JsonUtil.readValue(s, ref);
		} catch (IOException e) {
			throw new ApiException(e);
		}
	}

	String authData() {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		md.update(this.assetPassword.getBytes(StandardCharsets.UTF_8));
		md.update("hello, moto".getBytes(StandardCharsets.UTF_8));
		Map<String, String> map = new HashMap<>();
		map.put("assetPwd", DatatypeConverter.printHexBinary(md.digest()).toLowerCase());
		try {
			return ApiSignature.urlEncode(JsonUtil.writeValue(map));
		} catch (IOException e) {
			throw new RuntimeException("Get json failed: " + e.getMessage());
		}
	}

	// Encode as "a=1&b=%20&c=&d=AAA"
	String toQueryString(Map<String, String> params) {
		return String.join("&", params.entrySet().stream().map((entry) -> {
			return entry.getKey() + "=" + ApiSignature.urlEncode(entry.getValue());
		}).collect(Collectors.toList()));
	}

	// create OkHttpClient:
	static OkHttpClient createOkHttpClient() {
		return new Builder().connectTimeout(CONN_TIMEOUT, TimeUnit.SECONDS)
				.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS).writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
				.build();
	}

}

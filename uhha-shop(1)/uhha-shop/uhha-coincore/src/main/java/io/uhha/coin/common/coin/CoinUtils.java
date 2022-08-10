package io.uhha.coin.common.coin;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
//import com.neemre.btcdcli4j.core.BitcoindException;
//import com.neemre.btcdcli4j.core.CommunicationException;
//import com.neemre.btcdcli4j.core.client.BtcdClient;
//import com.neemre.btcdcli4j.core.client.BtcdClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoinUtils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(CoinUtils.class);

	// 用户名
	private String accessKey = null;
	// 密码
	private String secretKey = null;
	// 钱包IP地址
	private String ip = null;
	// 端口
	private String port = null;
	// 加密
	// update
	private String HMAC_SHA1_ALGORITHM = "HmacMD5";
	// Wei
	private static final String ETHWEI = "1000000000000000000";

	public CoinUtils(String accessKey, String secretKey, String ip, String port) {
		this.accessKey = accessKey;
		this.secretKey = secretKey;
		this.ip = ip;
		this.port = port;
	}

	public CoinUtils(String ip, String port) {
		this.ip = ip;
		this.port = port;
	}

	public JSONObject go(String method, String condition) {
		String result = "";
		String tonce = "" + (System.currentTimeMillis() * 1000);
		String url = "http://" + accessKey + ":" + secretKey + "@" + ip + ":" + port;
		String params = "tonce=" + tonce.toString() + "&accesskey=" + accessKey + "&requestmethod=post&id=1&method="
				+ method + "&params=" + condition;
		int responseCode = 0;
		// 提供http连接的认证, 类似con.setRequestProperty("Authorization", "Basic
		// base64(accessKey:secretKey)");
		// BTC LTC
		authenticator();
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			String userpass = accessKey + ":" + getSignature(params, secretKey);
			String basicAuth = "Basic " + DatatypeConverter.printBase64Binary(userpass.getBytes());
			// add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("Json-Rpc-Tonce", tonce.toString());
			con.setRequestProperty("Authorization", basicAuth);
			String postdata = "{\"method\":\"" + method + "\", \"params\":" + condition + ", \"id\": 1}";
			// Send post request
			con.setDoOutput(true);

			try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
				wr.writeBytes(postdata);
				wr.flush();
			}
			responseCode = con.getResponseCode();

			try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
				result = in.readLine();
			}
		} catch (Exception e) {
			logger.error("Coin go failed:" + url + ",method=" + method + ",result=" + result + ",e=" + e.getMessage(),
					e);
			return JSON.parseObject("{\"result\":\"null\",\"error\":null,\"id\":1}");
		}
		if (responseCode != 200) {
			logger.error("Coin go responseCode not 200 : {}_{}", responseCode, result);
			return JSON.parseObject("{\"result\":\"null\",\"error\":" + responseCode + ",\"id\":1}");
		}
		return JSON.parseObject(result);
	}

	public JSONObject goETC4Https(String method, String params) {
		return goETC("https", method, params);
	}

	public JSONObject goETC(String method, String params) {
		return goETC("http", method, params);
	}

	private JSONObject goETC(String p, String method, String params) {
		try {
			String result = "";
			String url = p + "://" + ip + ":" + port;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-type", "application/json");
			String postdata = "{\"method\":\"" + method + "\", \"params\":" + params + ", \"id\": 1}";

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(postdata);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
		
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			inputLine = in.readLine();
			response.append(inputLine);
			in.close();
			result = response.toString();

			if (responseCode != 200) {
				logger.error("Etc etc failed : {}_{}", responseCode, result);
				return JSON.parseObject("{\"result\":\"null\",\"error\":" + responseCode + ",\"id\":1}");
			}

			return JSON.parseObject(result);
		} catch (Exception e) {
			e.printStackTrace();
			return JSON.parseObject("{\"result\":\"null\",\"error\":null,\"id\":1}");
		}
	}

	public JSONObject goGXC(String method, String params) {
		try {
			String result = "";
			String url = "http://" + ip + ":" + port;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-type", "application/json");

			String postdata = "{\"jsonrpc\": \"2.0\", \"method\":\"" +method+ "\", \"params\":[" +params+ "], \"id\": 1} ";

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(postdata);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			inputLine = in.readLine();
			response.append(inputLine);
			in.close();
			result = response.toString();

			if (responseCode != 200) {
				//logger.error("Etc etc failed : {}_{}", responseCode, result);
				return JSON.parseObject("{\"result\":\"null\",\"error\":" + responseCode + ",\"id\":1}");
			}

			return JSON.parseObject(result);
		} catch (Exception e) {
			e.printStackTrace();
			return JSON.parseObject("{\"result\":\"null\",\"error\":null,\"id\":1}");
		}
	}

	public Object goETP(String method, List params) {
		try {
			JsonRpcHttpClient client = new JsonRpcHttpClient(new URL("http://"+ip+":"+port+"/rpc/v2"));
			Object result = client.invoke(method, params,Object.class);

			return result;
		} catch (Exception e) {
			logger.warn("This page does not query the ETP recharge record -- 1");
			return JSON.parseObject("null");
		} catch (Throwable throwable) {
			logger.warn("This page does not query the ETP recharge record -- 2");
			return JSON.parseObject("null");
		}
	}

//	public BtcdClient goSynx() {
//		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
//		CloseableHttpClient httpProvider = HttpClients.custom().setConnectionManager(cm).build();
//		BtcdClient client = null;
//		try {
//			client = new BtcdClientImpl(httpProvider, "http", ip, Integer.valueOf(port), accessKey, secretKey);
//			return client;
//		} catch (BitcoindException e) {
//			e.printStackTrace();
//			return null;
//		} catch (CommunicationException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}

	private String getSignature(String data, String key) throws Exception {
		// get an hmac_sha1 key from the raw key bytes
		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);

		// get an hmac_sha1 Mac instance and initialize with the signing key
		Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
		mac.init(signingKey);

		// compute the hmac on input data bytes
		byte[] rawHmac = mac.doFinal(data.getBytes());
		return bytArrayToHex(rawHmac);
	}

	private String bytArrayToHex(byte[] a) {
		StringBuilder sb = new StringBuilder();
		for (byte b : a)
			sb.append(String.format("%02x", b & 0xff));
		return sb.toString();
	}

	// The easiest way to tell Java to use HTTP Basic authentication is to set a default Authenticator:
	private void authenticator() {
		Authenticator.setDefault(new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(accessKey, secretKey.toCharArray());
			}
		});
	}

	/**
	 * 大数:16进制转string
	 * @return
	 */
	public static String BigHexToString(String bigHex) {
		BigInteger bigInteger = new BigInteger(bigHex.substring(2), 16);
		return bigInteger.toString();
	}
	
	/**
	 * ETH余额:16进制 换算为10进制2位小数
	 * @param hexBalance
	 * @return
	 */
	public static String ETHBalanceHexToStr(String hexBalance) {
		BigInteger hexBalanceTmp = new BigInteger(hexBalance.substring(2), 16);
		BigDecimal balance = new BigDecimal(hexBalanceTmp.toString());
		BigDecimal balanceWei = new BigDecimal(ETHWEI);
		BigDecimal result = balance.divide(balanceWei,4,RoundingMode.DOWN);
		return result.toString();
	}

	/**
	 * ETH余额:10进制2位小数换算位16进制
	 * @param Balance
	 * @return
	 */
	public static String ToETHBalanceHex(String Balance) {
		BigDecimal balance = new BigDecimal(Balance);
		BigDecimal balanceWei = new BigDecimal(ETHWEI);
		BigInteger hexBalanceTmp = balance.multiply(balanceWei).toBigInteger();
		String resultStr = "0x" + hexBalanceTmp.toString(16);
		return resultStr;
	}
}

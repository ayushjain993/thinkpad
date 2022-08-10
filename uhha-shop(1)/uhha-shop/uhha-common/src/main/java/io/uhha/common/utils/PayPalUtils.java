package io.uhha.common.utils;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.uhha.common.utils.bean.OrderInfoAfterPay;
import io.uhha.common.utils.bean.OrderInfoForPay;
import io.uhha.common.utils.bean.PayPalSetting;
import io.uhha.common.utils.http.BasicAuthInterceptor;
import io.uhha.common.utils.http.OKHttp3ClientUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Paypal integration
 * https://developer.paypal.com/docs/checkout/advanced/integrate/
 *
 * 参考：https://www.cnblogs.com/bl123/p/13865458.html
 */
@Slf4j
public class PayPalUtils {

    private static Long lastUpdate = 0L;
    private static String accessToken = "";
    private static Long expiresIn = 0L;

    /**
     * PayPal 手机网页支付
     *
     * @param payPalSetting   PayPal设置
     * @param orderInfoForPay 订单信息
     * @return 前台页面请求需要的url
     */
    public static String wapPay(PayPalSetting payPalSetting, OrderInfoForPay orderInfoForPay){
        log.debug("PayPalPayUtils wapPay and payPalSetting:{} \r\n orderInfoForPay:{}  ", payPalSetting, orderInfoForPay);

        String accessToken ="";

        //1.获取paypal的token
        try {
            accessToken = getAccessToken(payPalSetting);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //2.创建订单
        Map<String, String> headerParams = new HashMap<String, String>();
        headerParams.put("Content-Type", "application/json");
        headerParams.put("Accept-language", "en_US");
        headerParams.put("Authorization", "Bearer "+accessToken);

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();//订单信息
        Map<String, Object> order = new HashMap<String, Object>();
        Map<String, String> orderInfo = new HashMap<String, String>();
        orderInfo.put("currency_code", orderInfoForPay.getCurrency());//支付货币
        orderInfo.put("value", orderInfoForPay.getPrice().toString());//支付金额
        order.put("reference_id",orderInfoForPay.getOrderCode());//订单编号，多个订单时使用
        order.put("amount", orderInfo);
        list.add(order);

        Map<String, String> redirects = new HashMap<String, String>();
        redirects.put("return_url", payPalSetting.getBeforeCallbackUrl());//付款成功返回地址

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("intent", "CAPTURE");//创建付款
        params.put("purchase_units", list);//订单信息
        params.put("application_context", redirects);//返回地址，无效的地址

        String strParams = JSON.toJSONString(params);

        OKHttp3ClientUtils okHttp3ClientUtils = OKHttp3ClientUtils.getInstance();
        String url = payPalSetting.getUrl()+"/v2/checkout/orders";

        //2.提交交易到paypal
        Response response = null;
        String jsonStr = null;

        try {
            log.debug("request body: \r\n[{}]", strParams);
            response = okHttp3ClientUtils.postData(url, headerParams, strParams);
            if (response == null || !response.isSuccessful()) {
                log.error("Paypal Payment server:{} \r\n response:{}.", url, response);
                return null;
            }
            jsonStr = response.body().string();

            log.debug("Paypal returns: {}", jsonStr);
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            if(jsonObject == null){
                log.error("broken response from paypal.");
                return "";
            }
            String status = jsonObject.getString("status");
            String orderId = jsonObject.getString("id");
            if("CREATED".equalsIgnoreCase(status) && StringUtils.isNotEmpty(orderId)){
                JSONArray jsonArray = jsonObject.getJSONArray("links");
                List<PayPalLink> links = jsonArray.toJavaList(PayPalLink.class);

                List<PayPalLink> approvedLinks = links.stream().filter(x->"approve".equalsIgnoreCase(x.getRel())).collect(Collectors.toList());
                if(CollectionUtils.isEmpty(approvedLinks)){
                    log.error("approved link from paypal is empty");
                    return "";
                }
                if(approvedLinks.size()!=1){
                    log.error("no approved link from paypal");
                    return "";
                }
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("orderId", orderId);
                hashMap.put("url",approvedLinks.get(0).getHref());
                return JSON.toJSONString(hashMap);
            }
            return "";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**执行paypal付款
     */
    public static String executePayment(PayPalSetting payPalSetting, String orderId) throws Exception {

        //1.获取paypal的token
        String accessToken ="";

        try {
            accessToken = getAccessToken(payPalSetting);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, String> headerParams = new HashMap<String, String>();
        headerParams.put("Content-Type", "application/json");
        headerParams.put("Authorization", "Bearer "+accessToken);

        OKHttp3ClientUtils okHttp3ClientUtils = OKHttp3ClientUtils.getInstance();

        //2.提交交易到paypal
        Response response = null;
        String jsonStr = null;
        try {
            String url = payPalSetting.getUrl()+"/v2/checkout/orders/"+orderId+"/capture";

            response = okHttp3ClientUtils.postData(url, headerParams, "");
            if (response == null || !response.isSuccessful()) {
                log.error("Paypal Payment server:{} \r\n response:{}.", url, response);
                return null;
            }
            jsonStr = response.body().string();
            log.debug("Paypal returns: {}", jsonStr);
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            JSONObject jsonObj = jsonObject.getJSONObject("data");

            if(!ObjectUtils.isEmpty(jsonObj.getString("statusCode")) && jsonObj.getString("statusCode").startsWith("2")){
                return jsonObj.getString("id");
            }else{
                log.error("paypal支付失败,token："+accessToken+",返回结果："+jsonStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**获取付款详情
     */
    public static String getPaymentDetails(PayPalSetting payPalSetting, String orderId) throws Exception {
        //1.获取paypal的token
        String accessToken ="";

        try {
            accessToken = getAccessToken(payPalSetting);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer "+accessToken);

        OKHttp3ClientUtils okHttp3ClientUtils = OKHttp3ClientUtils.getInstance();

        //2.执行查询
        Response response = null;
        String jsonStr = null;
        try {
            String url = payPalSetting.getUrl()+"/v2/checkout/orders/"+orderId;

            response = okHttp3ClientUtils.getData(url);
            if (response == null || !response.isSuccessful()) {
                log.error("Paypal Payment server:{} \r\n response:{}.", url, response);
                return null;
            }
            jsonStr = response.body().string();
            log.debug("Paypal returns: {}", jsonStr);
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            JSONObject jsonObj = jsonObject.getJSONObject("data");

            if(!ObjectUtils.isEmpty(jsonObj.getString("statusCode")) && jsonObj.getString("statusCode").startsWith("2")){
                return jsonObj.toString();
            }else{
                log.error("Fail to query paypal order.");
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 获取AccessToken
     * @return
     */
    private static String getAccessToken(PayPalSetting payPalSetting) throws IOException {
        log.debug("PayPalPayUtils getAccessToken and payPalSetting:{} ", payPalSetting);

        //access token依然有效
        Long timeStamp = Calendar.getInstance().getTimeInMillis();
        if(timeStamp - (lastUpdate+expiresIn*1000) <0){
            log.info("access token is still valid at timestamp={}.", timeStamp);
            return accessToken;
        }
        //记录当前时间戳
        lastUpdate = timeStamp;

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(payPalSetting.getClientId(), payPalSetting.getAppSecret()))
                .build();

        String url = payPalSetting.getUrl()+"/v1/oauth2/token";

        FormBody.Builder formEncodingBuilder = new FormBody.Builder();
        formEncodingBuilder.add("grant_type","client_credentials");
        RequestBody requestBody = formEncodingBuilder.build();
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        if (response == null || !response.isSuccessful()) {
            log.error("Paypal Payment server:{} \r\n response:{}.", url, response);
            return null;
        }
        String jsonStr = Objects.requireNonNull(response.body()).string();
        log.debug("Paypal returns: {}", jsonStr);
        JSONObject jsonObject = JSON.parseObject(jsonStr);

        accessToken = jsonObject.getString("access_token");
        expiresIn = jsonObject.getLong("expires_in");
        return accessToken;
    }

    /**
     * 生成客户端的token
     * @return
     */
    public static String generateClientToken(PayPalSetting payPalSetting) throws IOException {
        String accessToken = getAccessToken(payPalSetting);

        OKHttp3ClientUtils okHttp3ClientUtils = OKHttp3ClientUtils.getInstance();

        String url = payPalSetting.getUrl()+"/v1/identity/generate-token";

        String jsonStr;

        Map<String, String> headerParams = new HashMap<>();
        headerParams.put("Content-Type", "application/json");
        headerParams.put("Authorization", "Bear " + accessToken);
        headerParams.put("Accept-Language", "en_US");

        Response response = null;
        response = okHttp3ClientUtils.postData(url, headerParams, "");
        if (response == null || !response.isSuccessful()) {
            log.error("Paypal Payment server:{} \r\n response:{}.", url, response);
            return null;
        }
        jsonStr = Objects.requireNonNull(response.body()).string();
        log.debug("Paypal returns: {}", jsonStr);
        JSONObject jsonObject = JSON.parseObject(jsonStr);

        return jsonObject.getString("client_token");

    }

    /**
     * 验证返回信息
     *
     * @param requestMap 异步通知中收到的所有参数
     * @return 订单支付信息
     */
    public static OrderInfoAfterPay afterPayInfo(PayPalSetting pPayPalSetting, Map<String, String[]> requestMap) {
        Map<String, String> paramsMap = new HashMap<>();
        for (Iterator<?> iter = requestMap.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = requestMap.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            // valueStr = new String(valueStr.getBytes(ISO_8859_1), "gbk");
            paramsMap.put(name, valueStr);
        }

        log.debug("paramsMap:{}", paramsMap.toString());
        log.debug("payload:{}", paramsMap.get("payload"));
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(paramsMap);
        log.debug("PPayPalPayUtils afterPayInfo and pPayPalSetting:{}\r\n requestMap:{}", pPayPalSetting, jsonObject.toString());

        //decode JWT token
        // 1. verify JWT token
        String jwtReponse = "";
        if (jsonObject.containsKey("payload")) {
            jwtReponse = jsonObject.getString("payload");
        } else {
            log.error("error paying PayPal: {}", jsonObject.toString());
            return null;
        }

        OrderInfoAfterPay orderInfoAfterPay = new OrderInfoAfterPay();
        orderInfoAfterPay.setSuccess(false);

        return orderInfoAfterPay;
    }
}

@Data
class AccessTokenResponse{
    private String scope;
    private String accessToken;
    private String tokenType;
    private String appId;
    private Long expiresIn;
    private String nonce;
}

@Data
class PayPalLink{
    private String href;
    private String rel;
    private String method;
}
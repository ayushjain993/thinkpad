package io.uhha.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.uhha.common.utils.bean.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

@Slf4j
public class P2c2pPayUtils {
    /**
     * 2c2p网关（固定)
     */
//    private final static String PROD_PAY_URL = "https://pgw.2c2p.com";
    /**
     * 2c2p网关（沙箱)
     */
//    private final static String DEV_PAY_URL = "https://sandbox-pgw.2c2p.com";
//    private final static String PAY_URL = DEV_PAY_URL;

    /**
     * 2c2p 手机网页支付
     * https://developer.2c2p.com/recipes/generate-jwt-token
     *
     * @param p2c2pSetting    2c2p设置
     * @param orderInfoForPay 订单信息
     * @return 前台页面请求需要的完整form表单的html（包含自动提交脚本）
     */
    public static String wapPay(P2c2pSetting p2c2pSetting, OrderInfoForPay orderInfoForPay) {
        log.debug("P2c2pPayUtils wapPay and p2c2pSetting:{} \r\n orderInfoForPay:{}  ", p2c2pSetting, orderInfoForPay);

        String form = "";

        String token = "";

        List<String> paymentChannels = new ArrayList();
        paymentChannels.add("ALL");
        Map<String, Object> payload = new HashMap<>();
        payload.put("merchantId", p2c2pSetting.getMerchantId());
        payload.put("invoiceNo", orderInfoForPay.getOrderCode());
        payload.put("description", orderInfoForPay.getGoodsId() + "-" + orderInfoForPay.getGoodsName());
        payload.put("amount", orderInfoForPay.getPrice().doubleValue());
        payload.put("currencyCode", p2c2pSetting.getCurrencyCode());
        payload.put("paymentChannel", paymentChannels);
        payload.put("backendReturnUrl", p2c2pSetting.getBackCallbackUrl());
        payload.put("frontendReturnUrl", p2c2pSetting.getBeforeCallbackUrl());

        log.debug("payload={}", payload);

        try {
            Algorithm algorithm = Algorithm.HMAC256(p2c2pSetting.getSecretCode());

            token = JWT.create()
                    .withPayload(payload).sign(algorithm);

        } catch (JWTCreationException | IllegalArgumentException e) {
            //Invalid Signing configuration / Couldn't convert Claims.
            log.error(e.toString());
        }

        JSONObject requestData = new JSONObject();
        requestData.put("payload", token);

        try {
            String endpoint = p2c2pSetting.getUrl() + "/payment/4.1/PaymentToken";
            URL obj = new URL(endpoint);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/*+json");
            con.setRequestProperty("Accept", "text/plain");

            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(requestData.toString());
            log.debug("2c2p PaymentToken request:{}", requestData);
            wr.flush();
            wr.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            form = response.toString();
            in.close();
        } catch (Exception e) {
            log.error(e.toString());
        }
        log.debug("2c2p PaymentToken response form: {}", form);

        //decode JWT token
        // 1. verify JWT token
        JSONObject jsonObject = new JSONObject(form);
        String jwtReponse = "";
        if (jsonObject.has("payload")) {
            jwtReponse = jsonObject.getString("payload");
        } else {
            log.error("error paying 2c2p: {}", form);
            return jsonObject.toString();
        }

        Algorithm algorithm = Algorithm.HMAC256(p2c2pSetting.getSecretCode());
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        try {
            verifier.verify(jwtReponse);
            log.debug("JWT token verified");
        } catch (JWTCreationException ex) {
            log.error("JWT verification failed");
            return null;
        }

        //2.decode encoded payload
        try {
            DecodedJWT jwt = JWT.decode(jwtReponse);
            Map<String, Claim> responseData = jwt.getClaims();

            if (ObjectUtils.isEmpty(responseData)) {
                log.error("fail to decode jwt token");
                return null;
            }
            P2c2pTokenResponse p2c2pTokenResponse = P2c2pTokenResponse.builder()
                    .paymentToken(responseData.get("paymentToken").toString().replace("\"", ""))
                    .webPaymentUrl(responseData.get("webPaymentUrl").toString().replace("\"", ""))
                    .respCode(responseData.get("respCode").toString().replace("\"", ""))
                    .respDesc(responseData.get("respDesc").toString().replace("\"", ""))
                    .build();

            if (!"0000".equalsIgnoreCase(p2c2pTokenResponse.getRespCode())) {
                log.error("2c2p payment return error {}", p2c2pTokenResponse);
                return p2c2pTokenResponse.toString();
            }

            if (StringUtils.isEmpty(p2c2pTokenResponse.getWebPaymentUrl()) || StringUtils.isEmpty(p2c2pTokenResponse.getPaymentToken())) {
                log.error(" webPaymentUrl:{} or paymentToken:{} is empty.", p2c2pTokenResponse.getWebPaymentUrl(), p2c2pTokenResponse.getPaymentToken());
                return p2c2pTokenResponse.toString();
            }
            return p2c2pTokenResponse.getWebPaymentUrl();
        } catch (JWTDecodeException ex) {
            log.error("JWT decode failed");
            return null;
        }

    }

    /**
     * 验证返回信息
     *
     * @param requestMap 异步通知中收到的所有参数
     * @return 订单支付信息
     */
    public static OrderInfoAfterPay afterPayInfo(P2c2pSetting p2c2pSetting, Map<String, String[]> requestMap) {
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
        log.debug("P2c2pPayUtils afterPayInfo and p2c2pSetting:{}\r\n requestMap:{}", p2c2pSetting, jsonObject.toString());

        //decode JWT token
        // 1. verify JWT token
        String jwtReponse = "";
        if (jsonObject.containsKey("payload")) {
            jwtReponse = jsonObject.getString("payload");
        } else {
            log.error("error paying 2c2p: {}", jsonObject.toString());
            return null;
        }

        Algorithm algorithm = Algorithm.HMAC256(p2c2pSetting.getSecretCode());
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        verifier.verify(jwtReponse);
        log.debug("JWT token verified");

        //2.decode encoded payload
        DecodedJWT jwt = JWT.decode(jwtReponse);
        Map<String, Claim> responseData = jwt.getClaims();

        if (ObjectUtils.isEmpty(responseData)) {
            log.error("fail to decode jwt token");
            return null;
        }
        P2c2pPaymentResponse paymentResponse = P2c2pPaymentResponse.builder()
                .merchantID(responseData.get("merchantID").toString().replace("\"", ""))
                .invoiceNo(responseData.get("invoiceNo").toString().replace("\"", ""))
                .cardNo(responseData.get("cardNo").toString().replace("\"", ""))
                .amount(responseData.get("amount").toString().replace("\"", ""))
                .currencyCode(responseData.get("currencyCode").toString().replace("\"", ""))
                .tranRef(responseData.get("tranRef").toString().replace("\"", ""))
                .referenceNo(responseData.get("referenceNo").toString().replace("\"", ""))
                .approvalCode(responseData.get("approvalCode").toString().replace("\"", ""))
                .eci(responseData.get("eci").toString().replace("\"", ""))
                .transactionDateTime(responseData.get("transactionDateTime").toString().replace("\"", ""))
                .respCode(responseData.get("respCode").toString().replace("\"", ""))
                .respDesc(responseData.get("respDesc").toString().replace("\"", ""))
                .build();

        log.debug("paymentResponse = {}", paymentResponse);

        OrderInfoAfterPay orderInfoAfterPay = new OrderInfoAfterPay();
        orderInfoAfterPay.setSuccess(false);

        if ("0000".equalsIgnoreCase(paymentResponse.getRespCode())) {
            orderInfoAfterPay.setSuccess(true);
            orderInfoAfterPay.setOrderCode(paymentResponse.getReferenceNo());
            //TODO check this
            orderInfoAfterPay.setType(1);
            orderInfoAfterPay.setTransCode(paymentResponse.getTranRef());
        }

        return orderInfoAfterPay;
    }
}

package io.uhha.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.uhha.common.utils.bean.*;
import io.uhha.common.utils.http.OKHttp3ClientUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.commons.lang3.ObjectUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Slf4j
public class DLocalPayUtils {
    /**
     * Dlocal 手机网页支付
     * https://dlocal.gitbook.io/dlocal-china/api-dui-jie-wen-dang/payins-api-wen-dang/chuang-jian-fu-kuan/yin-hang-ka-fu-kuan
     *
     * @param dlocalSetting   dlocal设置
     * @param orderInfoForPay 订单信息
     * @return 前台页面请求需要的完整form表单的html（包含自动提交脚本）
     */
    public static String wapPay(DlocalSetting dlocalSetting, OrderInfoForPay orderInfoForPay) {
        log.debug("DlocalPayUtils wapPay and dlocalSetting:{} \r\n orderInfoForPay:{}", dlocalSetting, orderInfoForPay);

        //按照smartField要求加密cvv和卡号
        DLocalCreditCardObject cardObject = orderInfoForPay.getCard();
        String token = orderInfoForPay.getToken();
        DLocalPayerObject payer = orderInfoForPay.getDLocalPayerObject();
        cardObject.setSave(true);
        cardObject.setEncryptedData(token);

        //创建交易对象
        DLocalPaymentRequest paymentObject = DLocalPaymentRequest.builder()
                .payer(payer)
                .amount(orderInfoForPay.getPrice())
                .orderId(orderInfoForPay.getOrderCode())
                .currency(orderInfoForPay.getCurrency())
                .card(cardObject)
                .paymentMethodId("CARD")
                .paymentMethodFlow("DIRECT")
                .notificationUrl(dlocalSetting.getBackCallbackUrl())
                .build();
        String body = JSON.toJSONString(paymentObject);

        OKHttp3ClientUtils okHttp3ClientUtils = OKHttp3ClientUtils.getInstance();
        String url = dlocalSetting.getUrl() + "/payments";

        Map<String, String> headerParams = null;
        try {
            headerParams = composeHeaders(dlocalSetting, body);

            log.debug("payload body={}", body);

            Response response = okHttp3ClientUtils.postData(url, headerParams, body);

            if (response == null || !response.isSuccessful()) {
                log.error("DLocal Payment server:{} \r\n response:{}.", url, response);
                return null;
            }
            String jsonStr = Objects.requireNonNull(response.body()).string();
            DLocalPaymentResponse dLocalPaymentResponse = JSON.parseObject(jsonStr, DLocalPaymentResponse.class);

            if("200".equalsIgnoreCase(dLocalPaymentResponse.getStatus())){
                log.error("DLocal Payment server:{} \r\n dLocalPaymentResponse:{}.", url, dLocalPaymentResponse);
                return dLocalPaymentResponse.getNotificationUrl();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return "payment failed.";
    }

    /**
     * 验证返回信息
     *
     * @param requestMap 异步通知中收到的所有参数
     * @return 订单支付信息
     */
    public static OrderInfoAfterPay afterPayInfo(DlocalSetting dlocalSetting, Map<String, String[]> requestMap) {
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
        log.debug("DLocalPayUtils afterPayInfo and dlocalSetting:{}\r\n requestMap:{}", dlocalSetting, jsonObject.toString());

        //decode JWT token
        // 1. verify JWT token
        String jwtReponse = "";
        if (jsonObject.containsKey("payload")) {
            jwtReponse = jsonObject.getString("payload");
        } else {
            log.error("error paying Dlocal: {}", jsonObject.toString());
            return null;
        }

        Algorithm algorithm = Algorithm.HMAC256(dlocalSetting.getSecretKey());
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

    /**
     * PayIn api的签名方法
     * 签名方法需使用 SHA256 进行 HMAC 运算，签名Header需要包含版本前缀：包括签名版本和使用的hash算法，目前，我们使用：V2-HMAC-SHA256。
     * <p>
     * https://dlocal.gitbook.io/dlocal-china/api-dui-jie-wen-dang/payins-api-wen-dang/fu-kuan-an-quan
     *
     * @param dlocalSetting
     * @param body
     * @return
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    private static String signatureCalculator(DlocalSetting dlocalSetting, String body) throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        final String HMAC_ALGORITHM = "HmacSHA256";
        final String CHARSET = "UTF-8";

        String xLogin = dlocalSetting.getXLogin();
        String secretKey = dlocalSetting.getSecretKey();

        // Create byte array with the required data for the signature.
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        bout.write(xLogin.getBytes(CHARSET));
        bout.write(DateUtils.dateTimeNowISO8601().getBytes(CHARSET));
        bout.write(body.getBytes(CHARSET));

        // Calculate the signature.
        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(), HMAC_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        mac.init(signingKey);
        byte[] signature = mac.doFinal(bout.toByteArray());

        // Create a String with the signature value.
        Formatter formatter = new Formatter();
        for (byte b : signature) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }


    /**
     * 使用官方接口查询支付类型
     * https://api.dlocal.com/payment-methods
     *
     * @param dlocalSetting dlocal 设置
     * @param countryCode   两位国家码，从FCountry中获取
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public DLocalPaymentMethod getPaymentMethods(DlocalSetting dlocalSetting, String countryCode) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        String url = dlocalSetting.getUrl() + "/payments_methods";

        Map<String, String> headerParams = composeHeaders(dlocalSetting, "");
        Map<String, String> reqParams = new HashMap<>();
        reqParams.put("country", countryCode);

        OKHttp3ClientUtils okHttp3ClientUtils = OKHttp3ClientUtils.getInstance();
        Response response = okHttp3ClientUtils.getData(url, headerParams);

        if (response == null) {
            log.error("DLocal Payment server:{} return null.", url);
            return null;
        }

        if (response.isSuccessful()) {
            String jsonStr = Objects.requireNonNull(response.body()).string();
            return JSON.parseObject(jsonStr, DLocalPaymentMethod.class);
        } else {
            log.error("DLocal Payment server response failure.\r\n response: {}", response);
            return null;
        }
    }

    /**
     * 构造DLocal请求的headers
     *
     * @param dlocalSetting
     * @param body
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    private static Map<String, String> composeHeaders(DlocalSetting dlocalSetting, String body) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put("Content-Type", "application/*+json");
        headerParams.put("Accept", "text/plain");
        headerParams.put("xLogin", dlocalSetting.getXLogin());
        headerParams.put("xTransKey", dlocalSetting.getXTransKey());
        headerParams.put("X-Version", "2.1");
        headerParams.put("User-Agent", "UHHA / 1.0");
        headerParams.put("Authorization", signatureCalculator(dlocalSetting, body));
        return headerParams;
    }

    public static void main(String[] args) {
        DLocalPayUtils dLocalPayUtils = new DLocalPayUtils();
        DlocalSetting dlocalSetting = DlocalSetting.builder()
                .xLogin("123")
                .xTransKey("456")
                .currency("USD")
                .secretKey("666")
                .build();
        try {
            dLocalPayUtils.getPaymentMethods(dlocalSetting,"CN");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

}

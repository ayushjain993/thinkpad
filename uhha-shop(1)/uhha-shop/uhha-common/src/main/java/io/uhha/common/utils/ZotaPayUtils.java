package io.uhha.common.utils;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.uhha.common.utils.bean.*;
import io.uhha.common.utils.http.OKHttp3ClientUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Slf4j
public class ZotaPayUtils {

    /**
     * ZotaPay deposit
     * https://docs.zotapay.com/deposit/1.0/#introduction
     *
     * @param zotaPaySetting  zotapay设置
     * @param orderInfoForPay 订单信息
     * @return Zotapay的支付订单orderId
     */
    public static String depositPay(ZotaPaySetting zotaPaySetting, OrderInfoForPay orderInfoForPay) {
        log.debug("ZotaPayPayUtils depositPay and zotaPaySetting:{} \r\n orderInfoForPay:{}", zotaPaySetting, orderInfoForPay);

        OKHttp3ClientUtils okHttp3ClientUtils = OKHttp3ClientUtils.getInstance();
        ZotaPayPayerObject payer = orderInfoForPay.getZotaPayPayerObject();
        String currency = orderInfoForPay.getCurrency();
        String endpointId = getEndpointID(zotaPaySetting, currency);
        String url = zotaPaySetting.getUrl() + "/api/v1/deposit/request/" + endpointId+"/";

        String signature = depositRequestSignCalculator(zotaPaySetting,
                orderInfoForPay.getOrderCode(),
                currency,
                orderInfoForPay.getPrice(),
                payer.getCustomerEmail());

        //自定义参数填入支付类型，支付类型 1:订单支付 2:预存款充值(必传)
        ZotaPayCustomParam param = ZotaPayCustomParam.builder()
                .type(Integer.toString(orderInfoForPay.getType()))
                .build();

        //创建DeposityPay交易对象
        ZotaDepositPayRequest paymentObject = ZotaDepositPayRequest.builder()
                .merchantOrderID(orderInfoForPay.getOrderCode())
                .merchantOrderDesc(orderInfoForPay.getGoodsName())
                .orderAmount(orderInfoForPay.getPrice().toString())
                .orderCurrency(orderInfoForPay.getCurrency())
                .customerEmail(payer.getCustomerEmail())
                .customerFirstName(payer.getCustomerFirstName())
                .customerLastName(payer.getCustomerLastName())
                .customerAddress(payer.getCustomerAddress())
                .customerCountryCode(payer.getCustomerCountryCode())
                .customerCity(payer.getCustomerCity())
                .customerState(payer.getCustomerState())
                .customerZipCode(payer.getCustomerZipCode())
                .customerPhone(payer.getCustomerPhone())
                .customerBankCode(payer.getCustomerBankCode())
                .customerIP(payer.getCustomerIP())
                .callbackUrl(zotaPaySetting.getBackCallbackUrl())
                .redirectUrl(zotaPaySetting.getBeforeCallbackUrl())
                .checkoutUrl("http://uhha.io:8500")
                .customParam(JSON.toJSONString(param))
                .signature(signature)
                .build();
        String body = JSON.toJSONString(paymentObject);

        log.debug("payload body={}", body);

        try {
            Response response = null;
            String jsonStr = null;
            response = okHttp3ClientUtils.postData(url, body);
            if (response == null) {
                log.error("ZotaPay Payment server:{} \r\n response:{}.", url, response);
                return null;
            }
            jsonStr = Objects.requireNonNull(response.body()).string();
            log.debug("ZotaPay returns: {}", jsonStr);
            ZotaPayResponse zotaPayResponse = JSON.parseObject(jsonStr, ZotaPayResponse.class);

            if ("200".equalsIgnoreCase(zotaPayResponse.getCode())) {
                log.info("Zotapay Payment server:{} \r\n ZotaPayResponse:{}.", url, zotaPayResponse);
                JSONObject jsonObject = (JSONObject)zotaPayResponse.getData();
                return jsonObject.getString("depositUrl");
            }

            if("409".equalsIgnoreCase(zotaPayResponse.getCode())){
                log.info("Zotapay Payment server:{} \r\n ZotaPayResponse:{}.", url, zotaPayResponse);
                log.info("order already created, resume payment!");
                JSONObject jsonObject = (JSONObject)zotaPayResponse.getData();
                return jsonObject.getString("url");
            }else{
                log.warn("not handled response code");
                log.warn("Zotapay Payment server:{} \r\n ZotaPayResponse:{}.", url, zotaPayResponse);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        return "";
    }

    /**
     * ZotaPay Payout
     * https://docs.zotapay.com/payout/1.0/#introduction
     *
     * @param zotaPaySetting  zotapay设置
     * @param orderInfoForPay 订单信息
     * @return Zotapay的支付订单orderId
     */
    public static String payout(ZotaPaySetting zotaPaySetting, OrderInfoForPay orderInfoForPay) {
        log.debug("ZotaPayPayUtils payout and zotaPaySetting:{} \r\n orderInfoForPay:{}", zotaPaySetting, orderInfoForPay);

        OKHttp3ClientUtils okHttp3ClientUtils = OKHttp3ClientUtils.getInstance();
        ZotaPayPayerObject payer = orderInfoForPay.getZotaPayPayerObject();
        String currency = orderInfoForPay.getCurrency();
        String endpointId = getEndpointID(zotaPaySetting, currency);
        String url = zotaPaySetting.getUrl() + "/api/v1/payout/request/" + endpointId+"/";

        String signature = payoutRequestSignCalculator(zotaPaySetting,
                orderInfoForPay.getOrderCode(),
                currency,
                orderInfoForPay.getPrice(),
                payer.getCustomerEmail(),
                payer.getCustomerBankAccountNumber());

        //自定义参数填入支付类型，支付类型 1:订单支付 2:预存款充值(必传)
        ZotaPayCustomParam param = ZotaPayCustomParam.builder()
                .type(Integer.toString(orderInfoForPay.getType()))
                .build();

        //创建PAYOUT交易对象
        ZotaPayoutRequest paymentObject = ZotaPayoutRequest.builder()
                .merchantOrderID(orderInfoForPay.getOrderCode())
                .merchantOrderDesc(orderInfoForPay.getGoodsName())
                .orderAmount(orderInfoForPay.getPrice().toString())
                .orderCurrency(orderInfoForPay.getCurrency())
                .customerEmail(payer.getCustomerEmail())
                .customerFirstName(payer.getCustomerFirstName())
                .customerLastName(payer.getCustomerLastName())
                .customerPhone(payer.getCustomerPhone())
                .customerAddress(payer.getCustomerEmail())
                .customerBankCode(payer.getCustomerBankCode())
                .customerBankAccountNumber(payer.getCustomerBankAccountNumber())
                .customerBankAccountName(payer.getCustomerBankAccountName())
                .customerIP(payer.getCustomerIP())
                .callbackUrl(zotaPaySetting.getBackCallbackUrl())
                .customParam(JSON.toJSONString(param))
                .signature(signature)
                .build();
        String body = JSON.toJSONString(paymentObject);

        log.debug("payload body={}", body);

        try {
            Response response = null;
            String jsonStr = null;
            response = okHttp3ClientUtils.postData(url, body);
            if (response == null || !response.isSuccessful()) {
                log.error("ZotaPay Payment server:{} \r\n response:{}.", url, response);
                return null;
            }
            jsonStr = Objects.requireNonNull(response.body()).string();
            log.debug("ZotaPay returns: {}", jsonStr);
            ZotaPayResponse zotaPayResponse = JSON.parseObject(jsonStr, ZotaPayResponse.class);

            if ("200".equalsIgnoreCase(zotaPayResponse.getCode())) {
                log.info("Zotapay Payment server:{} \r\n ZotaPayResponse:{}.", url, zotaPayResponse);
                JSONObject jsonObject = (JSONObject)zotaPayResponse.getData();
                ZotaPayPayoutResponseData responseData = ZotaPayPayoutResponseData.builder()
                        .merchantOrderID(jsonObject.getString("merchantOrderID"))
                        .orderID(jsonObject.getString("orderID"))
                        .build();
                log.info("ZotaPayPayoutResponseData:{}",responseData);
                return JSON.toJSONString(responseData);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        return "payment failed.";
    }

    /**
     * 验证返回信息
     *
     * @param notification 异步通知中收到的所有参数
     * @return 订单支付信息
     */
    public static OrderInfoAfterPay afterPayInfo(ZotaPaySetting zotaPaySetting, ZotaPayCallbackNotification notification) {

        log.debug("ZotaPayUtils afterPayInfo and zotaPaySetting:{}\r\n requestMap:{}", zotaPaySetting, notification);

        String signature = null;
        //对支付平台，电商平台是店铺
        String orderID = notification.getMerchantOrderID();
        String providedSign = notification.getSignature();
        String processorTransactionID = notification.getProcessorTransactionID();
        String strCustomParam = notification.getCustomParam();

        JSONObject jsonObject = JSON.parseObject(strCustomParam);
        String type = jsonObject.getString("type");

        signature = payoutNotificationSignCalculator(zotaPaySetting, notification);

        OrderInfoAfterPay orderInfoAfterPay = new OrderInfoAfterPay();
        //校验SHA256签名
        if (!signature.equals(providedSign)) {
            log.error("signature validation failed!");
            orderInfoAfterPay.setSuccess(false);
        } else {
            orderInfoAfterPay.setSuccess(true);
            orderInfoAfterPay.setOrderCode(orderID);
            orderInfoAfterPay.setType(Integer.parseInt(type));
            orderInfoAfterPay.setTransCode(processorTransactionID);
        }
        return orderInfoAfterPay;
    }

    /**
     * 通过orderID和merchantOrderID查询订单状态
     *
     * @param orderID
     * @param merchantOrderID
     * @return
     */
    public static ZotaPayQueryResponseData queryOrderStatus(ZotaPaySetting zotaPaySetting, String orderID, String merchantOrderID) {
        log.debug("ZotaPayPayUtils queryOrderStatus and zotaPaySetting:{} \r\n orderID:{}  \r\n merchantOrderID:{}", zotaPaySetting, orderID, merchantOrderID);

        OKHttp3ClientUtils okHttp3ClientUtils = OKHttp3ClientUtils.getInstance();
        String url = zotaPaySetting.getUrl() + "/api/v1/query/order-status/";
        String timestamp = Long.toString(Calendar.getInstance().getTimeInMillis());

        String signature = queryOrderStatusSignCalculator(zotaPaySetting, merchantOrderID, orderID,timestamp);


        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("merchantID", zotaPaySetting.getMerchantID());
        requestParams.put("orderID",orderID);
        requestParams.put("merchantOrderID",merchantOrderID);
        requestParams.put("timestamp", timestamp);
        requestParams.put("signature", signature);

        Response response;
        String jsonStr;
        try {
            response = okHttp3ClientUtils.getData(url, requestParams, null);
            if (response == null || !response.isSuccessful()) {
                log.error("ZotaPay Payment server:{} \r\n response:{}.", url, response);
                ZotaPayQueryResponseData responseData = ZotaPayQueryResponseData.builder()
                        .status("failed")
                        .orderID(orderID)
                        .merchantOrderID(merchantOrderID)
                        .build();
                return responseData;
            }
            jsonStr = Objects.requireNonNull(response.body()).string();

            ZotaPayResponse zotaPayResponse = JSON.parseObject(jsonStr, ZotaPayResponse.class);
            JSONObject jsonObject = (JSONObject) zotaPayResponse.getData();

            ZotaPayQueryResponseData responseData = ZotaPayQueryResponseData.builder()
                    .orderID(jsonObject.getString("orderID"))
                    .amount(jsonObject.getString("amount"))
                    .merchantOrderID(jsonObject.getString("merchantOrderID"))
                    .type(jsonObject.getString("type"))
                    .customerEmail(jsonObject.getString("customerEmail"))
                    .currency(jsonObject.getString("currency"))
                    .status(jsonObject.getString("status"))
                    .customParam(jsonObject.getString("customParam"))
                    .build();
            return responseData;

        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        ZotaPayQueryResponseData responseData = ZotaPayQueryResponseData.builder()
                .status("failed")
                .orderID(orderID)
                .merchantOrderID(merchantOrderID)
                .errorMessage("timeout")
                .build();
        return responseData;
    }

    /**
     * deposity request Zotapay的签名方法，使用SHA256 checksum
     * https://docs.zotapay.com/deposit/1.0/?shell#authentication
     *
     * @param zotaPaySetting
     * @param merchantOrderID
     * @param orderAmount
     * @param customerEmail
     * @return
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    private static String depositRequestSignCalculator(ZotaPaySetting zotaPaySetting, String merchantOrderID, String currency,BigDecimal orderAmount, String customerEmail) {

        String merchantSecretKey = zotaPaySetting.getMerchantSecretKey();
        String endpointID = getEndpointID(zotaPaySetting, currency);

        String body = endpointID + merchantOrderID + orderAmount.toString() + customerEmail + merchantSecretKey;
        String sha256hash = SecureUtil.sha256(body);
        return sha256hash.toLowerCase();
    }


    /**
     * Payout request 的Zotapay的签名方法，使用SHA256 checksum
     * https://docs.zotapay.com/deposit/1.0/?shell#authentication
     *
     * @param zotaPaySetting
     * @param merchantOrderID
     * @param orderAmount
     * @param customerEmail
     * @return
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    private static String payoutRequestSignCalculator(ZotaPaySetting zotaPaySetting, String merchantOrderID, String currency,BigDecimal orderAmount, String customerEmail, String customerBankAccountNumber) {

        String merchantSecretKey = zotaPaySetting.getMerchantSecretKey();
        String endpointID = getEndpointID(zotaPaySetting,currency);

        String body = endpointID + merchantOrderID + orderAmount.toString() + customerEmail + customerBankAccountNumber + merchantSecretKey;
        String sha256hash = SecureUtil.sha256(body);
        return sha256hash.toLowerCase();
    }

    /**
     * Payout notification 的Zotapay的签名方法，使用SHA256 checksum
     * https://docs.zotapay.com/payout/1.0/#callback-notification
     *
     * @param zotaPaySetting
     * @param notification
     * @return
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    private static String payoutNotificationSignCalculator(ZotaPaySetting zotaPaySetting, ZotaPayCallbackNotification notification) {

        String orderID = notification.getOrderID();
        String merchantOrderID = notification.getMerchantOrderID();
        String status = notification.getStatus();
        String currency = notification.getCurrency();
        String amount = notification.getAmount();
        String customerEmail = notification.getCustomerEmail();

        String merchantSecretKey = zotaPaySetting.getMerchantSecretKey();
        String endpointID = getEndpointID(zotaPaySetting, currency);

        String body = endpointID + orderID + merchantOrderID + status + amount + customerEmail + merchantSecretKey;
        String sha256hash = SecureUtil.sha256(body);
        return sha256hash.toLowerCase();
    }

    private static String queryOrderStatusSignCalculator(ZotaPaySetting zotaPaySetting, String merchantOrderID, String orderId, String timestamp) {

        String merchantSecretKey = zotaPaySetting.getMerchantSecretKey();

        String body = zotaPaySetting.getMerchantID()+merchantOrderID + orderId + timestamp + merchantSecretKey;
        String sha256hash = SecureUtil.sha256(body);
        return sha256hash.toLowerCase();
    }

    /**
     * 通过币种获取endpointID
     * @param zotaPaySetting
     * @param currency
     * @return
     */
    private static String getEndpointID(ZotaPaySetting zotaPaySetting, String currency){
        switch (currency){
            case "CNY":
                return zotaPaySetting.getCnyEndpointID();
            case "USD":
                return zotaPaySetting.getUsdEndpointID();
            case "EUR":
                return zotaPaySetting.getEurEndpointID();
            case "JPY":
                return zotaPaySetting.getJpyEndpointID();
            case "MYR":
                return zotaPaySetting.getMyrEndpointID();
            default:
                return "Not Configured endpoint!";
        }
    }


}

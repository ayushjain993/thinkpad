package io.uhha.coin.common.coin.driver;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.uhha.coin.common.coin.CoinDriver;
import io.uhha.coin.common.coin.CoinUtils;
import io.uhha.coin.common.coin.TxInfo;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * BTCDriver
 *
 * @author jany
 */
public class BTCDriver implements CoinDriver {

    private CoinUtils coinUtils = null;
    private String passWord = null;
    private Integer coinSort = null;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public BTCDriver(String accessKey, String secretKey, String ip, String port, String pass, Integer coinSort) {
        coinUtils = new CoinUtils(accessKey, secretKey, ip, port);
        this.passWord = pass;
        this.coinSort = coinSort;
    }

    @Override
    public BigDecimal getBalance() {
        JSONObject resultJson = coinUtils.go("getbalance", "[]");
        String result = resultJson.get("result").toString();
        if (result.equals("null")) {
            return null;
        }
        return new BigDecimal(result);
    }

    @Override
    public String getNewAddress(String uId) {
        JSONObject resultJson = coinUtils.go("getnewaddress", "[\"" + uId + "\"]");
        String result = resultJson.get("result").toString();
        if (result.equals("null")) {
            return null;
        }
        return result;
    }

    //BTCZ生成地址
    public String getNewAddress() {
        JSONObject resultJson = coinUtils.go("getnewaddress", null);
        String result = resultJson.get("result").toString();
        if (result.equals("null")) {
            return null;
        }
        return result;
    }

    @Override
    public void walletLock() {
        if (passWord == null || passWord.length() <= 0) {
            return;
        }
        coinUtils.go("walletlock", "[]");
    }

    @Override
    public void walletPassPhrase(int times) {
        if (passWord == null || passWord.length() <= 0) {
            return;
        }
        // 将密钥存储在缓存中X秒
        coinUtils.go("walletpassphrase", "[\"" + passWord + "\"," + times + "]");
    }

    @Override
    public boolean setTxFee(BigDecimal fee) {
        JSONObject resultJson = coinUtils.go("settxfee", "[" + fee + "]");
        String result = resultJson.get("result").toString();
        if (result.equals("null")) {
            return false;
        }
        return true;
    }

    @Override
    public List<TxInfo> listTransactions(int count, int from) {
    	// 增加是否包括监听地址
        JSONObject resultJson = coinUtils.go("listtransactions", "[\"*\"," + count + "," + from + ",true]");
        Object result = resultJson.get("result");

        if (result == null || !(result instanceof JSONArray)) {
        	return null;
        }
        JSONArray jsonArray = JSONArray.class.cast(result);
        List<TxInfo> txInfos = new ArrayList<TxInfo>();

        for (Object object : jsonArray) {
        	if (!(object instanceof JSONObject)) {
        		continue;
        	}
            JSONObject txObject = JSONObject.class.cast(object);
            Object category = txObject.get("category");

            if (!"receive".equals(category)) {
                continue;
            }
            TxInfo txInfo = new TxInfo();
            Object accout = txObject.get("account");
            txInfo.setAccount(accout == null ? String.valueOf(txObject.get("label")) : accout.toString());
            // 约定确保有地址值
            txInfo.setAddress(txObject.get("address").toString());
            // 约定确保有金额值
            txInfo.setAmount(new BigDecimal(txObject.get("amount").toString()));
            // 收款
            txInfo.setCategory(category.toString());
            // 确认数
            String confirmations = String.valueOf(txObject.get("confirmations")).trim();
            txInfo.setConfirmations(NumberUtils.toInt(confirmations, 0));
            // 
            txInfo.setVout(txObject.getInteger("vout"));
            // 交易时间
            long time = NumberUtils.toLong(String.valueOf(txObject.get("time")).trim() + "000", 0);
            txInfo.setTime(new Date(time));
            // 约定确保有ID值
            txInfo.setTxid(txObject.get("txid").toString());
            txInfos.add(txInfo);
        }
//        Collections.reverse(txInfos);
        return txInfos;
    }

    @Override
    public TxInfo getTransaction(String txId) {
        JSONObject json = coinUtils.go("gettransaction", "[\"" + txId + "\"]");
        Object result = json.get("result");

        if (result == null || !(result instanceof JSONObject)) {
        	return null;
        }
        JSONObject resultJson = JSONObject.class.cast(result);
        TxInfo txInfo = new TxInfo();
        // 临时再用
        result = resultJson.get("details");

        if (result != null && result instanceof JSONArray) {
        	JSONArray detailsArray = JSONArray.class.cast(result);

        	 for (Object object : detailsArray) {
        		if (!(object instanceof JSONObject)) {
             		continue;
             	}
                JSONObject detailsObject = JSONObject.class.cast(object);

                if (!"receive".equals(detailsObject.get("category"))) {
                    continue;
                }
                Object accout = detailsObject.get("account");
                txInfo.setAccount(accout == null ? String.valueOf(detailsObject.get("label")) : accout.toString());
                txInfo.setAddress(detailsObject.get("address").toString());
                txInfo.setAmount(new BigDecimal(detailsObject.get("amount").toString()));
                break;
            }
       }
        txInfo.setCategory("receive");
        // 确认数
        String confirmations = String.valueOf(resultJson.get("confirmations")).trim();
        txInfo.setConfirmations(NumberUtils.toInt(confirmations, 0));
        // 交易时间
        long time = NumberUtils.toLong(String.valueOf(resultJson.get("time")).trim() + "000", 0);
        txInfo.setTime(new Date(time));
        // 约定确保有ID值
        txInfo.setTxid(resultJson.get("txid").toString());
        return txInfo;
    }

    @Override
    public String sendToAddress(String address, BigDecimal account, String comment, BigDecimal fee) {
        if (fee.compareTo(BigDecimal.ZERO) == 1 && !setTxFee(fee)) {
            logger.error("set fee error");
            return null;
        }

        walletPassPhrase(30);
        // 
        JSONObject resultJson = coinUtils.go("sendtoaddress", "[\"" + address + "\"," + account + "," + "\"" + comment + "\"]");
        walletLock();

        String result = resultJson.get("result").toString();
        if (result.equals("null")) {
            logger.error("resultJson null {}",resultJson.toString());
            return null;
        }
        return result;
    }

    @Override
    public String sendToAddress(String to, String amount) {
        return null;
    }

    @Override
    public Integer getCoinSort() {
        return this.coinSort;
    }
}

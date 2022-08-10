package io.uhha.coin.common.coin.driver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.uhha.coin.common.coin.CoinDriver;
import io.uhha.coin.common.coin.CoinUtils;
import io.uhha.coin.common.coin.TxInfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * BTCDriver
 *
 * @author hf
 */
public class USDTDriver implements CoinDriver {

    private CoinUtils coinUtils = null;
    private String passWord = null;
    private Integer coinSort = null;
    private String USDTMAINACCOUNT="***";

    public USDTDriver(String accessKey, String secretKey, String ip, String port, String pass, Integer coinSort) {
        coinUtils = new CoinUtils(accessKey, secretKey, ip, port);
        this.passWord = pass;
        this.coinSort = coinSort;
    }

    @Override
    public BigDecimal getBalance() {
        JSONObject resultJson = coinUtils.go("omni_getbalance", "[\"" + USDTMAINACCOUNT + "\"," + 31 + "]");
        String result = resultJson.get("result").toString();
        if (result.equals("null")) {
            return null;
        }
        String balance = JSON.parseObject(result).getString("balance").toString();
        return new BigDecimal(balance);
    }

    public BigDecimal getBalance(String address,Integer type) {
        JSONObject resultJson = null;
        if (type == 1){
            resultJson = coinUtils.go("omni_getbalance", "[\"" + address + "\"," + 31 + "]");
        }else if (type == 2){
            resultJson = coinUtils.go("getbalance", "[\"" + address + "\"]");
        }
        String result = null;
        try {
            result = resultJson.get("result").toString();
            if (result.equals("null")) {
                return null;
            }
            if (type == 1){
                result = JSON.parseObject(result).getString("balance").toString();
            }
        } catch (Exception e){
            e.printStackTrace();
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
        JSONObject resultJson = coinUtils.go("omni_listtransactions", "[\"*\"," + count + "," + from + "]");
        String result = resultJson.get("result").toString();
        if (result.equals("null")) {
            return null;
        }
        List<TxInfo> txInfos = new ArrayList<TxInfo>();
        JSONArray jsonArray = JSONArray.parseArray(result);
        for (Object object : jsonArray) {
            JSONObject txObject = JSON.parseObject(object.toString());
            if (!"true".equals(txObject.get("valid").toString())){
                continue;
            }
            TxInfo txInfo = new TxInfo();
            //txInfo.setAccount(txObject.get("account").toString());
            txInfo.setAddress(txObject.get("referenceaddress").toString());
            txInfo.setBlockNumber(txObject.getInteger("block"));
            txInfo.setAmount(new BigDecimal(txObject.get("amount").toString()));
            txInfo.setFrom(txObject.get("sendingaddress").toString());
            txInfo.setTo(txObject.get("referenceaddress").toString());
            //txInfo.setCategory("receive");
            if (txObject.get("confirmations") != null && txObject.get("confirmations").toString().trim().length() > 0) {
                txInfo.setConfirmations(Integer.parseInt(txObject.get("confirmations").toString()));
            } else {
                txInfo.setConfirmations(0);
            }
            //txInfo.setVout(txObject.getInteger("vout"));
            long time = Long.parseLong(txObject.get("blocktime").toString());
            txInfo.setTime(new Date(time));
            txInfo.setTxid(txObject.get("txid").toString());
            txInfos.add(txInfo);
        }
        Collections.reverse(txInfos);
        return txInfos;
    }

    @Override
    public TxInfo getTransaction(String txId) {
        JSONObject json = coinUtils.go("gettransaction", "[\"" + txId + "\"]");
        String result = json.get("result").toString();
        if (result.equals("null")) {
            return null;
        }
        JSONObject resultJson = json.getJSONObject("result");
        JSONArray detailsArray = JSONArray.parseArray(resultJson.get("details").toString());
        TxInfo txInfo = new TxInfo();
        for (Object object : detailsArray) {
            JSONObject detailsObject = JSON.parseObject(object.toString());
            if (!detailsObject.get("category").toString().equals("receive")) {
                continue;
            }
            txInfo.setAccount(detailsObject.get("account").toString());
            txInfo.setAddress(detailsObject.get("address").toString());
            txInfo.setAmount(new BigDecimal(detailsObject.get("amount").toString()));
            break;
        }
        txInfo.setCategory("receive");
        if (resultJson.get("confirmations") != null && resultJson.get("confirmations").toString().trim().length() > 0) {
            txInfo.setConfirmations(Integer.parseInt(resultJson.get("confirmations").toString()));
        } else {
            txInfo.setConfirmations(0);
        }
        long time = Long.parseLong(resultJson.get("time").toString());
        txInfo.setTime(new Date(time));
        txInfo.setTxid(resultJson.get("txid").toString());
        return txInfo;
    }

    @Override
    public String sendToAddress(String address, BigDecimal account, String comment, BigDecimal fee) {
        walletPassPhrase(30);
        JSONObject resultJson = coinUtils.go("omni_send", "[\"" + USDTMAINACCOUNT + "\",\"" + address + "\"," + 31 +"," + "\"" + account + "\"]");
        walletLock();

        String result = resultJson.get("result").toString();
        if (result.equals("null")) {
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


    public String sendToAddress(String from ,String to,BigDecimal amount){
//        if (fee.compareTo(BigDecimal.ZERO) == 1 && !setTxFee(fee)) {
//            return null;
//        }
        walletPassPhrase(30);
        /*BigDecimal UNIT = new BigDecimal(100000000);
        amount = amount.multiply(UNIT);*/
        JSONObject resultJson = coinUtils.go("omni_send", "[\"" + from + "\",\"" + to + "\"," + 31 +"," + "\"" + amount + "\"]");
        walletLock();

        String result = resultJson.get("result").toString();
        if (result.equals("null")) {
            return null;
        }
        return result;
    }

    public String sendToAddress(String to,BigDecimal amount,String comment){
        walletPassPhrase(30);
        JSONObject resultJson = coinUtils.go("sendtoaddress", "[\"" + to + "\"," + amount + "," + "\"" + comment + "\"]");
        walletLock();

        String result = resultJson.get("result").toString();
        if (result.equals("null")) {
            return null;
        }
        return result;
    }
}

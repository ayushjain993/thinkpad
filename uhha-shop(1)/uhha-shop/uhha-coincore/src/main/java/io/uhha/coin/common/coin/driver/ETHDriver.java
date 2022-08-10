package io.uhha.coin.common.coin.driver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.uhha.coin.common.coin.CoinDriver;
import io.uhha.coin.common.coin.CoinUtils;
import io.uhha.coin.common.coin.ETHUtils;
import io.uhha.coin.common.coin.TxInfo;
import org.web3j.tx.ChainId;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class ETHDriver implements CoinDriver {

    private CoinUtils coinUtils = null;
    private String keystore = null;
    private Integer coinSort = null;
    private String account = null;
    private String passWord = "***";
    private ETHUtils ethUtils = null;

    public ETHDriver(String ip, String port, Integer coinSort, String keystore, String account,String secretKey) {
        coinUtils = new CoinUtils(ip, port);
        ethUtils = new ETHUtils(account,keystore, secretKey);
        this.keystore = keystore;
        this.coinSort = coinSort;
        this.account = account;
        this.passWord = secretKey;
    }

    public ETHDriver(String ip, String port, Integer coinSort, String account, String privateKey) {
        coinUtils = new CoinUtils(ip, port);
        ethUtils = new ETHUtils(account, privateKey);
        this.keystore = "";
        this.passWord = "";
        this.coinSort = coinSort;
        this.account = account;
    }

    private static String methodStr(String... params) {
        StringBuffer strBuffer = new StringBuffer();
        for (int i = 0; i < params.length; i++) {
            if (i == 0) {
                strBuffer.append("[\"" + params[i] + "\"");
            } else {
                strBuffer.append(",\"" + params[i] + "\"");
            }
            if (i == params.length - 1) {
                strBuffer.append("]");
            }
        }
        return strBuffer.toString();
    }

    private static String methodStrJson(String... params) {
        StringBuffer strBuffer = new StringBuffer();
        for (int i = 0; i < params.length; i++) {
            if (i == 0) {
                strBuffer.append("[" + params[i] + "");
            } else {
                strBuffer.append(",\"" + params[i] + "\"");
            }
            if (i == params.length - 1) {
                strBuffer.append("]");
            }
        }
        return strBuffer.toString();
    }

    /**
     * 查询以太坊测试余额方法
     * @param address
     * @return
     */
    public BigDecimal getEthBalance(String address){
        return ethUtils.getBalance(address);
    }

    /**
     * 查询以太坊代币余额
     * @param address
     * @param contractAddress
     * @return
     */
    public BigDecimal getEthTokenBalance(String address,String contractAddress){
        BigDecimal balance = new BigDecimal(-1);
        try{
            balance=  ethUtils.getERC20Balance(address,contractAddress);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  balance;
    }

    @Override
    public BigDecimal getBalance() {
        JSONObject resultJson = coinUtils.goETC("eth_getBalance", methodStr(account, "latest"));
        String result = resultJson.get("result").toString();
        if (result.equals("null")) {
            return null;
        }
        String balance = CoinUtils.ETHBalanceHexToStr(result);
        return new BigDecimal(balance);
    }

    @Override
    public String getNewAddress(String uId) {
        JSONObject resultJson = coinUtils.goETC("personal_newAccount", methodStr(passWord));
        String result = resultJson.get("result").toString();
        if (result.equals("null")) {
            return null;
        }
        return result;
    }

    @Override
    public String sendToAddress(String to, String amount) {
        BigDecimal vol = new BigDecimal(amount);
        return ethUtils.transfer(account,to, vol, ChainId.ROPSTEN);
    }

    private Long getHeight() {
        JSONObject resultJson = coinUtils.goETC("eth_getBlock", methodStr("latest"));
        String result = resultJson.get("result").toString();
        if (result.equals("null")) {
            return null;
        }
        System.out.println(result);
        JSONObject res =  JSON.parseObject(result);
        Long height = res.getLong("number");

        return height;
    }

    public String subscribeEvents(List<String> addresses){
        JSONObject resultJson = coinUtils.goETC("eth_newFilter", methodStr("13308155","latest", Arrays.toString(addresses.toArray())));
        String result = resultJson.get("result").toString();
        if (result.equals("null")) {
            return null;
        }
        System.out.println(result);
        return null;
    }

    public BigDecimal getBalance(String address) {
        return ethUtils.getBalance(address);
    }

    public String sendToAddress(String to, String amount, String tokenAddress) throws ExecutionException, InterruptedException {
//        String txId;
//        Map<String, String> params = new HashMap<>();
//        params.put("to", to);
//        params.put("password", password);
//        params.put("amount", amount);
//        params.put("tokenAddress", tokenAddress);
//        txId = HttpPoolingManager.getInstance().post(ethApiReqUrl + "erc20sendTransaction", HttpSSLType.NONE, params);
//        return txId;
        return ethUtils.transferERC20Token(account,to, new BigInteger(amount), tokenAddress);
    }

    public TxInfo getETCTransactionByHash(String txHash) {
        JSONObject json = coinUtils.goETC("eth_getTransactionByHash", methodStr(txHash));
        String result = json.get("result").toString();
        if (result.equals("null")) {
            return null;
        }
        JSONObject resultJson = json.getJSONObject("result");
        String blockNumberStr = CoinUtils.BigHexToString(resultJson.get("blockNumber").toString());
        String amount = CoinUtils.ETHBalanceHexToStr(resultJson.get("value").toString());
        TxInfo txinfo = new TxInfo();
        txinfo.setFrom(resultJson.get("from").toString());
        txinfo.setTo(resultJson.get("to").toString());
        txinfo.setBlockNumber(Integer.parseInt(blockNumberStr));
        txinfo.setAmount(new BigDecimal(amount));
        return txinfo;
    }


    public Integer getETCBlockNumber() throws IOException {
    	// 使用HTTPS
//        JSONObject resultJson = coinUtils.goETC4Https("eth_blockNumber", "[]");
//        String result = resultJson.get("result").toString();
//        if (result.equals("null")) {
//            return null;
//        }
//        String blockNumberStr = CoinUtils.BigHexToString(resultJson.get("result").toString());
//        return Integer.parseInt(blockNumberStr);
        BigInteger block = ethUtils.getLatestBlockNumber();
        return Integer.parseInt(block.toString());
    }

//    public Integer getETCBlockNumber() {
//        JSONObject resultJson = coinUtils.goETC("eth_blockNumber", "[]");
//        String result = resultJson.get("result").toString();
//        if (result.equals("null")) {
//            return null;
//        }
//        String blockNumberStr = CoinUtils.BigHexToString(resultJson.get("result").toString());
//        return Integer.parseInt(blockNumberStr);
//    }

    public String getETCSHA3(String str) {
        JSONObject resultJson = coinUtils.goETC("web3_sha3", methodStr(str));
        String result = resultJson.get("result").toString();
        if (result.equals("null")) {
            return null;
        }
        return result;
    }

    @Override
    public void walletLock() {

    }

    @Override
    public void walletPassPhrase(int times) {

    }

    @Override
    public boolean setTxFee(BigDecimal fee) {
        return false;
    }

    @Override
    public List<TxInfo> listTransactions(int count, int from) {
        return null;
    }

    @Override
    public TxInfo getTransaction(String txId) {
        return null;
    }

    @Override
    public String sendToAddress(String address, BigDecimal account, String comment, BigDecimal fee) {
        return null;
    }

    @Override
    public Integer getCoinSort() {
        return this.coinSort;
    }

    public String getAccount() {
        return account;
    }
}

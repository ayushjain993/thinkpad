package io.uhha.coin.common.coin;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.uhha.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ChainId;
import org.web3j.tx.Contract;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * <p>备选的web3j操作ETH节点，也可请使用CoinUtils</p>
 *
 * @see <a href="https://github.com/ethjava/web3j-sample/tree/master/src/main/java/com/ethjava">sample one</a>
 * @see <a href="https://gitee.com/yuxin123/web3j-eth/blob/master/src/main/java/cn/coin/ox/test/TestGetBalance.java">sample two</a>
 * @see CoinUtils
 */
@Slf4j
public class ETHUtils {
    private static Web3j web3j;
    private static Admin admin;

    private static final BigDecimal WEI = new BigDecimal(1000000);
    private static final BigDecimal defaultGasPrice     = BigDecimal.valueOf(5);
    private static final String TRANSFER = "transfer";

    /**
     * 发送所用地址
     */
    private String fromAddr = "0xD4a7D7dfBf8cDeCe34788ccd4a63491C2047EcAe";

    /**
     * 默认的keystore为测试所用
     */
    private String keystore = "{\"address\":\"d4a7d7dfbf8cdece34788ccd4a63491c2047ecae\",\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"2370753ed3763e679e1d669f84c858824df2554a4fbb1432a327babb0c66017e\",\"cipherparams\":{\"iv\":\"f8dfcdc6d339e143dfc0e0a913e66424\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":262144,\"p\":1,\"r\":8,\"salt\":\"bb2fa9dcc77a282fc0cdbc2cc8174c113ec95f0bda99c21e7b8d13944e3edc70\"},\"mac\":\"4a22bdd0f2180ffd41af38112eacd5acf3eaa53ba5b2be7d7ffd16ccd1393c9b\"},\"id\":\"b3dc17da-6e5a-4928-a38a-aee9aecfb817\",\"version\":3}";

    /**
     *    keystore密码
     */
    private String password = "hAD6cXxRel2j6WN";

    /**
     * 私钥
     */
    private String privateKey = "";

    /**
     * 链id
     */
    private final byte chainId = ChainId.ROPSTEN;

//    private final String projectId = "ffcbd630d4e244ef984e3a0df3596d45";
//    private final String projectSecret = "176c2104b45045908cf2a5983d9bd2e4";
    private final String infuraRopstenUrl = "https://ropsten.infura.io/v3/ffcbd630d4e244ef984e3a0df3596d45";
    private final String infuraMainnetUrl = "https://mainnet.infura.io/v3/ffcbd630d4e244ef984e3a0df3596d45";
    private final String localNodeUrl = "http://localhost:8545";


    /**
     * 如果同时提供keystore和privateKey，优先使用keystore
     *
     * @param fromAddr
     * @param keystore
     * @param password
     */
    public ETHUtils(String fromAddr, String keystore, String password) {
        web3j = Web3j.build(new HttpService(infuraRopstenUrl));
        admin = Admin.build(new HttpService(localNodeUrl));

        if (StringUtils.isEmpty(fromAddr) || StringUtils.isEmpty(password)
                || (StringUtils.isEmpty(keystore) && (StringUtils.isEmpty(privateKey)))) {
            log.warn("fromaddr or password is empty orkeystore is empty. But you still can use this for query ETH network info. use default constructor.");
        } else {
            this.fromAddr = fromAddr;
            this.keystore = keystore;
            this.password = password;
        }

        if (StringUtils.isEmpty(keystore)) {
            //获取私钥
            this.privateKey = decryptWallet(password);
        }

    }

    /**
     * 如果同时提供keystore和privateKey，优先使用keystore
     *
     * @param fromAddr
     * @param privateKey
     */
    public ETHUtils(String fromAddr, String privateKey) {
        web3j = Web3j.build(new HttpService(infuraRopstenUrl));
        admin = Admin.build(new HttpService(localNodeUrl));

        if (StringUtils.isEmpty(fromAddr) || (StringUtils.isEmpty(keystore) && (StringUtils.isEmpty(privateKey)))) {
            log.warn("fromaddr or privateKey is empty. But you still can use this for query ETH network info. use default constructor.");
        } else {
            this.fromAddr = fromAddr;
            this.privateKey = privateKey;
            this.password = "";
            this.keystore = "";
        }
    }


    public ETHUtils() {
        log.warn("fromaddr,keystore or password is empty. You can't send transactions.");
        web3j = Web3j.build(new HttpService(infuraRopstenUrl));
        admin = Admin.build(new HttpService(localNodeUrl));
        this.fromAddr = "";
        this.keystore = "";
        this.privateKey = "";
        this.password = "";
    }

    /**
     * 获取版本信息
     *
     * @return 版本信息
     */
    public String getVersion() throws IOException {
        Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
        return web3ClientVersion.getWeb3ClientVersion();
    }


    /**
     * 创建ETH账户
     *
     * @param password 密码
     * @return 生成的地址
     */
    public String createNewAccount(String password) {
        try {
            NewAccountIdentifier newAccountIdentifier = admin.personalNewAccount(password).send();
            String address = newAccountIdentifier.getAccountId();
            return address;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取ERC-20 token指定地址余额
     *
     * @param address         查询地址
     * @param contractAddress 合约地址
     * @return BigDecimal ERC20余额
     * @throws InterruptedException
     */
    public BigDecimal getERC20Balance(String address, String contractAddress) throws ExecutionException, InterruptedException {
        String methodName = "balanceOf";
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        Address fromAddress = new Address(address);
        inputParameters.add(fromAddress);

        TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
        };
        outputParameters.add(typeReference);
        Function function = new Function(methodName, inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(address, contractAddress, data);

        EthCall ethCall;
        BigDecimal balanceValue = BigDecimal.ZERO;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            if (results != null && results.size() > 0) {
//                balanceValue = Convert.fromWei(new BigDecimal(String.valueOf(results.get(0).getValue())), Convert.Unit.ETHER);
                balanceValue = Convert.fromWei(new BigDecimal(String.valueOf(results.get(0).getValue())), Convert.Unit.ETHER);
                balanceValue = balanceValue.multiply(new BigDecimal("100"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return balanceValue;
    }


    /**
     * 获取以太坊ETH的余额
     *
     * @param address 地址
     * @return 余额 ETHER
     */
    public BigDecimal getBalance(String address) {
        try {
            EthGetBalance ethGetBalanceWei = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
            return Convert.fromWei(new BigDecimal(ethGetBalanceWei.getBalance()), Convert.Unit.ETHER);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 转账以太坊ETH
     * refer https://kauri.io/#communities/Java%20Ethereum/manage-an-ethereum-account-with-java-and-web3j/
     *
     * @param fromAddr 源地址
     * @param toAddr   目标地址
     * @param amount   数量
     * @return 交易hash 失败返回null
     */
    public String transfer(String fromAddr, String toAddr, BigDecimal amount, byte chainId) {

        BigInteger value = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();
        String txHash = null;
        try {
            //获取Nonce值
            BigInteger nonce = getNonce(fromAddr);

            //获取gasLimit
            Transaction transaction = Transaction.createEtherTransaction(fromAddr, null, null, null, toAddr, value);
            //获取gasLimit
            BigInteger gasLimit = getTransactionGasLimit(transaction);
            // A transfer cost 21,000 units of gas
//            BigInteger gasLimit = BigInteger.valueOf(21000);
            //该值为大部分矿工可接受的gasPrice
            BigInteger gasPrice = getGasPrice();
            //1.1倍的gas price
            gasPrice = gasPrice.add(gasPrice.divide(new BigInteger("10")));
            // I am willing to pay 1Gwei (1,000,000,000 wei or 0.000000001 ether) for each unit of gas consumed by the transaction.
//            BigInteger gasPrice = Convert.toWei("1", Convert.Unit.GWEI).toBigInteger();

            log.info("nonce={},gasPrice={},gasLimit={},amount={}", nonce.toString(10), gasPrice.toString(10),
                    gasLimit.toString(10), value);
            log.info("amount of is : {}GWei", value);
            log.info("txfee of is :  {}GWei", gasPrice.multiply(gasLimit));
            // gas limit * gas price + value
            BigInteger total = value.add(gasPrice.multiply(gasLimit));
            log.info("to be transferred is : {}", total);

            // 查询调用者余额，检测余额是否充足
            BigDecimal ethBalance = getBalance(fromAddr);
            assert ethBalance != null;
            BigInteger balance = Convert.toWei(ethBalance, Convert.Unit.ETHER).toBigInteger();
            log.info("balance of {} is {} GWei", fromAddr, balance);

            if (balance.compareTo(total) < 0) {
                throw new RuntimeException("手续费不足，请核实");
            }

            //签名Transaction，这里要对交易做签名
            String signedData = signTransaction(nonce, gasPrice, gasLimit, toAddr, value, "", chainId, privateKey);

            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(signedData).send();
            if (ethSendTransaction.hasError()) {
                log.error(ethSendTransaction.getError().getMessage());
                return ethSendTransaction.getError().getMessage();
            }
            txHash = ethSendTransaction.getTransactionHash();

            log.info("txHash:{}", txHash);
        } catch (IOException e) {
            log.error(e.toString());
            e.printStackTrace();
        }
        return txHash;
    }

    /**
     * 发送ETH
     * @param toAddr
     * @param amount
     */
    public String transto(String toAddr, BigDecimal amount) {
        //创建credentials
        if (privateKey.startsWith("0x")) {
            privateKey = privateKey.substring(2);
        }
        ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(privateKey, 16));
        Credentials credentials = Credentials.create(ecKeyPair);

        TransactionReceipt send = null;
        try {
            send = Transfer.sendFunds(web3j, credentials, toAddr, amount, Convert.Unit.KWEI).sendAsync().get();

            log.info("Transaction complete:");
            log.info("trans hash=" + send.getTransactionHash());
            log.info("from :" + send.getFrom());
            log.info("to:" + send.getTo());
            log.info("gas used=" + send.getGasUsed());
            log.info("status: " + send.getStatus());
            return send.getTransactionHash();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransactionException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 转账代币 方法2
     *
     * @param fromAddr     源地址
     * @param toAddr       目标地址
     * @param contractAddr 合约地址
     * @param amount       数量
     * @return 交易hash 失败返回null
     */
    public String transferToken(String fromAddr, String toAddr, String contractAddr, BigDecimal amount) throws ExecutionException, InterruptedException {

        BigInteger nonce = getNonce(fromAddr);
        // 构建方法调用信息
        String method = "transfer";

        // 构建输入参数
        List<Type> inputArgs = new ArrayList<>();
        inputArgs.add(new Address(toAddr));
        inputArgs.add(new Uint256(amount.multiply(BigDecimal.TEN.pow(18)).toBigInteger()));

        // 合约返回值容器
        List<TypeReference<?>> outputArgs = new ArrayList<>();

        String funcABI = FunctionEncoder.encode(new Function(method, inputArgs, outputArgs));

        //加载转账所需的凭证，用私钥
        //获取nonce，交易笔数
        //get gasPrice  Gas Price就是你愿意为一个单位的Gas出多少ether，一般用Gwei作单位。
        BigInteger gasPrice = getGasPrice().divide(new BigInteger(String.valueOf(6)));
//        BigInteger gasLimit = Contract.GAS_LIMIT;
//        BigInteger value = Convert.toWei("0.001", Convert.Unit.ETHER).toBigInteger();
//        System.out.println(gasLimit);
        BigInteger gasLimit = Contract.GAS_LIMIT;
        //以太坊的交易手续费为：TxFee = gas * gas Price，单位Gwei

        Transaction transaction = Transaction.createFunctionCallTransaction(fromAddr, nonce, gasPrice, null, contractAddr, funcABI);
//        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, null, contractAddr, null, funcABI);

        // 预计手续费
        try {
            gasLimit = getTransactionGasLimit(transaction);
        } catch (Exception e) {
            log.error("---预计手续费失败,错误信息:" + e.getMessage());
        }
        log.info("------------本次交易预计手续费: " + gasLimit);
        //
        BigDecimal tokenBalance = getERC20Balance(fromAddr, contractAddr);

        // 查询调用者余额，检测余额是否充足
        BigDecimal ethBalance = getBalance(fromAddr);
        assert ethBalance != null;
        BigInteger balance = Convert.toWei(ethBalance, Convert.Unit.ETHER).toBigInteger();

        if (balance.compareTo(gasLimit) < 0) {
            throw new RuntimeException("手续费不足，请核实");
        }
        if (tokenBalance.compareTo(amount) < 0) {
            throw new RuntimeException("代币不足，请核实");
        }

        //创建credentials
        if (privateKey.startsWith("0x")) {
            privateKey = privateKey.substring(2);
        }
        ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(privateKey, 16));
        Credentials credentials = Credentials.create(ecKeyPair);

        return signAndSend(nonce, gasPrice, gasLimit, contractAddr, BigInteger.ZERO, funcABI, chainId, credentials);
    }


    /**
     * 转账代币 方法1
     *
     * @param from            转账账号
     * @param to              收账账号
     * @param amount          额度
     * @param contractAddress 合约地址
     * @return 交易hash 失败返回null
     */
    public String transferERC20Token(String from, String to, BigInteger amount, String contractAddress) throws ExecutionException, InterruptedException {
        //加载转账所需的凭证，用私钥
        //获取nonce，交易笔数
        BigInteger nonce = getNonce(from);
        //get gasPrice  Gas Price就是你愿意为一个单位的Gas出多少ether，一般用Gwei作单位。
        // 19000000000
        BigInteger gasPrice = getGasPrice().divide(BigInteger.valueOf(8));
//        BigInteger gasLimit = BigInteger.valueOf(Constants.GAS_LIMIT);
        // Gas Limit就是一次交易中gas的可用上限
        BigInteger gasLimit = Contract.GAS_LIMIT;
        //以太坊的交易手续费为：TxFee = gas * gas Price，单位Gwei
        // amount 加上 gas price 和 gaslimit 的乘积然后和余额进行比较

        //创建RawTransaction交易对象
        Function function = tranFunction(to, amount);
        String encodedFunction = FunctionEncoder.encode(function);
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit,
                contractAddress, encodedFunction);

        //创建credentials
        if (privateKey.startsWith("0x")) {
            privateKey = privateKey.substring(2);
        }
        ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(privateKey, 16));
        Credentials credentials = Credentials.create(ecKeyPair);

        //签名Transaction，这里要对交易做签名
        byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, chainId,credentials);
        String hexValue = Numeric.toHexString(signMessage);
        //发送交易
//        EthSendTransaction ethSendTransaction = sendTransaction(web3j, hexValue);
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        String hash = ethSendTransaction.getTransactionHash();
        if (hash != null) {
            System.err.println("交易成功: " + hash);
            return hash;
        } else {
            System.err.println("交易出错: " + ethSendTransaction.getError().getMessage());
        }
        return null;
    }


    /**
     * 对交易签名，并发送交易
     *
     * @param nonce
     * @param gasPrice
     * @param gasLimit
     * @param to
     * @param value
     * @param data
     * @return
     */
    protected String signAndSend(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to,
                               BigInteger value, String data, byte chainId, Credentials credentials) {
        String txHash = "";
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, to, value, data);

        byte[] signMessage;
        if (chainId > ChainId.NONE) {
            signMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
        } else {
            signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        }

        String signData = Numeric.toHexString(signMessage);
        if (!"".equals(signData)) {
            try {
                EthSendTransaction send = web3j.ethSendRawTransaction(signData).send();
                txHash = send.getTransactionHash();
                System.out.println("txHash:---> " + txHash);
            } catch (IOException e) {
                throw new RuntimeException("交易异常");
            }
        }
        return txHash;
    }

    /**
     * 估算手续费上限
     *
     * @param transaction
     * @return
     */
    protected BigInteger getTransactionGasLimit(Transaction transaction) {
        try {
            EthEstimateGas ethEstimateGas = web3j.ethEstimateGas(transaction).send();
            if (ethEstimateGas.hasError()) {
                throw new RuntimeException(ethEstimateGas.getError().getMessage());
            }
            return ethEstimateGas.getAmountUsed();
        } catch (IOException e) {
            throw new RuntimeException("net error");
        }
    }


    /**
     * 获取nonce
     *
     * @param from 转账地址
     * @return BigInteger类型nonce
     */
    protected BigInteger getNonce(String from) {
        EthGetTransactionCount transactionCount;
        BigInteger nonce = null;
        try {
            transactionCount = web3j.ethGetTransactionCount(from, DefaultBlockParameterName.LATEST).sendAsync().get();
            nonce = transactionCount.getTransactionCount();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Web3j --> ethGetTransactionCount:" + e.getCause().getMessage());
        }
        return nonce;
    }

    /**
     * 获取gas
     *
     * @return BigInteger 当前gas
     */
    protected BigInteger getGasPrice() {
        BigInteger gas = null;
        try {
            EthGasPrice ethGasPrice = web3j.ethGasPrice().sendAsync().get();
            if (ethGasPrice != null) {
                gas = ethGasPrice.getGasPrice();
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("Web3j --> ethGasPrice:" + e.getCause().getMessage());
        }
        return gas;
    }

    /**
     * 合约方法
     *
     * @param to    收账地址
     * @param value 转账额
     * @return Function
     */
    private Function tranFunction(String to, BigInteger value) {
        Function function = null;
        try {
            function = new Function(
                    TRANSFER,
                    Arrays.asList(new Address(to), new Uint256(value)),
                    Collections.singletonList(new TypeReference<Type>() {
                    }));
        } catch (Exception e) {
            log.error(e.toString());
            e.printStackTrace();
        }

        return function;
    }

    /**
     * 发送交易
     *
     * @param hexValue hex
     * @return 交易
     */
    private EthSendTransaction sendTransaction(String hexValue) {
        EthSendTransaction transaction = null;
        try {
            transaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
            if (transaction.hasError()) {
                log.error("Web3j 发送交易 --> ethSendRawTransaction:" + transaction.getError().getMessage());
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("Web3j 发送交易 --> ethSendRawTransaction:" + e.getMessage());
        }
        return transaction;
    }

    /**
     * 解密keystore 得到私钥
     */
    private String decryptWallet(String password) {
        String privateKey = null;
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        try {
            WalletFile walletFile = objectMapper.readValue(this.keystore, WalletFile.class);
            ECKeyPair ecKeyPair = null;
            ecKeyPair = Wallet.decrypt(password, walletFile);
            privateKey = ecKeyPair.getPrivateKey().toString(16);
        } catch (CipherException e) {
            if ("Invalid password provided".equals(e.getMessage())) {
                log.error("密码错误");
            }
            log.error(e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            log.error(e.toString());
            e.printStackTrace();
        }
        return privateKey;
    }

    /**
     * 签名交易
     */
    private String signTransaction(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to,
                                   BigInteger value, String data, byte chainId, String privateKey) throws IOException {
        byte[] signedMessage;
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                gasPrice,
                gasLimit,
                to,
                value,
                data);

        if (privateKey.startsWith("0x")) {
            privateKey = privateKey.substring(2);
        }
        ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(privateKey, 16));
        Credentials credentials = Credentials.create(ecKeyPair);
        if (chainId > ChainId.NONE) {
            signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
        } else {
            signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        }

        return Numeric.toHexString(signedMessage);
    }

    public BigInteger getLatestBlockNumber() throws IOException {
        EthBlockNumber blockNumber = web3j.ethBlockNumber().send();
        return blockNumber.getBlockNumber();
    }

    public Optional<TransactionReceipt> getTransactionReceipt(String txHash) throws IOException {
        EthGetTransactionReceipt ethGetTransactionReceiptResp = web3j.ethGetTransactionReceipt(txHash).send();
        Optional<TransactionReceipt> transactionReceipt = ethGetTransactionReceiptResp.getTransactionReceipt();
        return transactionReceipt;
    }

    public static void main(String[] args) throws Exception {
        test2();
//        check();
    }

    public static void test1() {
        // UHHA CONTRACT
        final String UHHA_CONTRACT_ADDR = "0xa5502EB978282c1e33C4fd9241301EE93939d8C4";
        try {
            ETHUtils ethUtils = new ETHUtils();
            //test getVersion and newAccount
            log.debug(ethUtils.getVersion());
//            log.debug(ethUtils.createNewAccount("password"));

            //test getBalance for ETH and Token
            log.info("UHHA Blanace: {}", ethUtils.getERC20Balance("0xD4a7D7dfBf8cDeCe34788ccd4a63491C2047EcAe", UHHA_CONTRACT_ADDR));
            log.info("ETH Blanace: {}", ethUtils.getBalance("0xD4a7D7dfBf8cDeCe34788ccd4a63491C2047EcAe"));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void test2() throws IOException, InterruptedException {
        String fromAddr = "0xD0284a1A1896802f14438bD0208Ec42F76e60736";
        String toAddr = "0x4d5c03348ff42a9958605e879ea12f5b59fe62bb";
        ETHUtils ethUtils = new ETHUtils(fromAddr, "1a914eabea3ab643c27bdb1146e8e7b6c41c2f531c12c35f04acba987c9ba281");
        BigDecimal amount = new BigDecimal("0.01");

        log.info("Transfer {} ether from : {} to: {}", amount, fromAddr, toAddr);

        //test transfer ETH
        log.info("ToAddr: {} \r\n ETH: {}", toAddr, ethUtils.getBalance(toAddr));
        log.info("FromAddr: {} \r\n ETH: {}", fromAddr, ethUtils.getBalance(fromAddr));

        String txHash = ethUtils.transfer(fromAddr, toAddr,amount, ChainId.ROPSTEN);
        log.info("ToAddr: {} \r\n ETH: {}", toAddr, ethUtils.getBalance(toAddr));
        log.info("FromAddr: {} \r\n ETH: {}", fromAddr, ethUtils.getBalance(fromAddr));

        // Wait for transaction to be mined
        Optional<TransactionReceipt> transactionReceipt = null;
        do {
            System.out.println("checking if transaction " + txHash + " is mined....");
            transactionReceipt = ethUtils.getTransactionReceipt(txHash);
            Thread.sleep(3000); // Wait 3 sec
        } while(!transactionReceipt.isPresent());
    }

    public static void test3() {
        // UHHA CONTRACT
        final String UHHA_CONTRACT_ADDR = "0xa5502EB978282c1e33C4fd9241301EE93939d8C4";

        String fromAddr = "0xD0284a1A1896802f14438bD0208Ec42F76e60736";
        String toAddr = "0x4d5c03348ff42a9958605e879ea12f5b59fe62bb";

        ETHUtils ethUtils = new ETHUtils("0xD0284a1A1896802f14438bD0208Ec42F76e60736", "1a914eabea3ab643c27bdb1146e8e7b6c41c2f531c12c35f04acba987c9ba281");

        log.info("Transfer {} UHHA from : {} to: {}", 20, fromAddr, toAddr);
        try {
            log.info("ToAddr: {} \r\n UHHA: {}", toAddr, ethUtils.getERC20Balance(toAddr, UHHA_CONTRACT_ADDR));
            log.info("FromAddr: {} \r\n UHHA: {}", fromAddr, ethUtils.getERC20Balance(fromAddr, UHHA_CONTRACT_ADDR));
            ethUtils.transferERC20Token(fromAddr, toAddr, new BigInteger("199"), UHHA_CONTRACT_ADDR);
            log.info("ToAddr: {} \r\n UHHA: {}", toAddr, ethUtils.getERC20Balance(toAddr, UHHA_CONTRACT_ADDR));
            log.info("FromAddr: {} \r\n UHHA: {}", fromAddr, ethUtils.getERC20Balance(fromAddr, UHHA_CONTRACT_ADDR));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void test4(){
        // UHHA CONTRACT
        final String UHHA_CONTRACT_ADDR = "0xa5502EB978282c1e33C4fd9241301EE93939d8C4";

        String fromAddr = "0xD0284a1A1896802f14438bD0208Ec42F76e60736";
        String toAddr = "0x4d5c03348ff42a9958605e879ea12f5b59fe62bb";

        ETHUtils ethUtils = new ETHUtils("0xD0284a1A1896802f14438bD0208Ec42F76e60736", "1a914eabea3ab643c27bdb1146e8e7b6c41c2f531c12c35f04acba987c9ba281");

        try {
            log.info("ToAddr: {} \r\n UHHA: {}", toAddr, ethUtils.getERC20Balance(toAddr, UHHA_CONTRACT_ADDR));
            log.info("FromAddr: {} \r\n UHHA: {}", fromAddr, ethUtils.getERC20Balance(fromAddr, UHHA_CONTRACT_ADDR));
            log.info("ToAddr: {} \r\n UHHA: {}", toAddr, ethUtils.getBalance(toAddr));
            log.info("FromAddr: {} \r\n UHHA: {}", fromAddr, ethUtils.getBalance(fromAddr));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void check() throws IOException, InterruptedException {
        String fromAddr = "0xD0284a1A1896802f14438bD0208Ec42F76e60736";
        String toAddr = "0x4d5c03348ff42a9958605e879ea12f5b59fe62bb";
        ETHUtils ethUtils = new ETHUtils(fromAddr, "1a914eabea3ab643c27bdb1146e8e7b6c41c2f531c12c35f04acba987c9ba281");

        String txHash = "0xf0ed484f25692b394b577e17a8eb7e97dee12d4eca5b04da0bca04217314ab23";

        // Wait for transaction to be mined
        Optional<TransactionReceipt> transactionReceipt = null;
        do {
            System.out.println("checking if transaction " + txHash + " is mined....");
            transactionReceipt = ethUtils.getTransactionReceipt(txHash);
            Thread.sleep(3000); // Wait 3 sec
        } while(!transactionReceipt.isPresent());
    }

}

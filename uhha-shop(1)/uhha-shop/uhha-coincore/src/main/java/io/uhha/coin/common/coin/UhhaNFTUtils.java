package io.uhha.coin.common.coin;

import io.uhha.coin.common.coin.contract.UhhaNFTContract;
import io.uhha.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;

@Slf4j
public class UhhaNFTUtils extends ETHUtils{

    private static Web3j web3j;

    private static final String UHHA_NFT_CONTRACT = "0x9A814600C4b94820a3A9392BED7F1133391E891C";

    //发送所用地址
    private String fromAddr;
    //私钥
    private String privateKey;

    /**
     * @param fromAddr
     * @param privateKey
     */
    public UhhaNFTUtils(String fromAddr, String privateKey) {
        web3j = Web3j.build(new HttpService("http://localhost:8545"));

        if (StringUtils.isEmpty(fromAddr) || (StringUtils.isEmpty(privateKey))) {
            log.warn("fromaddr or privateKey is empty.");
        } else {
            this.fromAddr = fromAddr;
            this.privateKey = privateKey;
        }
    }

    public TransactionReceipt mintNFT(String player, String contentUrl) throws Exception {

        if (this.privateKey.startsWith("0x")) {
            privateKey = privateKey.substring(2);
        }
        ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(privateKey, 16));
        Credentials credentials = Credentials.create(ecKeyPair);

        TransactionReceipt receipt;
        // UHHA 合约交易
        ContractGasProvider gasProvider = new DefaultGasProvider();
        UhhaNFTContract contract = UhhaNFTContract.load(UHHA_NFT_CONTRACT, web3j, credentials, gasProvider);
        log.info("================== 开始ERC20合约交易 ==================");
        // 调用mint方法
        receipt = contract.mintNFT(player, contentUrl).sendAsync().get();
        // 打印交易Hash
        log.info("================== [ERC20合约] 交易完成! 交易Hash: ---> " + receipt.getTransactionHash() + " ==================");
        return receipt;
    }

    public static void main(String[] args) {
        String PRIVATE_KEY="fbce9eed53f7eac75ea3413ac6d1defcde5a0f4133a0560af603a41dd9025fcb";
        String fromAddr = "0x4af711fdB715652d4925fBCad5275Ca8348Ec055";

        UhhaNFTUtils utils = new UhhaNFTUtils(fromAddr, PRIVATE_KEY);

        try {
            utils.mintNFT(fromAddr,"Helloworld");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

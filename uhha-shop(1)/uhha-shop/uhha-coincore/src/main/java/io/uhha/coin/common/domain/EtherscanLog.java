package io.uhha.coin.common.domain;

import lombok.Data;

@Data
public class EtherscanLog {
    private String timeStamp;
    private String gasUsed;
    private String gasPrice;
    private String address;
    private String logIndex;
    private String data;
    private String blockNumber;
    private String transactionHash;
    private String transactionIndex;


}

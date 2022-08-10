package io.uhha.coin.common.coin;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 虚拟币驱动
 * @author jany
 */
public interface CoinDriver extends Serializable {
	
	/**
	 * 获取分类
	 * @return
	 */
	Integer getCoinSort();
	
	/**
	 * 获取余额
	 * @return
	 */
	BigDecimal getBalance();
	
	/**
	 * 生成地址
	 * @param uId
	 * @return
	 */
	String getNewAddress(String uId);
	
	/**
	 * 钱包加锁
	 */
	void walletLock();
	
	/**
	 * 钱包解锁
	 * @param times
	 */
	void walletPassPhrase(int times);
	
	/**
	 * 设置手续费
	 * @param fee
	 * @return
	 */
	boolean setTxFee(BigDecimal fee);
	
	/**
	 * 获取交易列表（接收币的交易）
	 * @param count
	 * @param from
	 * @return
	 */
	List<TxInfo> listTransactions(int count, int from);
	
	/**
	 * 获取交易详情
	 * @param txId
	 * @return
	 */
	TxInfo getTransaction(String txId);
	
	/**
	 * 发送
	 * @param address
	 * @param amount
	 * @param comment
	 * @param fee
	 * @return
	 */
	String sendToAddress(String address, BigDecimal amount, String comment, BigDecimal fee);
	
	/**
	 * ETH发送
	 * @param to
	 * @param amount
	 * @return
	 */
	String sendToAddress(String to, String amount);

}

package io.uhha.coin.common.coin.model;

import java.io.Serializable;

public class Transaction implements Serializable {

	private LapBalances balance;
	private String[] myaddresses;
	private String[] addresses;
	private Integer confirmations;
	private String txid;
	private String timereceived;

	public LapBalances getBalance() {
		return balance;
	}

	public void setBalance(LapBalances balance) {
		this.balance = balance;
	}

	public String[] getMyaddresses() {
		return myaddresses;
	}

	public void setMyaddresses(String[] myaddresses) {
		this.myaddresses = myaddresses;
	}

	public String[] getAddresses() {
		return addresses;
	}

	public void setAddresses(String[] addresses) {
		this.addresses = addresses;
	}

	public Integer getConfirmations() {
		return confirmations;
	}

	public void setConfirmations(Integer confirmations) {
		this.confirmations = confirmations;
	}

	public String getTxid() {
		return txid;
	}

	public void setTxid(String txid) {
		this.txid = txid;
	}

	public String getTimereceived() {
		return timereceived;
	}

	public void setTimereceived(String timereceived) {
		this.timereceived = timereceived;
	}
}
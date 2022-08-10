package io.uhha.coin.common.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author ISME
 * @Date 2018/1/14
 * @Time 14:16
 */

public class Merged implements Serializable {

    private static final long serialVersionUID = -6938081411405955147L;
    /**
     * id : 1499225271
     * close : 1885
     * open : 1960
     * high : 1985
     * low : 1856
     * amount : 81486.2926
     * count : 42122
     * vol : 1.57052744857082E8
     * ask : [1885,21.8804]
     * bid : [1884,1.6702]
     */

    private Long id;
    private BigDecimal close;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal amount;
    private int count;
    private BigDecimal vol;
    private Long version;
    private BigDecimal[] ask;
    private BigDecimal[] bid;

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getVol() {
        return vol;
    }

    public void setVol(BigDecimal vol) {
        this.vol = vol;
    }

    public BigDecimal[] getAsk() {
        return ask;
    }

    public void setAsk(BigDecimal[] ask) {
        this.ask = ask;
    }

    public BigDecimal[] getBid() {
        return bid;
    }

    public void setBid(BigDecimal[] bid) {
        this.bid = bid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}

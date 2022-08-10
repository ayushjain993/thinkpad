package io.uhha.coin.common.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author ISME
 * @Date 2018/1/14
 * @Time 11:35
 */

public class Kline implements Serializable {


    private static final long serialVersionUID = 3260042143079042105L;
    private Long id;
    private BigDecimal amount;
    private Integer count;//成交笔数
    private BigDecimal open;
    private BigDecimal close;
    private BigDecimal low;
    private BigDecimal high;
    private BigDecimal vol;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getVol() {
        return vol;
    }

    public void setVol(BigDecimal vol) {
        this.vol = vol;
    }
}

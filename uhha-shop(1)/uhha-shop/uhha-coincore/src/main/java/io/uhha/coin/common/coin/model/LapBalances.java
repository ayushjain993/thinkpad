package io.uhha.coin.common.coin.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class LapBalances implements Serializable {
    private BigDecimal amount;
    private List<Asset> assets;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }
}

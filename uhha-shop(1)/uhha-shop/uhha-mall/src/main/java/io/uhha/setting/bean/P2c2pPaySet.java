package io.uhha.setting.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.StringUtils;

public class P2c2pPaySet {
    /**
     * URL
     */
    private String url;

    private String merchantId;

    private String secretCode;

    private String currencyCode;

    /**
     * 是否启用 1启用 0不启用
     */

    private String isUse;

    public String getIsUse() {
        return isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(String secretCode) {
        this.secretCode = secretCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 检测是否启用 true 启用，反之不启用
     */
    @JsonIgnore
    public boolean checkIsUse() {
        return StringUtils.isEmpty(isUse) || "1".equals(isUse);
    }

    /**
     * 清理敏感信息
     */
    public void clearSensitiveInfo() {
        this.url = "";
        this.secretCode = "";
        this.merchantId = "";
        this.currencyCode = "";
    }

}

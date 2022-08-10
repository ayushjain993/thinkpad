package io.uhha.setting.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.uhha.common.utils.StringUtils;


public class DlocalPaySet {
    /**
     * URL
     */
    private String url;

    private String xLogin;
    private String xTransKey;

    private String secretKey;

    /**
     * 供smart Fields方案中使用
     */
    private String apiToken;

    private String currency;

    public String getxLogin() {
        return xLogin;
    }

    public void setxLogin(String xLogin) {
        this.xLogin = xLogin;
    }

    public String getxTransKey() {
        return xTransKey;
    }

    public void setxTransKey(String xTransKey) {
        this.xTransKey = xTransKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getIsUse() {
        return isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 是否启用 1启用 0不启用
     */

    private String isUse;

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
        this.secretKey = "";
        this.xLogin = "";
        this.xTransKey = "";
        this.apiToken = "";
        this.currency = "";
    }
}

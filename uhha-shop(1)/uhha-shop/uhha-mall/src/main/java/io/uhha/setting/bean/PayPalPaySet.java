package io.uhha.setting.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.uhha.common.utils.StringUtils;
import lombok.Data;

/**
 * Paypal setting
 */
@Data
public class PayPalPaySet {
    /**
     * URL
     */
    private String url;

    /**
     * appAccount
     */
    private String appAccount;

    /**
     * appSecret
     */
    private String appSecret;

    /**
     * clientId
     */
    private String clientId;

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
        this.clientId = "";
        this.appSecret = "";
        this.appAccount ="";
    }

}

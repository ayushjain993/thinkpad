package io.uhha.setting.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.uhha.common.utils.StringUtils;
import lombok.Data;

/**
 * Zotapay setting
 * https://docs.zotapay.com/deposit/1.0/?javascript&_gl=1*1123fna*_ga*NDgyNzI4NDYuMTYzNzkwOTg4MA..*_ga_6FREG02JW5*MTYzNzkwOTg3OS4xLjEuMTYzNzkxMDAxNy4w#introduction
 */
@Data
public class ZotaPaySet {
    /**
     * URL
     */
    private String url;
    /**
     * A merchant unique identifier, used for identification.
     */
    private String merchantID;

    /**
     * A secret key to keep privately and securely, used for authentication.
     */
    private String merchantSecretKey;

    /**
     *CNY unique endpoint identifiers to use in API requests.
     */
    private String cnyEndpointID;

    /**
     * USD unique endpoint identifiers to use in API requests.
     */
    private String usdEndpointID;

    /**
     * EUR unique endpoint identifiers to use in API requests.
     */
    private String eurEndpointID;

    /**
     * JPY unique endpoint identifiers to use in API requests.
     */
    private String jpyEndpointID;

    /**
     * MYR unique endpoint identifiers to use in API requests.
     */
    private String myrEndpointID;
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
        this.merchantID = "";
        this.merchantSecretKey = "";
        this.cnyEndpointID = "";
        this.usdEndpointID = "";
        this.eurEndpointID = "";
        this.jpyEndpointID = "";
        this.myrEndpointID = "";
    }

}

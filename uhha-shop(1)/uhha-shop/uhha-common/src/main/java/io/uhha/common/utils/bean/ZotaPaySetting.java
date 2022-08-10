package io.uhha.common.utils.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZotaPaySetting {

    private String url;

    /**
     * merchantID
     */
    private String merchantID;

    /**
     * merchantSecretKey
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
     * 前台回调地址
     */
    private String beforeCallbackUrl;

    /**
     * 后台回调地址
     */
    private String backCallbackUrl;

    /**
     * 构造c2cp支付实体
     *
     * @param beforeCallbackUrl 前台回调地址
     * @param backCallbackUrl   后台回调地址
     * @return 返回2c2p配置信息
     */
    public static ZotaPaySetting build(String beforeCallbackUrl, String backCallbackUrl) {
        ZotaPaySetting zotaPaySetting = new ZotaPaySetting();

        //前台同步回调地址
        zotaPaySetting.setBeforeCallbackUrl(beforeCallbackUrl);
        //异步回调地址
        zotaPaySetting.setBackCallbackUrl(backCallbackUrl);

        return zotaPaySetting;
    }

    /**
     * 检测支付时的参数
     */
    public boolean checkPayParams() {
        return !StringUtils.isEmpty(url)
                &&!StringUtils.isEmpty(merchantID)
                && !StringUtils.isEmpty(merchantSecretKey)
                && !StringUtils.isEmpty(cnyEndpointID)
                && !StringUtils.isEmpty(usdEndpointID)
                && !StringUtils.isEmpty(eurEndpointID)
                && !StringUtils.isEmpty(jpyEndpointID)
                && !StringUtils.isEmpty(eurEndpointID)
                ;
    }
}

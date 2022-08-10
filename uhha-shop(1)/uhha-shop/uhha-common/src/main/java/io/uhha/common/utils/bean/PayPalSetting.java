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
public class PayPalSetting {

    private String url;

    /**
     * account
     */
    private String appAccount;

    /**
     * clientId
     */
    private String clientId;

    /**
     * Secret
     */
    private String appSecret;


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
    public static PayPalSetting build(String beforeCallbackUrl, String backCallbackUrl) {
        PayPalSetting zotaPaySetting = new PayPalSetting();

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
                &&!StringUtils.isEmpty(appSecret)
                &&!StringUtils.isEmpty(appAccount)
                && !StringUtils.isEmpty(clientId)
                ;
    }
}

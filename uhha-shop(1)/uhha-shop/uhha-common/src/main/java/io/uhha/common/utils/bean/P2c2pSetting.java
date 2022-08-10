package io.uhha.common.utils.bean;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class P2c2pSetting {
    private String url;

    /**
     * 商户号
     */
    private String merchantId;

    /**
     * 安全码
     */
    private String secretCode;

    /**
     * 币种
     */
    private String currencyCode;

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
    public static P2c2pSetting build(String beforeCallbackUrl, String backCallbackUrl) {
        P2c2pSetting p2c2pSetting = new P2c2pSetting();

        //前台同步回调地址
        p2c2pSetting.setBeforeCallbackUrl(beforeCallbackUrl);
        //异步回调地址
        p2c2pSetting.setBackCallbackUrl(backCallbackUrl);

        return p2c2pSetting;
    }

    /**
     * 检测支付时的参数
     */
    public boolean checkPayParams() {
        return !StringUtils.isEmpty(url)&&!StringUtils.isEmpty(merchantId) && !StringUtils.isEmpty(secretCode)&&!StringUtils.isEmpty(currencyCode) ;
    }
}

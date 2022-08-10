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
public class DlocalSetting {
    private String url;

    /**
     * xLogin
     */
    private String xLogin;

    /**
     * xTransKey
     */
    private String xTransKey;

    /**
     * 安全码
     */
    private String secretKey;

    /**
     * currency
     */
    private String currency;

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
    public static DlocalSetting build(String beforeCallbackUrl, String backCallbackUrl) {
        DlocalSetting dlocalSetting = new DlocalSetting();

        //前台同步回调地址
        dlocalSetting.setBeforeCallbackUrl(beforeCallbackUrl);
        //异步回调地址
        dlocalSetting.setBackCallbackUrl(backCallbackUrl);

        return dlocalSetting;
    }

    /**
     * 检测支付时的参数
     */
    public boolean checkPayParams() {
        return !StringUtils.isEmpty(url)&&!StringUtils.isEmpty(xLogin) && !StringUtils.isEmpty(xTransKey) && !StringUtils.isEmpty(secretKey) && !StringUtils.isEmpty(currency);
    }
}

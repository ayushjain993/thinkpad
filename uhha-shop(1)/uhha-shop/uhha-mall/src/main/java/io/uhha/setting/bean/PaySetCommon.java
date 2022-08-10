package io.uhha.setting.bean;


import java.util.List;
import java.util.Objects;

/**
 * 支付设置统一类
 *
 * @author mj on 2017/5/17.
 */

public class PaySetCommon {

    /**
     * 阿里支付
     */
    private AliPaySet aliPaySet = new AliPaySet();
    /**
     * 微信支付(公众号、扫码、H5)
     */

    private WechatPaySet wechatPaySet = new WechatPaySet();
    /**
     * 微信支付(app)
     */

    private WechatPaySet wechatAppPaySet = new WechatPaySet();

    /**
     * 微信支付(小程序支付）
     */

    private WechatPaySet wechatAppletPaySet = new WechatPaySet();

    /**
     * 银联支付
     */
    private UnionPaySet unionPaySet = new UnionPaySet();

    /**
     * 预存款支付
     */
    private PrePaySet prePaySet = new PrePaySet();

    /**
     * 2c2p支付
     */
    private P2c2pPaySet p2c2pPaySet = new P2c2pPaySet();

    /**
     * dlocal支付
     */
    private DlocalPaySet dlocalPaySet = new DlocalPaySet();

    /**
     * Zotapay 支付
     */
    private ZotaPaySet zotaPaySet = new ZotaPaySet();

    /**
     * paypal 支付
     */
    private PayPalPaySet paypalPaySet = new PayPalPaySet();

    /**
     * 用于组装支付设置对象在前端显示
     *
     * @param paySetCommon 支付设置对象
     * @param paySets      数据库映射对象
     * @return 支付设置对象
     */
    public static PaySetCommon getPaySetCommon(PaySetCommon paySetCommon, List<PaySet> paySets) {
        paySets.forEach(paySet -> {
            String value = paySet.getColumnValue();
            //支付宝
            if ("1".equals(paySet.getCodeType())) {
                if ("appId".equals(paySet.getColumnName())) {
                    paySetCommon.aliPaySet.setAppId(value);
                }
                if ("alipayPublicKey".equals(paySet.getColumnName())) {
                    paySetCommon.aliPaySet.setAlipayPublicKey(value);
                }
                if ("appPrivateKey".equals(paySet.getColumnName())) {
                    paySetCommon.aliPaySet.setAppPrivateKey(value);
                }
                if ("isUse".equals(paySet.getColumnName())) {
                    paySetCommon.aliPaySet.setIsUse(value);
                }
            }
            //微信(公众号、扫码、H5)
            if ("2".equals(paySet.getCodeType())) {
                if ("appId".equals(paySet.getColumnName())) {
                    paySetCommon.wechatPaySet.setAppId(value);
                }
                if ("appSecret".equals(paySet.getColumnName())) {
                    paySetCommon.wechatPaySet.setAppSecret(value);
                }
                if ("merchantNum".equals(paySet.getColumnName())) {
                    paySetCommon.wechatPaySet.setMerchantNum(value);
                }
                if ("apiKey".equals(paySet.getColumnName())) {
                    paySetCommon.wechatPaySet.setApiKey(value);
                }
                if ("loginNotice".equals(paySet.getColumnName())) {
                    paySetCommon.wechatPaySet.setLoginNotice(value);
                }
                if ("isUse".equals(paySet.getColumnName())) {
                    paySetCommon.wechatPaySet.setIsUse(value);
                }
            }
            //银联
            if ("3".equals(paySet.getCodeType())) {
                if ("merchantNum".equals(paySet.getColumnName())) {
                    paySetCommon.unionPaySet.setMerchantNum(value);
                }
                if ("apiKey".equals(paySet.getColumnName())) {
                    paySetCommon.unionPaySet.setApiKey(value);
                }

                if ("isUse".equals(paySet.getColumnName())) {
                    paySetCommon.unionPaySet.setIsUse(value);
                }
            }
            //预存款
            if ("4".equals(paySet.getCodeType())) {
                if ("isUse".equals(paySet.getColumnName())) {
                    paySetCommon.prePaySet.setIsUse(value);
                }
            }
            //微信支付(app)
            if ("5".equals(paySet.getCodeType())) {
                if ("appId".equals(paySet.getColumnName())) {
                    paySetCommon.wechatAppPaySet.setAppId(value);
                }
                if ("appSecret".equals(paySet.getColumnName())) {
                    paySetCommon.wechatAppPaySet.setAppSecret(value);
                }
                if ("merchantNum".equals(paySet.getColumnName())) {
                    paySetCommon.wechatAppPaySet.setMerchantNum(value);
                }
                if ("apiKey".equals(paySet.getColumnName())) {
                    paySetCommon.wechatAppPaySet.setApiKey(value);
                }
                if ("isUse".equals(paySet.getColumnName())) {
                    paySetCommon.wechatAppPaySet.setIsUse(value);
                }
            }

            //微信支付(小程序)
            if ("6".equals(paySet.getCodeType())) {
                if ("appId".equals(paySet.getColumnName())) {
                    paySetCommon.wechatAppletPaySet.setAppId(value);
                }
                if ("appSecret".equals(paySet.getColumnName())) {
                    paySetCommon.wechatAppletPaySet.setAppSecret(value);
                }
                if ("merchantNum".equals(paySet.getColumnName())) {
                    paySetCommon.wechatAppletPaySet.setMerchantNum(value);
                }
                if ("apiKey".equals(paySet.getColumnName())) {
                    paySetCommon.wechatAppletPaySet.setApiKey(value);
                }
                if ("isUse".equals(paySet.getColumnName())) {
                    paySetCommon.wechatAppletPaySet.setIsUse(value);
                }
            }

            //2c2p
            if ("7".equals(paySet.getCodeType())) {
                if ("url".equals(paySet.getColumnName())) {
                    paySetCommon.p2c2pPaySet.setUrl(value);
                }
                if ("merchantId".equals(paySet.getColumnName())) {
                    paySetCommon.p2c2pPaySet.setMerchantId(value);
                }
                if ("secretCode".equals(paySet.getColumnName())) {
                    paySetCommon.p2c2pPaySet.setSecretCode(value);
                }
                if ("currencyCode".equals(paySet.getColumnName())) {
                    paySetCommon.p2c2pPaySet.setCurrencyCode(value);
                }
                if ("isUse".equals(paySet.getColumnName())) {
                    paySetCommon.p2c2pPaySet.setIsUse(value);
                }
            }

            //dlocal
            if ("8".equals(paySet.getCodeType())) {
                if ("url".equals(paySet.getColumnName())) {
                    paySetCommon.dlocalPaySet.setUrl(value);
                }
                if ("xLogin".equals(paySet.getColumnName())) {
                    paySetCommon.dlocalPaySet.setxLogin(value);
                }
                if ("xTransKey".equals(paySet.getColumnName())) {
                    paySetCommon.dlocalPaySet.setxTransKey(value);
                }
                if ("secretKey".equals(paySet.getColumnName())) {
                    paySetCommon.dlocalPaySet.setSecretKey(value);
                }
                if ("apiToken".equals(paySet.getColumnName())) {
                    paySetCommon.dlocalPaySet.setApiToken(value);
                }
                if ("currency".equals(paySet.getColumnName())) {
                    paySetCommon.dlocalPaySet.setCurrency(value);
                }
                if ("isUse".equals(paySet.getColumnName())) {
                    paySetCommon.dlocalPaySet.setIsUse(value);
                }
            }

            //zotapay
            if ("9".equals(paySet.getCodeType())) {
                if ("url".equals(paySet.getColumnName())) {
                    paySetCommon.zotaPaySet.setUrl(value);
                }
                if ("merchantID".equals(paySet.getColumnName())) {
                    paySetCommon.zotaPaySet.setMerchantID(value);
                }
                if ("merchantSecretKey".equals(paySet.getColumnName())) {
                    paySetCommon.zotaPaySet.setMerchantSecretKey(value);
                }
                if ("cnyEndpointID".equals(paySet.getColumnName())) {
                    paySetCommon.zotaPaySet.setCnyEndpointID(value);
                }
                if ("usdEndpointID".equals(paySet.getColumnName())) {
                    paySetCommon.zotaPaySet.setUsdEndpointID(value);
                }
                if ("eurEndpointID".equals(paySet.getColumnName())) {
                    paySetCommon.zotaPaySet.setEurEndpointID(value);
                }
                if ("jpyEndpointID".equals(paySet.getColumnName())) {
                    paySetCommon.zotaPaySet.setJpyEndpointID(value);
                }
                if ("myrEndpointID".equals(paySet.getColumnName())) {
                    paySetCommon.zotaPaySet.setMyrEndpointID(value);
                }
                if ("isUse".equals(paySet.getColumnName())) {
                    paySetCommon.zotaPaySet.setIsUse(value);
                }
            }

            //paypal
            if ("10".equals(paySet.getCodeType())) {
                if ("url".equals(paySet.getColumnName())) {
                    paySetCommon.paypalPaySet.setUrl(value);
                }
                if ("clientId".equals(paySet.getColumnName())) {
                    paySetCommon.paypalPaySet.setClientId(value);
                }
                if ("appSecret".equals(paySet.getColumnName())) {
                    paySetCommon.paypalPaySet.setAppSecret(value);
                }
                if ("appAccount".equals(paySet.getColumnName())) {
                    paySetCommon.paypalPaySet.setAppAccount(value);
                }
                if ("isUse".equals(paySet.getColumnName())) {
                    paySetCommon.paypalPaySet.setIsUse(value);
                }
            }
        });
        return paySetCommon;
    }

    /**
     * 清除敏感 信息
     *
     * @return 返回当前对象
     */
    public PaySetCommon clearSensitiveInfo() {

        if (Objects.nonNull(this.wechatPaySet)) {
            wechatPaySet.clearSensitiveInfo();
        }

        if (Objects.nonNull(this.wechatAppPaySet)) {
            wechatAppPaySet.clearSensitiveInfo();
        }

        if (Objects.nonNull(this.wechatAppletPaySet)) {
            wechatAppletPaySet.clearSensitiveInfo();
        }

        if (Objects.nonNull(this.aliPaySet)) {
            aliPaySet.clearSensitiveInfo();
        }

        if (Objects.nonNull(this.unionPaySet)) {
            unionPaySet.clearSensitiveInfo();
        }

        if (Objects.nonNull(this.p2c2pPaySet)) {
            p2c2pPaySet.clearSensitiveInfo();
        }

        if (Objects.nonNull(this.dlocalPaySet)) {
            dlocalPaySet.clearSensitiveInfo();
        }

        if (Objects.nonNull(this.zotaPaySet)) {
            zotaPaySet.clearSensitiveInfo();
        }

        if (Objects.nonNull(this.paypalPaySet)) {
            paypalPaySet.clearSensitiveInfo();
        }

        return this;
    }

    public AliPaySet getAliPaySet() {
        return aliPaySet;
    }

    public void setAliPaySet(AliPaySet aliPaySet) {
        this.aliPaySet = aliPaySet;
    }

    public WechatPaySet getWechatPaySet() {
        return wechatPaySet;
    }

    public void setWechatPaySet(WechatPaySet wechatPaySet) {
        this.wechatPaySet = wechatPaySet;
    }

    public WechatPaySet getWechatAppPaySet() {
        return wechatAppPaySet;
    }

    public void setWechatAppPaySet(WechatPaySet wechatAppPaySet) {
        this.wechatAppPaySet = wechatAppPaySet;
    }

    public WechatPaySet getWechatAppletPaySet() {
        return wechatAppletPaySet;
    }

    public void setWechatAppletPaySet(WechatPaySet wechatAppletPaySet) {
        this.wechatAppletPaySet = wechatAppletPaySet;
    }

    public UnionPaySet getUnionPaySet() {
        return unionPaySet;
    }

    public void setUnionPaySet(UnionPaySet unionPaySet) {
        this.unionPaySet = unionPaySet;
    }

    public PrePaySet getPrePaySet() {
        return prePaySet;
    }

    public void setPrePaySet(PrePaySet prePaySet) {
        this.prePaySet = prePaySet;
    }

    public P2c2pPaySet getP2c2pPaySet() {
        return p2c2pPaySet;
    }

    public void setP2c2pPaySet(P2c2pPaySet p2c2pPaySet) {
        this.p2c2pPaySet = p2c2pPaySet;
    }

    public DlocalPaySet getDlocalPaySet() {
        return dlocalPaySet;
    }

    public void setDlocalPaySet(DlocalPaySet dlocalPaySet) {
        this.dlocalPaySet = dlocalPaySet;
    }

    public ZotaPaySet getZotaPaySet() {
        return zotaPaySet;
    }

    public void setZotaPaySet(ZotaPaySet zotapaySet) {
        this.zotaPaySet = zotapaySet;
    }

    public PayPalPaySet getPaypalPaySet() {
        return paypalPaySet;
    }

    public void setPaypalPaySet(PayPalPaySet paypalPaySet) {
        this.paypalPaySet = paypalPaySet;
    }
}

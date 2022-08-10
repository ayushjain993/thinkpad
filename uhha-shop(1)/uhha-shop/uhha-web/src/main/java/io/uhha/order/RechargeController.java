package io.uhha.order;


import io.uhha.appletsutil.AppletsLoginUtils;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.enums.PaymentTypeEnum;
import io.uhha.common.utils.bean.ZotaPayPayerObject;
import io.uhha.member.service.RechargeService;
import io.uhha.order.vo.ZotaPayParam;
import io.uhha.util.CommonConstant;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by dujinkai on 2020/2/18.
 * 预存款充值接口
 */
@RestController
@Api(tags = "预存款充值接口")
public class RechargeController {

    /**
     * 注入预存款服务
     */
    @Autowired
    private RechargeService rechargeService;


    /**
     * 获取ip方法使用此参数
     */
    private static final String UNKNOWN = "unknown";


    /**
     * 2c2p支付
     *
     * @return 返回码和支付宝支付的form 返回码说明  -1:用户不存在 -2:生成充值记录出错 -5:支付宝生成订单出错 -7 没有设置网站地址 -8 缺少配置 1 成功
     */
    @GetMapping("/recharge_to2c2pagepay")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "BigDecimal", name = "money", value = "充值金额"),
            @ApiImplicitParam(paramType = "form", dataType = "String", name = "payType", value = " 1 公众号 2 h5 3 小程序 4 app 5 pc"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回码和支付宝支付的html 返回码说明  -1:用户不存在 -2:生成充值记录出错 -5:支付宝生成订单出错 -7 没有设置网站地址 -8 缺少配置  -9 没有启用  1 成功", response = String.class)
    })
    public AjaxResult to2c2pPay(
            HttpServletRequest request,
            @RequestParam("money") BigDecimal money,
            @RequestParam("payType") Integer payType
    ) throws IOException {
        return AjaxResult.success(rechargeService.c2cpPay("", money, payType, AppletsLoginUtils.getInstance().getCustomerId(request), PaymentTypeEnum.RECHARGE_PAY.getCode()));
    }

    /**
     * zotapay支付
     *
     * @return 返回码和支付宝支付的form 返回码说明  -1:用户不存在 -2:生成充值记录出错 -5:支付宝生成订单出错 -7 没有设置网站地址 -8 缺少配置 1 成功
     */
    @GetMapping("/recharge_tozotapay")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "BigDecimal", name = "money", value = "充值金额"),
            @ApiImplicitParam(paramType = "form", dataType = "String", name = "payType", value = " 1 公众号 2 h5 3 小程序 4 app 5 pc"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回码和Zotapay支付的html 返回码说明  -1:用户不存在 -2:生成充值记录出错 -5:支付宝生成订单出错 -7 没有设置网站地址 -8 缺少配置  -9 没有启用  1 成功", response = String.class)
    })
    public AjaxResult tozotapay(
            ZotaPayParam payParam,
            HttpServletRequest request
    )  {
        ZotaPayPayerObject payer = ZotaPayPayerObject.builder()
                .customerFirstName(payParam.getCustomerFirstName())
                .customerLastName(payParam.getCustomerLastName())
                .customerEmail(payParam.getCustomerEmail())
                .customerAddress(payParam.getCustomerAddress())
                .customerCountryCode(payParam.getCustomerCountryCode())
                .customerCity(payParam.getCustomerCity())
                .customerState(payParam.getCustomerState())
                .customerZipCode(payParam.getCustomerZipCode())
                .customerPhone(payParam.getCustomerPhone())
                .customerIP(getIpAddr(request))
                .customerBankCode(payParam.getCustomerBankCode())
                .customerBankAccountNumber(payParam.getCustomerBankAccountNumber())
                .customerBankAccountName(payParam.getCustomerBankAccountName())
                .build();

        return AjaxResult.success(rechargeService.zotapay("", payParam.getMoney(), payParam.getCurrency(), payParam.getPayType(), AppletsLoginUtils.getInstance().getCustomerId(request), PaymentTypeEnum.RECHARGE_PAY.getCode(), payer));
    }

    /**
     * 支付宝pc支付
     *
     * @return 返回码和支付宝支付的form 返回码说明  -1:用户不存在 -2:生成充值记录出错 -5:支付宝生成订单出错 -7 没有设置网站地址 -8 缺少配置 1 成功
     */
    @GetMapping("/recharge_toalipagepay")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "BigDecimal", name = "money", value = "充值金额"),
            @ApiImplicitParam(paramType = "form", dataType = "String", name = "payType", value = " 1 公众号 2 h5 3 小程序 4 app 5 pc"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回码和支付宝支付的html 返回码说明  -1:用户不存在 -2:生成充值记录出错 -5:支付宝生成订单出错 -7 没有设置网站地址 -8 缺少配置  -9 没有启用  1 成功", response = String.class)
    })
    public AjaxResult toAliPagePay(BigDecimal money, int payType, HttpServletRequest request) throws IOException {
        return AjaxResult.success(rechargeService.aliPagePay("", money, payType, AppletsLoginUtils.getInstance().getCustomerId(request), PaymentTypeEnum.RECHARGE_PAY.getCode()));
    }

    /**
     * 支付宝pc支付
     *
     * @return 返回码和支付宝支付的form 返回码说明  -1:用户不存在 -2:生成充值记录出错 -5:支付宝生成订单出错 -7 没有设置网站地址 -8 缺少配置 1 成功
     */
    @GetMapping("/recharge_toaliwappay")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "BigDecimal", name = "money", value = "充值金额"),
            @ApiImplicitParam(paramType = "form", dataType = "String", name = "payType", value = " 1 公众号 2 h5 3 小程序 4 app 5 pc"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回码和支付宝支付的html 返回码说明  -1:用户不存在 -2:生成充值记录出错 -5:支付宝生成订单出错 -7 没有设置网站地址 -8 缺少配置  -9 没有启用  1 成功", response = String.class)
    })
    public AjaxResult toAliWapPay(BigDecimal money, int payType, HttpServletRequest request) throws IOException {
        return AjaxResult.success(rechargeService.aliWapPay("", money, payType, AppletsLoginUtils.getInstance().getCustomerId(request), PaymentTypeEnum.RECHARGE_PAY.getCode()));
    }

    /**
     * 获取微信扫码支付(pc)参数
     *
     * @return 返回码和扫码支付url 返回码说明  -1:用户不存在 -2:生成充值记录出错 -5:微信生成订单出错 -7 没有设置网站地址 -8 缺少配置  1 成功
     */
    @GetMapping("/recharge_wechatqrpayparm")
    @ApiOperation(value = "获取微信扫码支付(pc)参数", notes = "获取微信扫码支付(pc)参数（需要认证）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "String", name = "transCode", value = "交易流水号"),
            @ApiImplicitParam(paramType = "form", dataType = "String", name = "type", value = "1 公众号 2 h5 3 小程序 4 app 5 pc"),
            @ApiImplicitParam(paramType = "form", dataType = "BigDecimal", name = "money", value = "充值金额"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回码和扫码支付url 返回码说明  -1:用户不存在 -2:生成充值记录出错 -5:微信生成订单出错 -7 没有设置网站地址 -8 缺少配置  1 成功", response = String.class)
    })
    public AjaxResult wechatQRPayParm(String transCode, int payType, BigDecimal money, HttpServletRequest request) {
        return AjaxResult.success(rechargeService.wechatQRPay(transCode, money, payType, AppletsLoginUtils.getInstance().getCustomerId(request), getIpAddr(request), PaymentTypeEnum.RECHARGE_PAY.getCode()));
    }

    /**
     * 获取ip地址
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.indexOf(",") > -1) {
            ip = ip.split(",")[0];
        }
        return ip;
    }

    /**
     * 查询充值订单集合
     *
     * @param transcode 订单code
     * @return 返回订充值单集合
     */
    @ResponseBody
    @GetMapping(value = "/rechargeDepositByTranscode")
    @ApiOperation(value = "查询订单集合", notes = "查询订单集合（需要认证）")
    public AjaxResult queryOrdersByOrderCode(String transcode, HttpServletRequest request) {
        return AjaxResult.success(rechargeService.queryDepositByTranscode(transcode, AppletsLoginUtils.getInstance().getCustomerId(request)));
    }


}

package io.uhha.pay;


import io.uhha.appletsutil.AppletsLoginUtils;
import io.uhha.common.annotation.UnAuth;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.utils.AliPayUtils;
import io.uhha.common.utils.WechatUtils;
import io.uhha.common.utils.bean.*;
import io.uhha.order.OrderPayService;
import io.uhha.order.vo.*;
import io.swagger.annotations.*;
import io.uhha.util.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 订单支付控制器
 */
@RestController
@Api(tags = "订单支付接口")
@Slf4j
public class OrderPayController {


    /**
     * 获取ip地址用
     */
    private static final String UNKNOWN = "unknown";
    /**
     * 注入订单支付服务
     */
    @Autowired
    private OrderPayService orderPayService;

    /**
     * 获取微信小程序支付参数
     *
     * @param type      1 订单支付 2 预存款充值 3 门店订单支付
     * @param orderCode 订单code
     * @return 返回码和调起支付需要的参数 返回码说明  -1:用户不存在 -3:没有待支付的订单 -5:微信生成订单出错 -7 没有设置网站地址 -8 缺少配置  -10没有绑定微信 1 成功
     */
    @PostMapping("/wechatappletpayparams")
    @ResponseBody
    @ApiOperation(value = "获取微信公众号支付参数", notes = "获取微信公众号支付参数（需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "type", value = "1 订单支付 2 预存款充值 3 门店订单支付"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "orderCode", value = "订单code"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回码和调起支付需要的参数 返回码说明  -1:用户不存在 -3:没有待支付的订单 -5:微信生成订单出错 -7 没有设置网站地址 -8 缺少配置  -10没有绑定微信 1 成功", response = PrepayResult.class)
    })
    public AjaxResult wechatAppletPayParm(int type, String orderCode, HttpServletRequest request) {
        return AjaxResult.success(orderPayService.wechatAppletPay(orderCode, AppletsLoginUtils.getInstance().getCustomerId(request), getIpAddr(request), type));
    }


    /**
     * 微信支付回调
     */
    @ApiOperation(value = "微信支付回调", notes = "微信支付回调（不需要认证）")
    @PostMapping("/wechatnotify")
    @UnAuth
    @ApiIgnore
    public void weChatNotify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (orderPayService.weChatAppletNotify(request.getInputStream()) > 0) {
            sendMessage(response, WechatUtils.SUCCESS_RETURN);
        }
    }

    /**
     * 预存款支付
     *
     * @param type      1 订单支付 2 预存款充值 3 门店订单支付
     * @param orderCode 订单code (可能为主订单号也可能为子订单号)
     * @param password  支付密码
     * @return 返回码说明  -1:用户不存在 -2:支付密码错误 -3:没有待支付的订单 -4:用户预存款金额不足 -6:没有设置支付密码 1 成功
     */
    @PostMapping("/toprepay")
    @ResponseBody
    @ApiOperation(value = "预存款支付", notes = "预存款支付（需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "type", value = "1 订单支付 2 预存款充值 3 门店订单支付"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "orderCode", value = "订单code"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "password", value = "支付密码"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回码说明  -1:用户不存在 -2:支付密码错误 -3:没有待支付的订单 -4:用户预存款金额不足 -6:没有设置支付密码 1 成功", response = Integer.class)
    })
    public AjaxResult toPrePay(@RequestBody PrepayParam prepayParam, HttpServletRequest request) {
        return AjaxResult.success(orderPayService.predepositPay(prepayParam.getOrderCode(), prepayParam.getPassword(), AppletsLoginUtils.getInstance().getCustomerId(request), prepayParam.getType()));
    }

    /**
     * 支付宝h5支付
     *
     * @param type      支付类型 1:订单支付 2:预存款充值 3:门店线下支付
     * @param orderCode 订单code
     * @param orderType 订单类型
     * @param orderId   订单id
     */
    @GetMapping("/toaliwappay")
    @ApiOperation(value = "支付宝h5支付", notes = "支付宝h5支付（需要认证）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "orderType", value = "订单类型"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "orderId", value = "订单id"),
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "type", value = "1 订单支付 2 预存款充值 3 门店订单支付"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "orderCode", value = "订单code"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = " 返回码和支付宝支付的html 返回码说明  -1:用户不存在 -3:没有待支付的订单 -5:支付宝生成订单出错 -7 没有设置网站地址 -8 缺少配置  -9 没有启用 1 成功", response = String.class)
    })
    public AjaxResult toAliWapPay(PayParam payParam, HttpServletRequest request) throws IOException {
        return AjaxResult.success(orderPayService.aliWapPay(payParam.getOrderCode(), AppletsLoginUtils.getInstance().getCustomerId(request), payParam.getType(), payParam.getOrderType(), payParam.getOrderId()));
    }

    /**
     * 获取微信H5支付参数
     *
     * @param type      1 订单支付 2 预存款充值 3 门店订单支付
     * @param orderCode 订单code
     * @param orderType 订单类型
     * @param orderId   订单id
     * @return 返回码和扫码支付url 返回码说明  -1:用户不存在 -3:没有待支付的订单 -5:微信生成订单出错 -7 没有设置网站地址 -8 缺少配置  1 成功
     */
    @GetMapping("/wechath5payparm")
    @ApiOperation(value = "获取微信H5支付参数", notes = "获取微信H5支付参数（需要认证）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "orderType", value = "订单类型"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "orderId", value = "订单id"),
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "type", value = "1 订单支付 2 预存款充值 3 门店订单支付"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "orderCode", value = "订单code"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "微信支付返回实体", response = WechatPayResponse.class)
    })
    public AjaxResult wechatH5PayParm(PayParam payParam, HttpServletRequest request) {
        return AjaxResult.success(orderPayService.wechatH5Pay(payParam.getOrderCode(), AppletsLoginUtils.getInstance().getCustomerId(request), getIpAddr(request), payParam.getType(), payParam.getOrderType(), payParam.getOrderId()));
    }

    /**
     * 获取微信公众号支付参数
     *
     * @param type      1 订单支付 2 预存款充值 3 门店订单支付
     * @param orderCode 订单code
     * @return 返回码和调起支付需要的参数 返回码说明  -1:用户不存在 -3:没有待支付的订单 -5:微信生成订单出错 -7 没有设置网站地址 -8 缺少配置  -10没有绑定微信 1 成功
     */
    @GetMapping("/wechatofficialaccountpayparm")
    @ApiOperation(value = "获取微信公众号支付参数", notes = "获取微信公众号支付参数（需要认证）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "type", value = "1 订单支付 2 预存款充值 3 门店订单支付"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "orderCode", value = "订单code"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "下单接口返回实体类", response = PrepayResult.class)
    })
    public AjaxResult wechatOfficialAccountPayParm( PayParam payParam, HttpServletRequest request) {
        return AjaxResult.success(orderPayService.wechatOfficialAccountPay(payParam.getOrderCode(), AppletsLoginUtils.getInstance().getCustomerId(request), getIpAddr(request), payParam.getType()));
    }

    /**
     * 支付宝pc支付
     *
     * @param type      支付方式 1 订单 3 门店订单
     * @param orderCode 订单code
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "type", value = "1 订单支付 2 预存款充值 3 门店订单支付"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "orderCode", value = "订单code"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = " 返回码和支付宝支付的html 返回码说明  -1:用户不存在 -3:没有待支付的订单 -5:支付宝生成订单出错 -7 没有设置网站地址 -8 缺少配置  -9 没有启用 1 成功", response = String.class)
    })
    @GetMapping("/toalipagepay")
    @ApiOperation(value = "支付宝pc支付", notes = "支付宝pc支付（需要认证）")
    public AjaxResult toAliPagePay(@NotNull PayParam payParam, HttpServletRequest request) throws IOException {
        return AjaxResult.success(orderPayService.aliPagePay(payParam.getOrderCode(), AppletsLoginUtils.getInstance().getCustomerId(request), payParam.getType()));
    }
    /**
     * 获取微信扫码支付(pc)参数
     *
     * @param type      支付类型 1 订单  3 门店订单
     * @param orderCode 订单code
     * @return 返回码和扫码支付url 返回码说明  -1:用户不存在 -3:没有待支付的订单 -5:微信生成订单出错 -7 没有设置网站地址 -8 缺少配置  1 成功
     */
    @GetMapping("/wechatqrpayparm")
    @ApiOperation(value = "获取微信扫码支付(pc)参数", notes = "获取微信扫码支付(pc)参数（需要认证）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "type", value = "1 订单支付 2 预存款充值 3 门店订单支付"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "orderCode", value = "订单code"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "-1:用户不存在 -3:没有待支付的订单 -5:微信生成订单出错 -7 没有设置网站地址 -8 缺少配置  1 成功", response = WechatPayResponse.class)
    })
    public AjaxResult wechatQRPayParm(PayParam payParam,  HttpServletRequest request) {
        return AjaxResult.success(orderPayService.wechatQRPay(payParam.getOrderCode(), AppletsLoginUtils.getInstance().getCustomerId(request), getIpAddr(request), payParam.getType()));
    }

    /**
     * APP支付宝支付
     *
     * @param orderCode 订单code
     * @param type      1 订单支付 2 预存款充值 3 门店订单支付
     * @return 返回支付宝的url
     */
    @ApiOperation(value = "支付宝支付", notes = "支付宝支付（需要认证）")
    @GetMapping(value = "/alipay")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "type", value = "1 订单支付 2 预存款充值 3 门店订单支付"),
            @ApiImplicitParam(paramType = "form", dataType = "String", name = "orderCode", value = "订单code"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回支付宝的url", response = String.class)
    })
    public AjaxResult aliPay(PayParam payParam,  HttpServletRequest request) {
        return AjaxResult.success(orderPayService.aliAppPay(payParam.getOrderCode(), AppletsLoginUtils.getInstance().getCustomerId(request), payParam.getType()));
    }

    /**
     * APP微信支付
     *
     * @param orderCode 订单code
     * @param type      1 订单支付 2 预存款充值 3 门店订单支付
     * @return 返回微信
     */
    @ApiOperation(value = "微信支付", notes = "微信支付（需要认证）")
    @GetMapping(value = "/wxpay")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "type", value = "1 订单支付 2 预存款充值 3 门店订单支付"),
            @ApiImplicitParam(paramType = "form", dataType = "String", name = "orderCode", value = "订单code"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回微信", response = PrepayResult.class)
    })
    public AjaxResult wxPay(PayParam payParam,  HttpServletRequest request) {
        return AjaxResult.success(orderPayService.wechatAppPay(payParam.getOrderCode(), AppletsLoginUtils.getInstance().getCustomerId(request), getIpAddr(request), payParam.getType()));
    }
    /**
     * 支付宝支付回调
     */
    @ApiOperation(value = "支付宝支付回调", notes = "支付宝支付回调（不需要认证）")
    @PostMapping("/alipaynotify")
    @UnAuth
    @ApiIgnore
    public void aliPayNotify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (orderPayService.aliPayNotify(request.getParameterMap()) > 0) {
            sendMessage(response, AliPayUtils.SUCCESS);
        }
    }

    /**
     * 2c2p REDIRECT支付
     *
     * @param type      支付类型 1 订单支付 2 预存款充值 3 门店订单支付
     * @param orderCode 订单code
     * @param orderType 订单类型
     * @param orderId   订单id
     */
    @GetMapping("/toc2cppay")
    @ApiOperation(value = "2c2p REDIRECT支付", notes = "2c2p REDIRECT支付（需要认证）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "orderType", value = "订单类型"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "orderId", value = "订单id"),
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "type", value = "1 订单支付 2 预存款充值 3 门店订单支付"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "orderCode", value = "订单code"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = " 返回码和2C2P支付的html 返回码说明  -1:用户不存在 -3:没有待支付的订单 -5:支付宝生成订单出错 -7 没有设置网站地址 -8 缺少配置  -9 没有启用 1 成功", response = String.class)
    })
    public AjaxResult toC2cpPay(@NotNull PayParam payParam, HttpServletRequest request) {
        return AjaxResult.success(orderPayService.p2c2pRedirectPay(payParam.getOrderCode(), AppletsLoginUtils.getInstance().getCustomerId(request), payParam.getType(), payParam.getOrderType(), payParam.getOrderId()));
    }

    /**
     * 2c2p支付回调
     */
    @ApiOperation(value = "2c2p支付回调", notes = "2c2p支付回调（不需要认证）")
    @PostMapping("/p2c2pnotify")
    @UnAuth
    @ApiIgnore
    public void p2c2pnotify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (orderPayService.p2c2pNotify(request.getParameterMap()) > 0) {
            //TODO debug 2c2p
            sendMessage(response, WechatUtils.SUCCESS_RETURN);
        }
    }

    /**
     * DLOCAL REDIRECT支付
     *
     * @param type      支付类型 1 订单支付 2 预存款充值 3 门店订单支付
     * @param orderCode 订单code
     * @param orderType 订单类型
     * @param orderId   订单id
     * @param token   DLocal要求的加密token
     */
    @GetMapping("/todlocalpay")
    @ApiOperation(value = "DLOCAL REDIRECT支付", notes = "DLOCAL REDIRECT支付（需要认证）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "orderType", value = "订单类型"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "orderId", value = "订单id"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "token", value = "DLocal要求的加密token"),
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "type", value = "1 订单支付 2 预存款充值 3 门店订单支付"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "orderCode", value = "订单code"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = " 返回码和DLOCAL支付的html 返回码说明  -1:用户不存在 -3:没有待支付的订单 -5:DLocal生成订单出错 -7 没有设置网站地址 -8 缺少配置  -9 没有启用 1 成功", response = String.class)
    })
    public AjaxResult toDlocalSmartFieldPay(@NotNull DLocalPayParam payParam, HttpServletRequest request) {

        DLocalPayerObject payer = DLocalPayerObject.builder()
                .name(payParam.getName())
                .email(payParam.getEmail())
                .birthDate(payParam.getBirthDate())
                .phone(payParam.getPhone())
                .build();

        return AjaxResult.success(orderPayService.dlocalPay(payParam.getOrderCode(),
                payParam.getCurrency(),
                payParam.getToken(),
                AppletsLoginUtils.getInstance().getCustomerId(request),
                payParam.getType(), payParam.getOrderType(),
                payParam.getOrderId(),
                payer));
    }

    /**
     * dlocal支付回调
     */
    @ApiOperation(value = "dlocal支付回调", notes = "dlocal支付回调（不需要认证）")
    @PostMapping("/dlocanotify")
    @UnAuth
    @ApiIgnore
    public void dlocalnotify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (orderPayService.p2c2pNotify(request.getParameterMap()) > 0) {
            sendMessage(response, WechatUtils.SUCCESS_RETURN);
        }
    }

    /**
     * ZOTAPAY 支付
     *
     * @param type      支付类型 1 订单支付 2 预存款充值 3 门店订单支付
     * @param orderCode 订单code
     * @param orderType 订单类型
     * @param orderId   订单id
     */
    @GetMapping("/tozotapay")
    @ApiOperation(value = "Zotapay 支付", notes = "Zotapay支付（需要认证）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "orderType", value = "订单类型"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "orderId", value = "订单id"),
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "type", value = "1 订单支付 2 预存款充值 3 门店订单支付"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "orderCode", value = "订单code"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = " 返回码 返回码说明  -1:用户不存在 -3:没有待支付的订单 -5:支付宝生成订单出错 -7 没有设置网站地址 -8 缺少配置  -9 没有启用 1 成功", response = String.class)
    })
    public AjaxResult toZotapay(@NotNull ZotaPayParam payParam, HttpServletRequest request) {

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

        return AjaxResult.success(orderPayService.zotaPay(
                payParam.getOrderCode(),
                payParam.getCurrency(),
                AppletsLoginUtils.getInstance().getCustomerId(request),
                payParam.getType(),
                payParam.getOrderType(),
                payParam.getOrderId(),
                payer));
    }

    /**
     * zotapay支付回调
     */
    @ApiOperation(value = "zotapay支付回调", notes = "zotapay支付回调（不需要认证）")
    @PostMapping("/zotapaynotify")
    @UnAuth
    @ApiIgnore
    public void zotapaynotify(@RequestBody ZotaPayCallbackNotification notification, HttpServletResponse response) {
        if (orderPayService.zotaPayNotify(notification)>0) {
            sendMessage(response, WechatUtils.SUCCESS_RETURN);
        }
    }

    /**
     * 从第三方支付接口查询订单状态并更改订单状态
     */
    @ApiOperation(value = "从第三方支付接口查询订单状态并更改订单状态", notes = "从第三方支付接口查询订单状态并更改订单状态（需要认证）")
    @GetMapping("/queryAndProcessZotaPayOrder")
    public AjaxResult queryAndProcessZotaPayOrder(@RequestParam("channel") String channel,
                                        @RequestParam("orderID") String orderID,
                                        @RequestParam("merchantOrderID") String merchantOrderID){
        if("zotapay".equalsIgnoreCase(channel)) {
            CommonResponse<String> res = orderPayService.queryAndProcessZotaPayOrder(orderID, merchantOrderID);
            if(res.getFlag()==1){
                return AjaxResult.success("success", res.getData());
            }else{
                log.error("queryAndProcessZotaPayOrder failed!");
                return AjaxResult.error("failed");
            }
        }else{
            return AjaxResult.error("not implemented!");
        }
    }

    /**
     * 从第三方支付接口查询订单状态
     */
    @ApiOperation(value = "从第三方支付接口查询订单状态", notes = "从第三方支付接口查询订单状态（需要认证）")
    @GetMapping("/queryPayOrder")
    public AjaxResult queryPayOrder(@RequestParam("channel") String channel,
                                        @RequestParam("orderID") String orderID,
                                        @RequestParam("merchantOrderID") String merchantOrderID){
        if("zotapay".equalsIgnoreCase(channel)) {
            CommonResponse<String> res = orderPayService.queryZotaPayOrder(orderID, merchantOrderID);
            if(res.getFlag()==1){
                return AjaxResult.success("success", res.getData());
            }else{
                log.error("queryPayOrder failed!");
                return AjaxResult.error("failed");
            }
        }else{
            return AjaxResult.error("not implemented!");
        }
    }

    /**
     * PAYPAL 支付
     *
     * @param type      支付类型 1 订单支付 2 预存款充值 3 门店订单支付
     * @param orderCode 订单code
     * @param orderType 订单类型
     * @param orderId   订单id
     */
    @GetMapping("/topaypal")
    @ApiOperation(value = "Paypal 支付", notes = "Paypal支付（需要认证）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "orderType", value = "订单类型"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "orderId", value = "订单id"),
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "type", value = "1 订单支付 2 预存款充值 3 门店订单支付"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "orderCode", value = "订单code"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = " 返回码 返回码说明  -1:用户不存在 -3:没有待支付的订单 -5:支付宝生成订单出错 -7 没有设置网站地址 -8 缺少配置  -9 没有启用 1 成功", response = String.class)
    })
    public AjaxResult toPaypal(PayPalParam payParam, HttpServletRequest request) {

        return AjaxResult.success(orderPayService.paypalPay(
                payParam.getOrderCode(),
                payParam.getCurrency(),
                AppletsLoginUtils.getInstance().getCustomerId(request),
                payParam.getType(),
                payParam.getOrderType(),
                payParam.getOrderId()));
    }

    /**
     * PAYPAL Capture查询订单执行情况（未测试）
     *
     * @param type      支付类型 1 订单支付 2 预存款充值 3 门店订单支付
     * @param orderCode 订单code
     * @param orderType 订单类型
     * @param orderId   订单id
     */
    @GetMapping("/paypalcapture")
    @ApiOperation(value = "Paypal Capture", notes = "Paypal Capture（需要认证）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "orderType", value = "订单类型"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "orderId", value = "订单id"),
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "type", value = "1 订单支付 2 预存款充值 3 门店订单支付"),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "orderCode", value = "订单code"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = " 返回码 返回码说明  -1:用户不存在 -3:没有待支付的订单 -5:支付宝生成订单出错 -7 没有设置网站地址 -8 缺少配置  -9 没有启用 1 成功", response = String.class)
    })
    public AjaxResult toPaypalCapture(PayParam payParam, HttpServletRequest request) {
        return AjaxResult.success(orderPayService.paypalCapture(payParam));
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
     * 回传信息
     *
     * @param message 信息
     */
    private void sendMessage(HttpServletResponse response, String message) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.println(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }

    }


}

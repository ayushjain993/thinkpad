package io.uhha.web.controller.settlement;

import io.swagger.annotations.ApiOperation;
import io.uhha.common.annotation.UnAuth;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.utils.bean.ZotaPayCallbackNotification;
import io.uhha.common.utils.bean.ZotaPayPayerObject;
import io.uhha.common.utils.bean.ZotaPayQueryResponseData;
import io.uhha.order.vo.ZotaPayParam;
import io.uhha.settlement.service.IWithdrawPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 转出Controller
 *
 * @author uhha
 * @date 2021-12-31
 */
@RestController
@RequestMapping("/settlement/payout")
@Slf4j
public class PayoutController {

    @Autowired
    private IWithdrawPayService withdrawPayService;


    /**
     * ZOTAPAY 支付
     *
     */
    @GetMapping("/zotapay")
    @ApiOperation(value = "Zotapay 支付", notes = "Zotapay支付（需要认证）")
    public AjaxResult toZotapay(ZotaPayParam payParam, HttpServletRequest request) {

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
                .customerBankCode(payParam.getCustomerBankCode())
                .customerBankAccountNumber(payParam.getCustomerBankAccountNumber())
                .customerBankAccountName(payParam.getCustomerBankAccountName())
                .build();

        return AjaxResult.success(withdrawPayService.zotaPayout(
                Long.parseLong(payParam.getOrderCode()),
                payParam.getCurrency(),
                payParam.getCustomerId(),
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
    public int zotapaynotify(@RequestBody ZotaPayCallbackNotification notification, HttpServletResponse response) {
        return withdrawPayService.zotaPayoutNotify(notification);
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
            ZotaPayQueryResponseData responseData = withdrawPayService.queryZotaPayOrder(orderID, merchantOrderID);
            if("APPROVED".equalsIgnoreCase(responseData.getStatus())){
                return AjaxResult.success("success",responseData);
            }else{
                log.error("queryZotaPayOrder failed!");
                return AjaxResult.error("failed");
            }
        }else{
            log.error("not implemented payment channel: {}!", channel);
            return AjaxResult.error("not implemented!");
        }
    }
}

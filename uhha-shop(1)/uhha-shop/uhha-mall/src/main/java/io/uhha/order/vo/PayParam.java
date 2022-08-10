package io.uhha.order.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.*;
import io.uhha.common.utils.bean.DLocalPayerObject;
import io.uhha.common.utils.bean.ZotaPayPayerObject;
import lombok.Data;

import java.io.Serializable;

@Data
public class PayParam implements Serializable {
    /**
     * 订单类型
     */
    String orderType;

    /**
     * 订单id
     */
    String orderId;

    /**
     * 支付类型 1 订单支付 2 充值订单 3 门店订单支付
     */
    int type;

    /**
     * 订单code
     */
    String orderCode;


}

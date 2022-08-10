package io.uhha.common.utils.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Card 对象
 * 对于信用卡付款，除非您 完全PCI DSS合规 您才可以使用信用卡信息。否则您需要使用 Smart Fields 来收集信用卡信息。对于周期性付款（如按月支付会员费等），您可以先 保存银行卡 ，然后使用card_id来调用信用卡。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DLocalCreditCardObject implements Serializable {
    /**
     * 持卡人全名， 没有提供token 和 card_id 时必填。
     */
    @JSONField(name="holder_name")
    String holderName;

    /**
     * 两位数字，表示银行卡有效期月份。 没有提供token 和 card_id 时必填
     */
    @JSONField(name = "expiration_month")
    Integer expirationMonth;

    /**
     * 四位数字，表示银行卡有效期年份。 没有提供token 和 card_id 时必填。
     */
    @JSONField(name = "expiration_year")
    Integer expirationYear;

    /**
     * 卡号，不能包含任何分隔符。 没有提供encrypted_data，token 和 card_id 时必填。
     */
    @JSONField(name = "number")
    String number;

    /**
     * 三位数字信用卡安全码， 选填，印度必填。
     */
    @JSONField(name = "cvv")
    String cvv;

    /**
     * JWE 加密后的参数，选填。
     */
    @JSONField(name = "encrypted_data")
    String encryptedData;

    /**
     * 使用Smart Fields生成的用于安全通信的信用卡Token， 选填。
     */
    @JSONField(name = "token")
    String token;

    /**
     * 使用CVV-only Smart Fields生成的用于安全通信的CVV Token. 选填。
     */
    @JSONField(name = "cvv_token")
    String cvvToken;

    /**
     * 通过创建信用卡方法响应中获得的信用卡ID， 选填。
     */
    @JSONField(name = "card_id")
    String cardId;

    /**
     * 分期付款期数（1-12期），默认值1，选填。
     */
    @JSONField(name = "installments")
    String installments;

    /**
     * 已创建好的分期付款计划ID，选填。
     */
    @JSONField(name = "installments_id")
    String installmentsId;

    /**
     * 动态付款描述，选填。
     */
    @JSONField(name = "descriptor")
    String descriptor;

    /**
     * 是否立即获取付款。 如果为false，则此付款会请求预授权（Authorization)，并且需要后续捕获（Capture）付款。 默认值 TRUE. 选填。
     */
    @JSONField(name = "capture")
    Boolean capture;

    /**
     * 是否保存银行卡以便后续发起更多付款。如为“ture“，响应中将会返回card_id。。
     */
    @JSONField(name = "save")
    Boolean save;
}

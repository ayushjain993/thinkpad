package io.uhha.order.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.uhha.common.utils.bean.DLocalPayerObject;
import io.uhha.common.utils.bean.ZotaPayPayerObject;
import lombok.Data;

import java.io.Serializable;

@Data
public class DLocalPayParam extends PayParam implements Serializable {


    /**
     * 币种
     */
    String currency;

    /**
     * dlocal支付使用smartFields方案调研所返回的
     */
    String token;

    /**
     * DLocal要求的付款人信息
     */
    @JSONField(name = "name")
    String name;

    @JSONField(name = "email")
    String email;

    @JSONField(name = "birth_date")
    String birthDate;

    @JSONField(name = "phone")
    String phone;

    @JSONField(name = "document")
    String document;

    @JSONField(name = "document2")
    String document2;

    @JSONField(name = "user_reference")
    String userReference;

    @JSONField(name = "ip")
    String ip;

    @JSONField(name = "device_id")
    String deviceId;

}

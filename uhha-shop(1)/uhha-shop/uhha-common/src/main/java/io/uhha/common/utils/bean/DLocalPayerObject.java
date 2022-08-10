package io.uhha.common.utils.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DLocalPayerObject implements Serializable {

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

    @JSONField(name = "address")
    DLocalAddressObject address;
}
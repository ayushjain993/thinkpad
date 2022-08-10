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
public class DLocalAddressObject implements Serializable {
    @JSONField(name = "state")
    String state;
    @JSONField(name = "city")
    String city;
    @JSONField(name = "zip_code")
    String zipCode;
    @JSONField(name = "street")
    String street;
    @JSONField(name = "number")
    String number;
}

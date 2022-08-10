package io.uhha.common.utils.bean;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ZotaPayResponse implements Serializable {
    private String code;
    private String message;
    private Object data;
}


package io.uhha.common.utils.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Direct Debit 对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DLocalDirectDebt implements Serializable {

    /**
     * 银行账户所有人姓名，必填。
     */
    @JSONField(name="holder_name")
    String holderName;

    /**
     * 银行账户所有人邮箱，必填。
     */
    @JSONField(name="email")
    String email;

    /**
     * 银行账户所有人账户编号，必填。
     */
    @JSONField(name = "document")
    String document;

    /**
     * 银行账户所有人CBU ，仅阿根廷必填。
     */
    @JSONField(name = "cbu")
    String cbu;
}

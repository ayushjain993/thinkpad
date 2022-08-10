package io.uhha.common.utils.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZotaPayCustomParam {
    /**
     * 支付类型
     */
    String type;
}

package io.uhha.order.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyBackOrderParams {

    /**
     * 用户id
     */
    private Long customerId;

    /**
     * 订单id
     */
    @NotNull
    private Long orderId;

    /**
     * 原因
     */
    @NotNull
    private String reason;

    /**
     * 详细描述
     */
    private String description;

    private String evidence;
}

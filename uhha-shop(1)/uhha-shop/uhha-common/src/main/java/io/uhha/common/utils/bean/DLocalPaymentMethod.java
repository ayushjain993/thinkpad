package io.uhha.common.utils.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 付款方式对象
 * 此对象定义了dLocal所接受的所有付款方式
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DLocalPaymentMethod implements Serializable {
    String id;
    String type;
    String name;
    String[] countries;
    String logo;
    String[] allowedFlows;
}
package io.uhha.order.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.uhha.common.annotation.Excel;
import io.uhha.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付流水对象 oms_trans_records
 *
 * @author mj
 * @date 2020-07-24
 */
@Data
@Builder
@ApiModel(description = "支付流水对象")
public class OmsTransRecords extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    private Long id;

    /**
     * 所属店铺
     */
    @Excel(name = "所属店铺")
    private Long storeId;

    /**
     * 系统内部订单id
     */
    @Excel(name = "系统内部订单id")
    private String lsTransCode;
    /**
     * 外部系统交易流水号
     */
    @Excel(name = "外部系统交易流水号")
    private String transCode;
    /**
     * 支付流水类型 1 门店订单 2 订单 3 预存款
     * @see io.uhha.common.enums.PayTransTypeEnum
     */
    @Excel(name = "类型 1 门店订单 2 订单 3 预存款")
    private String type;
    /**
     * 交易金额
     */
    @Excel(name = "交易金额")
    private BigDecimal money;
    /**
     * 订单id
     */
    @Excel(name = "订单id")
    private Long orderId;
    /**
     * 支付方式
     */
    @Excel(name = "支付方式")
    private String channel;
    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel(name = "支付时间", width = 30, dateFormat = "yyyy-MM-dd ")
    private Date payTime;

    @Tolerate
    public OmsTransRecords() {
    }


}

package io.uhha.order.vo;


import io.uhha.common.enums.BackOrderStatusEnum;
import io.uhha.common.enums.BackOrderTypeEnum;
import io.uhha.common.utils.IteratorUtils;
import io.uhha.order.domain.OmsBackOrder;
import io.uhha.order.domain.OmsOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * Created by mj on 17/11/15.
 * 申请退货请求参数
 */
@Data
@ApiModel(description = "申请退货请求参数")
public class ApplyReturnParams {

    /**
     * 会员id
     */
    @ApiModelProperty(value = "会员id")
    private Long customerId;

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    private Long orderId;

    /**
     * 退货原因
     */
    @ApiModelProperty(value = "退货原因")
    private String reason;

    /**
     * 退货说明
     */
    @ApiModelProperty(value = "退货说明")
    private String desc;

    /**
     * 凭据
     */
    @ApiModelProperty(value = "凭据")
    private String credential;

    /**
     * 凭据的图片
     */
    @ApiModelProperty(value = "凭据的图片")
    private String pics;

    /**
     * 退货的单品
     */
    @ApiModelProperty(value = "退货的单品")
    private List<ReturnSku> returnSkus;

    /**
     * 构造退货实体
     *
     * @param order         订单
     * @param backOrderCode 退单号
     * @return 返回退货实体
     */
    public OmsBackOrder buildBackOrder(OmsOrder order, String backOrderCode) {
        if (Objects.isNull(order)) {
            return null;
        }

        OmsBackOrder backOrder = new OmsBackOrder();
        backOrder.setBackCode(backOrderCode);
        backOrder.setOrderId(order.getId());
        backOrder.setOrderCode(order.getOrderCode());
        backOrder.setStoreId(order.getStoreId());
        backOrder.setSkuidNums(getSkuIds());
        backOrder.setCustomerId(order.getCustomerId());
        backOrder.setType(BackOrderTypeEnum.RETURN_GOODS.getCode());
        backOrder.setPics(this.pics);
        backOrder.setReason(this.reason);
        backOrder.setDesc(this.desc);
        backOrder.setCredential(this.credential);
        backOrder.setBackType(BackOrderTypeEnum.RETURN_FUND.getCode());
        backOrder.setBackPrice(getBackPrice(order));
        backOrder.setStatus(BackOrderStatusEnum.RETURN_APPLIED.getCode());
        backOrder.setPredepositPay(order.getPredepositPay());
        return backOrder;
    }

    /**
     * 获得退单的总价格
     *
     * @param order 订单
     * @return 返回退单的总价格
     */
    private BigDecimal getBackPrice(OmsOrder order) {
        IteratorUtils.zip(this.returnSkus, order.getOrderSkus(), (returnSku, orderSku) -> returnSku.getSkuId().equals(orderSku.getSkuId()),
                (returnSku1, orderSku1) -> returnSku1.setPrice(orderSku1.getUnitPrice().multiply(new BigDecimal(returnSku1.getNum()))));

        return this.returnSkus.stream().map(ReturnSku::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 获得退货的单品信息
     *
     * @return 返回退货的单品信息
     */
    private String getSkuIds() {
        if (CollectionUtils.isEmpty(this.returnSkus)) {
            return "";
        }
        return this.returnSkus.stream().map(returnSku -> new StringBuilder(returnSku.getSkuId() + "-" + returnSku.getNum() + ",")).reduce(new StringBuilder(""), StringBuilder::append).toString();
    }
}

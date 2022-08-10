package io.uhha.order.task;

import io.uhha.common.utils.DateUtils;
import io.uhha.order.service.OrderServiceApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("orderTask")
@Slf4j
public class OrderExecTask {
    @Autowired
    private OrderServiceApi orderServiceApi;

    public void autoConfirmReceipt(){
        log.info("autoConfirmReceipt starts at {} ", DateUtils.dateTimeNow());
        orderServiceApi.autoConfirmReceipt();
        log.info("autoConfirmReceipt ends at {} ", DateUtils.dateTimeNow());
    }

    public void autoCancelOrder(){
        log.info("autoCancelOrder starts at {} ", DateUtils.dateTimeNow());
        orderServiceApi.autoCancelOrder();
        log.info("autoCancelOrder ends at {} ", DateUtils.dateTimeNow());
    }
}

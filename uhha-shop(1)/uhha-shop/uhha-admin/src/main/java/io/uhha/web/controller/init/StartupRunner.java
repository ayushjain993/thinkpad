package io.uhha.web.controller.init;

import io.uhha.coin.market.service.impl.AutoMarket;
import io.uhha.coin.system.service.ICoinRedisInitService;
import io.uhha.system.service.IMallRedisInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private ICoinRedisInitService coinRedisInitService;

    @Autowired
    private IMallRedisInitService mallRedisInitService;

    @Autowired
    private AutoMarket autoMarket;

    @Override
    public void run(String... args) throws Exception {
        coinRedisInitService.redisInit();
        mallRedisInitService.redisInit();
        autoMarket.init();
    }
}

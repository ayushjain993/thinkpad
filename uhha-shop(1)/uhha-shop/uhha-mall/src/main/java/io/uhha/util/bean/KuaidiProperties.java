package io.uhha.util.bean;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class KuaidiProperties {
    @Value("${kuaidi.key}")
    private String key;

    @Value("${kuaidi.customer}")
    private String customer;

    @Value("${kuaidi.secret}")
    private String secret;

    @Value("${kuaidi.userid}")
    private String userid;
}

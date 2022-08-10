package io.uhha;

import io.uhha.coin.system.service.ICoinRedisInitService;
import io.uhha.common.utils.spring.SpringUtils;
import io.uhha.system.service.IMallRedisInitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 启动程序
 *
 * @author ruoyi
 */
@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class AdminApplication {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(AdminApplication.class, args);

        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");

        log.info("\n----------------------------------------------------------\n\t" +
                "Application AppletsApp is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost:" + port  + "/\n\t" +
                "External: \thttp://" + ip + ":" + port  + "/\n\t" +
                "swagger-ui: \thttp://" + ip + ":" + port  + "/swagger-ui/index.html\n\t" +
                "----------------------------------------------------------\n\t");
        log.info("(♥◠‿◠)ﾉﾞ  系统管理后台启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}

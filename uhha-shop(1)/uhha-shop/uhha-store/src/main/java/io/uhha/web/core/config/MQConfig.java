package io.uhha.web.core.config;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import io.uhha.coin.common.framework.mq.ScoreHelper;
import io.uhha.coin.common.mq.MQTopic;
import io.uhha.coin.common.properties.MQProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;


@Configuration
@EnableConfigurationProperties(MQProperties.class)
public class MQConfig {

    @Bean
    public MQTopic mqTopic() {
        return new MQTopic();
    }

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public ProducerBean validateProducer(MQProperties mqProperties) {
        ProducerBean producerBean = new ProducerBean();
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.AccessKey, mqProperties.getAccessKey());
        properties.setProperty(PropertyKeyConst.SecretKey, mqProperties.getSecretKey());
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, mqProperties.getOnsAddr());
        properties.setProperty(PropertyKeyConst.GROUP_ID, mqProperties.getPid().getAdminAction());
        producerBean.setProperties(properties);
        return producerBean;
    }

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public ProducerBean adminActionProducer(MQProperties mqProperties) {
        ProducerBean producerBean = new ProducerBean();
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.AccessKey, mqProperties.getAccessKey());
        properties.setProperty(PropertyKeyConst.SecretKey, mqProperties.getSecretKey());
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, mqProperties.getOnsAddr());
        properties.setProperty(PropertyKeyConst.GROUP_ID, mqProperties.getPid().getAdminAction());
        producerBean.setProperties(properties);
        return producerBean;
    }

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public ProducerBean userActionProducer(MQProperties mqProperties) {
        ProducerBean producerBean = new ProducerBean();
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.AccessKey, mqProperties.getAccessKey());
        properties.setProperty(PropertyKeyConst.SecretKey, mqProperties.getSecretKey());
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, mqProperties.getOnsAddr());
        properties.setProperty(PropertyKeyConst.GROUP_ID, mqProperties.getPid().getUserAction());
        producerBean.setProperties(properties);
        return producerBean;
    }


    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public ProducerBean scoreProducer(MQProperties mqProperties) {
        ProducerBean producerBean = new ProducerBean();
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.AccessKey, mqProperties.getAccessKey());
        properties.setProperty(PropertyKeyConst.SecretKey, mqProperties.getSecretKey());
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, mqProperties.getOnsAddr());
        properties.setProperty(PropertyKeyConst.GROUP_ID, mqProperties.getPid().getScore());
        producerBean.setProperties(properties);
        return producerBean;
    }

    @Bean
    public ScoreHelper scoreHelper(ProducerBean scoreProducer) {
        ScoreHelper scoreHelper = new ScoreHelper();
        scoreHelper.setScoreProducer(scoreProducer);
        return scoreHelper;
    }

}

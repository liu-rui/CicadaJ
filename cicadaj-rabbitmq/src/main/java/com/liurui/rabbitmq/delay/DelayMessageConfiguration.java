package com.liurui.rabbitmq.delay;

import com.liurui.rabbitmq.delay.config.DelayMessageProperties;
import com.liurui.rabbitmq.delay.producer.DelayMessageProducer;
import com.liurui.rabbitmq.delay.producer.DelayMessageProducerImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liu-rui
 * @date 2019-08-27 16:41
 * @description
 */
@Configuration
@EnableConfigurationProperties({DelayMessageProperties.class})
public class DelayMessageConfiguration {

    @Bean
    public DelayMessageProducer delayMessageProducer() {
        return new DelayMessageProducerImpl();
    }

    @Bean
    public DelayMessageInit delayMessageInit() {
        return new DelayMessageInit();
    }
}

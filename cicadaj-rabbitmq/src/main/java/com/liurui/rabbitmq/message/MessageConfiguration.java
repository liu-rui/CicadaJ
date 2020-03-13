package com.liurui.rabbitmq.message;

import com.liurui.rabbitmq.message.config.MessageProperties;
import com.liurui.rabbitmq.message.producer.MessageProducer;
import com.liurui.rabbitmq.message.producer.MessageProducerImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liu-rui
 * @date 2019-08-27 16:54
 * @description
 */
@Configuration
@EnableConfigurationProperties(MessageProperties.class)
public class MessageConfiguration {
    @Bean
    public MessageProducer messageProducer() {
        return new MessageProducerImpl();
    }

    @Bean
    public MessageInit messageInit() {
        return new MessageInit();
    }
}

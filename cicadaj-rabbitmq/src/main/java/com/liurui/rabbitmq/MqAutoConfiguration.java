package com.liurui.rabbitmq;

import com.liurui.rabbitmq.delay.DelayMessageConfiguration;
import com.liurui.rabbitmq.message.MessageConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author liu-rui
 * @date 2019-08-23 14:17
 * @description
 */
@Configuration
@Import({MessageConfiguration.class, DelayMessageConfiguration.class})
public class MqAutoConfiguration {
    @Bean
    public FailedMessageLogByFile failedMessageLogByFile() {
        return new FailedMessageLogByFileImpl();
    }
}

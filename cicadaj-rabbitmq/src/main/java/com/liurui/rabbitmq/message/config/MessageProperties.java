package com.liurui.rabbitmq.message.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @author liu-rui
 * @date 2019-08-19 15:14
 * @description
 */
@Data
@ConfigurationProperties("cicadaj.rabbitmq.message")
public class MessageProperties {
    private Map<String, MessageItemProperties> items;

    public void check() {
        if (CollectionUtils.isEmpty(items)) {
            return;
        }

        for (Map.Entry<String, MessageItemProperties> entry : items.entrySet()) {
            entry.getValue().check(entry.getKey());
        }
    }
}

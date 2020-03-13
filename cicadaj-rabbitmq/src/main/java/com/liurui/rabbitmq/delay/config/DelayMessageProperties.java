package com.liurui.rabbitmq.delay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @author liu-rui
 * @date 2019-08-23 18:47
 * @description
 */
@ConfigurationProperties(prefix = "cicadaj.rabbitmq.delay")
@Data
public class DelayMessageProperties {
    private Map<String, DelayMessageItemProperties> items;

    public void check() {
        if (CollectionUtils.isEmpty(items)) {
            return;
        }

        for (Map.Entry<String, DelayMessageItemProperties> entry : items.entrySet()) {
            entry.getValue().check(entry.getKey());
        }
    }
}

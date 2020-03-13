package com.liurui.rabbitmq;

import com.google.common.collect.Maps;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.Map;

/**
 * @author liu-rui
 * @date 2019-08-28 20:07
 * @description
 */
public class MqEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {
    private static final int ORDER = Ordered.LOWEST_PRECEDENCE - 20;

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Map<String, Object> configs = Maps.newHashMap();

        configs.put("management.health.rabbit.enabled", Boolean.FALSE.toString());
        MapPropertySource mapPropertySource = new MapPropertySource(MqEnvironmentPostProcessor.class.getSimpleName(), configs);

        environment.getPropertySources().addFirst(mapPropertySource);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}

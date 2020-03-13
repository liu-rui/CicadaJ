package com.liurui.rabbitmq.web;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.Map;

/**
 * @author liu-rui
 * @date 2019-07-26 10:54
 * @description 生产环境配置保护；
 * 开发人员业务开发时，老是忘记生产环境下一些必要的配置，比如说：
 * swagger必须关闭
 * 通过此类，如果是生产环境，强制开启以上的配置
 */
public class ProdConfigProtectEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {
    private static final int ORDER = Ordered.LOWEST_PRECEDENCE - 10;

    /**
     * 生产环境profile
     */
    private static final String PROD_PROFILE = "prod";

    /**
     * [生产环境配置保护]配置的key
     */
    private static final String PROD_CONFIG_PROTECTED_KEY = "cicadaj.web.prodConfigProtected";


    /**
     * [swagger启动]配置的key
     */
    private static final String SWAGGER_ENABLED_KEY = "cicadaj.web.swagger.enabled";


    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        //不是生产环境，退出
        if (!environment.acceptsProfiles(PROD_PROFILE)) {
            return;
        }
        //未开启生产环境配置保护，退出
        if (!StringUtils.equalsIgnoreCase(Boolean.TRUE.toString(),
                environment.getProperty(PROD_CONFIG_PROTECTED_KEY, Boolean.TRUE.toString()))) {
            return;
        }

        Map<String, Object> configs = Maps.newHashMap();

        configs.put(SWAGGER_ENABLED_KEY, Boolean.FALSE.toString());
        MapPropertySource mapPropertySource = new MapPropertySource(ProdConfigProtectEnvironmentPostProcessor.class.getSimpleName(), configs);

        environment.getPropertySources().addFirst(mapPropertySource);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}

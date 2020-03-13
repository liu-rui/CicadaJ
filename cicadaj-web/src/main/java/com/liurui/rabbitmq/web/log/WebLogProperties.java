package com.liurui.rabbitmq.web.log;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * @author: 刘锐
 * @date: 18-12-24 下午7:30
 * @description: controller层日志配置属性
 */
@ConfigurationProperties("cicadaj.web.log")
@Data
public class WebLogProperties {
    /**
     * 慢查询操作临界间隔
     */
    @DurationUnit(ChronoUnit.MILLIS)
    private Duration slowQueryLimit = Duration.ofSeconds(1);
}
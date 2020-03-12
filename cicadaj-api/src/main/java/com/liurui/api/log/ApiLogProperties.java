package com.liurui.api.log;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * @author: 刘锐
 * @date: 19-06-24 下午19:21
 * @description: 日志配置类
 */
@ConfigurationProperties("cicadaj.api.log")
@Data
public class ApiLogProperties {
    /**
     * 慢查询操作临界间隔
     */
    @DurationUnit(ChronoUnit.MILLIS)
    private Duration slowQueryLimit = Duration.ofSeconds(1);
}

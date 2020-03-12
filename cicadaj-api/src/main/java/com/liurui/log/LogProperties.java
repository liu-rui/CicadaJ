package com.liurui.log;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: 刘锐
 * @date: 19-06-24 下午19:21
 * @description: 日志配置类
 */
@ConfigurationProperties("daling.log")
@Data
public class LogProperties {
    /**
     * 对象序列化为字符串最大长度，如果大于此长度将不会产生日志
     */
    private int objectMaxLength = 1024;
}

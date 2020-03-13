package com.liurui.rabbitmq.delay.config;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * rabbitmq 配置文件类
 *
 * @author 刘锐
 * @date 2018-11-19 11:07:17
 */
@Data
public class DelayMessageItemProperties {

    public static final String MASTER = "master";
    /**
     * 主机名
     */
    private String host;
    /**
     * 端口
     */
    private Integer port;

    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;

    /**
     * 虚拟目录
     */
    private String virtualHost;

    /**
     * 队列名前缀
     */
    private String queueNamePrefix;

    /**
     * 最小并发消费数
     */
    private Integer concurrentConsumers = 1;

    /**
     * 最大并发消费数
     */
    private Integer maxConcurrentConsumers = 5;

    /**
     * 预读取数量
     */
    private Integer prefetchCount = 1;

    /**
     * 在重试期间返回失败时，日志级别是否标记为Error
     */
    private boolean errorLogLevelWhenRetryFailed = true;

    /**
     * 尝试
     */
    private RetryProperties retry = new RetryProperties();


    public String getMasterQueueName() {
        return queueNamePrefix + "master_queue";
    }

    public String getDelayQueueName() {
        return queueNamePrefix + "delay_queue";
    }

    public String getDeadLetterExchange() {
        return queueNamePrefix + "dead_letter_exchange";
    }

    public String getMasterRoutingKey() {
        return MASTER;
    }

    public void check(String key) {
        if (StringUtils.isBlank(host)) {
            throw new IllegalArgumentException(String.format("延迟队列的【主机名】不能为空，请配置cicadaj.rabbitmq.delay.%s.host", key));
        }

        if (Objects.isNull(port)) {
            throw new IllegalArgumentException(String.format("延迟队列的【端口】不能为空，请配置cicadaj.rabbitmq.delay.%s.port", key));
        }

        if (StringUtils.isBlank(virtualHost)) {
            throw new IllegalArgumentException(String.format("延迟队列的【虚拟目录】不能为空，请配置cicadaj.rabbitmq.delay.%s.virtualHost", key));
        }

        if (StringUtils.isBlank(queueNamePrefix)) {
            throw new IllegalArgumentException(String.format("延迟队列的【队列名前缀】不能为空，请配置cicadaj.rabbitmq.delay.%s.queueNamePrefix", key));
        }

        if (Objects.isNull(concurrentConsumers)) {
            throw new IllegalArgumentException(String.format("延迟队列的【最小并发消费数】不能为空，请配置cicadaj.rabbitmq.delay.%s.concurrentConsumers", key));
        }

        if (Objects.isNull(maxConcurrentConsumers)) {
            throw new IllegalArgumentException(String.format("延迟队列的【最大并发消费数】不能为空，请配置cicadaj.rabbitmq.delay.%s.maxConcurrentConsumers", key));
        }

        if (retry != null) {
            retry.check(key);
        }
    }
}

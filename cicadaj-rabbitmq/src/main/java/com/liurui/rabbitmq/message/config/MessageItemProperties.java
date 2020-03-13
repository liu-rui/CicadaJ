package com.liurui.rabbitmq.message.config;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;

import java.util.Objects;

/**
 * @author liu-rui
 * @date 2019-08-19 15:11
 * @description
 */
@Data
public class MessageItemProperties {
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
     * 最小并发消费数
     */
    private Integer concurrentConsumers = 1;

    /**
     * 最大并发消费数
     */
    private Integer maxConcurrentConsumers = 5;

    /**
     * 预读取数量,执行的任务比较耗时时建议调低
     */
    private Integer prefetchCount = AbstractMessageListenerContainer.DEFAULT_PREFETCH_COUNT;


    /**
     * 交换机
     */
    private String exchange;

    /**
     * 路由key
     */
    private String routingKey;

    /**
     * 队列
     */
    private String queue;

    /**
     * 是否持久化
     */
    private boolean durable = true;

    /**
     * 手动ack,默认是true;关闭ack性能能达到几万/s;如果开启的话，性能也就几百/s
     */
    private boolean manualAck = true;

    /**
     * 失败后的保存路径；如果为空的话，失败后会被丢弃的
     */
    private String failedSavePath = "failedMessage.txt";

    public void check(String key) {
        if (StringUtils.isBlank(host)) {
            throw new IllegalArgumentException(String.format("消息队列的【主机名】不能为空，请配置cicadaj.rabbitmq.message.%s.host", key));
        }

        if (Objects.isNull(port)) {
            throw new IllegalArgumentException(String.format("消息队列的【端口】不能为空，请配置cicadaj.rabbitmq.message.%s.port", key));
        }

        if (StringUtils.isBlank(virtualHost)) {
            throw new IllegalArgumentException(String.format("消息队列的【虚拟目录】不能为空，请配置cicadaj.rabbitmq.message.%s.virtualHost", key));
        }

        if (Objects.isNull(concurrentConsumers)) {
            throw new IllegalArgumentException(String.format("消息队列的【最小并发消费数】不能为空，请配置cicadaj.rabbitmq.message.%s.concurrentConsumers", key));
        }

        if (Objects.isNull(maxConcurrentConsumers)) {
            throw new IllegalArgumentException(String.format("消息队列的【最大并发消费数】不能为空，请配置cicadaj.rabbitmq.message.%s.maxConcurrentConsumers", key));
        }

        if (StringUtils.isBlank(exchange)) {
            throw new IllegalArgumentException(String.format("消息队列的【虚拟目录】不能为空，请配置cicadaj.rabbitmq.message.%s.exchange", key));
        }

        if (StringUtils.isBlank(failedSavePath)) {
            throw new IllegalArgumentException(String.format("消息队列的【失败后的保存路径】不能为空，请配置cicadaj.rabbitmq.message.%s.failedSavePath", key));
        }
    }
}

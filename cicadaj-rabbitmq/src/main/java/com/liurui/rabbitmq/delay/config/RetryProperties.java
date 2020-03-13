package com.liurui.rabbitmq.delay.config;

import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.List;

/**
 * @author liu-rui
 * @date 2019/6/25 15:16
 * @description 消费者重试配置属性
 */
@Data
public class RetryProperties {
    /**
     * 是否开启重试次数.默认为开启；当为关闭时，消息执行失败会一直在队列中，一直处于消费状态。
     */
    private boolean enabled = true;

    /**
     * 重试时间间隔列表
     */
    private List<Duration> durations = Lists.newArrayList(Duration.ofSeconds(5),
            Duration.ofSeconds(10),
            Duration.ofSeconds(20));

    /**
     * 超过重试次数时写入文件的保存路径（例如:d:\tmp\1.txt)
     */
    private String failedSavePath = "delay_message_retry_failed.txt";

    public void check(String key) {
        if (!enabled) {
            return;
        }

        if (CollectionUtils.isEmpty(durations)) {
            throw new IllegalArgumentException(String.format("延迟队列启用了重试机制时，【重试时间间隔列表】不能为空，请配置cicadaj.rabbitmq.delay.%s.retry.durations", key));
        }

        if (StringUtils.isBlank(failedSavePath)) {
            throw new IllegalArgumentException(String.format("延迟队列启用了重试机制时，【超过重试次数时写入文件的保存路径】不能为空，请配置cicadaj.rabbitmq.delay.%s.retry.failedSavePath", key));
        }
    }
}

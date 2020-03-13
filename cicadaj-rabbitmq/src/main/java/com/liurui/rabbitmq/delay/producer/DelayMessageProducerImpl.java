package com.liurui.rabbitmq.delay.producer;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Objects;

/**
 * @author liu-rui
 * @date 2019-08-26 9:34
 * @description
 */
@Slf4j
public class DelayMessageProducerImpl implements DelayMessageProducer {
    private Map<String, DelayMessageSender> delayMessageSenderMap = Maps.newHashMap();

    @Override
    public void send(String key, long executeMillis, Object data) {
        Assert.notNull(key, "key is null");
        Assert.notNull(data, "data is null");
        final DelayMessageSender delayMessageSender = delayMessageSenderMap.get(key);

        if (Objects.isNull(delayMessageSender)) {
            throw new IllegalArgumentException("key无效，没有找到对应的延迟队列配置项");
        }

        if (log.isInfoEnabled()) {
            log.info("发送延迟消息 key:{} 执行时间:{} 内容:{}", key, executeMillis, data);
        }
        delayMessageSender.send(executeMillis, data);
    }

    public void add(String key, DelayMessageSender delayMessageSender) {
        Assert.notNull(key, "key  is null");
        Assert.notNull(delayMessageSender, "delayMessageSender is null");
        delayMessageSenderMap.put(key, delayMessageSender);
    }
}

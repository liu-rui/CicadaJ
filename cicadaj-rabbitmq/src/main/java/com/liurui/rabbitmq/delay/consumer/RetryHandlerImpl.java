package com.liurui.rabbitmq.delay.consumer;

import com.liurui.rabbitmq.FailedMessageLogByFile;
import com.liurui.rabbitmq.delay.config.DelayMessageItemProperties;
import com.liurui.rabbitmq.delay.producer.DelayMessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;

/**
 * 重试次数处理类
 *
 * @author 刘锐
 * @date 2019-1-21 14:34:16
 */
@Slf4j
public class RetryHandlerImpl implements RetryHandler {
    private DelayMessageSender delayMessageSender;
    private DelayMessageItemProperties delayMessageItemProperties;
    private FailedMessageLogByFile failedMessageLogByFile;

    public RetryHandlerImpl(DelayMessageSender delayMessageSender,
                            DelayMessageItemProperties delayMessageItemProperties,
                            FailedMessageLogByFile failedMessageLogByFile) {
        this.delayMessageSender = delayMessageSender;
        this.delayMessageItemProperties = delayMessageItemProperties;
        this.failedMessageLogByFile = failedMessageLogByFile;
    }

    @Override
    public boolean exec(Message message) {
        if (delayMessageItemProperties.getRetry() == null || !delayMessageItemProperties.getRetry().isEnabled()) {
            return false;
        }
        int retryTimes = delayMessageSender.getRetry(message);

        if (retryTimes < delayMessageItemProperties.getRetry().getDurations().size()) {
            long duration = delayMessageItemProperties.getRetry().getDurations().get(retryTimes).toMillis();
            long executeMillis = System.currentTimeMillis() + duration;

            delayMessageSender.send(executeMillis, message.getBody(), ++retryTimes);
            return true;
        }
        failedMessageLogByFile.log(delayMessageItemProperties.getRetry().getFailedSavePath(),
                String.format("队列:%s 内容:%s", delayMessageItemProperties.getMasterQueueName(), new String(message.getBody())));
        return false;
    }
}

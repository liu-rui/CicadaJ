package com.liurui.rabbitmq.delay;


import com.google.common.collect.Maps;
import com.liurui.rabbitmq.FailedMessageLogByFile;
import com.liurui.rabbitmq.delay.config.DelayMessageItemProperties;
import com.liurui.rabbitmq.delay.config.DelayMessageProperties;
import com.liurui.rabbitmq.delay.consumer.DelayMessageConsumer;
import com.liurui.rabbitmq.delay.consumer.DelayMessageListener;
import com.liurui.rabbitmq.delay.consumer.RetryHandler;
import com.liurui.rabbitmq.delay.consumer.RetryHandlerImpl;
import com.liurui.rabbitmq.delay.producer.DelayMessageProducer;
import com.liurui.rabbitmq.delay.producer.DelayMessageProducerImpl;
import com.liurui.rabbitmq.delay.producer.DelayMessageSender;
import com.liurui.rabbitmq.delay.producer.DelayMessageSenderImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * rabbitmq配置类
 *
 * @author 刘锐
 * @date 2018-11-19 11:06:50
 */
@Slf4j
public class DelayMessageInit {
    @Autowired
    ConfigurableApplicationContext applicationContext;
    @Autowired
    DelayMessageProperties properties;

    @PostConstruct
    public void init() {
        if (properties == null || CollectionUtils.isEmpty(properties.getItems())) {
            if (log.isWarnEnabled()) {
                log.warn("未发现有效的延迟队列配置项");
            }
            return;
        }
        properties.check();

        if (log.isInfoEnabled()) {
            log.info("发现有{}个延迟队列需要绑定", properties.getItems().size());
        }
        for (Map.Entry<String, DelayMessageItemProperties> entry : properties.getItems().entrySet()) {
            register(entry.getKey(), entry.getValue());
        }
    }

    private void register(String key, DelayMessageItemProperties messageItemProperties) {
        final ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
        CachingConnectionFactory connectionFactory = createCachingConnectionFactory(messageItemProperties);
        final RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        final DirectExchange directExchange = createExchange(rabbitAdmin, messageItemProperties);
        createDeadLetterQueueMap(rabbitAdmin, directExchange, messageItemProperties);
        final Queue masterQueue = createMasterQueue(rabbitAdmin, directExchange, messageItemProperties);
        DelayMessageSender delayMessageSender = createDelayMessageSender(connectionFactory, directExchange);

        registerDelayMessageProducer(beanFactory, delayMessageSender, key);

        if (log.isInfoEnabled()) {
            log.info("绑定延迟队列消息生产端,key:{},交换机:{}", key, directExchange.getName());
        }
        final String delayMessageConsumerName = getDelayMessageConsumerName(key);

        if (beanFactory.containsBean(delayMessageConsumerName)) {
            final RetryHandler retryHandler = createRetryTimesHandler(delayMessageSender, messageItemProperties, beanFactory);
            final DelayMessageListener delayMessageListener = new DelayMessageListener(delayMessageConsumerName,
                    masterQueue,
                    delayMessageSender,
                    beanFactory.getBean(delayMessageConsumerName, DelayMessageConsumer.class),
                    retryHandler,
                    messageItemProperties);

            createMessageListenerContainer(connectionFactory, masterQueue, messageItemProperties, delayMessageListener);

            if (log.isInfoEnabled()) {
                log.info("绑定延迟队列消息消费端,key:{},队列:{},消费端类:{}", key, masterQueue.getName(), delayMessageConsumerName);
            }
        } else {
            if (log.isWarnEnabled()) {
                log.warn("未绑定延迟队列消息消费端,key:{},因为未找到消费端实现类{}", key, delayMessageConsumerName);
            }
        }
    }


    private static CachingConnectionFactory createCachingConnectionFactory(DelayMessageItemProperties messageItemProperties) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        //设置host
        connectionFactory.setHost(messageItemProperties.getHost());
        //设置端口
        connectionFactory.setPort(messageItemProperties.getPort());
        //设置虚拟目录
        connectionFactory.setVirtualHost(messageItemProperties.getVirtualHost());
        //设置用户名
        connectionFactory.setUsername(messageItemProperties.getUserName());
        //设置密码
        connectionFactory.setPassword(messageItemProperties.getPassword());
        return connectionFactory;
    }

    private DirectExchange createExchange(RabbitAdmin rabbitAdmin, DelayMessageItemProperties messageItemProperties) {
        final DirectExchange directExchange = (DirectExchange) ExchangeBuilder.directExchange(messageItemProperties.getDeadLetterExchange())
                .durable(true)
                .build();

        rabbitAdmin.declareExchange(directExchange);
        return directExchange;
    }

    private static void createDeadLetterQueueMap(RabbitAdmin rabbitAdmin, DirectExchange directExchange, DelayMessageItemProperties properties) {
        createDeadLetterQueue(rabbitAdmin, directExchange, properties, "2d", TimeUnit.DAYS.toMillis(2));
        createDeadLetterQueue(rabbitAdmin, directExchange, properties, "1d", TimeUnit.DAYS.toMillis(1));
        createDeadLetterQueue(rabbitAdmin, directExchange, properties, "5h", TimeUnit.HOURS.toMillis(5));
        createDeadLetterQueue(rabbitAdmin, directExchange, properties, "1h", TimeUnit.HOURS.toMillis(1));
        createDeadLetterQueue(rabbitAdmin, directExchange, properties, "15m", TimeUnit.MINUTES.toMillis(15));
        createDeadLetterQueue(rabbitAdmin, directExchange, properties, "5m", TimeUnit.MINUTES.toMillis(5));
        createDeadLetterQueue(rabbitAdmin, directExchange, properties, "1m", TimeUnit.MINUTES.toMillis(1));
        createDeadLetterQueue(rabbitAdmin, directExchange, properties, "15s", TimeUnit.SECONDS.toMillis(15));
        createDeadLetterQueue(rabbitAdmin, directExchange, properties, "5s", TimeUnit.SECONDS.toMillis(5));
        createDeadLetterQueue(rabbitAdmin, directExchange, properties, "2s", TimeUnit.SECONDS.toMillis(2));
        createDeadLetterQueue(rabbitAdmin, directExchange, properties, "1s", TimeUnit.SECONDS.toMillis(1));
    }

    private static Queue createDeadLetterQueue(RabbitAdmin rabbitAdmin, DirectExchange directExchange, DelayMessageItemProperties delayMessageItemProperties, String queueType, long expiresMileSeconds) { //定义延时队列
        Map<String, Object> arguments = Maps.newHashMapWithExpectedSize(3);
        //过期消息转向路由
        arguments.put("x-message-ttl", expiresMileSeconds);
        //过期消息转向路由
        arguments.put("x-dead-letter-exchange", delayMessageItemProperties.getDeadLetterExchange());
        //过期消息转向路由相匹配routingkey
        arguments.put("x-dead-letter-routing-key", delayMessageItemProperties.getMasterRoutingKey());
        final Queue ret = QueueBuilder.durable(delayMessageItemProperties.getDelayQueueName() + "_" + queueType).withArguments(arguments).build();
        final Binding binding = BindingBuilder.bind(ret)
                .to(directExchange)
                .with(queueType);

        rabbitAdmin.declareQueue(ret);
        rabbitAdmin.declareBinding(binding);
        return ret;
    }

    private static Queue createMasterQueue(RabbitAdmin rabbitAdmin, DirectExchange directExchange, DelayMessageItemProperties properties) {
        final Queue masterQueue = QueueBuilder.durable(properties.getMasterQueueName()).build();

        rabbitAdmin.declareQueue(masterQueue);
        final Binding binding = BindingBuilder.bind(masterQueue)
                .to(directExchange)
                .with(properties.getMasterRoutingKey());

        rabbitAdmin.declareBinding(binding);
        return masterQueue;
    }

    private static DelayMessageSender createDelayMessageSender(CachingConnectionFactory connectionFactory, DirectExchange directExchange) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();

        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setExchange(directExchange.getName());
        return new DelayMessageSenderImpl(rabbitTemplate);
    }

    private static void registerDelayMessageProducer(ConfigurableListableBeanFactory beanFactory, DelayMessageSender delayMessageSender, String key) {
        final DelayMessageProducerImpl delayMessageProducer = (DelayMessageProducerImpl) beanFactory.getBean(DelayMessageProducer.class);

        delayMessageProducer.add(key, delayMessageSender);
    }

    private static RetryHandler createRetryTimesHandler(DelayMessageSender delayMessageSender, DelayMessageItemProperties properties, ConfigurableBeanFactory beanFactory) {
        if (properties.getRetry() == null || !properties.getRetry().isEnabled()) {
            return RetryHandler.DEFAULT;
        }
        return new RetryHandlerImpl(delayMessageSender, properties,
                beanFactory.getBean(FailedMessageLogByFile.class));
    }

    private static void createMessageListenerContainer(CachingConnectionFactory connectionFactory,
                                                       Queue masterQueue,
                                                       DelayMessageItemProperties properties,
                                                       DelayMessageListener delayMessageListener) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        //设置要监听的队列
        container.setQueues(masterQueue);
        container.setExposeListenerChannel(true);
        container.setPrefetchCount(properties.getPrefetchCount());
        //设置最大处理消息数
        container.setMaxConcurrentConsumers(properties.getMaxConcurrentConsumers());
        //设置最小处理消息数
        container.setConcurrentConsumers(properties.getConcurrentConsumers());
        //设置手动ACK
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //设置消息监听者
        container.setMessageListener(delayMessageListener);
        container.start();
    }

    private static String getDelayMessageProducerName(String key) {
        return key + "DelayMessageProducer";
    }

    private static String getDelayMessageConsumerName(String key) {
        return key + "DelayMessageConsumer";
    }
}

package com.liurui.rabbitmq.message;


import com.google.common.base.Strings;
import com.liurui.rabbitmq.FailedMessageLogByFile;
import com.liurui.rabbitmq.message.config.MessageItemProperties;
import com.liurui.rabbitmq.message.config.MessageProperties;
import com.liurui.rabbitmq.message.consumer.MessageConsumer;
import com.liurui.rabbitmq.message.consumer.MessageListenerImpl;
import com.liurui.rabbitmq.message.producer.MessageProducer;
import com.liurui.rabbitmq.message.producer.MessageProducerImpl;
import com.liurui.rabbitmq.message.producer.MessageSender;
import com.liurui.rabbitmq.message.producer.MessageSenderImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author liu-rui
 * @date 2019-08-19 15:04
 * @description
 */

@Slf4j
public class MessageInit {
    @Autowired
    ConfigurableApplicationContext applicationContext;
    @Autowired
    MessageProperties messageProperties;

    @PostConstruct
    public void init() {
        if (messageProperties == null || CollectionUtils.isEmpty(messageProperties.getItems())) {
            log.warn("未发现有效的消息队列配置项");
            return;
        }
        messageProperties.check();
        if (log.isInfoEnabled()) {
            log.info("发现有{}个消息队列需要绑定", messageProperties.getItems().size());
        }

        for (Map.Entry<String, MessageItemProperties> entry : messageProperties.getItems().entrySet()) {
            register(entry.getKey(), entry.getValue());
        }
    }

    private void register(String key, MessageItemProperties properties) {
        CachingConnectionFactory connectionFactory = createCachingConnectionFactory(properties);
        AmqpAdmin admin = new RabbitAdmin(connectionFactory);
        final Exchange exchange = createFanoutExchange(properties, admin);

        registerMessageProducer(key, properties, connectionFactory, exchange);

        if (log.isInfoEnabled()) {
            log.info("绑定消息队列消息生产端,key:{},交换机:{}", key, exchange.getName());
        }
        final String messageConsumerName = getMessageConsumerName(key);

        if (applicationContext.containsBean(messageConsumerName)) {
            if (StringUtils.isBlank(properties.getQueue())) {
                throw new IllegalArgumentException(String.format("消息队列的【队列名】不能为空，请配置daling.message.%s.queue", key));
            }
            final Queue queue = createQueue(properties, admin, exchange);

            createSimpleMessageListenerContainer(key, properties, connectionFactory, queue);
            if (log.isInfoEnabled()) {
                log.info("绑定消息队列消息消费端,key:{},队列:{},消费端类:{}", key, queue.getName(), messageConsumerName);
            }
        } else {
            if (log.isWarnEnabled()) {
                log.warn("未绑定消息队列消息消费端,key:{},因为未找到消费端实现类{}", key, messageConsumerName);
            }
        }
    }


    private static CachingConnectionFactory createCachingConnectionFactory(MessageItemProperties properties) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(properties.getHost());
        connectionFactory.setPort(properties.getPort());
        connectionFactory.setUsername(properties.getUserName());
        connectionFactory.setPassword(properties.getPassword());
        connectionFactory.setVirtualHost(properties.getVirtualHost());
        return connectionFactory;
    }

    private static Exchange createFanoutExchange(MessageItemProperties properties, AmqpAdmin admin) {
        Exchange exchange;

        if (Strings.isNullOrEmpty(properties.getRoutingKey())) {
            exchange = new FanoutExchange(properties.getExchange(), true, false);
        } else {
            exchange = new DirectExchange(properties.getExchange(), true, false);
        }
        admin.declareExchange(exchange);
        return exchange;
    }

    private static Queue createQueue(MessageItemProperties properties, AmqpAdmin admin, Exchange exchange) {
        final Queue queue = QueueBuilder.durable(properties.getQueue()).build();
        final Binding binding = BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(Strings.nullToEmpty(properties.getRoutingKey()))
                .noargs();

        admin.declareQueue(queue);
        admin.declareBinding(binding);
        return queue;
    }

    private void createSimpleMessageListenerContainer(String key, MessageItemProperties properties, CachingConnectionFactory connectionFactory, Queue queue) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        //设置要监听的队列
        container.setQueues(queue);
        container.setExposeListenerChannel(true);
        container.setPrefetchCount(properties.getPrefetchCount());
        //设置最大处理消息数
        container.setMaxConcurrentConsumers(properties.getMaxConcurrentConsumers());
        //设置最小处理消息数
        container.setConcurrentConsumers(properties.getConcurrentConsumers());
        //设置手动ACK
        container.setAcknowledgeMode(properties.isManualAck() ? AcknowledgeMode.MANUAL : AcknowledgeMode.NONE);
        final String messageConsumerName = getMessageConsumerName(key);
        final MessageConsumer messageListener = applicationContext.getBean(messageConsumerName, MessageConsumer.class);
        //设置消息监听者
        container.setMessageListener(new MessageListenerImpl(messageConsumerName,
                applicationContext.getBean(FailedMessageLogByFile.class),
                properties,
                messageListener));
        container.start();
    }

    private void registerMessageProducer(String key, MessageItemProperties properties, CachingConnectionFactory connectionFactory, Exchange exchange) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();

        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setExchange(exchange.getName());
        MessageSender messageSender = new MessageSenderImpl(properties, rabbitTemplate);
        final MessageProducerImpl messageProducer = (MessageProducerImpl) applicationContext.getBean(MessageProducer.class);

        messageProducer.add(key, messageSender);
    }

    private static String getMessageConsumerName(String key) {
        return key + "MessageConsumer";
    }

    private static String getMessageProducerName(String key) {
        return key + "MessageProducer";
    }
}

package com.liurui.rabbitmq.delay.consumer;


/**
 * 消息监听接口类
 *
 * @author 刘锐
 * @date 2018-11-19 11:10:15
 */
public interface DelayMessageConsumer<T> {

    /**
     * 接收消息
     *
     * @param message 消息内容
     * @return 成功返回true，失败返回false
     */
    boolean onMessage(T message);
}

package com.liurui.rabbitmq.log;


import com.liurui.rabbitmq.ReturnData;

/**
 * @author liu-rui
 * @date 2019-07-24 12:01
 * @description
 */
public interface StringConverter {
    ReturnData<String> to(Object object);
}

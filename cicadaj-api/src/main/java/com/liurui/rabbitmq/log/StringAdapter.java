package com.liurui.rabbitmq.log;

/**
 * @author liu-rui
 * @date 2019-07-24 12:02
 * @description 将对象转换为字符串供日志输出
 * 内置了byte[],MultipartFile类型的转换，如果自定义转换请实现{@link StringConverter}
 */
public interface StringAdapter {
    /**
     * 将对象转换为字符串供日志输出
     *
     * @param object
     * @return
     */
    String to(Object object);
}

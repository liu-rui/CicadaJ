package com.liurui.rabbitmq.web.log;

import java.util.List;

/**
 * @author: 刘锐
 * @date: 18-12-24 下午7:30
 * @description: controller层日志上下文
 * 此接口在调试的时候非常有用，可以将方法执行过程中产生的中间结果输出到controller层日志。
 * 使用场景：将当前用户信息输入到controller层日志。
 */
public interface WebLogContext {
    /**
     * 添加数据
     *
     * @param data
     */
    void addData(Object data);


    /**
     * 获取数据列表
     *
     * @return
     */
    List<Object> getDataList();

    /**
     * 清空数据
     */
    void clearData();
}

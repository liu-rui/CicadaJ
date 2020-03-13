package com.liurui.rabbitmq.web.log;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author: 刘锐
 * @date: 18-12-24 下午7:30
 * @description: controller层日志上下文默认实现类
 */
public class DefaultWebLogContext implements WebLogContext {
    ThreadLocal<List<Object>> dataListThreadLocal = new ThreadLocal<>();

    @Override
    public void addData(Object data) {
        if (null == data) {
            return;
        }
        List<Object> dataList = dataListThreadLocal.get();

        if (dataList == null) {
            dataList = Lists.newArrayList();
            dataListThreadLocal.set(dataList);
        }
        dataList.add(data);
    }

    @Override
    public List<Object> getDataList() {
        return dataListThreadLocal.get();
    }

    @Override
    public void clearData() {
        dataListThreadLocal.remove();
    }
}

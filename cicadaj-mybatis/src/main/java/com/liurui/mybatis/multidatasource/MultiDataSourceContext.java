package com.liurui.mybatis.multidatasource;

import com.google.common.collect.Queues;

import java.util.Deque;

/**
 * @author liu-rui
 * @date 2019/10/11 下午6:16
 * @description
 * @since 0.3.0
 */

public class MultiDataSourceContext {
    private ThreadLocal<Deque<String>> dequeThreadLocal = ThreadLocal.withInitial(() -> Queues.newArrayDeque());

    public void push(String dataSource) {
        dequeThreadLocal.get().push(dataSource);
    }

    public void pop() {
        dequeThreadLocal.get().pop();
    }

    public String peek() {
        return dequeThreadLocal.get().peek();
    }
}

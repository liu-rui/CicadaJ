package com.liurui.limit;

/**
 * @author liu-rui
 * @date 2019/12/5 上午10:14
 * @description 限流器接口
 * @since 0.5.0
 */
public interface Limitable {
    /**
     * 初始化
     *
     * @param limiter 限流器注解
     */
    void init(Limiter limiter);

    /**
     * 获取限流器
     *
     * @return 限流器
     */
    Limiter getLimiter();

    /**
     * 尝试获取锁
     *
     * @return 是否获取到锁
     */
    boolean tryAcquire();
}

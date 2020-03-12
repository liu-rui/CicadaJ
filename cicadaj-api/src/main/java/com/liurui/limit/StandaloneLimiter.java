package com.liurui.limit;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.util.Assert;

/**
 * @author liu-rui
 * @date 2019/12/5 上午10:19
 * @description 单机版的限流器
 * @since 0.5.0
 */
public class StandaloneLimiter implements Limitable {
    private Limiter limiter;
    private RateLimiter rateLimiter;

    /**
     * 初始化
     *
     * @param limiter 限流器注解
     */
    @Override
    public void init(Limiter limiter) {
        Assert.notNull(limiter, "limiter 不能为空");
        Assert.isTrue(limiter.qps() > 0, "qps必须大于0");
        this.limiter = limiter;
        rateLimiter = RateLimiter.create(limiter.qps());
    }

    /**
     * 获取限流器
     *
     * @return 限流器
     */
    @Override
    public Limiter getLimiter() {
        return limiter;
    }

    /**
     * 尝试获取锁
     *
     * @return 是否获取到锁
     */
    @Override
    public boolean tryAcquire() {
        Assert.notNull(rateLimiter, "rateLimiter不能为null,请调用init方法完成初始化");
        return rateLimiter.tryAcquire(limiter.timeout(), limiter.unit());
    }
}

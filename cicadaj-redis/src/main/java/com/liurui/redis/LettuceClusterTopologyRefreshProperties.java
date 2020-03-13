package com.liurui.redis;

import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author liu-rui
 * @date 2020/1/7 下午3:00
 * @description
 * @since
 */
@Data
@ConfigurationProperties("spring.redis.lettuce.cluster-topology-refresh")
public class LettuceClusterTopologyRefreshProperties {
    /**
     * 是否启动自适应刷新
     */
    private boolean enableAllAdaptiveRefresh = true;

    /**
     * 自适应更新集群拓扑视图触发重连次数
     */
    private int refreshTriggersReconnectAttempts = ClusterTopologyRefreshOptions.DEFAULT_REFRESH_TRIGGERS_RECONNECT_ATTEMPTS;

    /**
     * 自适应更新集群拓扑视图触发器超时设置
     */
    private Duration adaptiveRefreshTimeout = ClusterTopologyRefreshOptions.DEFAULT_ADAPTIVE_REFRESH_TIMEOUT_DURATION;

    /**
     * 是否启用间隔性刷新
     */
    private boolean enablePeriodicRefresh = true;

    /**
     * 间隔性刷新频次
     */
    private Duration periodicRefresh = Duration.ofSeconds(ClusterTopologyRefreshOptions.DEFAULT_REFRESH_PERIOD);
}

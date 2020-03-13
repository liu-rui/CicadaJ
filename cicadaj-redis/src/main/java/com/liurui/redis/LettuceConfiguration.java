package com.liurui.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liu-rui
 * @date 2020/1/7 下午4:04
 * @description Lettuce默认不支持redis集群模式下节点挂掉后客户端自动修复，通过当前的配置类
 * 控制拓扑刷新机制
 * @since
 */
@Configuration
@ConditionalOnClass(RedisClient.class)
@EnableConfigurationProperties(LettuceClusterTopologyRefreshProperties.class)
public class LettuceConfiguration {
    @Bean
    public LettuceClientConfigurationBuilderCustomizer topologyRefreshCustomizer(LettuceClusterTopologyRefreshProperties lettuceClusterTopologyRefreshProperties) {
        return clientConfigurationBuilder -> {
            final ClusterTopologyRefreshOptions.Builder builder = ClusterTopologyRefreshOptions.builder();

            if (lettuceClusterTopologyRefreshProperties.isEnableAllAdaptiveRefresh()) {
                builder.enableAllAdaptiveRefreshTriggers();
                builder.adaptiveRefreshTriggersTimeout(lettuceClusterTopologyRefreshProperties.getAdaptiveRefreshTimeout());
                builder.refreshTriggersReconnectAttempts(lettuceClusterTopologyRefreshProperties.getRefreshTriggersReconnectAttempts());
            }

            if (lettuceClusterTopologyRefreshProperties.isEnablePeriodicRefresh()) {
                builder.enablePeriodicRefresh(lettuceClusterTopologyRefreshProperties.getPeriodicRefresh());
            }
            clientConfigurationBuilder.clientOptions(ClusterClientOptions.builder()
                    .topologyRefreshOptions(builder.build()).build());
        };
    }
}

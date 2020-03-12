package com.liurui.limit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liu-rui
 * @date 2019/12/5 上午11:28
 * @description
 * @since
 */
@Configuration
public class LimitConfiguration {
    @Bean
    LimitContext limitContext() {
        return new LimitContext();
    }

    @Bean
    LimitAdvice limitAdvice() {
        return new LimitAdvice();
    }

    @Bean
    LimitPointcut limitPointcut() {
        return new LimitPointcut();
    }

    @Bean
    LimitAdvisor limitAdvisor(){
        return new LimitAdvisor();
    }
}

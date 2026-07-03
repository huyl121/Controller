package com.example.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {
    @Bean("singleBizExecutor")
    public Executor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1); // 关键：只有1个工作线程，任务排队
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(300); // 排队最大请求数
        executor.initialize();
        return executor;
    }
}
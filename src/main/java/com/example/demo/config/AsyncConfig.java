package com.example.demo.config;


import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@EnableRetry
public class AsyncConfig {

    @Bean(name = "xmlExecutor")
    public Executor xmlExecutor() {

        ThreadPoolTaskExecutor executor =
                new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(20);

        executor.setMaxPoolSize(50);

        executor.setQueueCapacity(500);

        executor.setThreadNamePrefix("xml-worker-");

        executor.initialize();

        return executor;
    }
}

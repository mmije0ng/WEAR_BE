package com.backend.wear.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
public class SchedulerConfig implements SchedulingConfigurer {
//    private final static int POOL_SIZE = 4;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        final ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

        // Thread 개수 설정
        int threadPoolSize = Runtime.getRuntime().availableProcessors();
        threadPoolTaskScheduler.setPoolSize(threadPoolSize);
        threadPoolTaskScheduler.setThreadNamePrefix("hello-");
        threadPoolTaskScheduler.initialize();

        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }
}
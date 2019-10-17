package com.kzh.busi.task;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 配置定时任务，在app.properties里面控制启停
 */
@Configuration
@EnableScheduling
@PropertySource({"classpath:app.properties"})
public class TaskConfig implements SchedulingConfigurer {
    private static final Logger logger = Logger.getLogger(TaskConfig.class);

    @Value("${scheduler}")
    private String scheduler;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        if (("on".equals(scheduler))) {
            taskRegistrar.setScheduler(taskScheduler());
            logger.info("===========TaskConfig this Scheduler is On!!");
        } else {
            taskRegistrar.setCronTasksList(null);
            taskRegistrar.setFixedDelayTasksList(null);
            taskRegistrar.setFixedRateTasksList(null);
            taskRegistrar.setTriggerTasksList(null);
            logger.info("===========TaskConfig this Scheduler is Off!!");
        }
    }

    @Bean(destroyMethod = "shutdown")
    public Executor taskScheduler() {
        return Executors.newScheduledThreadPool(30);
    }

    @Bean
    public TaskWorker myTaskWorker() {
        return new TaskWorker();
    }
}

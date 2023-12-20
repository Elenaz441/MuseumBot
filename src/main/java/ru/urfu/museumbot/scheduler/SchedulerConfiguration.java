package ru.urfu.museumbot.scheduler;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
@Configuration



public class SchedulerConfiguration implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        // "Calls scheduleTasks() at bean construction time" - docs
        taskRegistrar.afterPropertiesSet();
    }
}

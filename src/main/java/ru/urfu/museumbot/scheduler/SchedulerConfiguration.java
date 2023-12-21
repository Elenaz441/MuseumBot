package ru.urfu.museumbot.scheduler;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * Класс конфигурации запланированных повторяющихся заданий
 */
@Configuration
public class SchedulerConfiguration implements SchedulingConfigurer {
    /**
     * задаёт конфигурацию для запланированных повторяющихся заданий
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.afterPropertiesSet();
    }
}

package ru.urfu.museumbot.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.scheduler.CronTaskService;

import java.util.LinkedHashMap;

/**
 * Класс управляющий отложенными повторяющимися заданиями
 */
@Service
@EnableScheduling
public class SchedulerService implements CommandLineRunner {

    /**
     * словарь, содержащий ключ - идентификатор чата, значение - повторяющимися заданиями,
     * предназначенное пользователю с чатом ключа
     * необходимо быстро бежать по элементам, поэтому выбрана эта реализация интерфейса Map.
     */
    LinkedHashMap<Long, CronTask> cronTasks;
    SchedulingConfigurer configurer;
    private ScheduledTaskRegistrar taskRegistrar;

    @Autowired
    public SchedulerService(SchedulingConfigurer configurer, CronTaskService cronTaskService) {
        this.cronTasks = cronTaskService.getCronTasks();
        this.configurer = configurer;
    }

    /**
     * Задаёт конфигурацию повторяющимся заданиям, т.е. приводит текущие задания в действие
     */
    @Override
    public void run(String... args) {
        this.taskRegistrar = new ScheduledTaskRegistrar();
        for (CronTask task : cronTasks.values()) {
            taskRegistrar.addCronTask(task);
        }
        configurer.configureTasks(taskRegistrar);
    }

    /**
     * Добавить новую задачу
     */
    public void addNewCron(Long chatId, CronTask task) {
        cronTasks.put(chatId, task);
        this.taskRegistrar.destroy();
        run();
    }

}

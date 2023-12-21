package ru.urfu.museumbot.jpa.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.support.CronTrigger;
import ru.urfu.museumbot.FakeBot;
import ru.urfu.museumbot.message.Message;
import ru.urfu.museumbot.scheduler.CronTaskService;
import ru.urfu.museumbot.scheduler.SchedulerConfiguration;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Класс для тестирования класса {@link SchedulerService}
 */
@ExtendWith(MockitoExtension.class)
class SchedulerServiceTest {
    private final CronTaskService cronTaskService;
    private final FakeBot fakeBot;
    private final SimpleDateFormat formatter;
    private final SchedulerService schedulerService;

    public SchedulerServiceTest() {
        this.fakeBot = new FakeBot();
        cronTaskService = Mockito.mock(CronTaskService.class);
        SchedulerConfiguration config = new SchedulerConfiguration();
        formatter = new SimpleDateFormat("ss");
        String seconds = formatter.format(Date.from(Instant.now().plus(2, ChronoUnit.SECONDS)));
        CronTask beReturned = new CronTask(() -> fakeBot.sendMessage(
                new Message(1L, "initial the distribution worked")),
                new CronTrigger(String.format("%s * * * * *", seconds)));

        LinkedHashMap<Long, CronTask> initialTasks = new LinkedHashMap<>();
        initialTasks.put(1L, beReturned);

        Mockito.doReturn(initialTasks).when(cronTaskService).getCronTasks();
        schedulerService = new SchedulerService(config, cronTaskService);
    }

    /**
     * Тестирование метода, который задаёт конфигурацию повторяющимся заданиям, т.е. приводит текущие задания в действие
     */
    @Test
    void run() throws InterruptedException {
        assertEquals(1, cronTaskService.getCronTasks().size());
        schedulerService.run();
        assertEquals(0, fakeBot.getMessages().size());
        Thread.sleep(3100);
        assertEquals("initial the distribution worked", fakeBot.getMessages().get(0).getText());
    }

    /**
     * Тестирование добавления отложенного потворяющегося задания в очередь заданий на выполнение
     */
    @Test
    void addCron() throws InterruptedException {
        String seconds = formatter.format(Date.from(Instant.now().plus(5, ChronoUnit.SECONDS)));
        CronTask beReturned = new CronTask(() -> fakeBot.sendMessage(
                new Message(2L, "2. the distribution worked")),
                new CronTrigger(String.format("%s * * * * *", seconds)));
        schedulerService.run();
        schedulerService.addCron(2L, beReturned);
        assertEquals(0, fakeBot.getMessages().size());
        Thread.sleep(5100);
        assertEquals("initial the distribution worked", fakeBot.getMessages().get(0).getText());
        assertEquals("2. the distribution worked", fakeBot.getMessages().get(1).getText());
    }

    /**
     * Тестирование удаления отложенного потворяющегося задания из очереди заданий на выполнение
     */
    @Test
    void removeNewCron() throws InterruptedException {
        schedulerService.run();
        Thread.sleep(3100);
        assertEquals("initial the distribution worked", fakeBot.getMessages().get(0).getText());
        assertEquals(1, cronTaskService.getCronTasks().size());
        schedulerService.removeCron(1L);
        Thread.sleep(3100);
        assertEquals(0, cronTaskService.getCronTasks().size());
        assertEquals(1, fakeBot.getMessages().size());
    }
}
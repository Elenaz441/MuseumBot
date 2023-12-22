package ru.urfu.museumbot.commands;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.support.CronTrigger;
import ru.urfu.museumbot.enums.State;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.models.Notification;
import ru.urfu.museumbot.jpa.models.User;
import ru.urfu.museumbot.jpa.service.NotificationService;
import ru.urfu.museumbot.jpa.service.SchedulerService;
import ru.urfu.museumbot.jpa.service.UserService;
import ru.urfu.museumbot.message.Message;
import ru.urfu.museumbot.scheduler.CronTaskService;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Класс для тестирования класса {@link SetTimeNotificationsNonCommand}
 */
@ExtendWith(MockitoExtension.class)
class SetTimeNotificationsNonCommandTest {

    @Mock
    private UserService userService;

    @Mock
    private SchedulerService schedulerService;

    @Mock
    private CronTaskService cronTaskService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private SetTimeNotificationsNonCommand setTimeNotificationsNonCommand;

    private final CommandArgs args;

    public SetTimeNotificationsNonCommandTest() {
        args = new CommandArgs();
        args.setChatId(1L);
    }

    /**
     * Тестирование команды, если пользовательский ввод не соответствует формату строки для сервиса,
     */
    @Test
    void getMessageIfUserInputIncorrect() {
        args.setUserInput("123");
        Message message = setTimeNotificationsNonCommand.getMessage(args);
        Mockito.verify(userService, Mockito.never()).updateUserState(Mockito.anyLong(), Mockito.any(State.class));
        Mockito.verify(userService, Mockito.never()).updateNotificationTime(Mockito.anyLong(), Mockito.any(Date.class));
        Mockito.verify(notificationService, Mockito.never())
                .resetNotificationTimeForUser(Mockito.any(Notification.class), Mockito.any(User.class));
        assertEquals("Напишите конкретное время, например 12:00.", message.getText());
    }

    /**
     * Тестирование команды, если пользовательский ввод соответствует формату строки для сервиса,
     * который создаёт отложенные задания
     * принимающего время в виде строки
     */
    @Test
    void getMessageIfUserInputCorrect(){
        User user = new User();
        user.setId(1L);
        user.setChatId(1L);
        user.setSettingReminders(false);
        user.setRandomExposureSetting(true);
        Mockito.doReturn(user).when(userService).getUserByChatId(1L);
        CronTask beReturned = new CronTask(() -> System.out.println("the distribution worked"),
                new CronTrigger("1/2 * * * * *"));
        Mockito.doReturn(beReturned).when(cronTaskService).createCronTaskRandomExhibit(1L);
        Instant now = Instant.now();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String input = format.format(Date.from(now));
        args.setUserInput(input);
        Message message = setTimeNotificationsNonCommand.getMessage(args);
        Mockito.verify(userService, Mockito.times(1)).updateUserState(1L, State.INIT);
        Mockito.verify(cronTaskService, Mockito.times(1)).createCronTaskRandomExhibit(1L);
        Mockito.verify(schedulerService, Mockito.times(1)).addCron(Mockito.eq(1L), Mockito.any(CronTask.class));
        Mockito.verify(userService, Mockito.times(1)).updateNotificationTime(Mockito.anyLong(), Mockito.any(Date.class));
        assertEquals("Настройки успешно заданы.", message.getText());
    }

    /**
     * Протестировать работу изменения уведомлений
     */
    @Test
    void testNotificationsIfUserInputCorrect() {
        args.setUserInput("12:55");

        User user = new User();
        user.setId(1L);
        user.setChatId(1L);
        user.setSettingReminders(true);
        user.setRandomExposureSetting(false);
        Event event1 = new Event();
        event1.setId(1L);
        Event event2 = new Event();
        event2.setId(2L);
        Notification notification = new Notification();
        notification.setId(1L);
        notification.setUser(user);
        notification.setEvent(event1);
        Mockito.doReturn(user).when(userService).getUserByChatId(1L);
        Mockito.doReturn(List.of(event1, event2)).when(userService).getUserEvents(1L);
        Mockito.doReturn(true).when(notificationService).isNotificationExistedByUserAndEvent(user, event1);
        Mockito.doReturn(false).when(notificationService).isNotificationExistedByUserAndEvent(user, event2);
        Mockito.doReturn(notification).when(notificationService).getNotificationByUserAndEvent(user, event1);

        setTimeNotificationsNonCommand.getMessage(args);

        Mockito.verify(notificationService, Mockito.times(1)).resetNotificationTimeForUser(notification, user);
        Mockito.verify(notificationService, Mockito.times(1)).createNotificationEvent(user, event2);
    }
}
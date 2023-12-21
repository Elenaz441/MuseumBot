package ru.urfu.museumbot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.enums.State;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.models.Notification;
import ru.urfu.museumbot.jpa.models.User;
import ru.urfu.museumbot.jpa.service.NotificationService;
import ru.urfu.museumbot.jpa.service.SchedulerService;
import ru.urfu.museumbot.jpa.service.UserService;
import ru.urfu.museumbot.message.Message;
import ru.urfu.museumbot.scheduler.CronTaskService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Команда, которая устанавливает в какое время отрпавлять уведомления пользователю
 */
@Service
public class SetTimeNotificationsNonCommand implements ExecutableWithState{
    private final static String MESSAGE_SUCCESS_SETTINGS = "Настройки успешно заданы.";
    private final static String FAILURE_MESSAGE = "Напишите конкретное время, например 12:00.";
    private final static String FAILURE_SUBSCRIBE_MESSAGE = "Не удалось применить некоторые настройки, " +
            "введите команду /view_settings, чтобы посмотреть настройки.";

    private final UserService userService;
    private final NotificationService notificationService;
    private final SchedulerService schedulerService;
    private final CronTaskService cronTaskService;

    @Autowired
    public SetTimeNotificationsNonCommand(UserService userService,
                                          NotificationService notificationService,
                                          SchedulerService schedulerService,
                                          CronTaskService cronTaskService) {
        this.userService = userService;
        this.notificationService = notificationService;
        this.schedulerService = schedulerService;
        this.cronTaskService = cronTaskService;
    }

    @Override
    public Message getMessage(CommandArgs args) {
        String userInput = args.getUserInput();
        Long chatId = args.getChatId();
        Optional<Date> timeNotification = checkTimeFormat(userInput);
        if (timeNotification.isEmpty()){
            return new Message(chatId, FAILURE_MESSAGE);
        }
        User user = userService.getUserByChatId(chatId);
        userService.updateNotificationTime(chatId, timeNotification.get());
        if (user.isRandomExposureSetting()) {
            try {
                schedulerService.addCron(chatId, cronTaskService.createCronTaskRandomExhibit(chatId));
            } catch (Exception e) {
                userService.updateRandomExposureSetting(chatId, false);
                System.out.println("Возникло исключение в ходе подписывания пользователя на рассылку.");
                userService.updateUserState(chatId, State.INIT);
                return new Message(chatId, FAILURE_SUBSCRIBE_MESSAGE);
            }
        }
        if (user.isSettingReminders()) {
            createNotificationsForUser(chatId);
        }
        userService.updateUserState(chatId, State.INIT);
        return new Message(chatId, MESSAGE_SUCCESS_SETTINGS);
    }

    /**
     * Проверка формат ввода времени
     */
    private Optional<Date> checkTimeFormat(String userInput) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date timeToNotification = null;
        try{
            timeToNotification = format.parse(userInput);
        } catch (ParseException e) {
            System.out.println(String.format("Пользовательский ввод не соответствует формату" +
                    " шаблона времени: %s", e.getMessage()));
        }
        return Optional.ofNullable(timeToNotification);
    }

    /**
     * Создать напоминания для пользователя на те мероприятия, на которые он записан
     */
    private void createNotificationsForUser(Long chatId) {
        User user = userService.getUserByChatId(chatId);
        List<Event> events = userService.getUserEvents(chatId);
        for (Event event:events) {
            if (notificationService.isNotificationExistedByUserAndEvent(user, event)){
                Notification notification = notificationService.getNotificationByUserAndEvent(user, event);
                notificationService.resetNotificationTimeForUser(notification, user);
            }
            else {
                notificationService.createNotificationEvent(user, event);
            }
        }
    }

    @Override
    public State getCommandState() {
        return State.SET_TIME;
    }
}

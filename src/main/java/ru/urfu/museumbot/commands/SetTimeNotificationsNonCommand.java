package ru.urfu.museumbot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.enums.State;
import ru.urfu.museumbot.jpa.models.User;
import ru.urfu.museumbot.jpa.service.SchedulerService;
import ru.urfu.museumbot.jpa.service.UserService;
import ru.urfu.museumbot.message.Message;
import ru.urfu.museumbot.scheduler.CronTaskService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

/**
 * Команда, которая устанавливает в какое время отрпавлять уведомления пользователю
 */
@Service
public class SetTimeNotificationsNonCommand implements ExecutableWithState{
    private final static String MESSAGE_SUCCESS_SETTINGS = "Настройки успешно заданы.";
    private final static String FALURE_MESSAGE = "Напишите конкретное время, например 12:00.";
    private final static String FALURE_SUBSCRIBE_MESSAGE = "Не удалось применить некоторые настройки, " +
            "введите команду /view_settings, чтобы посмотреть настройки.";

    private final UserService userService;
    private final SchedulerService schedulerService;
    private final CronTaskService cronTaskService;

    @Autowired
    public SetTimeNotificationsNonCommand(UserService userService, SchedulerService schedulerService, CronTaskService cronTaskService) {
        this.userService = userService;
        this.schedulerService = schedulerService;
        this.cronTaskService = cronTaskService;
    }

    @Override
    public Message getMessage(CommandArgs args) {
        String userInput = args.getUserInput();
        Long chatId = args.getChatId();
        Optional<Date> timeNotification = checkTimeFormat(userInput);
        if(timeNotification.isEmpty()){
            return new Message(chatId, FALURE_MESSAGE);
        }
        User user = userService.getUserByChatId(chatId);
        userService.updateNotificationTime(chatId, timeNotification.get());
        if(user.isRandomExposureSetting()) {
            try {
                schedulerService.addCron(chatId, cronTaskService.createCronTaskRandomExhibit(chatId));
            } catch (Exception e) {
                System.out.println("Возникло исключение в ходе подписывания пользователя на рассылку.");
                userService.updateRandomExposureSetting(chatId, false);
                userService.updateUserState(chatId, State.INIT);
                return new Message(chatId, FALURE_SUBSCRIBE_MESSAGE);
            }
        }
        else{
            schedulerService.removeCron(chatId);
        }
        userService.updateUserState(chatId, State.INIT);
        return new Message(chatId, MESSAGE_SUCCESS_SETTINGS);
    }

    private Optional<Date> checkTimeFormat(String userInput) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date timeToNotification = null;
        try{
            timeToNotification = format.parse(userInput);
        } catch (ParseException e) {
            System.out.println(String.format("Пользовательский ввод не соответвует формату" +
                    " шаблона времени: %s", e.getMessage()));
        }
        return Optional.ofNullable(timeToNotification);
    }

    @Override
    public State getCommandState() {
        return State.SET_TIME;
    }
}

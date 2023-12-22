package ru.urfu.museumbot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.enums.State;
import ru.urfu.museumbot.jpa.models.Notification;
import ru.urfu.museumbot.jpa.models.User;
import ru.urfu.museumbot.jpa.service.NotificationService;
import ru.urfu.museumbot.jpa.service.UserService;
import ru.urfu.museumbot.message.Message;
import ru.urfu.museumbot.util.UserInputChecker;

/**
 * Команда, которая устанавливает настройку уведомлять пользователя о мероприятии,
 * на которое он записаны, за день до начала
 */
@Service
public class SetSettingNotificationNonCommand implements ExecutableWithState {
    private static final String SET_DISTRIBUTION_MESSAGE = "Присылать ли вам информацию о случайном экспонате? " +
            "(Напишите да или нет)";
    private static final String FAILURE_MESSAGE = "Настройка не выбрана. Чтобы установить настройку напишите," +
            " пожалуйста, да или нет.";

    private final UserService userService;
    private final NotificationService notificationService;

    @Autowired
    public SetSettingNotificationNonCommand(UserService userService, NotificationService notificationService) {
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @Override
    public Message getMessage(CommandArgs args) {
        Long chatId = args.getChatId();
        String userInput = args.getUserInput();
        boolean isInputCorrect = new UserInputChecker().checkUserInputSetting(userInput);
        if (!isInputCorrect) {
            return new Message(chatId, FAILURE_MESSAGE);
        }
        boolean setting = userInput.equalsIgnoreCase("да");
        userService.updateNotificationSettings(chatId, setting);
        userService.updateUserState(chatId, State.SET_DISTRIBUTION);
        if (!setting) {
            User user = userService.getUserByChatId(chatId);
            for (Notification notification:user.getNotifications()) {
                notificationService.deleteNotification(notification);
            }
        }
        return new Message(chatId, SET_DISTRIBUTION_MESSAGE);
    }

    @Override
    public State getCommandState() {
        return State.SET_NOTIFICATION;
    }
}

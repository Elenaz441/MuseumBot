package ru.urfu.museumbot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.enums.State;
import ru.urfu.museumbot.jpa.service.UserService;
import ru.urfu.museumbot.message.Message;

/**
 * Команда, которая изменяет настройки уведомлений пользователя
 */
@Service
public class ChangeSettingsCommand implements Command {
    private static final String CHANGES_SETTINGS_MESSAGE = "Присылать ли вам напоминание о мероприятии," +
            " на которое вы записаны, за день до начала? (Напишите да или нет)";

    private final UserService userService;

    @Autowired
    public ChangeSettingsCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String getCommandName() {
        return Commands.CHANGE_SETTINGS;
    }

    @Override
    public Message getMessage(CommandArgs args) {
        Long chatId = args.getChatId();
        userService.updateUserState(chatId, State.SET_NOTIFICATION);
        return new Message(chatId, CHANGES_SETTINGS_MESSAGE);
    }
}

package ru.urfu.museumbot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.dataFormat.UserFormat;
import ru.urfu.museumbot.jpa.models.User;
import ru.urfu.museumbot.jpa.service.UserService;
import ru.urfu.museumbot.message.Message;

import static ru.urfu.museumbot.commands.Commands.VIEW_SETTINGS;

/**
 * Команда для просмотра настроек уведомлений
 */
@Service
public class ViewSettingsCommand implements Command {

    private final UserService userService;

    @Autowired
    public ViewSettingsCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String getCommandName() {
        return VIEW_SETTINGS;
    }

    @Override
    public Message getMessage(CommandArgs args) {
        Long chatId = args.getChatId();
        User user = userService.getUserByChatId(chatId);
        String answer = new UserFormat().convertUserSettingsToString(user);
        return new Message(chatId, answer);
    }
}

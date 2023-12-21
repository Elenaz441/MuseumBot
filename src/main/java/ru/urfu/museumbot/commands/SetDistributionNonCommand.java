package ru.urfu.museumbot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.enums.State;
import ru.urfu.museumbot.jpa.models.User;
import ru.urfu.museumbot.jpa.service.UserService;
import ru.urfu.museumbot.message.Message;
import ru.urfu.museumbot.util.UserInputChecker;

/**
 * Команда, которая устанавливает посылать ли еженедельно информацию о случайном экспонате
 */
@Service
public class SetDistributionNonCommand implements ExecutableWithState {
    private static final String SET_TIME_MESSAGE = "Когда вам присылать уведомления?" +
            " (Напишите конкретное время, например 12:00)";
    private static final String CHANGE_FAILURE_MESSAGE = "Настройка не выбрана. Чтобы установить настройку напишите," +
            " пожалуйста, да или нет.";

    private static final String SET_DAY_OF_WEEK_MESSAGE = "Напишите в какой день недели" +
            " вы хотели бы получать информацию о случайном экспонате. Например, Пятница.";
    private static final String SUCCESS_SET_MESSAGE = "Настройки успешно заданы.";

    private final UserService userService;

    @Autowired
    public SetDistributionNonCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Message getMessage(CommandArgs args) {
        Long chatId = args.getChatId();
        String userInput = args.getUserInput();
        boolean isInputCorrect = new UserInputChecker().checkUserInputSetting(userInput);
        if (!isInputCorrect) {
            return new Message(chatId, CHANGE_FAILURE_MESSAGE);
        }
        User user = userService.getUserByChatId(chatId);
        boolean setting = userInput.equalsIgnoreCase("да");
        userService.updateRandomExposureSetting(chatId, setting);
        if (setting || user.isSettingReminders()) {
            if (setting) {
                userService.updateUserState(chatId, State.SET_DAY_OF_WEEK);
                return new Message(chatId, SET_DAY_OF_WEEK_MESSAGE);
            }
            userService.updateUserState(chatId, State.SET_TIME);
            return new Message(chatId, SET_TIME_MESSAGE);
        } else {
            userService.updateUserState(chatId, State.INIT);
            return new Message(chatId, SUCCESS_SET_MESSAGE);
        }
    }

    @Override
    public State getCommandState() {
        return State.SET_DISTRIBUTION;
    }
}

package ru.urfu.museumbot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.enums.DayOfWeek;
import ru.urfu.museumbot.enums.State;
import ru.urfu.museumbot.jpa.service.UserService;
import ru.urfu.museumbot.message.Message;

import java.util.Arrays;

/**
 * Команда, которая устранавливает в какой день недели отправлять расслыку о рандомном экспонате
 */
@Service
public class SetDayOfWeeForDistributionNonCommand implements ExecutableWithState {
    private static final String SET_TIME_MESSAGE = "Когда вам присылать уведомления?" +
            " (Напишите конкретное время, например 12:00)";

    private static final String FALURE_MESSAGE = "Настройка не выбрана. Чтобы установить настройку напишите, " +
            "пожалуйста, день недели. Например, Пятница.";

    private final UserService userService;

    @Autowired
    public SetDayOfWeeForDistributionNonCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Message getMessage(CommandArgs args) {
        Long chatId = args.getChatId();
        String userInput = args.getUserInput();
        try {
            DayOfWeek dayOfWeek = Arrays.stream(DayOfWeek.values())
                    .filter(state -> state.getDayString().equalsIgnoreCase(userInput))
                    .findFirst().orElse(DayOfWeek.SATURDAY);
            userService.updateDayOfWeekDistribution(chatId, dayOfWeek.ordinal());
        } catch (IllegalArgumentException e) {
            System.out.println(String.format("Некорректный ввод от пользователя." +
                    " Требуется ввести день недели. %s", e.getMessage()));
            return new Message(chatId, FALURE_MESSAGE);
        }
        userService.updateUserState(chatId, State.SET_TIME);
        return new Message(chatId, SET_TIME_MESSAGE);
    }

    @Override
    public State getCommandState() {
        return State.SET_DAY_OF_WEEK;
    }
}

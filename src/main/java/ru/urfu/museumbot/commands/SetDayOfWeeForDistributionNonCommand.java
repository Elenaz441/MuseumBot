package ru.urfu.museumbot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.customException.IncorrectUserInputException;
import ru.urfu.museumbot.enums.DayOfWeek;
import ru.urfu.museumbot.enums.State;
import ru.urfu.museumbot.jpa.service.UserService;
import ru.urfu.museumbot.message.Message;

import java.util.Arrays;
import java.util.Optional;

/**
 * Команда, которая устанавливает в какой день недели отправлять рассылку о случайном экспонате
 */
@Service
public class SetDayOfWeeForDistributionNonCommand implements ExecutableWithState {
    private static final String SET_TIME_MESSAGE = "Когда вам присылать уведомления?" +
            " (Напишите конкретное время, например 12:00)";

    private static final String FAILURE_MESSAGE = "Настройка не выбрана. Чтобы установить настройку напишите, " +
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
            Optional<DayOfWeek> dayOfWeek = Optional.ofNullable(Arrays.stream(DayOfWeek.values())
                    .filter(state -> state.getDayString().equalsIgnoreCase(userInput))
                    .findFirst().orElse(null));
        try{
            if (dayOfWeek.isPresent()) {
                userService.updateDayOfWeekDistribution(chatId, dayOfWeek.get().ordinal());
            } else {
                throw new IncorrectUserInputException("Некорректный ввод от пользователя." +
                        " Требуется ввести день недели.");
            }
        }
            catch (IncorrectUserInputException e){
            System.out.println(String.format("День недели не поставился. %s", e.getMessage()));
            return new Message(chatId, FAILURE_MESSAGE);
        }
        userService.updateUserState(chatId, State.SET_TIME);
        return new Message(chatId, SET_TIME_MESSAGE);
    }

    @Override
    public State getCommandState() {
        return State.SET_DAY_OF_WEEK;
    }
}

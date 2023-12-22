package ru.urfu.museumbot.commands;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.urfu.museumbot.enums.State;
import ru.urfu.museumbot.jpa.service.UserService;
import ru.urfu.museumbot.message.Message;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Класс для тестирования класса {@link SetDayOfWeekForDistributionNonCommand}
 */
@ExtendWith(MockitoExtension.class)
class SetDayOfWeekForDistributionNonCommandTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private SetDayOfWeekForDistributionNonCommand distributionNonCommand;
    private final CommandArgs args;

    public SetDayOfWeekForDistributionNonCommandTest() {
        args = new CommandArgs();
        args.setChatId(1L);
    }

    /**
     * Тестирование команды, если получили некорректный пользовательский ввод
     */
    @Test
    void getMessageIfUserInputIncorrect() {
        args.setUserInput("четыверг");
        Message message = distributionNonCommand.getMessage(args);
        Mockito.verify(userService, Mockito.never()).updateUserState(Mockito.anyLong(), Mockito.any(State.class));
        Mockito.verify(userService, Mockito.never()).updateDayOfWeekDistribution(Mockito.anyLong(), Mockito.anyInt());
        assertEquals("Настройка не выбрана. Чтобы установить настройку напишите, " +
                "пожалуйста, день недели. Например, Пятница.", message.getText());
    }

    /**
     * Тестирование команды, когда пользовательский ввод корректен
     */
    @Test
    void getMessageIfUserInputCorrect() {
        args.setUserInput("среда");
        Message message = distributionNonCommand.getMessage(args);
        Mockito.verify(userService, Mockito.times(1))
                .updateUserState(1L, State.SET_TIME);
        Mockito.verify(userService, Mockito.times(1)).updateDayOfWeekDistribution(1L, 3);
        assertEquals("Когда вам присылать уведомления?" +
                " (Напишите конкретное время, например 12:00)", message.getText());

    }
}
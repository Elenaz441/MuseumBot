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

@ExtendWith(MockitoExtension.class)
class SetSettingNotificationNonCommandTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private SetSettingNotificationNonCommand settingNotificationNonCommand;
    private final CommandArgs args;

    public SetSettingNotificationNonCommandTest() {
        args = new CommandArgs();
        args.setChatId(1L);
    }

    /**
     * Тестирование команды, когда пользвоательский не соответствует формату
     */
    @Test
    void getMessageIfUserInputIncorrect() {
        args.setUserInput("123");
        Message message = settingNotificationNonCommand.getMessage(args);
        Mockito.verify(userService, Mockito.never()).updateUserState(Mockito.anyLong(), Mockito.any(State.class));
        Mockito.verify(userService, Mockito.never()).updateNotificationSettings(Mockito.anyLong(), Mockito.anyBoolean());
        assertEquals("Настройка не выбрана. Чтобы установить настройку напишите," +
                " пожалуйста, да или нет.", message.getText());
    }

    /**
     * Тестирование команды, когда пользвоательский соответствует формату
     */
    @Test
    void getMessageIfUserInputCorrect() {
        args.setUserInput("Да");
        Message message = settingNotificationNonCommand.getMessage(args);
        Mockito.verify(userService, Mockito.times(1)).updateNotificationSettings(1L, true);
        Mockito.verify(userService, Mockito.times(1)).updateUserState(1L, State.SET_DISTRIBUTION);
        assertEquals("Присылать ли вам информацию о случайном экспонате? " +
                "(Напишите да или нет)", message.getText());
    }
}
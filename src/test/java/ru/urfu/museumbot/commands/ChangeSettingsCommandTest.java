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
class ChangeSettingsCommandTest {
    private final CommandArgs args;
    @Mock
    private UserService userService;
    @InjectMocks
    private ChangeSettingsCommand changeSettingsCommand;

    public ChangeSettingsCommandTest() {
        args = new CommandArgs();
        args.setChatId(1L);
    }

    /**
     * Тестирование начала команды /change_settings
     */
    @Test
    void getMessage() {
        Message message = changeSettingsCommand.getMessage(args);
        Mockito.verify(userService, Mockito.times(1))
                .updateUserState(1L, State.SET_NOTIFICATION);
        assertEquals("Присылать ли вам напоминание о мероприятии," +
                " на которое вы записаны, за день до начала? (Напишите да или нет)", message.getText());
    }

}
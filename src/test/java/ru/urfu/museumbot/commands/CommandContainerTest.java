package ru.urfu.museumbot.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.urfu.museumbot.enums.State;
import ru.urfu.museumbot.jpa.models.User;
import ru.urfu.museumbot.jpa.service.UserService;
import ru.urfu.museumbot.message.Message;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CommandContainerTest {

    @Mock
    private ExecutableWithState nonCommand;

    @Mock
    private Command command;

    @Mock
    private UserService userService;

    private CommandContainer commandContainer;

    /**
     * Настройка данных перед каждым тестом
     */
    @BeforeEach
    void setUp() {
        Message message1 = new Message(1L, "Testing CommandContainer return Command");
        Message message2 = new Message(1L, "Testing CommandContainer return CommandWithState");
        Mockito.doReturn("/test").when(command).getCommandName();
        Mockito.doReturn(message1).when(command).getMessage(Mockito.any(CommandArgs.class));
        Mockito.doReturn(State.RATE).when(nonCommand).getCommandState();
        Mockito.doReturn(message2).when(nonCommand).getMessage(Mockito.any(CommandArgs.class));

        commandContainer = new CommandContainer(List.of(nonCommand), List.of(command), userService);
    }

    /**
     * Тестирование {@link CommandContainer#retrieveCommand метода}
     * по возвращению интерфейса
     * {@link ru.urfu.museumbot.commands.Command}
     */
    @Test
    void retrieveCommandTestCommandClass() {
        Message message1 = new Message(1L, "Testing CommandContainer return Command");
        Mockito.doReturn("/test").when(command).getCommandName();
        Mockito.doReturn(message1).when(command).getMessage(Mockito.any(CommandArgs.class));
        User user = new User();
        user.setChatId(1L);
        Mockito.doReturn(user).when(userService).getUserByChatId(1L);

        Executable res = commandContainer.retrieveCommand(1L, "/test");
        CommandArgs args = new CommandArgs();
        args.setChatId(1L);
        Message mess = res.getMessage(args);

        Assertions.assertEquals(State.INIT.getStateString(), user.getState());
        Assertions.assertEquals("Testing CommandContainer return Command", mess.getText());
        Assertions.assertEquals(1L, mess.getChatId());
    }

    /**
     * Тестирование {@link CommandContainer#retrieveCommand метода}
     * по возвращению интерфейса
     * {@link ru.urfu.museumbot.commands.ExecutableWithState}
     */
    @Test
    void retrieveCommandTestNonCommandClass() {
        Message message2 = new Message(1L, "Testing CommandContainer return CommandWithState");
        Mockito.doReturn(State.RATE).when(nonCommand).getCommandState();
        Mockito.doReturn(message2).when(nonCommand).getMessage(Mockito.any(CommandArgs.class));
        User user = new User();
        user.setChatId(1L);
        user.setState(State.RATE.getStateString());
        Mockito.doReturn(user).when(userService).getUserByChatId(1L);

        Executable res = commandContainer.retrieveCommand(1L, "Test");
        CommandArgs args = new CommandArgs();
        args.setChatId(1L);
        Message mess = res.getMessage(args);

        Assertions.assertNotEquals(State.INIT.getStateString(), user.getState());
        Assertions.assertEquals("Testing CommandContainer return CommandWithState", mess.getText());
        Assertions.assertEquals(1L, mess.getChatId());
    }

    /**
     * Тестирование {@link CommandContainer#retrieveCommand метода}
     * если команда не распознана
     */
    @Test
    void retrieveCommandTestUnknownCommand() {
        User user = new User();
        user.setChatId(1L);
        user.setState(State.RATE_PREV.getStateString());
        Mockito.doReturn(user).when(userService).getUserByChatId(1L);

        Executable res = commandContainer.retrieveCommand(1L, "/new_command");
        CommandArgs args = new CommandArgs();
        args.setChatId(1L);
        Message mess = res.getMessage(args);

        Assertions.assertEquals(
                "Извините, команда не распознана, напишите /help чтобы узнать что я умею.",
                mess.getText());
        Assertions.assertEquals(1L, mess.getChatId());
    }

}
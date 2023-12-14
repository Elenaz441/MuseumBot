package ru.urfu.museumbot.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.urfu.museumbot.jpa.models.User;
import ru.urfu.museumbot.jpa.service.UserService;
import ru.urfu.museumbot.message.Message;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class CommandContainerTest {
    private final ExecutableWithState nonCommand;
    private final Command command;
    @Mock
    UserService userService;
    private CommandContainer commandContainer;

    public CommandContainerTest() {
        nonCommand = new ExecutableWithState() {
            @Override
            public State getCommandState() {
                return State.RATE;
            }

            @Override
            public Message getMessage(CommandArgs args) {
                return new Message(1L, "Testing CommandContainer return CommandWithState");
            }
        };
        command = new Command() {
            @Override
            public String getCommandName() {
                return "/test";
            }

            @Override
            public Message getMessage(CommandArgs args) {
                return new Message(args.getChatId(), "Testing CommandContainer return Commnad");
            }
        };
    }

    /**
     * Тестирование {@link CommandContainer#retrieveCommand метода}
     * по возвращению интерфейса
     * {@link ru.urfu.museumbot.commands.Command}
     */
    @Test
    void retrieveCommandTestCommandClass() {
        User user = new User();
        user.setChatId(1L);
        Mockito.doReturn(user).when(userService).getUserByChatId(1L);
        commandContainer = new CommandContainer(userService, List.of(), List.of(command));

        Executable res = commandContainer.retrieveCommand(1L, "/test");
        CommandArgs args = new CommandArgs();
        args.setChatId(1L);
        Message mess = res.getMessage(args);
        Assertions.assertEquals(State.INIT.getStateString(), user.getState());
        Assertions.assertEquals("Testing CommandContainer return Commnad", mess.getText());
        Assertions.assertEquals(1L, mess.getChatId());
    }

    /**
     * Тестирование {@link CommandContainer#retrieveCommand метода}
     * по возвращению интерфейса
     * {@link ru.urfu.museumbot.commands.ExecutableWithState}
     */
    @Test
    void retrieveCommandTestNonCommandClass() {
        User user = new User();
        user.setChatId(1L);
        user.setState(State.RATE.getStateString());

        Mockito.doReturn(user).when(userService).getUserByChatId(1L);
        commandContainer = new CommandContainer(userService, List.of(nonCommand), List.of());

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
        commandContainer = new CommandContainer(userService, List.of(), List.of());

        Executable res = commandContainer.retrieveCommand(1L, "/new_command");
        CommandArgs args = new CommandArgs();
        args.setChatId(1L);
        Message mess = res.getMessage(args);
        Assertions.assertEquals("Извините, команда не распознана, напишите /help чтобы узнать что я умею.", mess.getText());
        Assertions.assertEquals(1L, mess.getChatId());
    }

}
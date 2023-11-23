package ru.urfu.museumbot.commands;

import ru.urfu.museumbot.jpa.service.SendBotMessageService;
import ru.urfu.museumbot.jpa.service.ServiceContext;

import java.util.HashMap;
import java.util.Map;
import static ru.urfu.museumbot.commands.Commands.*;

/**
 * Хранилище {@link Command}s, которое используется для обработки входящих сообщений.
 */
public class CommandContainer {

    private final Map<String, Command> commandMap;
    private final Command unknownCommand;

    public CommandContainer(SendBotMessageService sendBotMessageService, ServiceContext serviceContext) {
        this.commandMap = new HashMap<>();
        commandMap.put(START, new StartCommand(sendBotMessageService));
        commandMap.put(HELP, new HelpCommand(sendBotMessageService));
        commandMap.put(VIEW_UPCOMING_EVENTS, new ViewUpcomingEventsCommand(sendBotMessageService, serviceContext));
        commandMap.put(VIEW_MY_EVENTS, new ViewMyEventsCommand(sendBotMessageService, serviceContext));
        commandMap.put(SIGN_UP_FOR_EVENT, new PreSignUpCommand(sendBotMessageService, serviceContext));
        commandMap.put(CANCEL, new PreCancelCommand(sendBotMessageService, serviceContext));
        commandMap.put(ADD_EVENT, new SignUpCommand(sendBotMessageService, serviceContext));
        commandMap.put(CANCEL_EVENT, new CancelCommand(sendBotMessageService, serviceContext));
        unknownCommand = new NonCommand(sendBotMessageService);
    }

    /**
     * Выбор нужного класса команды
     * @param commandIdentifier - идентификатор команды
     * @return - класс обработки команды
     */
    public Command retrieveCommand(String commandIdentifier) {
        return commandMap.getOrDefault(commandIdentifier, unknownCommand);
    }

}

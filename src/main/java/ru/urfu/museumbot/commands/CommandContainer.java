package ru.urfu.museumbot.commands;

import ru.urfu.museumbot.jpa.service.*;

import java.util.HashMap;
import java.util.Map;
import static ru.urfu.museumbot.commands.Commands.*;

/**
 * Хранилище {@link Command}s, которое используется для обработки входящих сообщений.
 */
public class CommandContainer {

    private final Map<String, Command> commandMap;
    private final Command unknownCommand;

    public CommandContainer(EventService eventService,
                            ReviewService reviewService,
                            UserService userService) {
        this.commandMap = new HashMap<>();
        commandMap.put(START, new StartCommand());
        commandMap.put(HELP, new HelpCommand());
        commandMap.put(VIEW_UPCOMING_EVENTS, new ViewUpcomingEventsCommand(eventService));
        commandMap.put(VIEW_MY_EVENTS, new ViewMyEventsCommand(userService));
        commandMap.put(SIGN_UP_FOR_EVENT, new PreSignUpCommand(eventService));
        commandMap.put(CANCEL, new PreCancelCommand(userService));
        commandMap.put(ADD_EVENT, new SignUpCommand(eventService, reviewService, userService));
        commandMap.put(CANCEL_EVENT, new CancelCommand(eventService, reviewService, userService));
        unknownCommand = new NonCommand();
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

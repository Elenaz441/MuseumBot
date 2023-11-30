package ru.urfu.museumbot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Хранилище {@link Command}s, которое используется для обработки входящих сообщений.
 */
@Service
public class CommandContainer {
    private final Map<String, Command> commandMap;
    private final Command unknownCommand;
    @Autowired
    public CommandContainer(
                            StartCommand startCommand,
                            HelpCommand helpCommand,
                            ViewUpcomingEventsCommand viewUpcomingEventsCommand,
                            ViewMyEventsCommand viewMyEventsCommand,
                            PreSignUpCommand preSignUpCommand,
                            PreCancelCommand preCancelCommand,
                            SignUpCommand signUpCommand,
                            CancelCommand cancelCommand) {
        List<Command> commands = List.of(startCommand,
                helpCommand,
                viewUpcomingEventsCommand,
                viewMyEventsCommand,
                preCancelCommand,
                preSignUpCommand,
                signUpCommand,
                cancelCommand);
        this.commandMap = commands.stream().collect(Collectors.toMap(Command::getCommandName, command -> command));
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

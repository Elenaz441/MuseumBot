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
    private final NonCommand unknownCommand;
    @Autowired
    private final List<Command> commandList;

    public CommandContainer(List<Command> commandList) {
        this.commandList = commandList;
        this.commandMap = commandList.stream().collect(Collectors.toMap(Command::getCommandName, command -> command));
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

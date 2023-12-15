package ru.urfu.museumbot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.jpa.models.User;
import ru.urfu.museumbot.jpa.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Хранилище {@link Command}s, которое используется для обработки входящих сообщений.
 */
@Service
public class CommandContainer {

    private final Map<String, Executable> commandMap;
    private final Map<State, Executable> nonCommandMap;
    private final Executable unknownCommand;
    private final UserService userService;

    @Autowired
    public CommandContainer(List<ExecutableWithState> nonCommandList,
                            List<Command> commandList,
                            UserService userService) {
        this.userService = userService;
        this.commandMap = commandList.stream().collect(Collectors.toMap(Command::getCommandName, command -> command));
        this.nonCommandMap = nonCommandList.stream().collect(Collectors.toMap(ExecutableWithState::getCommandState, command -> command));
        unknownCommand = new UnknownCommand();
    }

    /**
     * Выбор нужного класса команды
     * @param chatId Идентификатор чата пользователя с которым взаимодействует бот
     * @param commandIdentifier - идентификатор команды
     * @return - класс обработки команды
     */
    public Executable retrieveCommand(Long chatId, String commandIdentifier) {
        User user = userService.getUserByChatId(chatId);
        State userState = State.valueOf(user.getState().toUpperCase());
        if (userState == State.INIT) {
            return commandMap.getOrDefault(commandIdentifier, unknownCommand);
        }
        return nonCommandMap.getOrDefault(userState, unknownCommand);
    }

}

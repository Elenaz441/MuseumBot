package ru.urfu.museumbot.commands;

import ru.urfu.museumbot.jpa.models.User;
import ru.urfu.museumbot.message.Message;

import static ru.urfu.museumbot.commands.State.INIT;

/**
 * Интерфейс команды для обработки входящих сообщений
 */
public interface Command extends Executable{
    /**
     * @return имя команды отвечающее за вызов пользователем
     */
    String getCommandName();
}

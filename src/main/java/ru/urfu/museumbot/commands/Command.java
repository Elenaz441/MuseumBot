package ru.urfu.museumbot.commands;

/**
 * Интерфейс команды для обработки входящих сообщений
 */
public interface Command extends Executable{
    /**
     * @return имя команды отвечающее за вызов пользователем
     */
    String getCommandName();
}

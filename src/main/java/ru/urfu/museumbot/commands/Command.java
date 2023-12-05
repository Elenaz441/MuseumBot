package ru.urfu.museumbot.commands;

import ru.urfu.museumbot.message.Message;

/**
 * Интерфейс команды для обработки входящих сообщений
 */
public interface Command {

    /**
     * Основной метод, который вызывает работу команды
     */
    Message getMessage(CommandArgs args);

    /**
     * @return имя команды отвечающее за вызов пользователем
     */
    String getCommandName();
}

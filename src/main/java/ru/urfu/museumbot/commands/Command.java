package ru.urfu.museumbot.commands;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Интерфейс команды для обработки входящих сообщений
 */
public interface Command {

    /**
     * Основной метод, который вызывает работу команды
     */
    void execute(Update update);
}

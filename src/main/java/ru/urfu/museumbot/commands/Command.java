package ru.urfu.museumbot.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Интерфейс команды для обработки входящих сообщений
 */
public interface Command {

    /**
     * Основной метод, который вызывает работу команды
     */
    SendMessage getMessage(Update update);
}

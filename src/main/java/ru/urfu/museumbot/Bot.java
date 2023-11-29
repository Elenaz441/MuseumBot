package ru.urfu.museumbot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Интерфейс бота
 */
public interface Bot {

    /**
     * Метод для отправки сообщения
     */
    void sendMessage(SendMessage message);

}

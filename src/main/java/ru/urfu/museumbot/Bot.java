package ru.urfu.museumbot;

import ru.urfu.museumbot.message.Message;

/**
 * Интерфейс бота
 */
public interface Bot {

    /**
     * Метод для отправки сообщения
     */
    void sendMessage(Message message);

}

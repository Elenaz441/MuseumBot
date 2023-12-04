package ru.urfu.museumbot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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

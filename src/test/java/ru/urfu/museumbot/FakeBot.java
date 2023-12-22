package ru.urfu.museumbot;

import ru.urfu.museumbot.message.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, имитирующий поведение бота для тестов
 */
public class FakeBot implements Bot{

    private final List<Message> messages = new ArrayList<>();

    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public void sendMessage(Message message) {
        messages.add(message);
    }
}

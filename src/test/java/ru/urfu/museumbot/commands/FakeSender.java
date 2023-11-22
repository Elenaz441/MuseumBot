package ru.urfu.museumbot.commands;

import ru.urfu.museumbot.TelegramBot;
import ru.urfu.museumbot.jpa.service.SendBotMessageService;

import java.util.ArrayList;
import java.util.List;

public class FakeSender extends SendBotMessageService {

    private final List<String> messages = new ArrayList<>();

    public List<String> getMessages() {
        return messages;
    }

    public FakeSender(TelegramBot telegramBot) {
        super(telegramBot);
    }

    @Override
    public void sendMessage(String chatId, String message) {
        messages.add(message);
    }
}

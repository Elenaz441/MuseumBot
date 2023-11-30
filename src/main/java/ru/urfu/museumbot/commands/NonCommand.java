package ru.urfu.museumbot.commands;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Команды, которые начинаются не со /
 */
@Service
public class NonCommand implements Command {

    static final String UNKNOWN_MESSAGE = "Извините, команда не распознана, напишите /help чтобы узнать что я умею.";

    public NonCommand() {
    }

    /**
     * Основной метод, который вызывает работу команды
     */
    @Override
    public SendMessage getMessage(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId().toString());
        message.setText(UNKNOWN_MESSAGE);
        return message;
    }

    @Override
    public String getCommandName() {
        return null;
    }
}

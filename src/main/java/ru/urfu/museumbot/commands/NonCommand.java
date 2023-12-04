package ru.urfu.museumbot.commands;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.urfu.museumbot.message.Message;

/**
 * Команды, которые начинаются не со /
 */
public class NonCommand implements Command {

    static final String UNKNOWN_MESSAGE = "Извините, команда не распознана, напишите /help чтобы узнать что я умею.";

    public NonCommand() {
    }

    /**
     * Основной метод, который вызывает работу команды
     */
    @Override
    public Message getMessage(CommandArgs args) {
        return new Message(args.getChatId(), UNKNOWN_MESSAGE);
    }

    @Override
    public String getCommandName() {
        return null;
    }
}

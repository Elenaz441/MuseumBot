package ru.urfu.museumbot.commands;

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

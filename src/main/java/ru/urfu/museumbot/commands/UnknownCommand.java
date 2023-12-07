package ru.urfu.museumbot.commands;

import ru.urfu.museumbot.message.Message;

/**
 * Unknown {@link Command}.
 */
public class UnknownCommand implements Executable{

    public static final String UNKNOWN_MESSAGE = "Извините, команда не распознана, напишите /help чтобы узнать что я умею.";

    public UnknownCommand() {
    }

    @Override
    public Message getMessage(CommandArgs args) {
        return new Message(args.getChatId(), UNKNOWN_MESSAGE);
    }

}

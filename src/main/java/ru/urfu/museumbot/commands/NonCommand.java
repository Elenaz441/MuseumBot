package ru.urfu.museumbot.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.urfu.museumbot.jpa.service.SendBotMessageService;

/**
 * Команды, которые начинаются не со /
 */
public class NonCommand implements Command {

    public static final String UNKNOWN_MESSAGE = "Извините, команда не распознана, напишите /help чтобы узнать что я умею.";

    private final SendBotMessageService sendBotMessageService;

    public NonCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    /**
     * Основной метод, который вызывает работу команды
     */
    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), UNKNOWN_MESSAGE);
    }
}

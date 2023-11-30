package ru.urfu.museumbot.commands;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import static ru.urfu.museumbot.commands.Commands.START;


/**
 * Start {@link Command}.
 */
@Service
public class StartCommand implements Command {

    public final String START_MESSAGE = "Здравствуйте! " +
            "Я бот, который поможет вам отслеживать предстоящие культурные мероприятия. " +
            "Используйте команду /help, чтобы узнать, что я могу";

    public StartCommand() {
    }

    /**
     * Основной метод, который вызывает работу команды
     */
    @Override
    public SendMessage getMessage(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId().toString());
        message.setText(START_MESSAGE);
        return message;
    }

    @Override
    public String getCommandName() {
        return START;
    }
}

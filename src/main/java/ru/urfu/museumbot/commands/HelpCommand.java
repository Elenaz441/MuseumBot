package ru.urfu.museumbot.commands;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import static ru.urfu.museumbot.commands.Commands.HELP;


/**
 * Help {@link Command}.
 */
@Service
public class HelpCommand implements Command {

    public final String HELP_MESSAGE =  """
            Доступны следующие команды:
            \t/help - Справка
            \t/view_upcoming_events - Посмотреть предстоящие мероприятия
            \t/sign_up_for_event - Зарегистрироваться на мероприятие.
            \t/cancel - Отменить запись на мероприятие.
            \t/view_my_events - Посмотреть список мероприятий, на которые вы записаны.
            """;

    public HelpCommand() {
    }

    /**
     * Основной метод, который вызывает работу команды
     */
    @Override
    public SendMessage getMessage(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId().toString());
        message.setText(HELP_MESSAGE);
        return message;
    }

    @Override
    public String getCommandName() {
        return HELP;
    }
}

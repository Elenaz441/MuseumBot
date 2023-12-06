package ru.urfu.museumbot.commands;

import org.springframework.stereotype.Service;
import ru.urfu.museumbot.message.Message;

import static ru.urfu.museumbot.commands.Commands.HELP;


/**
 * Help {@link Command}.
 */
@Service
public class HelpCommand implements Command {

    static final String HELP_MESSAGE =  """
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
    public Message getMessage(CommandArgs args) {
        return new Message(args.getChatId(), HELP_MESSAGE);
    }

    @Override
    public String getCommandName() {
        return HELP;
    }
}

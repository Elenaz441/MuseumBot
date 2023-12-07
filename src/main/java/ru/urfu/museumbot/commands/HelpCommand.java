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
            \t/view_exhibit - Посмотреть информацию о экспонате.\040
            Данная функция доступна тогда, когда вы находитесь на каком-либо мероприятии.\040
            Название экспоната вы можете узнать в музее.
            \t/leave_review - Оставить отзыв.\040
            При вызове этой функции вам будет выдан список мероприятий, на которые вы ещё не оставили отзыв.
            \t/view_museum - Посмотреть информацию о музее.
            \t/view_museum_rank - Посмотреть рейтинг музея и прочитать отзывы о мероприятиях,\040
            которые проходили в этом музее.
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

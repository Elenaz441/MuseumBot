package ru.urfu.museumbot.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.urfu.museumbot.jpa.service.SendBotMessageService;

/**
 * Help {@link Command}.
 */
public class HelpCommand implements Command {

    private final SendBotMessageService sendBotMessageService;

    public static final String HELP_MESSAGE =  """
            Доступны следующие команды:
            \t/help - Справка
            \t/view_upcoming_events - Посмотреть предстоящие мероприятия
            \t/sign_up_for_event - Зарегистрироваться на мероприятие.
            \t/cancel - Отменить запись на мероприятие.
            \t/view_my_events - Посмотреть список мероприятий, на которые вы записаны.
            """;

    public HelpCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    /**
     * Основной метод, который вызывает работу команды
     */
    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), HELP_MESSAGE);
    }
}

package ru.urfu.museumbot.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.service.SendBotMessageService;
import ru.urfu.museumbot.jpa.service.ServiceContext;
import ru.urfu.museumbot.jpa.service.UserService;

import java.util.List;

import static ru.urfu.museumbot.commands.Commands.CANCEL_EVENT;

/**
 * Промежуточная команда перед отменой регистрации на мероприятие.
 * Здесь пользователю предоставляется список ближайших мероприятий, на которые он зарегистрирован.
 */
public class PreCancelCommand implements Command {
    public final static String CHOOSE_EVENT_MESSAGE = "Выберете мероприятие, на которое хотите отменить запись:";

    private final SendBotMessageService sendBotMessageService;
    private final UserService userService;

    public PreCancelCommand(SendBotMessageService sendBotMessageService, ServiceContext serviceContext) {
        this.sendBotMessageService = sendBotMessageService;
        this.userService = serviceContext.getUserService();
    }

    /**
     * Основной метод, который вызывает работу команды
     */
    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        sendBotMessageService.sendMessageWithButtons(chatId.toString(), CHOOSE_EVENT_MESSAGE, CANCEL_EVENT, viewMyEvents(chatId));
    }

    /**
     * <p>Посмотреть предстоящие мероприятия пользователя</p>
     */
    private List<Event> viewMyEvents(Long chatId) {
        return userService.getUserEvents(chatId);
    }
}

package ru.urfu.museumbot.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.service.EventService;
import ru.urfu.museumbot.jpa.service.SendBotMessageService;
import ru.urfu.museumbot.jpa.service.ServiceContext;

import java.util.List;

import static ru.urfu.museumbot.commands.Commands.ADD_EVENT;

/**
 * Промежуточная команда перед регистрацией на мероприятие.
 * Здесь пользователю предоставляется список ближайших мероприятий.
 */
public class PreSignUpCommand implements Command {

    public final static String CHOOSE_EVENT_MESSAGE = "Выберете мероприятие, на которое хотите записаться:";

    private final SendBotMessageService sendBotMessageService;
    private final EventService eventService;

    public PreSignUpCommand(SendBotMessageService sendBotMessageService, ServiceContext serviceContext) {
        this.sendBotMessageService = sendBotMessageService;
        this.eventService = serviceContext.getEventService();
    }

    /**
     * Основной метод, который вызывает работу команды
     */
    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        sendBotMessageService.sendMessageWithButtons(chatId.toString(), CHOOSE_EVENT_MESSAGE, ADD_EVENT, getEvents());
    }

    /**
     * Посмотреть предстоящие мероприятия (в ближайшие 7 дней)
     */
    private List<Event> getEvents() {
        return eventService.getListEvents();
    }
}

package ru.urfu.museumbot.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.service.EventService;
import ru.urfu.museumbot.jpa.service.SendBotMessageService;
import ru.urfu.museumbot.jpa.service.ServiceContext;

import java.util.stream.Collectors;

/**
 * Класс для обработки команды просмотра предстоящих мероприятий (в ближайшие 7 дней)
 */
public class ViewUpcomingEventsCommand implements Command{

    private final SendBotMessageService sendBotMessageService;
    private final EventService eventService;

    public ViewUpcomingEventsCommand(SendBotMessageService sendBotMessageService, ServiceContext serviceContext) {
        this.sendBotMessageService = sendBotMessageService;
        this.eventService = serviceContext.getEventService();
    }

    /**
     * Основной метод, который вызывает работу команды
     */
    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        sendBotMessageService.sendMessage(chatId.toString(), viewUpcomingEvents());
    }

    /**
     * <p>Посмотреть предстоящие мероприятия</p>
     */
    private String viewUpcomingEvents() {
        return eventService
                .getListEvents()
                .stream()
                .map(Event::toString)
                .collect(Collectors.joining("\n\n===============================\n\n"));
    }
}

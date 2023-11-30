package ru.urfu.museumbot.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.urfu.museumbot.dataFormat.EventFormat;
import ru.urfu.museumbot.jpa.service.EventService;

import java.util.stream.Collectors;

/**
 * Класс для обработки команды просмотра предстоящих мероприятий (в ближайшие 7 дней)
 */
public class ViewUpcomingEventsCommand implements Command{

    private final EventService eventService;

    public ViewUpcomingEventsCommand(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Основной метод, который вызывает работу команды
     */
    @Override
    public SendMessage getMessage(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId().toString());
        message.setText(viewUpcomingEvents());
        return message;
    }

    /**
     * <p>Посмотреть предстоящие мероприятия</p>
     */
    private String viewUpcomingEvents() {
        EventFormat eventFormat = new EventFormat();
        return eventService
                .getListEvents()
                .stream()
                .map(eventFormat::toFormattedString)
                .collect(Collectors.joining("\n\n===============================\n\n"));
    }
}

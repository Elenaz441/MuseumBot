package ru.urfu.museumbot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.urfu.museumbot.dataFormat.EventFormat;
import ru.urfu.museumbot.jpa.service.EventService;
import static ru.urfu.museumbot.commands.Commands.VIEW_UPCOMING_EVENTS;
import java.util.stream.Collectors;

/**
 * Класс для обработки команды просмотра предстоящих мероприятий (в ближайшие 7 дней)
 */
@Service
public class ViewUpcomingEventsCommand implements Command{

    private final EventService eventService;
    @Autowired
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

    @Override
    public String getCommandName() {
        return VIEW_UPCOMING_EVENTS;
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

package ru.urfu.museumbot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.service.EventService;
import ru.urfu.museumbot.message.MarkupButtonsTelegram;
import ru.urfu.museumbot.message.Message;

import java.util.List;

import static ru.urfu.museumbot.commands.Commands.*;


/**
 * Промежуточная команда перед регистрацией на мероприятие.
 * Здесь пользователю предоставляется список ближайших мероприятий.
 */
@Service
public class PreSignUpCommand implements Command {

    public final String CHOOSE_EVENT_MESSAGE = "Выберете мероприятие, на которое хотите записаться:";

    private final EventService eventService;
    @Autowired
    public PreSignUpCommand(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Основной метод, который вызывает работу команды
     */
    @Override
    public Message getMessage(CommandArgs args) {
        Message message = new Message(args.getChatId(), CHOOSE_EVENT_MESSAGE);
        message.setButtons(new MarkupButtonsTelegram(ADD_EVENT, getEvents()));
        return message;
    }

    @Override
    public String getCommandName() {
        return SIGN_UP_FOR_EVENT;
    }

    /**
     * Посмотреть предстоящие мероприятия (в ближайшие 7 дней)
     */
    private List<Event> getEvents() {
        return eventService.getListEvents();
    }
}

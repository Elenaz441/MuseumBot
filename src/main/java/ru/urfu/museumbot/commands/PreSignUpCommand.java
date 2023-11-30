package ru.urfu.museumbot.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.urfu.museumbot.buttons.ButtonsContent;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.service.EventService;

import java.util.List;

import static ru.urfu.museumbot.commands.Commands.ADD_EVENT;

/**
 * Промежуточная команда перед регистрацией на мероприятие.
 * Здесь пользователю предоставляется список ближайших мероприятий.
 */
public class PreSignUpCommand implements Command {

    public final String CHOOSE_EVENT_MESSAGE = "Выберете мероприятие, на которое хотите записаться:";

    private final EventService eventService;
    private final ButtonsContent buttonsContent;

    public PreSignUpCommand(EventService eventService) {
        this.eventService = eventService;
        this.buttonsContent = new ButtonsContent();
    }

    /**
     * Основной метод, который вызывает работу команды
     */
    @Override
    public SendMessage getMessage(Update update) {
        InlineKeyboardMarkup markupInline = buttonsContent.getMarkupInline(ADD_EVENT, getEvents());
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId().toString());
        message.setText(CHOOSE_EVENT_MESSAGE);
        message.setReplyMarkup(markupInline);
        return message;
    }

    /**
     * Посмотреть предстоящие мероприятия (в ближайшие 7 дней)
     */
    private List<Event> getEvents() {
        return eventService.getListEvents();
    }
}

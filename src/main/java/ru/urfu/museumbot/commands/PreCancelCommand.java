package ru.urfu.museumbot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.urfu.museumbot.buttons.ButtonsContent;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.service.UserService;

import java.util.List;

import static ru.urfu.museumbot.commands.Commands.CANCEL;
import static ru.urfu.museumbot.commands.Commands.CANCEL_EVENT;

/**
 * Промежуточная команда перед отменой регистрации на мероприятие.
 * Здесь пользователю предоставляется список ближайших мероприятий, на которые он зарегистрирован.
 */
@Service
public class PreCancelCommand implements Command {
    public final static String CHOOSE_EVENT_MESSAGE = "Выберете мероприятие, на которое хотите отменить запись:";

    private final UserService userService;
    private final ButtonsContent buttonsContent;
    @Autowired
    public PreCancelCommand(UserService userService) {
        this.userService = userService;
        this.buttonsContent = new ButtonsContent();
    }

    /**
     * Основной метод, который вызывает работу команды
     */
    @Override
    public SendMessage getMessage(Update update) {
        Long chatId = update.getMessage().getChatId();
        InlineKeyboardMarkup markupInline = buttonsContent.getMarkupInline(CANCEL_EVENT, viewMyEvents(chatId));
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(CHOOSE_EVENT_MESSAGE);
        message.setReplyMarkup(markupInline);
        return message;
    }

    @Override
    public String getCommandName() {
        return CANCEL;
    }

    /**
     * <p>Посмотреть предстоящие мероприятия пользователя</p>
     */
    private List<Event> viewMyEvents(Long chatId) {
        return userService.getUserEvents(chatId);
    }
}

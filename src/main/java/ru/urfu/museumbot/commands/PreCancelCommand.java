package ru.urfu.museumbot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.buttons.ButtonsContext;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.service.UserService;
import ru.urfu.museumbot.message.Message;

import java.util.List;

import static ru.urfu.museumbot.commands.Commands.CANCEL;
import static ru.urfu.museumbot.commands.Commands.CANCEL_EVENT;

/**
 * Промежуточная команда перед отменой регистрации на мероприятие.
 * Здесь пользователю предоставляется список ближайших мероприятий, на которые он зарегистрирован.
 */
@Service
public class PreCancelCommand implements Command {

    static final String CHOOSE_EVENT_MESSAGE = "Выберете мероприятие, на которое хотите отменить запись:";

    private final UserService userService;

    @Autowired
    public PreCancelCommand(UserService userService) {
        this.userService = userService;
    }

    /**
     * Основной метод, который вызывает работу команды
     */
    @Override
    public Message getMessage(CommandArgs args) {
        Long chatId = args.getChatId();
        Message message = new Message(chatId, CHOOSE_EVENT_MESSAGE);

        message.setButtonsContext(new ButtonsContext(CANCEL_EVENT, viewMyEvents(chatId)));
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

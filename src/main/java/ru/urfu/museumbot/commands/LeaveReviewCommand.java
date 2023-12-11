package ru.urfu.museumbot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.urfu.museumbot.buttons.ButtonsContext;
import ru.urfu.museumbot.jpa.models.Event;
import ru.urfu.museumbot.jpa.models.User;
import ru.urfu.museumbot.jpa.service.ServiceContext;
import ru.urfu.museumbot.jpa.service.UserService;
import ru.urfu.museumbot.message.Message;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.urfu.museumbot.commands.Commands.ADD_EVENT;
import static ru.urfu.museumbot.commands.Commands.LEAVE_REVIEW;

/**
 * Класс начала команды "оставить отзыв"
 */
@Service
public class LeaveReviewCommand implements Command {

    public static final String CALLBACK_DATA = "LeaveReview";
    public static final String MESSAGE_TEXT = "Выберите мероприятие:\n";
    private final UserService userService;
    static final String NO_EVENT = "У вас нет мероприятий, которые можно оценить.";

    @Autowired
    public LeaveReviewCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Message getMessage(CommandArgs args) {
        Long chatId = args.getChatId();
        List<Event> userEvents = getUserEvents(chatId);
        if(userEvents.isEmpty()){
            return new Message(chatId, NO_EVENT);
        }
        Message message = new Message(chatId, MESSAGE_TEXT);
        userService.updateUserState(chatId, State.RATE_PREV);
        Map<Long, String> variants = userEvents.stream().collect(Collectors.toMap(Event::getId, Event::getTitle));
        message.setButtonsContext(new ButtonsContext(CALLBACK_DATA, variants));
        return message;
    }

    @Override
    public String getCommandName() {
        return LEAVE_REVIEW;
    }

    /**
     * @param chatId идентификатор чата пользователя
     * @return список мероприятий, которые посещал пользователь
     */
    private List<Event> getUserEvents(Long chatId) {
        return userService.getAllVisitedEvents(chatId);
    }
}

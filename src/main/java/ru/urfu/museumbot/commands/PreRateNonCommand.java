package ru.urfu.museumbot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.jpa.service.UserService;
import ru.urfu.museumbot.message.Message;

/**
 * Второй этап оставления отзыва
 */
@Service
public class PreRateNonCommand implements ExecutableWithState {

    public static final String RATE_PREV_MESSAGE = "Как вы оцениваете данное мероприятие от 0 до 10," +
            " где 0 - это не понравилось совсем;" +
            " 10 - очень понравилось?";

    private final UserService userService;

    @Autowired
    public PreRateNonCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Message getMessage(CommandArgs args) {
        Long chatId = args.getChatId();
        userService.updateUserState(chatId, State.RATE);
        Long eventId = Long.valueOf(args.getCallbackData().split(" ")[1]);
        userService.setReviewingEvent(chatId, eventId);
        return new Message(chatId, RATE_PREV_MESSAGE);
    }

    @Override
    public State getCommandState() {
        return State.RATE_PREV;
    }
}

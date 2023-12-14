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

    private static final String RATE_PREV_MESSAGE = "Как вы оцениваете данное мероприятие от 0 до 10," +
            " где 0 - это не понравилось совсем;" +
            " 10 - очень понравилось?";

    private static final String NUMBER_FORMAT_EXCEPTION_TEXT = "Извините, не смог найти событие.";
    private final UserService userService;

    @Autowired
    public PreRateNonCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Message getMessage(CommandArgs args) {
        Long chatId = args.getChatId();
        String valueEventId = args.getCallbackData().split(" ")[1];
        try {
            Long eventId = Long.parseLong(valueEventId);
            userService.updateUserState(chatId, State.RATE);
            userService.setReviewingEvent(chatId, eventId);
            return new Message(chatId, RATE_PREV_MESSAGE);
        } catch (NumberFormatException e) {
            System.out.println(String.format("Не удалось привести СallBackData к числу %s", e.getMessage()));
        }
        userService.updateUserState(chatId, State.INIT);
        return new Message(chatId, NUMBER_FORMAT_EXCEPTION_TEXT);
    }

    @Override
    public State getCommandState() {
        return State.RATE_PREV;
    }
}

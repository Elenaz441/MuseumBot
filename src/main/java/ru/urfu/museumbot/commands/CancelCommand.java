package ru.urfu.museumbot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.museumbot.jpa.models.Review;
import ru.urfu.museumbot.jpa.service.*;
import ru.urfu.museumbot.message.Message;

import static ru.urfu.museumbot.commands.Commands.CANCEL_EVENT;

/**
 * Класс для команды отмены регистрации на мероприятие
 */
@Service
public class CancelCommand implements Command {

    static final String MESSAGE_SUCCESS = "Вы отменили свою запись на выбранное мероприятие";
    private final EventService eventService;
    private final ReviewService reviewService;
    private final UserService userService;

    @Autowired
    public CancelCommand(EventService eventService,
                         ReviewService reviewService,
                         UserService userService) {
        this.eventService = eventService;
        this.reviewService = reviewService;
        this.userService = userService;
    }

    /**
     * Основной метод, который вызывает работу команды
     */
    @Override
    public Message getMessage(CommandArgs args) {
        Long chatId = args.getChatId();
        String callback = args.getCallbackData();
        return new Message(chatId, cancel(callback, chatId));
    }

    @Override
    public String getCommandName() {
        return CANCEL_EVENT;
    }

    /**
     * Отмена регистрации на выбранное мероприятие
     */
    private String cancel(String callbackData, Long chatId) {
        String text = MESSAGE_SUCCESS;
        Long eventId = Long.valueOf(callbackData.replace(CANCEL_EVENT + " ", ""));
        Review review = reviewService.getReview(
                userService.getUserByChatId(chatId),
                eventService.getEventById(eventId));
        if (review == null) {
            text = "Вы не записаны на данное мероприятие";
        } else {
            reviewService.deleteReview(review);
        }
        return text;
    }

}
